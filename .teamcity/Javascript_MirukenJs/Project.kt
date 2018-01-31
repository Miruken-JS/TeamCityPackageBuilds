package Javascript_MirukenJs

import BuildTemplates.JavascriptPackage
import BuildTemplates.JavascriptProject
import BuildTemplates.configureJavascriptProject
import Javascript_MirukenJs.vcsRoots.Javascript_MirukenJs_TeamCityPackageBuilds
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

    subProject(configureJavascriptProject(JavascriptProject(
            guid              = "983f8966-9172-49d6-89b2-4ca5acbe22f8",
            parentId          = "Javascript_MirukenJs",
            id                = "Javascript_MirukenJs_Core2",
            name              = "Core Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/core.git",
            npmApiKey         = "%MirukenNugetApiKey%",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "4",
            javascriptPackages = listOf(
                    JavascriptPackage(
                            id          = "mirukenCore",
                            packageName = "miruken-core"
            )))))
})
