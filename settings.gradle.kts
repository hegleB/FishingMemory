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
        maven(url = "https://naver.jfrog.io/artifactory/maven/")
        maven(url = "https://plugins.gradle.org/m2/")
    }
}
rootProject.name = "fishingmemory"
include(":app")
include(":data")
include(":domain")
include(":navigator")
include(":presentation")
include(":presentation:splash")
include(":core")
include(":presentation:onboarding")
include(":core-design")
include(":presentation:login")
include(":presentation:home")
include(":build-property")
include(":presentation:history")
include(":presentation:create")
include(":presentation:memo")
include(":presentation:map")
include(":presentation:fishingspot")
include(":presentation:mypage")
include(":presentation:program_information")
include(":presentation:gallery")
include(":presentation:permission")
include(":testing")
include(
    ":core:designsystem",
    ":core:model",
    ":core:data",
    ":core:domain",
    ":core:ui",
    ":core:testing",
)
include(
    ":feature:create",
    ":feature:fishingspot",
