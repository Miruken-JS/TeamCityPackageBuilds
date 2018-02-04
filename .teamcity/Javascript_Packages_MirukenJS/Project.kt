package Javascript_Packages_MirukenJS

import BuildTemplates.JavascriptPackage
import BuildTemplates.JavascriptProject
import BuildTemplates.configureJsProject
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid     = "0fad2714-3357-4e54-8adc-dab7cf41f57b"
    id       = "Javascript_Packages_MirukenJS"
    parentId = "Javascript_Packages"
    name     = "Miruken-JS"

    subProject(configureJsProject(JavascriptProject(
            guid              = "983f8966-9172-49d6-89b2-4ca5acbe22f8",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_Core",
            name              = "Core Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/core.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "4",
            javascriptPackages = listOf(
                    JavascriptPackage(
                            id          = "mirukenCore",
                            packageName = "miruken-core"
                    )))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "c5fd1e3a-3ee5-4adb-9695-b4fc50557247",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_Callback",
            name              = "Callback Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/callback.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "4",
            javascriptPackages = listOf(
                    JavascriptPackage(
                            id          = "mirukenCallback",
                            packageName = "miruken-callback"
                    )))))
})
