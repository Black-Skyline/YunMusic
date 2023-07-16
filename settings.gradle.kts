pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://www.jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://www.jitpack.io")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
    }
}
rootProject.name = "YunMusic"
include(":app")
include(":module_login")
include(":lib_util")
include(":module_find")
include(":module_podcast")
include(":module_mine")
include(":lib_music")
include(":lib_mv")
include(":lib_search")
