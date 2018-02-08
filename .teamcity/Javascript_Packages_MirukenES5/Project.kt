package Javascript_Packages_MirukenES5

import BuildTemplates.Es5JavascriptPackage
import BuildTemplates.JavascriptProject
import BuildTemplates.configureEs5Project
import jetbrains.buildServer.configs.kotlin.v2017_2.Project

object Project : Project({
    uuid     = "6469cc81-0845-45da-b3fb-18a429b2123a"
    id       = "Javascript_Packages_MirukenES5"
    parentId = "Javascript_Packages"
    name     = "Miruken-ES5"

    subProject(configureEs5Project(JavascriptProject(
            guid              = "2992abf2-e74a-44e6-950a-30f41d7bfff5",
            parentId          = "Javascript_MirukenEs5",
            id                = "Javascript_MirukenEs5_Mirken",
            name              = "Miruken Project",
            codeGithubUrl     = "git@github.com:Miruken-ES5/miruken.git",
            majorVersion      = "2",
            minorVersion      = "0",
            patchVersion      = "24"),
            listOf(
                    Es5JavascriptPackage(
                        uuid             = "2946284d-8d74-4e4b-9c7a-cc5cad66a469",
                        id               = "mirukenEs5",
                        packageName      = "miruken-es5",
                        packageGithubUrl = "git@github.com:Miruken-ES5/miruken-es5.git",
                        unminifiedFile   = "miruken-bundle.js",
                        minifiedFile     = "miruken-bundle.min.js",
                        artifactsOut = """
                            Build.zip!/dist/miruken-bundle.js
                            Build.zip!/dist/miruken-bundle.min.js
                        """.trimIndent()
                    ),
                    Es5JavascriptPackage(
                        uuid             = "63511f1c-3e19-45b9-8063-7e0ff1bca834",
                        id               = "mirukenEs5Angular",
                        packageName      = "miruken-es5-angular",
                        packageGithubUrl = "git@github.com:Miruken-ES5/miruken-es5-angular.git",
                        unminifiedFile   = "miruken-ng-bundle.js",
                        minifiedFile     = "miruken-ng-bundle.min.js",
                        artifactsOut = """
                            Build.zip!/dist/miruken-ng-bundle.js
                            Build.zip!/dist/miruken-ng-bundle.min.js
                        """.trimIndent()
                    )
            )))
})
