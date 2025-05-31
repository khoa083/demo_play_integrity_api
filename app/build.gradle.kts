plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation)
    alias(libs.plugins.ksp)
    alias(libs.plugins.baselineprofile)
    id("kotlin-kapt")
}

android {
    namespace = "com.kblack.demo_play_integrity_api"
    compileSdk = ((rootProject.extra["versions"] as Map<*, *>)["target_sdk"] as Int?)!!

    signingConfigs {

    }

//    androidResources {
//        generateLocaleConfig = true
//    }

    buildFeatures {
        buildConfig = true
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

//    baselineProfile {
//        dexLayoutOptimization = true
//    }

    defaultConfig {
        applicationId = "com.kblack.demo_play_integrity_api"
        minSdk = ((rootProject.extra["versions"] as Map<*, *>)["min_sdk"] as Int?)!!
        targetSdk = ((rootProject.extra["versions"] as Map<*, *>)["target_sdk"] as Int?)!!
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
        setProperty("archivesBaseName", "ToeicK-$versionName${versionNameSuffix ?: ""}")
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
        }
        debug {
            isPseudoLocalesEnabled = true
            applicationIdSuffix = ".debug"
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
    kotlinOptions {
        jvmTarget = "23"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    defaultConfig{
        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures {
        buildConfig = true
    }

}

dependencies {

    implementation(project(":base"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.bundles.navigation)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.profileinstaller)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.bundles.retrofit2)
    implementation(libs.bundles.okhttp)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.lifecycleAware)
    implementation(libs.playIntegrityApi)
    "baselineProfile"(project(":baselineprofile"))
}

kapt {
    correctErrorTypes = true
}