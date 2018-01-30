package Javascript_MirukenJs.patches.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, create a vcsRoot with uuid = '74842ec8-07a9-466e-b029-d88a8d3150e0' (id = 'Javascript_MirukenJs_Core_CoreGitCi')
in the project with uuid = '8afe80d3-49b6-4e67-8c8e-126b73c04bd6' and delete the patch script.
*/
create("8afe80d3-49b6-4e67-8c8e-126b73c04bd6", GitVcsRoot({
    uuid = "74842ec8-07a9-466e-b029-d88a8d3150e0"
    id = "Javascript_MirukenJs_Core_CoreGitCi"
    name = "core.git_CI"
    url = "git@github.com:Miruken-JS/core.git"
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
}))
