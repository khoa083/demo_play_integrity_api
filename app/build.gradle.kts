import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation)
    alias(libs.plugins.ksp)
    id("kotlin-kapt")
    alias(libs.plugins.google.services.app.level)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
}

android {
    namespace = "com.kblack.demo_play_integrity_api"
    compileSdk = ((rootProject.extra["versions"] as Map<*, *>)["target_sdk"] as Int?)!!

    signingConfigs {
        create("release") {
            val properties = Properties().apply {
                load(rootProject.file("local.properties").inputStream())
            }

            storeFile = file(properties["RELEASE_STORE_FILE"] as String)
            storePassword = properties["RELEASE_STORE_PASSWORD"] as String
            keyAlias = properties["RELEASE_KEY_ALIAS"] as String
            keyPassword = properties["RELEASE_KEY_PASSWORD"] as String
        }
    }

//    androidResources {
//        generateLocaleConfig = true
//    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.jvmArgs(
                    "-XX:+EnableDynamicAgentLoading",
                    "-XX:-PrintWarnings",
                    "-Xshare:off"
                )
            }
        }
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }

    packaging {
        dex {
            useLegacyPackaging = false
        }
        jniLibs {
            useLegacyPackaging = false
        }
        resources {
            excludes += "META-INF/*.version"
            // https://youtrack.jetbrains.com/issue/KT-48019/Bundle-Kotlin-Tooling-Metadata-into-apk-artifacts
            excludes += "kotlin-tooling-metadata.json"
            // https://github.com/Kotlin/kotlinx.coroutines?tab=readme-ov-file#avoiding-including-the-debug-infrastructure-in-the-resulting-apk
            excludes += "DebugProbesKt.bin"
        }
    }

    lint {
        lintConfig = file("lint.xml")
    }

    defaultConfig {
        applicationId = "com.kblack.demo_play_integrity_api"
        minSdk = ((rootProject.extra["versions"] as Map<*, *>)["min_sdk"] as Int?)!!
        targetSdk = ((rootProject.extra["versions"] as Map<*, *>)["target_sdk"] as Int?)!!
        // Trên store lấy version dựa theo 2 phần này
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "MY_VERSION_NAME",
            "\"$versionName${rootProject.extra["myVersionName"] as String}\""
        )
        buildConfigField(
            "String",
            "MY_COMMIT_NAME",
            "\"${rootProject.extra["commitMessage"] as String}\""
        )
        val properties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        buildConfigField("String", "base64_of_encoded_decryption_key", "\"" + properties["base64_of_encoded_decryption_key"] + "\"")
        buildConfigField("String", "base64_of_encoded_verification_key", "\"" + properties["base64_of_encoded_verification_key"] + "\"")
        buildConfigField("String", "BASE_URL", "\"" + properties["BASE_URL"] + "\"")
        buildConfigField("String", "CLOUD_PROJECT_NUMBER", "\"" + properties["CLOUD_PROJECT_NUMBER"] + "\"")
//        setProperty("archivesBaseName", "Kblack-$versionName${versionNameSuffix ?: ""}")

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            packaging {
                dex {
                    useLegacyPackaging = false
                }
                jniLibs {
                    useLegacyPackaging = false
                    keepDebugSymbols += "**/arm64-v8a/*.so"
                    keepDebugSymbols += "**/armeabi-v7a/*.so"
                    keepDebugSymbols += "**/x86/*.so"
                    keepDebugSymbols += "**/x86_64/*.so"
                }
                resources {
                    excludes += "META-INF/*.version"
                    excludes += "kotlin-tooling-metadata.json"
                    excludes += "DebugProbesKt.bin"
                }
            }
        }
        debug {
            isPseudoLocalesEnabled = true
//            applicationIdSuffix = ".debug"
            versionNameSuffix = rootProject.extra["myVersionName"] as String
        }
//        create("staging") {
//            applicationIdSuffix = ".staging"
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_23)
        }
    }
    defaultConfig{
        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    // Project dependencies
    implementation(project(":base"))

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.profileinstaller)

    // UI dependencies
    implementation(libs.material)
    implementation(libs.bundles.navigation)

    // Network dependencies
    implementation(libs.bundles.retrofit2)
    implementation(libs.bundles.okhttp)
    implementation(platform(libs.okhttp.bom))

    // Lifecycle dependencies
    implementation(libs.bundles.lifecycleAware)

    // Play Integrity and Security
    implementation(libs.playIntegrityApi)
    implementation(libs.jose4j)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.slf4j.simple)

    // Android Test dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}