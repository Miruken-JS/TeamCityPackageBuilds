package BuildTemplates

import jetbrains.buildServer.configs.kotlin.v2017_2.BuildStep
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
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }
    }
    return buildType
}

fun jspmInstall(buildType: BuildType) : BuildType{
    buildType.steps {
        script {
            name          = "JSPM Install"
            scriptContent = "%jspm% install"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }
    }
    return buildType
}

fun test(buildType: BuildType) : BuildType{
    buildType.steps {
        script {
            name          = "Test"
            scriptContent = "%gulp% test"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }
    }
    return buildType
}

fun build(buildType: BuildType) : BuildType{
    buildType.steps {
        script {
            name          = "Build"
            scriptContent = "%gulp% build"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }
    }
    return buildType
}

fun gruntCI(buildType: BuildType) : BuildType{
    buildType.steps {
        script {
            name          = "Grunt CI"
            scriptContent = "%grunt% ci"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }
    }
    return buildType
}

fun gitShortHash(buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name                = "Git Short Hash"
            formatStderrAsError = true
            executionMode       = BuildStep.ExecutionMode.RUN_ON_SUCCESS
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
                            Write-Error ${'$'}_
                            Write-Host "##teamcity[buildStatus status='FAILURE' text='Failed to get shortHash']"
                        }
                    """.trimIndent()
            }
        }
    }
    return buildType
}

fun setMirukenVersion(buildType: BuildType) : BuildType{
    val fileName = "./lib/miruken.js"
    buildType.steps {
        powerShell {
            name                = "Set Miruken Version"
            formatStderrAsError = true
            executionMode       = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            scriptMode = script {
                content = """
                        try {
                            ${'$'}version = ${'$'}args[0]

                            if(!${'$'}version){
                                throw "version is empty"
                            }

                            ${'$'}package = Get-Content "$fileName" -Raw
                            ${'$'}updated = ${'$'}package -replace 'version\s*:\s*"(.*)"', "version: ""${'$'}version$TQ
                            ${'$'}updated | Set-Content '$fileName'

                            Write-Host "Updated $fileName to version ${'$'}version"
                        } catch {
                            Write-Error ${'$'}_
                            Write-Host "##teamcity[buildStatus status='FAILURE' text='Failed to set miruken version']"
                        }
                    """.trimIndent()
            }
            param("jetbrains_powershell_scriptArguments", "%PackageVersion%")
        }
    }
    return buildType
}

fun setPackageVersion(fileName: String, buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name                = "Set Version In $fileName"
            formatStderrAsError = true
            executionMode       = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            scriptMode = script {
                content = """
                        try {
                            ${'$'}version = ${'$'}args[0]

                            if(!${'$'}version){
                                throw "version is empty"
                            }

                            ${'$'}package = Get-Content "$fileName" -Raw
                            ${'$'}updated = ${'$'}package -replace '"(version)"\s*:\s*"(.*)"', ${TQ}version"": ""${'$'}version$TQ
                            ${'$'}updated | Set-Content '$fileName'

                            Write-Host "Updated $fileName to version ${'$'}version"
                        } catch {
                            Write-Error ${'$'}_
                            Write-Host "##teamcity[buildStatus status='FAILURE' text='Failed to set package version']"
                        }
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
            name          = "Increment PatchVersion And Reset Build Counters"
            id            = "${buildType.id}_VersionStep"
            platform      = PowerShellStep.Platform.x86
            edition       = PowerShellStep.Edition.Desktop
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            scriptMode = script {
                content = """
                        try {
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
                        } catch {
                            Write-Error ${'$'}_
                            Write-Host "##teamcity[buildStatus status='FAILURE' text='Failed to reset version numbers and build counters']"
                        }
                    """.trimIndent()
            }
            noProfile = false
        }
    }
    return buildType
}

fun commitPackageArtifactsToGit(unminified: String, minified: String, buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name          = "Commit Package Artifacts To Git"
            id            = "${buildType.id}_CommitToGit"
            platform      = PowerShellStep.Platform.x86
            edition       = PowerShellStep.Edition.Desktop
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            scriptMode = script {
                content = """
                        try {
                            ${'$'}branch = "%teamcity.build.branch%"

                            if(${'$'}branch -ne "master") { return 0 }

                            git add package.json
                            git add bower.json
                            git add $minified
                            git add $unminified
                            git commit -m "Package artifacts from ci cd"
                            git push origin master
                        } catch {
                            Write-Error ${'$'}_
                            Write-Host "##teamcity[buildStatus status='FAILURE' text='Failed to commit to git']"
                        }
                """.trimIndent()
            }
            noProfile = false
        }
    }

    return buildType
}

fun tagBuild(versionVariable: String, buildType: BuildType) : BuildType{
    buildType.steps {
        powerShell {
            name          = "Tag Build From Master Branch"
            id            = "${buildType.id}_TagStep"
            platform      = PowerShellStep.Platform.x86
            edition       = PowerShellStep.Edition.Desktop
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
            scriptMode = script {
                content = """
                        try {
                            ${'$'}branch = "%teamcity.build.branch%"

                            if(${'$'}branch -ne "master") { return 0 }

                            ${'$'}tag = "$versionVariable"
                            Write-Host "Taging build ${'$'}tag"

                            git tag ${'$'}tag
                            git push origin ${'$'}tag
                        } catch {
                            Write-Error ${'$'}_
                            Write-Host "##teamcity[buildStatus status='FAILURE' text='Failed to tag build']"
                        }
                """.trimIndent()
            }
            noProfile = false
        }
    }

    return buildType
}

fun packPackage(buildType: BuildType) : BuildType{

    buildType.steps {
        script {
            name          = "Pack"
            scriptContent = "%npm% pack"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }
    }

    return buildType
}

fun deployPreReleasePackage(buildType: BuildType) : BuildType{

    buildType.steps {
        script {
            name          = "Publish PreRelease"
            scriptContent = "%npm% publish %PackageName%-%PackageVersion%.tgz --tag prerelease --access public"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }
    }

    return buildType
}

fun deployReleasePackage(buildType: BuildType) : BuildType{

    buildType.steps {
        script {
            name          = "Publish Release"
            scriptContent = "%npm% publish %PackageName%-%PackageVersion%.tgz --tag latest --access public"
            executionMode = BuildStep.ExecutionMode.RUN_ON_SUCCESS
        }
    }

    return buildType
}
