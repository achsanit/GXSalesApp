plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

/**
 * VERSION INFORMATION
 * version using semantic versioning -> x.y.z
 */
val versionMajor = 1 // change major ( design or flow) --> x
val versionMinor = 0 // feature count --> y
val versionPatch = 0 // bug fix/improve count --> z

android {
    namespace = "com.achsanit.gxsales"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.achsanit.gxsales"
        minSdk = 24
        targetSdk = 34
        versionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val baseApiUrl: String by project // create config for base url
        buildConfigField("String", "BASE_URL", baseApiUrl)
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val nav_version = "2.7.6" // navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    val lifecycle_version = "2.7.0" // ViewModel LiveData (lifecycle)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    val retrofit_version = "2.9.0" // retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    val chucker_version = "3.5.2" // chucker for dev logging
    implementation("com.github.chuckerteam.chucker:library:$chucker_version")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:$chucker_version")

    val koin_version = "3.5.0" // koin for DI
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-android:$koin_version")

    val coil_version = "1.3.2" // coil for load image
    implementation("io.coil-kt:coil:$coil_version")

    val data_store_pref = "1.0.0" // local storage
    implementation("androidx.datastore:datastore-preferences:$data_store_pref")

    val timber_logging = "5.0.1"
    implementation("com.jakewharton.timber:timber:$timber_logging")
}