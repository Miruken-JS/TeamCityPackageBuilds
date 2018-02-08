package BuildTemplates

import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.finishBuildTrigger

fun configureEs5Project(solution: JavascriptProject, packages: List<Es5JavascriptPackage>) : Project{

    fun javascriptBuild(buildType: BuildType) : BuildType{
        gruntCI(
        yarnInstall(
        setMirukenVersion(
        gitShortHash(buildType))))

        buildType.buildNumberPattern = "%BuildFormatSpecification%"

        return buildType
    }

    val ciBuild =  javascriptBuild(BuildType({
        uuid        = "${solution.guid}_CIBuild"
        id          = solution.ciBuildId
        name        = "CI Build"
        description = "Builds any branch. Runs tests."

        allowExternalStatus = true

        params {
            param("BranchSpecification", "+:refs/heads/(*)")
            param("MajorVersion",        "0")
            param("MinorVersion",        "0")
            param("PatchVersion",        "0")
            param("PrereleaseVersion",   "-CI.%build.counter%")
        }

        vcs {
            root(ciVcsRoot(solution))
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

        allowExternalStatus = true

        params {
            param("BranchSpecification", """
            +:refs/heads/(develop)
            +:refs/heads/(feature/*)
        """.trimIndent())
        }

        vcs {
            root(preReleaseVcsRoot(solution))
            cleanCheckout = true
        }
    }))


    val releaseBuild =
            incrementProjectPatchVersion(
            tagBuild("%SemanticVersion%",
            javascriptBuild(
            BuildType({

        uuid          = "${solution.guid}_ReleaseBuild"
        id            = solution.releaseBuildId
        name          = "Release Build"
        description   = "This will push a release package from the MASTER branch."
        artifactRules = "%ArtifactsIn%"
        
        allowExternalStatus = true

        params {
            param("BranchSpecification",              "+:refs/heads/(master)")
            param("DefaultBranch",                    "master")
            param("PrereleaseVersion",                "")
        }

        vcs {
            root(releaseVcsRoot(solution))
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

        for(javascriptPackage in packages){
            subProject(configureEs5PackageDeployProject(solution, javascriptPackage, preReleaseBuild, releaseBuild))
        }
    })

    return Project({
        uuid        = solution.guid
        id          = solution.id
        parentId    = solution.parentId
        name        = solution.name
        description = "CI/CD"

        vcsRoot(ciVcsRoot(solution))
        vcsRoot(preReleaseVcsRoot(solution))
        vcsRoot(releaseVcsRoot(solution))

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

fun configureEs5PackageDeployProject(
        javascriptProject: JavascriptProject,
        javascriptPackage: Es5JavascriptPackage,
        preReleaseBuild: BuildType,
        releaseBuild: BuildType) : Project{

    val baseUuid = "${javascriptProject.guid}_${javascriptPackage.id}"
    val baseId   = "${javascriptProject.id}_${javascriptPackage.id}"

    fun baseReleaseBuild(buildType: BuildType) : BuildType{

        packPackage(
        setPackageVersion("bower.json",
        setPackageVersion("package.json",
        buildType)))

        return buildType
    }

    val deployPreRelease =
        deployPreReleasePackage(
        baseReleaseBuild(
        BuildType({

        uuid               = "${baseUuid}_DeployPreRelease"
        id                 = "${baseId}_DeployPreRelease"
        name               = "Deploy PreRelease"
        description        = "This will push a package with a -PreRelease tag"
        buildNumberPattern = "%BuildFormatSpecification%"

        params {
            param("BuildFormatSpecification", "%dep.${javascriptProject.preReleaseBuildId}.BuildFormatSpecification%")
            param("PackageVersion",           "%dep.${javascriptProject.preReleaseBuildId}.PackageVersion%")
        }

        vcs {
            root(packageVcsRoot(javascriptPackage))
            cleanCheckout = true
            checkoutMode  = CheckoutMode.ON_AGENT
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
                    cleanDestination = false
                    artifactRules    = "%ArtifactsOut%"
                }
            }
        }
    })))

    val deployRelease =
        tagBuild("%PackageVersion%",
        commitPackageArtifactsToGit(javascriptPackage.unminifiedFile, javascriptPackage.minifiedFile,
        deployReleasePackage(
        baseReleaseBuild(
        BuildType({

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

        vcs {
            root(packageVcsRoot(javascriptPackage))
            cleanCheckout = true
            checkoutMode  = CheckoutMode.ON_AGENT
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
                    cleanDestination = false
                    artifactRules    = "%ArtifactsOut%"
                }
            }
        }
    })))))

    return Project({
        uuid        = baseUuid
        id          = baseId
        parentId    = javascriptProject.deploymentProjectId
        name        = javascriptPackage.packageName
        description = "${javascriptPackage.packageName} npm package"

        vcsRoot(packageVcsRoot(javascriptPackage))

        buildType(deployPreRelease)
        buildType(deployRelease)

        params {
            param("PackageName",  javascriptPackage.packageName)
            param("ArtifactsOut", javascriptPackage.artifactsOut)
        }
    })
}
