import co.early.fore.Config_gradle.Shared

plugins {
    id("com.android.application")
    id("maven")
    id("idea")
    //id("com.getkeepsafe.dexcount") //uncomment for library method stats)
}

android {

    compileOptions {
        sourceCompatibility = Shared.Android.javaVersion
        targetCompatibility = Shared.Android.javaVersion
    }

    compileSdkVersion(Shared.Android.compileSdkVersion)

    lintOptions {
       isAbortOnError = true
       lintConfig = File(project.rootDir, "lint-examples.xml")
    }

    defaultConfig {
        applicationId = "foo.bar.example.foreui"
        minSdkVersion(Shared.Android.minSdkVersion)
        targetSdkVersion(Shared.Android.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

repositories {
    jcenter()
    mavenCentral()
    google()
}

dependencies {

    annotationProcessor("com.jakewharton:butterknife-compiler:${Shared.Versions.butterknife}")
    //noinspection AnnotationProcessorOnCompilePath
    implementation("com.jakewharton:butterknife:${Shared.Versions.butterknife}")

    implementation("co.early.fore:fore-core:${Shared.Versions.fore_version_for_examples}")
    implementation("co.early.fore:fore-lifecycle:${Shared.Versions.fore_version_for_examples}")
    //// implementation(project(":fore-core"))
    //implementation(project(":fore-lifecycle"))

    implementation("androidx.appcompat:appcompat:${Shared.Versions.appcompat}")
    implementation("com.google.android.material:material:${Shared.Versions.material}")
    implementation("androidx.constraintlayout:constraintlayout:${Shared.Versions.constraintlayout}")
}