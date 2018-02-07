package Javascript_Packages_MirukenES5.patches.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, create a vcsRoot with uuid = 'fbc1afcc-d2ad-42de-b4af-04d6eef89960' (id = 'Javascript_MirukenEs5_Mirken_mirukenEs5_MirukenEs5mirukenEs5')
in the project with uuid = '2992abf2-e74a-44e6-950a-30f41d7bfff5_mirukenEs5' and delete the patch script.
*/
create("2992abf2-e74a-44e6-950a-30f41d7bfff5_mirukenEs5", GitVcsRoot({
    uuid = "fbc1afcc-d2ad-42de-b4af-04d6eef89960"
    id = "Javascript_MirukenEs5_Mirken_mirukenEs5_MirukenEs5mirukenEs5"
    name = "Miruken-ES5/miruken-es5"
    url = "git@github.com:Miruken-ES5/miruken-es5.git"
    branchSpec = "%DefaultBranch%"
    agentCleanPolicy = GitVcsRoot.AgentCleanPolicy.ALWAYS
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
}))

