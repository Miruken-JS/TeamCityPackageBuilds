package Javascript_Packages

import Javascript_Packages.vcsRoots.Javascript_MirukenJs_TeamCityPackageBuilds
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.versionedSettings

object Project : Project({
    uuid     = "c3a61f40-54ed-43bc-9b0b-21a4a70c331c"
    id       = "Javascript_Packages"
    parentId = "Javascript"
    name     = "Packages"

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
