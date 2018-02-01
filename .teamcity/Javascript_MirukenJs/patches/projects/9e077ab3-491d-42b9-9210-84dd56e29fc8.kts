package Javascript_MirukenJs.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '9e077ab3-491d-42b9-9210-84dd56e29fc8' (id = 'Javascript_MirukenJs')
accordingly and delete the patch script.
*/
changeProject("9e077ab3-491d-42b9-9210-84dd56e29fc8") {
    params {
        add {
            param("PrereleaseVersion", "-alpha.%build.counter%")
        }
        add {
            param("SHA", "%GitShortHash%")
        }
    }
}
