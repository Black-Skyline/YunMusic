// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version Gradle.AGP_version apply false
    id("com.android.library") version Gradle.AGP_version apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}