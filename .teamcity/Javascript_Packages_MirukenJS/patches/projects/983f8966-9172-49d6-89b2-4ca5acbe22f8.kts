package Javascript_Packages_MirukenJS.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '983f8966-9172-49d6-89b2-4ca5acbe22f8' (id = 'Javascript_Packages_MirukenJs_Core')
accordingly, and delete the patch script.
*/
changeProject("983f8966-9172-49d6-89b2-4ca5acbe22f8") {
    params {
        expect {
            param("MajorVersion", "0")
        }
        update {
            param("MajorVersion", "2")
        }
        expect {
            param("PatchVersion", "5")
        }
        update {
            param("PatchVersion", "0")
        }
    }
}
