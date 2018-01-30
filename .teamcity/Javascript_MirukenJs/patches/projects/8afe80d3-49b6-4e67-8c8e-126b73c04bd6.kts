package Javascript_MirukenJs.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, create a project with uuid = '8afe80d3-49b6-4e67-8c8e-126b73c04bd6' (id = 'Javascript_MirukenJs_Core')
in the project with uuid = '9e077ab3-491d-42b9-9210-84dd56e29fc8' and delete the patch script.
*/
create("9e077ab3-491d-42b9-9210-84dd56e29fc8", Project({
    uuid = "8afe80d3-49b6-4e67-8c8e-126b73c04bd6"
    id = "Javascript_MirukenJs_Core"
    parentId = "Javascript_MirukenJs"
    name = "Core"
}))
