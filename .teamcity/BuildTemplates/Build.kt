package BuildTemplates

import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.PowerShellStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script


class JavascriptProject(
        val guid:           String,
        val id:             String,
        val parentId:       String,
        val name:           String,
        val codeGithubUrl:  String,
        val npmApiKey:      String,
        val majorVersion:   String,
        val minorVersion:   String,
        val patchVersion:   String,
        val javascriptPackages:  List<JavascriptPackage>){

    val ciVcsRootId: String
        get() = "${id}_CIVCSRoot"

    val preReleaseVcsRootId: String
        get() = "${id}_PreReleaseVCSRoot"

    val releaseVcsRootId: String
        get() = "${id}_ReleaseVCSRoot"

    val ciBuildId: String
        get() = "${id}_CIBuild"

    val preReleaseBuildId: String
        get() = "${id}_PreReleaseBuild"

    val releaseBuildId: String
        get() = "${id}_ReleaseBuild"

    val deploymentProjectId: String
        get() = "${id}_DeploymentProject"
}

class JavascriptPackage(
        val id:          String,
        val packageName: String)


fun configureJavascriptProject(solution: JavascriptProject) : Project{

    val ciVcsRoot = GitVcsRoot({
        uuid             = "${solution.guid}CIVcsRoot"
        id               = solution.ciVcsRootId
        name             = "${solution.codeGithubUrl}_CI"
        url              = solution.codeGithubUrl
        branch           = "%DefaultBranch%"
        branchSpec       = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })

    val preReleaseVcsRoot = GitVcsRoot({
        uuid             = "${solution.guid}PreReleaseVcsRoot"
        id               = solution.preReleaseVcsRootId
        name             = "${solution.codeGithubUrl}_PreRelease"
        url              = solution.codeGithubUrl
        branch           = "%DefaultBranch%"
        branchSpec       = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })

    val releaseVcsRoot = GitVcsRoot({
        uuid             = "${solution.guid}ReleaseVcsRoot"
        id               = solution.releaseVcsRootId
        name             = "${solution.codeGithubUrl}_Release"
        url              = solution.codeGithubUrl
        branch           = "%DefaultBranch%"
        branchSpec       = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })

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

    fun setPackageVersion(buildType: BuildType) : BuildType{
        buildType.steps {
            powerShell {
                name                = "Set Package Version"
                formatStderrAsError = true
                scriptMode = script {
                    content = """
                        ${'$'}version = ${'$'}args[0]

                        if(!${'$'}version){
                            throw "version is empty"
                        }

                        ${'$'}package = Get-Content "package.json" -Raw
                        ${'$'}updated = ${'$'}package -replace '"(version)"\s*:\s*"(.*)"', ${TQ}version"": ""${'$'}version$TQ
                        ${'$'}updated | Set-Content 'package.json'

                        Write-Host "Updated package.json to version ${'$'}version"
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

    fun javascriptBuild(buildType: BuildType) : BuildType{
        build(test(jspmInstall(yarnInstall(setPackageVersion(buildType)))))

        buildType.buildNumberPattern = "%BuildFormatSpecification%"

        return buildType
    }

    val ciBuild =  javascriptBuild(BuildType({
        uuid        = "${solution.guid}_CIBuild"
        id          = solution.ciBuildId
        name        = "CI Build"
        description = "Watches git repo & creates a build for any change to any branch. Runs tests. Does NOT package/deploy packages!"

        params {
            param("BranchSpecification", "+:refs/heads/(*)")
            param("MajorVersion",        "0")
            param("MinorVersion",        "0")
            param("PatchVersion",        "0")
            param("PrereleaseVersion",   "-CI.%build.counter%")
        }

        vcs {
            root(ciVcsRoot)
            cleanCheckout = true
        }

        triggers {
            vcs {
                id                       = "${solution.id}_ci_vcsTrigger"
                quietPeriodMode          = VcsTrigger.QuietPeriodMode.USE_DEFAULT
                perCheckinTriggering     = true
                groupCheckinsByCommitter = true
                enableQueueOptimization  = false
            }
        }
    }))

    val preReleaseBuild =  javascriptBuild(BuildType({
        uuid          = "${solution.guid}_PreReleaseBuild"
        id            = solution.preReleaseBuildId
        name          = "PreRelease Build"
        description   = "This will push a prerelease package"
        artifactRules = "%ArtifactsIn%"

        params {
            param("BranchSpecification", """
            +:refs/heads/(develop)
            +:refs/heads/(feature/*)
        """.trimIndent())
        }

        vcs {
            root(preReleaseVcsRoot)
            cleanCheckout = true
        }
    }))


    val releaseBuild = incrementProjectPatchVersion(tagBuild(javascriptBuild(BuildType({
        uuid          = "${solution.guid}_ReleaseBuild"
        id            = solution.releaseBuildId
        name          = "Release Build"
        description   = "This will push a release package from the MASTER branch."
        artifactRules = "%ArtifactsIn%"

        params {
            param("BranchSpecification",              "+:refs/heads/(master)")
            param("DefaultBranch",                    "master")
            param("PrereleaseVersion",                "")
        }

        vcs {
            root(releaseVcsRoot)
            cleanCheckout = true
            checkoutMode = CheckoutMode.ON_AGENT
        }
    }))))

    val deploymentProject = Project({
        uuid     = "${solution.guid}_DeploymentProject"
        id       = solution.deploymentProjectId
        parentId = solution.id
        name     = "Deployment"

        params {
            param("SHA", "")
            param("NpmApiKey", solution.npmApiKey)
        }

        for(javascriptPackage in solution.javascriptPackages){
            subProject(configurePackageDeployProject(solution, javascriptPackage, preReleaseBuild, releaseBuild))
        }
    })

    return Project({
        uuid        = solution.guid
        id          = solution.id
        parentId    = solution.parentId
        name        = solution.name
        description = "CI/CD for ${solution.name}"

        vcsRoot(ciVcsRoot)
        vcsRoot(preReleaseVcsRoot)
        vcsRoot(releaseVcsRoot)

        buildType(ciBuild)
        buildType(preReleaseBuild)
        buildType(releaseBuild)

        params {
            param("ArtifactsIn", """
            package.json => Build.zip!
            dist         => Build.zip!/dist
        """.trimIndent())
            param("ArtifactsOut", """
            Build.zip!/package.json
            Build.zip!/dist => dist
        """.trimIndent())
            param("MajorVersion",        solution.majorVersion)
            param("MinorVersion",        solution.minorVersion)
            param("PatchVersion",        solution.patchVersion)
            param("PreReleaseProjectId", solution.preReleaseBuildId)
            param("ReleaseProjectId",    solution.releaseBuildId)
        }

        subProject(deploymentProject)
    })
}

fun configurePackageDeployProject(
        javascriptProject: JavascriptProject,
        javascriptPackage: JavascriptPackage,
        preReleaseBuild: BuildType,
        releaseBuild: BuildType) : Project{

    val baseUuid = "${javascriptProject.guid}_${javascriptPackage.id}"
    val baseId   = "${javascriptProject.id}_${javascriptPackage.id}"

    fun deployPreReleasePackage(buildType: BuildType) : BuildType{

        buildType.steps {
        }

        return buildType
    }

    fun deployReleasePackage(apiKey: String, buildType: BuildType) : BuildType{

        buildType.steps {
        }

        return buildType
    }

    val deployPreRelease =  deployPreReleasePackage(BuildType({
        uuid               = "${baseUuid}_DeployPreRelease"
        id                 = "${baseId}_DeployPreRelease"
        name               = "Deploy PreRelease"
        description        = "This will push a package with a -PreRelease tag"
        buildNumberPattern = "%BuildFormatSpecification%"

        params {
            param("BuildFormatSpecification", "%dep.${javascriptProject.preReleaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion",           "%dep.${javascriptProject.preReleaseBuildId}.PackageVersion%")
        }

        triggers {
            finishBuildTrigger {
                id = "${baseId}_DeployPreRelease_TRIGGER"
                buildTypeExtId = javascriptProject.preReleaseBuildId
                successfulOnly = true
                branchFilter = "+:*"
            }
        }

        dependencies {
            dependency(preReleaseBuild) {
                snapshot {
                }

                artifacts {
                    id            = "${baseId}_PreRelease_ARTIFACT_DEPENDENCY"
                    artifactRules = "%ArtifactsOut%"
                }
            }
        }
    }))

    val deployRelease = deployReleasePackage(javascriptProject.npmApiKey, BuildType({
        uuid         = "${baseUuid}_DeployRelease"
        id           = "${baseId}_DeployRelease"
        name         = "Deploy Release"
        description  = "This will push a release package from the MASTER branch. NO CI."

        buildNumberPattern = "%BuildFormatSpecification%"

        params {
            param("BuildFormatSpecification", "%dep.${javascriptProject.releaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion",           "%dep.${javascriptProject.releaseBuildId}.PackageVersion%")
            param("PrereleaseVersion",        "")
        }

        triggers {
            finishBuildTrigger {
                id             = "${baseId}_Release_TRIGGER"
                buildTypeExtId = javascriptProject.releaseBuildId
                branchFilter   = "+:master"
            }
        }

        dependencies {
            dependency(releaseBuild){
                snapshot {
                }

                artifacts {
                    id            = "${baseId}_Release_ARTIFACT_DEPENDENCY"
                    artifactRules = "%ArtifactsOut%"
                }
            }
        }
    }))

    return Project({
        uuid        = baseUuid
        id          = baseId
        parentId    = javascriptProject.deploymentProjectId
        name        = javascriptPackage.packageName
        description = "${javascriptPackage.packageName} npm package"

        buildType(deployPreRelease)
        buildType(deployRelease)

        params {
            param("PackageName",        javascriptPackage.packageName)
        }
    })
}
