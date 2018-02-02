package BuildTemplates

import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.TQ
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script

fun yarnInstall(buildType: BuildType) : BuildType{
    buildType.steps {
        script {
            name          = "Yarn Install"
            scriptContent = "%yarn% install"
        }
    }
    return buildType
}

fun jspmInstall(buildType: BuildType) : BuildType{
    buildType.steps {
        script {
            name          = "JSPM Install"
            scriptContent = "%jspm% install"
        }
    }
    return buildType
}

fun test(buildType: BuildType) : BuildType{
    buildType.steps {
        script {
            name          = "Test"
            scriptContent = "%gulp% test"
        }
    }
    return buildType
}

fun build(buildType: BuildType) : BuildType{
    buildType.steps {
        script {
            name          = "Build"
            scriptContent = "%gulp% build"
        }
    }
    return buildType
}

fun gitShortHash(buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name                = "Git Short Hash"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                        try {
                            ${'$'}hash = "%system.build.vcs.number%"
                            ${'$'}shortHash = ${'$'}hash.substring(0,7)
                            ${'$'}buildNumber = "%SemanticVersion%%PrereleaseVersion%-${'$'}shortHash"

                            Write-Host "shortHash: ${'$'}shortHash"
                            Write-Host "buildNumber: ${'$'}buildNumber"

                            Write-Host "##teamcity[setParameter name='GitShortHash' value='${'$'}shortHash']"
                            Write-Host "##teamcity[buildNumber '${'$'}buildNumber']"
                        } catch {
                            return 1
                        }
                        return 0
                    """.trimIndent()
            }
        }
    }
    return buildType
}

fun setPackageVersion(buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name                = "Set Package Version"
            formatStderrAsError = true
            scriptMode = script {
                content = """
                        try {
                            ${'$'}version = ${'$'}args[0]

                            if(!${'$'}version){
                                throw "version is empty"
                            }

                            ${'$'}package = Get-Content "package.json" -Raw
                            ${'$'}updated = ${'$'}package -replace '"(version)"\s*:\s*"(.*)"', ${TQ}version"": ""${'$'}version$TQ
                            ${'$'}updated | Set-Content 'package.json'

                            Write-Host "Updated package.json to version ${'$'}version"
                        } catch {
                            return 1
                        }
                        return 0
                    """.trimIndent()
            }
            param("jetbrains_powershell_scriptArguments", "%PackageVersion%")
        }
    }
    return buildType
}

fun incrementProjectPatchVersion(buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name     = "Increment PatchVersion And Reset Build Counters"
            id       = "${buildType.id}_VersionStep"
            platform = PowerShellStep.Platform.x86
            edition  = PowerShellStep.Edition.Desktop
            scriptMode = script {
                content = """
                        ${'$'}baseUri           = "localhost"
                        ${'$'}projectId         = "%SolutionProjectId%"
                        ${'$'}preReleaseBuildId = "%PreReleaseProjectId%"
                        ${'$'}releaseBuildId    = "%ReleaseProjectId%"
                        ${'$'}branch            = "%teamcity.build.branch%"
                        ${'$'}username          = "%teamcityApiUserName%"
                        ${'$'}password          = "%teamcityApiPassword%"
                        ${'$'}base64AuthInfo    = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes(("{0}:{1}" -f ${'$'}username,${'$'}password)))

                        Write-Host "temp ${'$'}username ${'$'}password"

                        if(${'$'}branch -ne "master") {return 0};

                        function Increment-ProjectPatchVersion (${'$'}projectId) {
                            #get PatchVersion
                            ${'$'}paramUri    ="${'$'}baseUri/httpAuth/app/rest/projects/id:${'$'}projectId/parameters/PatchVersion"
                            Write-Host ${'$'}paramUri
                            ${'$'}paramResult = Invoke-RestMethod -Headers @{Authorization=("Basic {0}" -f ${'$'}base64AuthInfo)} -Method Get -Uri ${'$'}paramUri

                            #increment PatchVersion
                            ${'$'}newPatchVersion = ([int]${'$'}paramResult.property.value) + 1
                            ${'$'}updateResult    = Invoke-RestMethod -Headers @{Authorization=("Basic {0}" -f ${'$'}base64AuthInfo);"Content-Type"="text/plain"} -Method Put -Uri ${'$'}paramUri -Body ${'$'}newPatchVersion
                            Write-Host "Project ${'$'}projectId PatchVersion parameter incremented to ${'$'}newPatchVersion"
                        }

                        function Reset-BuildCounter(${'$'}buildId) {
                            ${'$'}buildCounterUri = "${'$'}baseUri/httpAuth/app/rest/buildTypes/id:${'$'}buildId/settings/buildNumberCounter"
                            Write-Host ${'$'}buildCounterUri
                            ${'$'}updateResult    = Invoke-RestMethod -Headers @{Authorization=("Basic {0}" -f ${'$'}base64AuthInfo);"Content-Type"="text/plain"} -Method Put -Uri ${'$'}buildCounterUri -Body 0
                            Write-Host "Reset build counter for ${'$'}(${'$'}_.name)"
                        }

                        Increment-ProjectPatchVersion ${'$'}projectId
                        Reset-BuildCounter            ${'$'}preReleaseBuildId
                        Reset-BuildCounter            ${'$'}releaseBuildId
                    """.trimIndent()
            }
            noProfile = false
        }
    }
    return buildType
}

fun tagBuild(buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name       = "Tag Build From Master Branch"
            id         = "${buildType.id}_TagStep"
            platform   = PowerShellStep.Platform.x86
            edition    = PowerShellStep.Edition.Desktop
            scriptMode = script {
                content = """
                    ${'$'}branch = "%teamcity.build.branch%"

                    if(${'$'}branch -ne "master") { return 0 }

                    ${'$'}tag = "%SemanticVersion%"
                    Write-Host "Taging build ${'$'}tag"

                    git tag ${'$'}tag
                    git push origin ${'$'}tag
                """.trimIndent()
            }
            noProfile = false
        }
    }

    return buildType
}
