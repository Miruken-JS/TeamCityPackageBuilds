package Javascript_Packages_MirukenES5.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '2992abf2-e74a-44e6-950a-30f41d7bfff5_mirukenEs5_DeployPreRelease' (id = 'Javascript_MirukenEs5_Mirken_mirukenEs5_DeployPreRelease')
accordingly and delete the patch script.
*/
changeBuildType("2992abf2-e74a-44e6-950a-30f41d7bfff5_mirukenEs5_DeployPreRelease") {
    check(paused == false) {
        "Unexpected paused: '$paused'"
    }
    paused = true

    vcs {

        check(checkoutMode == CheckoutMode.AUTO) {
            "Unexpected option value: checkoutMode = $checkoutMode"
        }
        checkoutMode = CheckoutMode.ON_AGENT

        check(cleanCheckout == false) {
            "Unexpected option value: cleanCheckout = $cleanCheckout"
        }
        cleanCheckout = true

        add("Javascript_MirukenEs5_Mirken_mirukenEs5_MirukenEs5")
    }
}
