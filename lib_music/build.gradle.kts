plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.handsome.lib.music"
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

        testInstrumentationRunner = BuildConfig.testInstrumentationRunner
//        consumerProguardFiles("consumer-rules.pro")
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
}

dependAndroidCommonBase()
dependAndroidKtx()
dependTestBase()
dependNetwork()
dependRoom()
dependencies {
    implementation(project(":lib_util"))

    implementation(Rxjava.rxjava3_android)
    implementation(Rxjava.rxjava3_kotlin)
    implementation(Network.adapter_rxjava3)
    implementation(Lifecycle.viewmodel_ktx)
    implementation(Lifecycle.runtime_ktx)
    implementation(Network.converter_gson)

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
    implementation ("androidx.room:room-common:2.5.1")
}
