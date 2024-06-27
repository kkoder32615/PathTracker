// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("androidx.room") version "2.6.1" apply false
    // Stick with this version until a new plugin is released (>1.9.20) or a new fix is pushed for this version (>1.0.14 11/2/2023)
    //noinspection GradleDependency
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}