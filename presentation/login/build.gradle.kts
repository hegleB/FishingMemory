//import java.io.FileInputStream
//import java.util.Properties
//
//plugins {
//    id("com.qure.feature")
//    alias(libs.plugins.google.services)
//}
//
//val localPropertiesFile = rootProject.file("local.properties")
//val localProperties = Properties()
//localProperties.load(FileInputStream(localPropertiesFile))
//
//android {
//    namespace = "com.qure.login"
//    val removeQuotationKakaoApiKey =
//        properties["KAKAO_API_KEY"]?.let { id -> (id as String).replace("\"", "") } ?: ""
//    defaultConfig {
//        buildConfigField("String", "KAKAO_API_KEY", localProperties["kakao_api_key"] as String)
//        manifestPlaceholders["KAKAO_API_KEY"] = removeQuotationKakaoApiKey
//    }
//    buildFeatures {
//        dataBinding = true
//        buildConfig = true
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.7"
//    }
//}
//
//dependencies {
////    implementation(project(":build-property"))
////    implementation(project(":core-design"))
////    implementation(project(":core"))
////    implementation(project(":navigator"))
////    implementation(project(":domain"))
////    implementation(project(":data"))
////
////    api(libs.kakao.user)
////    api(libs.kakao.talk)
////    api(libs.kakao.story)
////    api(libs.kakao.share)
////    api(libs.jetpack.activity)
////    api(libs.network.core)
////    api(libs.network.adapter)
////    api(libs.network.interceptor)
////    api(platform(libs.firebase.bom))
////    api(libs.firebase.auth)
////    api(libs.firebase.analytics)
//}
