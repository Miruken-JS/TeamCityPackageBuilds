package Javascript_Packages_MirukenJS.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '983f8966-9172-49d6-89b2-4ca5acbe22f8_mirukenCore_DeployPreRelease' (id = 'Javascript_Packages_MirukenJs_Core_mirukenCore_DeployPreRelease')
accordingly and delete the patch script.
*/
changeBuildType("983f8966-9172-49d6-89b2-4ca5acbe22f8_mirukenCore_DeployPreRelease") {
    check(paused == false) {
        "Unexpected paused: '$paused'"
    }
    paused = true
}
