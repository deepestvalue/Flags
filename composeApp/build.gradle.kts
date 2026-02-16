import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING


plugins {
    // These come from your [plugins] section in libs.versions.toml
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.serialization)

    // These are standalone plugins (not in your .toml yet)
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.1"
    id("com.codingfeline.buildkonfig") version "0.15.2"
}


kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvmToolchain(21)
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export(libs.kotlinx.serialization.json)
            binaryOption("bundleId", "org.k.flags")
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            api(libs.kotlinx.serialization.json)

            implementation("io.github.xxfast:kstore:0.9.1")
            implementation("io.github.xxfast:kstore-file:0.9.1")

            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.2")

            val coilVersion = "3.0.4" // Check for latest stable version

            // Core Compose support
            implementation("io.coil-kt.coil3:coil-compose:$coilVersion")

            // Network support (Coil 3 requires a separate network fetcher for KMP)
            // Use Ktor for multiplatform network image loading
            implementation("io.coil-kt.coil3:coil-network-ktor3:$coilVersion")

            // for logging
            implementation("co.touchlab:kermit:2.0.4")

            implementation("org.jetbrains.compose.components:components-resources:1.6.11") // or your current version
            implementation("org.jetbrains.compose.material:material-icons-extended:1.6.11")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

compose {
    resources {
        // This is the package name for your generated 'Res' class
        packageOfResClass = "org.k.flags.generated.resources"

        // This allows the generated class to be visible in commonMain
        generateResClass = always
    }
}


android {
    namespace = "org.k.flags"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.k.flags"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }


    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

buildkonfig {
    packageName = "org.k.flags"
    objectName = "ApiKeyConfig"
    exposeObjectWithName = "Config"

    defaultConfigs {
        buildConfigField(
            STRING,
            "WeatherApiKey",
            gradleLocalProperties(project.rootDir, providers).getProperty("WEATHER_API_KEY") ?: ""
        )
    }
}
