import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqldelight)
}

repositories {
    google()
    mavenCentral()
    maven {
        url = uri("https://packages.jetbrains.team/maven/p/skija/maven")
        url = uri("https://mvnrepository.com/artifact/io.insert-koin/koin-compose-viewmodel")
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("me.accountbook.database")
        }
    }
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.Experimental")
            }
        }
        androidTarget()

        jvm("desktop")

        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(compose.runtime)
                    implementation(compose.foundation)
                    implementation(compose.ui)
                    implementation(compose.materialIconsExtended)
                    implementation(compose.material3)
                    implementation(compose.components.resources)
                    implementation(compose.components.uiToolingPreview)
                    implementation(libs.firebase.database.ktx)
                    implementation(libs.androidx.window)
                    implementation(libs.jetbrains)
                    implementation(libs.reorderable)
                    implementation(libs.androidx.navigation.compose)
                    implementation(libs.kotlinx.coroutines.core)
                    implementation(libs.ktor.client.core)
                    implementation(libs.ktor.client.content.negotiation)
                    implementation(libs.ktor.serialization.kotlinx.json)
                    implementation(libs.runtime)
                    implementation(libs.kotlinx.datetime)
                    runtimeOnly(libs.slf4j.api)
                    runtimeOnly(libs.slf4j.simple)
                    implementation(project.dependencies.platform(libs.koin.bom))
                    implementation(libs.koin.core)
                    implementation(libs.koin.compose)
                    implementation(libs.koin.compose.viewmodel)
                    implementation(libs.koin.compose.viewmodel.navigation)

                }
            }
            val androidMain by getting {
                dependencies {
                    implementation(libs.androidx.lifecycle.viewmodel)
                    implementation(libs.androidx.lifecycle.runtime.compose)
                    implementation(libs.androidx.activity.compose)
                    implementation(libs.ktor.client.android)
                    implementation(libs.sqldelight.android.driver)
                    implementation(libs.koin.androidx.compose)
                    implementation(libs.koin.androidx.compose.navigation)
                }
            }
            val desktopMain by getting {
                dependencies {
                    implementation(compose.desktop.currentOs)
                    implementation(libs.kotlinx.coroutines.swing)
                    implementation(libs.ktor.client.java)
                    implementation(libs.sqldelight.driver)
                }
            }
        }
    }
}

android {
    namespace = "me.accountbook"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "me.accountbook"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

compose.desktop {
    application {
        mainClass = "me.accountbook.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "me.accountbook"
            packageVersion = "1.0.0"
        }
    }
}
