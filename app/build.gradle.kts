plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.apollographql.apollo") version "4.2.0"
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"
}

apollo {
    service("service") {
        packageName.set("com.example.treasurehunt")
        sourceFolder = "src/main/java/com/example/treasurehunt/utils/graphql"
        srcDir("src/main/java/com/example/treasurehunt/utils/graphql")
        schemaFile = file("src/main/java/com/example/treasurehunt/utils/graphql/schema.graphqls")
        mapScalar("Double", "kotlin.Double")
        mapScalar("Float", "kotlin.Float")
    }
}

android {
    namespace = "com.example.treasurehunt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.treasurehunt"
        minSdk = 24
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
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.compose.material3:material3:1.4.0-alpha16")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.apollographql.apollo:apollo-runtime:4.2.0")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.37.3")
    implementation("androidx.core:core-splashscreen:1.0.0-beta02")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.media3:media3-common-ktx:1.6.0")
    ksp("com.google.dagger:dagger-compiler:2.56.2") // Dagger compiler
    ksp("com.google.dagger:hilt-compiler:2.56.2")
    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.core:core:1.15.0")
    implementation("androidx.core:core-role:1.1.0")
    implementation("androidx.core:core-animation:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0-RC2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("io.coil-kt.coil3:coil-compose:3.2.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.23")
    testImplementation("io.mockk:mockk:1.14.4")
    testImplementation("io.mockk:mockk-android:1.14.4")
    testImplementation("io.mockk:mockk-agent:1.14.4")
    testImplementation("com.google.dagger:hilt-android-testing:2.56.2")
    testAnnotationProcessor("com.google.dagger:hilt-compiler:2.56.2")
    androidTestAnnotationProcessor("com.google.dagger:hilt-compiler:2.56.2")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.56.2")
    androidTestImplementation("androidx.core:core-animation-testing:1.0.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    testImplementation(kotlin("test"))
}
