package Javascript_MirukenJs.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, create a buildType with uuid = '7cfa874a-8d9e-4749-842f-06ca5d52e26d' (id = 'Javascript_MirukenJs_Core_CiBuild')
in the project with uuid = '8afe80d3-49b6-4e67-8c8e-126b73c04bd6' and delete the patch script.
*/
create("8afe80d3-49b6-4e67-8c8e-126b73c04bd6", BuildType({
    uuid = "7cfa874a-8d9e-4749-842f-06ca5d52e26d"
    id = "Javascript_MirukenJs_Core_CiBuild"
    name = "CI Build"

    steps {
        script {
            name = "Yarn Install"
            scriptContent = "%yarn% install"
        }
        script {
            name = "JSPM Install"
            scriptContent = "%JSPM% Install"
        }
    }
}))

