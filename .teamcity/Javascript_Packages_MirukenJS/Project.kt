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
            guid              = "DCF5584D-CBD2-46F0-A92F-BBE5076BBDBC",
            parentId          = "Javascript_Packages_MirukenMVC",
            id                = "Javascript_Packages_MirukenJs_MVC",
            name              = "Mvc Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/mvc.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "5"),
            listOf(
                    JavascriptPackage(
                            id          = "mirukenMVC",
                            packageName = "miruken-mvc"
                    ))))

    subProject(configureJsProject(JavascriptProject(
            guid              = "727fe51d-4d5e-4b63-a1bf-60ad03f5986a",
            parentId          = "Javascript_Packages_MirukenJS",
            id                = "Javascript_Packages_MirukenJs_Http",
            name              = "Http Project",
            codeGithubUrl     = "git@github.com:Miruken-JS/http.git",
            majorVersion      = "0",
            minorVersion      = "0",
            patchVersion      = "1"),
            listOf(
                    JavascriptPackage(
                            id          = "mirukenHttp",
                            packageName = "miruken-http"
                    ))))
})
