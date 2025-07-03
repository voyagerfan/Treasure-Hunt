// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("lifecycle_version", "2.6.2")
    }
}

plugins {
    id("com.android.application") version "8.10.1" apply false
    id("org.jetbrains.kotlin.android") version "2.1.21" apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" apply false
}
