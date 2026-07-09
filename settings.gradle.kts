pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WinduskEcosystem"
include(":ecosystem")
include(":basics")
include(":componentBasics")
include(":biteUi")
include(":lazyBundle")
include(":biteUiRender")
include(":clientBasics")
include(":testclientapp")
include(":testcomponentapp")
include(":sharedFile")
