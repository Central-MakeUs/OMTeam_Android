pluginManagement {
    includeBuild("build-logic")
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "OMTeam"
include(":app")
include(":core:domain")
include(":core:data")
include(":core:datastore")
include(":core:designsystem")
include(":core:network")
include(":core:presentation")
include(":feature:login:api")
include(":feature:login:impl")
include(":feature:main:api")
include(":feature:main:impl")
include(":feature:onboarding:api")
include(":feature:onboarding:impl")
