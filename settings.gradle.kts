pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "News Search"
include(":app")
include(":newsapi")
include(":database")
include(":features:news-main")
include(":news-data")
include(":news-common")
include(":news-uikit")
