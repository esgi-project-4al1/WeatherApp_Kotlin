plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.app_meteo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app_meteo"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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


    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Retrofit  && Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("junit:junit:4.12")
    // Dépendance pour les tests unitaires
    testImplementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Dépendance pour les tests avec JUnit
    testImplementation ("junit:junit:4.13.2")
    // Dépendance pour Mockito
    testImplementation ("org.mockito:mockito-core:3.12.4")
    // Dépendance pour kotlinx-coroutines-test (pour les tests coroutine)
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.0")
    // Dépendance pour okhttp3 mockwebserver (pour les tests Retrofit)
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.9.1")


    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}