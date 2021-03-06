package Javascript_Packages_MirukenJS.patches.projects

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the project with uuid = '727fe51d-4d5e-4b63-a1bf-60ad03f5986a' (id = 'Javascript_Packages_MirukenJs_Http')
accordingly, and delete the patch script.
*/
changeProject("727fe51d-4d5e-4b63-a1bf-60ad03f5986a") {
    params {
        expect {
            param("MajorVersion", "0")
        }
        update {
            param("MajorVersion", "2")
        }
        expect {
            param("PatchVersion", "1")
        }
        update {
            param("PatchVersion", "3")
        }
    }
}
