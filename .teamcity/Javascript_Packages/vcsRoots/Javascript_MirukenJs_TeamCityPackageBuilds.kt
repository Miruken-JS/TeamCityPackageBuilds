package Javascript_Packages.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

object Javascript_MirukenJs_TeamCityPackageBuilds : GitVcsRoot({
    uuid = "8f46b5f7-cb74-40dd-8c19-59f2cb04d23b"
    id = "Javascript_MirukenJs_TeamCityPackageBuilds"
    name = "TeamCityPackageBuilds"
    url = "git@github.com:Miruken-JS/TeamCityPackageBuilds.git"
    authMethod = uploadedKey {
        uploadedKey = "provenstyle"
    }
})
