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

sqldelight {
    databases {
        create("Database") {
            packageName.set("me.accountbook.database")
        }
    }
}


kotlin {
    sourceSets {
        androidTarget()
        jvm("desktop")

        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(compose.preview)
                    implementation(compose.runtime)
                    implementation(compose.foundation)
                    implementation(compose.ui)
                    implementation(compose.material3)
                    implementation(compose.materialIconsExtended)
                    implementation(compose.components.resources)

                    implementation(libs.androidx.window)
                    implementation(libs.androidx.navigation.compose)
                    implementation(libs.lifecycle.viewmodel.compose)
                    implementation(libs.reorderable)

                    implementation(libs.slf4j.api)
                    implementation (libs.kotlin.logging)
                    implementation(libs.slf4j.simple)

                    implementation(libs.kotlinx.coroutines.core)//协程


                    implementation(libs.kotlinx.datetime)
                    implementation(libs.kotlinx.serialization.json)
                    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-protobuf
                    implementation(libs.kotlinx.serialization.protobuf)

                    implementation(project.dependencies.platform(libs.koin.bom))
                    implementation(libs.koin.core)
                    implementation(libs.koin.compose)
                    implementation(libs.koin.compose.viewmodel)

                    implementation(project.dependencies.platform(libs.okhttp.bom))
                    implementation(libs.okhttp)
                    implementation(libs.okhttp.logging.interceptor)

                    implementation(libs.ktor.server.cio)

                    implementation(libs.spring.security.crypto)

                    implementation(libs.tink)

                    //implementation(libs.kandy.lets.plot)

                }
            }
            val androidMain by getting {
                dependencies {
                    implementation(libs.androidx.activity.compose)
                    implementation(libs.sqldelight.android.driver)
                    implementation(libs.koin.androidx.compose)

                    implementation(libs.vico.compose.m3)
                    implementation(libs.vico.core)
                    implementation(libs.vico.views)
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
        jvmArgs += listOf("-Xmx2G") // 设置最大内存为 2GB
        args += listOf("-customArgument") // 可以根据需要修改

        //禁用混淆
        buildTypes.release.proguard {
            obfuscate.set(false)
        }
        nativeDistributions {
            targetFormats(TargetFormat.Exe)
            packageName = "accountbook"
            packageVersion = "0.0.1"
            modules("java.instrument", "java.management", "java.sql", "jdk.unsupported")
        }
    }
}
