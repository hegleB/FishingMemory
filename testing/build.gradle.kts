plugins {
    id("com.qure.library")
    id("com.qure.hilt")
}

android {
    namespace = "com.example.testing"
}

dependencies {
    api(libs.test.coruntines)
    api(libs.test.junit)
}