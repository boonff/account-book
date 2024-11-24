import com.android.build.gradle.internal.ide.kmp.KotlinAndroidSourceSetMarker.Companion.android
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
                    implementation(compose.preview)
                    implementation(libs.firebase.database.ktx)
                    implementation(libs.androidx.window)
                    implementation(libs.jetbrains)
                    implementation(libs.reorderable)
                    implementation(libs.androidx.navigation.compose)
                    implementation(libs.kotlinx.coroutines.core)
                    implementation(libs.runtime)
                    implementation(libs.kotlinx.datetime)
                    implementation(libs.slf4j.api)
                    implementation(libs.slf4j.simple)
                    implementation(project.dependencies.platform(libs.koin.bom))
                    implementation(libs.koin.core)
                    implementation(libs.koin.compose)
                    implementation(libs.koin.compose.viewmodel)
                    implementation(libs.koin.compose.viewmodel.navigation)
                    implementation(libs.lifecycle.viewmodel.compose)
                    implementation(libs.kotlinx.serialization.json)
                }
            }
            val androidMain by getting {
                dependencies {
                    implementation(libs.androidx.activity.compose)
                    implementation(libs.sqldelight.android.driver)
                    implementation(libs.koin.androidx.compose)
                    implementation(libs.koin.androidx.compose.navigation)
                }
            }
            val desktopMain by getting {
                dependencies {
                    implementation(compose.desktop.currentOs)
                    implementation(libs.kotlinx.coroutines.swing)
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

    signingConfigs {
        create("release") {
            keyAlias = "my-key-alias"
            keyPassword = "qxxpdada" // 密钥密码
            storeFile = file("C:/Users/qxxpd/source/android/my-release-key.jks") // 密钥文件路径
            storePassword = "qxxpdada" // 密钥库密码
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release") // 正确引用 signingConfigs
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
