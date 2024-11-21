plugins {
    id("com.android.application")

}

android {
    namespace = "com.example.ammoroulette"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ammoroulette"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src\\main\\java", "src\\main\\java\\Activity", "src\\main\\java", "src\\main\\java\\Fragment", "src\\main\\java", "src\\main\\java\\Utility")
            }
        }
    }
}

dependencies {

    //Dependencies for Blur and GIF
    implementation("com.github.furkankaplan:fk-blur-view-android:1.0.1")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.23")

    //Dependencies for Shake and Easing
    implementation("com.daimajia.easing:library:2.0@aar")
    implementation("com.daimajia.androidanimations:library:2.3@aar")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}