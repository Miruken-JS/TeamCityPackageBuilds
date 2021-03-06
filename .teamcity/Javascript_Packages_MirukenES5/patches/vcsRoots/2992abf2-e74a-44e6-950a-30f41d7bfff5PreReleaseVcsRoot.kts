package Javascript_Packages_MirukenES5.patches.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the vcsRoot with uuid = '2992abf2-e74a-44e6-950a-30f41d7bfff5PreReleaseVcsRoot' (id = 'Javascript_MirukenEs5_Mirken_PreReleaseVCSRoot')
accordingly and delete the patch script.
*/
changeVcsRoot("2992abf2-e74a-44e6-950a-30f41d7bfff5PreReleaseVcsRoot") {
    val expected = GitVcsRoot({
        uuid = "2992abf2-e74a-44e6-950a-30f41d7bfff5PreReleaseVcsRoot"
        id = "Javascript_MirukenEs5_Mirken_PreReleaseVCSRoot"
        name = "git@github.com:Miruken-ES5/miruken.git_PreRelease"
        url = "git@github.com:Miruken-ES5/miruken.git"
        branch = "%DefaultBranch%"
        branchSpec = "%BranchSpecification%"
        agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
        authMethod = uploadedKey {
            uploadedKey = "provenstyle"
        }
    })

    check(this == expected) {
        "Unexpected VCS root settings"
    }

    (this as GitVcsRoot).apply {
        authMethod = uploadedKey {
            userName = ""
            uploadedKey = "provenstyle"
            passphrase = ""
        }
    }

}
