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



    fun javascriptBuild(buildType: BuildType) : BuildType{
        build(test(jspmInstall(yarnInstall(setPackageVersion(gitShortHash(buildType))))))

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
        description = "CI/CD"

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
            param("SolutionProjectId",   solution.id)
            param("PrereleaseVersion",   "-alpha.%build.counter%")
            param("SHA",                 "%GitShortHash%")
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

    fun packPackage(buildType: BuildType) : BuildType{

        buildType.steps {
            script {
                name          = "Pack"
                scriptContent = "%npm% pack"
            }
        }

        return buildType
    }

    fun deployPreReleasePackage(buildType: BuildType) : BuildType{

        buildType.steps {
            script {
                name          = "Publish PreRelease"
                scriptContent = "%npm% publish %PackageName%-%PackageVersion%.tgz --tag prerelease"
            }
        }

        return buildType
    }

    fun deployReleasePackage(buildType: BuildType) : BuildType{

        buildType.steps {
            script {
                name          = "Publish Release"
                scriptContent = "%npm% publish %PackageName%-%PackageVersion%.tgz --tag latest"
            }
        }

        return buildType
    }

    val deployPreRelease =  deployPreReleasePackage(packPackage(BuildType({
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
                    id               = "${baseId}_PreRelease_ARTIFACT_DEPENDENCY"
                    cleanDestination = true
                    artifactRules    = "%ArtifactsOut%"
                }
            }
        }
    })))

    val deployRelease = deployReleasePackage(packPackage(BuildType({
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
                    id               = "${baseId}_Release_ARTIFACT_DEPENDENCY"
                    cleanDestination = true
                    artifactRules    = "%ArtifactsOut%"
                }
            }
        }
    })))

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
