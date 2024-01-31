import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.qure.hilt")
}

val keystorePropertiesFile = rootProject.file("local.properties")
val properties = Properties()
properties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.qure.fishingmemory"
    compileSdk = 34

    val kakaoApiKey = properties["kakao_api_key"] as? String ?: ""
    val kakaoNativeAppKey = properties["kakao_native_app_key"] as? String ?: ""
    val firebaseApiKey = properties["firebase_api_key"] as? String ?: ""
    val firebaseDatabaseUrl = properties["firebase_database_url"] as? String ?: ""
    val firebaseStorageUrl = properties["firebase_storage_url"] as? String ?: ""
    val firebaseDatabaseProjectId = properties["firebase_database_project_id"] as? String ?: ""
    val weatherApiKey = properties["weather_api_key"] as? String ?: ""
    val weatherDatabaseUrl = properties["weather_database_url"] as? String ?: ""
    val naverMapBaseUrl = properties["naver_map_base_url"] as? String ?: ""
    val naverMapApiClientId = properties["naver_map_api_client_id"] as? String ?: ""
    val naverMapApiClientSecret = properties["naver_map_api_client_secret"] as? String ?: ""

    val removeQuotationKakaoApiKey = kakaoApiKey.replace("\"", "")
    val removeQuotationNaverMapApiClientId = naverMapApiClientId.replace("\"", "")
    val removeQuotationKakaoNativeAppkey = kakaoNativeAppKey.replace("\"", "")

    defaultConfig {
        applicationId = "com.qure.fishingmemory"
        minSdk = 26
        targetSdk = 34
        versionCode = 7
        versionName = "1.0.0"

        buildConfigField("String", "KAKAO_API_KEY", "$kakaoApiKey")
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "$kakaoNativeAppKey")
        buildConfigField("String", "FIREBASE_API_KEY", "$firebaseApiKey")
        buildConfigField("String", "FIREBASE_DATABASE_URL", "$firebaseDatabaseUrl")
        buildConfigField("String", "FIREBASE_STORAGE_URL", "$firebaseStorageUrl")
        buildConfigField("String", "FIREBASE_DATABASE_PROJECT_ID", "$firebaseDatabaseProjectId")
        buildConfigField("String", "WEATHER_API_KEY", "$weatherApiKey")
        buildConfigField("String", "WEATHER_DATABASE_URL", "$weatherDatabaseUrl")
        buildConfigField("String", "NAVER_MAP_BASE_URL", "$naverMapBaseUrl")
        buildConfigField("String", "NAVER_MAP_API_CLIENT_ID", "$naverMapApiClientId")
        buildConfigField("String", "NAVER_MAP_API_CLIENT_SECRET", "$naverMapApiClientSecret")
        manifestPlaceholders["KAKAO_API_KEY"] = removeQuotationKakaoApiKey
        manifestPlaceholders["NAVER_MAP_API_CLIENT_ID"] = removeQuotationNaverMapApiClientId
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = removeQuotationKakaoNativeAppkey
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = properties["key_alias"] as String
            keyPassword = properties["key_password"] as String
            storeFile = file(properties["store_file"] as String)
            storePassword = properties["store_password"] as String
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
//        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    lint {
        checkReleaseBuilds = false
    }

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.7"
//    }
}

val ktlint by configurations.creating
val detekt by configurations.creating

dependencies {
    ktlint(libs.ktlint) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
    detekt(libs.detekt)

    implementation(project(":build-property"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":navigator"))
    implementation(project(":presentation:splash"))
    implementation(project(":presentation:onboarding"))
    implementation(project(":presentation:login"))
    implementation(project(":presentation:home"))
    implementation(project(":presentation:history"))
    implementation(project(":presentation:create"))
    implementation(project(":presentation:memo"))
    implementation(project(":presentation:map"))
    implementation(project(":presentation:fishingspot"))
    implementation(project(":presentation:program_information"))
    implementation(project(":presentation:mypage"))
    implementation(project(":presentation:gallery"))
    implementation(project(":presentation:permission"))

    api(libs.jetpack.appCompat)
    api(libs.timber)
    api(libs.navermap.sdk)
    api(libs.kakao.user)
    api(libs.kakao.story)
    api(libs.kakao.share)
    api(libs.kakao.talk)
}

val ktlintCheck by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "**/src/main/**/*.kt",
    )
}

tasks.check {
    dependsOn(ktlintCheck)
}

tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    args(
        "-F",
        "**/src/main/**/*.kt",
    )
}

val detektTask by tasks.registering(JavaExec::class) {
    classpath = detekt

    val input = projectDir
    val config = "$projectDir/detekt.yml"
    val exclude = ".*/build/.*,.*/resources/.*"
    val params = listOf("-i", input, "-c", config, "-ex", exclude)

    args(params)
}

tasks.check {
    dependsOn(detektTask)
}
