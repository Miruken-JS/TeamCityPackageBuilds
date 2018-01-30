package Javascript_MirukenJs

import Javascript_MirukenJs.vcsRoots.*
import Javascript_MirukenJs.vcsRoots.Javascript_MirukenJs_TeamCityPackageBuilds
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.versionedSettings

object Project : Project({
    uuid = "9e077ab3-491d-42b9-9210-84dd56e29fc8"
    id = "Javascript_MirukenJs"
    parentId = "Javascript"
    name = "Miruken-JS"

    vcsRoot(Javascript_MirukenJs_TeamCityPackageBuilds)

    features {
        versionedSettings {
            id = "PROJECT_EXT_3"
            mode = VersionedSettings.Mode.ENABLED
            buildSettingsMode = VersionedSettings.BuildSettingsMode.USE_CURRENT_SETTINGS
            rootExtId = Javascript_MirukenJs_TeamCityPackageBuilds.id
            showChanges = false
            settingsFormat = VersionedSettings.Format.KOTLIN
            storeSecureParamsOutsideOfVcs = true
        }
    }
})
