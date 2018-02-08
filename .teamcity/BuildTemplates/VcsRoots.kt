package BuildTemplates

import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

fun ciVcsRoot(javascriptProject: JavascriptProject): GitVcsRoot {
    return GitVcsRoot({
        uuid             = "${javascriptProject.guid}CIVcsRoot"
        id               = javascriptProject.ciVcsRootId
        name             = "${javascriptProject.codeGithubUrl}_CI"
        url              = javascriptProject.codeGithubUrl
        branch           = "%DefaultBranch%"
        branchSpec       = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })
}

fun preReleaseVcsRoot (javascriptProject: JavascriptProject): GitVcsRoot {
    return  GitVcsRoot({
        uuid             = "${javascriptProject.guid}PreReleaseVcsRoot"
        id               = javascriptProject.preReleaseVcsRootId
        name             = "${javascriptProject.codeGithubUrl}_PreRelease"
        url              = javascriptProject.codeGithubUrl
        branch           = "%DefaultBranch%"
        branchSpec       = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })
}

fun releaseVcsRoot (javascriptProject: JavascriptProject): GitVcsRoot {
    return  GitVcsRoot({
        uuid             = "${javascriptProject.guid}ReleaseVcsRoot"
        id               = javascriptProject.releaseVcsRootId
        name             = "${javascriptProject.codeGithubUrl}_Release"
        url              = javascriptProject.codeGithubUrl
        branch           = "%DefaultBranch%"
        branchSpec       = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })
}

fun packageVcsRoot (javascriptPackage: Es5JavascriptPackage): GitVcsRoot {
    return GitVcsRoot({
        uuid             = javascriptPackage.packageVcsRootUuid
        id               = javascriptPackage.packageVcsRootId
        name             = "${javascriptPackage.packageGithubUrl}_Package"
        url              = javascriptPackage.packageGithubUrl
        branch           = "master"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })
}
