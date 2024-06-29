// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("androidx.room") version "2.6.1" apply false
    // Stick with this version until a new Kotlin plugin is released (>1.9.20)
    // or a new KSP version is pushed for this Kotlin version (>1.0.14)
    //noinspection GradleDependency
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}