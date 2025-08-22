plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) // For kotlinx-serialization
    alias(libs.plugins.androidx.navigation) // For Navigation Compose
    alias(libs.plugins.androidx.room) // For Room database
    id("kotlin-kapt")
    id("jacoco")

}

android {
    namespace = "com.example.sendmoney"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.sendmoney"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.compose) // Navigation
    implementation(libs.androidx.appcompat)
    implementation(libs.google.accompanist)
    implementation(libs.androidx.room.runtime) // Room
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler) // Room annotation processor
    implementation(libs.kotlinx.serialization.json) // Serialization
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.core)
    testImplementation(libs.mockito.core) // For unit testing
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.robolectric)
    testImplementation(libs.google.truth)
    testImplementation(libs.cash.turbine) // For Flow testing

    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.register("jacocoTestReport", JacocoReport::class) {
    group = "verification"
    description = "Generates JaCoCo code coverage reports for the debug build."

    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }


    val sourceDirs = files(
        "$projectDir/src/main/java",
        "$projectDir/src/main/kotlin"
    )
    sourceDirectories.setFrom(sourceDirs)

    val classesDir = fileTree("$buildDir/tmp/kotlin-classes/debug") {
        exclude(
            "**/R.class", "**/R$*.class",
            "**/*Manifest*.*",
            "**/BR.class",
            "**/databinding/**/*.*",
            "**/*_ViewBinding*.*",

            "**/Hilt_*.*",
            "**/*_HiltModules*.*",
            "**/Dagger*Component.*",
            "**/Dagger*Component$*.*",
            "**/*_Factory.*",
            "**/*_Factory$*.*",
            "**/*_MembersInjector.*",
            "**/*_MembersInjector$*.*",
            "**/*_Provide*Factory*.*",
            "**/*_Binds*.*",
            "**/*_Impl.class",
            "**/*_Impl$*.*",
            "**/*_DAO.class",
            "**/*_DAO_Impl.class",
            "**/*_DAO_Impl$*.*",


            "**/*Kt.class",


            "kotlin/**", "kotlinx/**",

            "**/*Test.*", "**/*Test$*.*",
            "**/*Spec.*", "**/*Spec$*.*",
            "**/*androidTest*.*",
            "**/*UnitTest*.*",



            "**/*Directions.*", "**/*Directions$*.*",
            "**/*Args.*", "**/*Args$*.*",

            "**/*JsonAdapter.*",

            "**/com/example/sendmoney/generated/**"
        )
    }
    classDirectories.setFrom(classesDir)

    val executionDataFile = "$buildDir/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
    executionData.setFrom(files(executionDataFile))

    doLast {
        println("------------------------------------------------------------------------")
        println("JaCoCo Code Coverage Report Generation")
        println("------------------------------------------------------------------------")
        if (reports.html.outputLocation.get().asFile.exists()) {
            println("HTML report: file://${reports.html.outputLocation.get().asFile}/index.html")
        } else {
            println("HTML report was not generated. Check configuration and test execution.")
        }
        if (reports.xml.outputLocation.get().asFile.exists()) {
            println("XML report:  file://${reports.xml.outputLocation.get().asFile}")
        } else {
            println("XML report was not generated.")
        }
        println("------------------------------------------------------------------------")
    }
}
