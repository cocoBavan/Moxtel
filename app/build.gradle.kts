plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.bltech.moxtel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bltech.moxtel"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifeCycleRuntime)
    implementation(Dependencies.composeActivity)
    implementation(platform(Dependencies.composeBom))
    implementation(Dependencies.composeUI)
    implementation(Dependencies.composeUIGraphics)
    implementation(Dependencies.composeUIToolingPreview)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeNavigation)

    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitGson)
    implementation(Dependencies.httpLoggingInterceptor)

    implementation(Dependencies.coreSplash)

    implementation(Dependencies.hiltNavigation)

    implementation(Dependencies.coroutines)
    implementation(Dependencies.composeLifeCycleRuntime)

    implementation(Dependencies.asyncImage)
    implementation(Dependencies.asyncImageComposeExtension)

    implementation(Dependencies.exoPlayer)
    implementation(Dependencies.exoPlayerUI)
    implementation(Dependencies.exoPlayerHLS)

    debugImplementation(Dependencies.composeUITooling)
    debugImplementation(Dependencies.uiTestManifest)

    implementation(Dependencies.roomRuntime)
    annotationProcessor(Dependencies.roomCompiler)
    kapt(Dependencies.roomCompiler)
    implementation(Dependencies.roomCoroutine)

    testImplementation(Dependencies.unitTest)

    androidTestImplementation(Dependencies.uiTest)
    androidTestImplementation(Dependencies.uiTestExpresso)
    androidTestImplementation(platform(Dependencies.composeBom))
    androidTestImplementation(Dependencies.uiTestCompose)

    implementation(Dependencies.hilt)
    kapt(Dependencies.hiltCompiler)

    testImplementation(Dependencies.unitTest)
    testImplementation(Dependencies.unitTestMockk)
    testImplementation(Dependencies.unitTestMockito)

}

kapt {
    correctErrorTypes = true
}
