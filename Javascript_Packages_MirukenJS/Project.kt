//package Javascript_Packages_MirukenJS
//
//import BuildTemplates.JavascriptPackage
//import BuildTemplates.JavascriptProject
//import BuildTemplates.configureJavascriptProject
//import Javascript_Packages.vcsRoots.Javascript_MirukenJs_TeamCityPackageBuilds
//import jetbrains.buildServer.configs.kotlin.v2017_2.Project
//import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.VersionedSettings
//import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.versionedSettings
//
//object Project : Project({
//    uuid     = "9e077ab3-491d-42b9-9210-84dd56e29fc8"
//    id       = "Javascript_Packages_MirukenJS"
//    parentId = "Javascript_Packages"
//    name     = "Packages"
//
//    subProject(configureJavascriptProject(JavascriptProject(
//            guid              = "983f8966-9172-49d6-89b2-4ca5acbe22f8",
//            parentId          = "Javascript_Packages_MirukenJS",
//            id                = "Javascript_Packages_MirukenJs_Core",
//            name              = "Core Project",
//            codeGithubUrl     = "git@github.com:Miruken-JS/core.git",
//            majorVersion      = "0",
//            minorVersion      = "0",
//            patchVersion      = "4",
//            javascriptPackages = listOf(
//                    JavascriptPackage(
//                            id          = "mirukenCore",
//                            packageName = "miruken-core"
//                    )))))
//
//    subProject(configureJavascriptProject(JavascriptProject(
//            guid              = "c5fd1e3a-3ee5-4adb-9695-b4fc50557247",
//            parentId          = "Javascript_Packages_MirukenJS",
//            id                = "Javascript_Packages_MirukenJs_Callback",
//            name              = "Callback Project",
//            codeGithubUrl     = "git@github.com:Miruken-JS/callback.git",
//            majorVersion      = "0",
//            minorVersion      = "0",
//            patchVersion      = "4",
//            javascriptPackages = listOf(
//                    JavascriptPackage(
//                            id          = "mirukenCallback",
//                            packageName = "miruken-callback"
//            )))))
//})
