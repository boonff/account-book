rootProject.name = "AccountBook"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        maven("https://jitpack.io")
        maven {
            url = uri("https://packages.jetbrains.team/maven/p/skija/maven")
            url = uri("https://mvnrepository.com/artifact/io.insert-koin/koin-compose-viewmodel")
            url = uri(" https://mvnrepository.com/artifact/com.soywiz.korlibs.krypto/krypto")
        }
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}



include(":composeApp")
