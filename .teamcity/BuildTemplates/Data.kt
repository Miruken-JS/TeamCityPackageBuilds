package BuildTemplates

class JavascriptProject(
        val guid:           String,
        val id:             String,
        val parentId:       String,
        val name:           String,
        val codeGithubUrl:  String,
        val majorVersion:   String,
        val minorVersion:   String,
        val patchVersion:   String) {

    val ciVcsRootId: String
        get() = "${id}_CIVCSRoot"

    val preReleaseVcsRootId: String
        get() = "${id}_PreReleaseVCSRoot"

    val releaseVcsRootId: String
        get() = "${id}_ReleaseVCSRoot"

    val ciBuildId: String
        get() = "${id}_CIBuild"

    val preReleaseBuildId: String
        get() = "${id}_PreReleaseBuild"

    val releaseBuildId: String
        get() = "${id}_ReleaseBuild"

    val deploymentProjectId: String
        get() = "${id}_DeploymentProject"
}

class JavascriptPackage(
        val id:          String,
        val packageName: String)

class Es5JavascriptPackage (
        val id:               String,
        val packageName:      String,
        val packageGithubUrl: String,
        val artifactsOut:     String)


