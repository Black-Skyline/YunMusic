plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.handsome.yunmusic"
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        applicationId = "com.handsome.yunmusic"
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        testInstrumentationRunner = BuildConfig.testInstrumentationRunner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kapt {
        arguments {
            arg("AROUTER_MODULE_NAME", project.name)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable=true
    }
}

dependTestBase()
dependencies {
    implementation(project(":lib_util"))
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
    kapt(ARouter.arouter_compiler)

    if(BuildConfig.isRelease) {
        implementation(project(":lib_music"))
        implementation(project(":lib_mv"))
        implementation(project(":lib_search"))

        implementation(project(":module_login"))
        implementation(project(":module_find"))
        implementation(project(":module_mine"))
        implementation(project(":module_podcast"))
    }
}