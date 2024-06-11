pluginManagement {
    includeBuild("build-logic")
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
        maven(url = "https://jitpack.io")
        maven(url = "https://devrepo.kakao.com/nexus/content/groups/public/")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://repository.map.naver.com/archive/maven")
    }
}
rootProject.name = "fishingmemory"
include(":app")

include(":build-property")

include(
    ":core:designsystem",
    ":core:model",
    ":core:data",
    ":core:domain",
    ":core:navigation",
    ":core:ui",
    ":core:testing",
)
include(
    ":feature:main",
    ":feature:splash",
    ":feature:home",
    ":feature:history",
    ":feature:mypage",
    ":feature:onboarding",
    ":feature:login",
    ":feature:permission",
    ":feature:create",
    ":feature:fishingspot",
    ":feature:gallery",
    ":feature:map",
    ":feature:memo",
    ":feature:program-information",
    ":feature:camera",
)
