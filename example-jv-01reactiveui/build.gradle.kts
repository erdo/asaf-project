import co.early.fore.Shared

plugins {
    id("com.android.application")
    id("maven")
    id("idea")
}

android {

    compileOptions {
        sourceCompatibility = Shared.Android.javaVersion
        targetCompatibility = Shared.Android.javaVersion
    }

    compileSdkVersion(Shared.Android.compileSdkVersion)

//    signingConfigs {
//        create("release") {
//            storeFile = file("../../keystore/debug.keystore")
//            storePassword = "android"
//            keyAlias = "android"
//            keyPassword = "android"
//        }
//    }

    defaultConfig {
        applicationId = "foo.bar.example.foredatabinding"
        minSdkVersion(Shared.Android.minSdkVersion)
        targetSdkVersion(Shared.Android.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
  //          signingConfig = signingConfigs.getByName("release")
        }
    }
    lintOptions {
        isAbortOnError = true
        lintConfig = File(project.rootDir, "lint-examples.xml")
    }
}

repositories {
    jcenter()
    mavenCentral()
    google()
}

dependencies {

    //implementation("co.early.fore:fore-core:${Shared.Versions.fore_version_for_examples}")
    implementation(project(":fore-core"))

    annotationProcessor("com.jakewharton:butterknife-compiler:${Shared.Versions.butterknife}")
    //noinspection AnnotationProcessorOnCompilePath
    implementation("com.jakewharton:butterknife:${Shared.Versions.butterknife}")

    implementation("androidx.appcompat:appcompat:${Shared.Versions.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Shared.Versions.constraintlayout}")

    testImplementation("junit:junit:${Shared.Versions.junit}")
    testImplementation("org.mockito:mockito-core:${Shared.Versions.mockito_core}")
    testImplementation("org.hamcrest:hamcrest-library:${Shared.Versions.hamcrest_library}")

    androidTestImplementation("org.mockito:mockito-core:${Shared.Versions.mockito_core}")
    androidTestImplementation("com.linkedin.dexmaker:dexmaker-mockito:${Shared.Versions.dexmaker_mockito}")
    androidTestImplementation("androidx.test:runner:${Shared.Versions.androidxtest}")
    androidTestImplementation("androidx.test:rules:${Shared.Versions.androidxtest}")
    androidTestImplementation("androidx.annotation:annotation:${Shared.Versions.annotation}")
    androidTestImplementation("androidx.core:core:${Shared.Versions.android_core}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Shared.Versions.espresso_core}") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
}