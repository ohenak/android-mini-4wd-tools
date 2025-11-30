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

rootProject.name = "Mini4WDLab"
include(":app")
include(":core:database")
include(":core:common")
include(":feature:profile")
include(":feature:timing")
include(":feature:rpm")
include(":feature:analytics")
include(":core:database")
include(":core:common")
include(":feature:profile")
include(":feature:timing")
include(":feature:rpm")
include(":feature:analytics")
