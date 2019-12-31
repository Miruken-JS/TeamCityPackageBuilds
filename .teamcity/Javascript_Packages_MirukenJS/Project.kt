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
            patchVersion      = "5"),
            listOf(
                JavascriptPackage(
                        id          = "mirukenCore",
                        packageName = "miruken-core"
                ))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "c5fd1e3a-3ee5-4adb-9695-b4fc50557247",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_Callback",
            name              = "Callback Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/callback.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "5"),
            listOf(
                    JavascriptPackage(
                            id          = "mirukenCallback",
                            packageName = "miruken-callback"
                    ))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "1FAAD777-65EB-4FBD-8D19-3D31C4C2F313",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_Context",
            name              = "Context Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/context.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "5"),
            listOf(
                    JavascriptPackage(
                            id          = "mirukenContext",
                            packageName = "miruken-context"
                    ))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "5C3AC82A-9C35-4C22-AE73-ADE208141348",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_Map",
            name              = "Map Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/map.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "5"),
            listOf(
                JavascriptPackage(
                        id          = "mirukenMap",
                        packageName = "miruken-map"
                ))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "D611F92E-2EEC-4F80-9B0D-0B34F6D0FEB0",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_Error",
            name              = "Error Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/error.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "5"),
            listOf(
                    JavascriptPackage(
                            id          = "mirukenError",
                            packageName = "miruken-error"
                    ))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "12DB5C1F-FB26-40ED-8F42-662DB4EE8124",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_Validate",
            name              = "Validate Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/validate.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "5"),
            listOf(
                    JavascriptPackage(
                            id          = "mirukenValidate",
                            packageName = "miruken-validate"
                    ))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "E132AC35-5F9F-4933-B579-E713B4C8F33F",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_IOC",
            name              = "IOC Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/ioc.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "5"),
            listOf(
                    JavascriptPackage(
                            id          = "mirukenIOC",
                            packageName = "miruken-ioc"
                    ))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "DCF5584D-CBD2-46F0-A92F-BBE5076BBDBC",
            parentId          = "Javascript_Packages_MirukenMVC",
            id                = "Javascript_Packages_MirukenJs_MVC",
            name              = "MVC Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/mvc.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "5"),
            listOf(
                    JavascriptPackage(
                            id          = "mirukenMVC",
                            packageName = "miruken-mvc"
                    ))))
})
