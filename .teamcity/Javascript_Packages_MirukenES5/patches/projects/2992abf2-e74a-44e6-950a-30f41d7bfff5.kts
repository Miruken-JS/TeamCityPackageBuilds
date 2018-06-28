package Javascript_Packages_MirukenES5.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '2992abf2-e74a-44e6-950a-30f41d7bfff5' (id = 'Javascript_MirukenEs5_Mirken')
accordingly and delete the patch script.
*/
changeProject("2992abf2-e74a-44e6-950a-30f41d7bfff5") {
    params {
        expect {
            param("PatchVersion", "24")
        }
        update {
            param("PatchVersion", "43")
        }
    }
}
