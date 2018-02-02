package Javascript_MirukenEs5

import BuildTemplates.JavascriptPackage
import BuildTemplates.JavascriptProject
import BuildTemplates.configureJavascriptProject
import Javascript_MirukenJs.vcsRoots.Javascript_MirukenJs_TeamCityPackageBuilds
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.versionedSettings

object Project : Project({
    uuid = "6469cc81-0845-45da-b3fb-18a429b2123a"
    id = "Javascript_MirukenEs5_2"
    parentId = "Javascript"
    name = "Miruken-ES5 2"

    subProject(configureJavascriptProject(JavascriptProject(
            guid              = "2992abf2-e74a-44e6-950a-30f41d7bfff5",
            parentId          = "Javascript_MirukenEs5_2",
            id                = "Javascript_MirukenEs5_Mirken",
            name              = "Miruken Project",
            codeGithubUrl     = "git@github.com:Miruken-ES5/miruken.git",
            majorVersion      = "2",
            minorVersion      = "0",
            patchVersion      = "24",
            javascriptPackages = listOf(
                    JavascriptPackage(
                        id          = "mirukenEs5Angular",
                        packageName = "miruken-es5-angular"
                    ),
                    JavascriptPackage(
                        id          = "mirukenEs5Angular",
                        packageName = "miruken-es5-angular"
                    )
            ))))

})
