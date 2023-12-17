@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            library("kotlin-stdlib", "org.jetbrains.kotlin:kotlin-stdlib:1.9.21")
            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
            plugin("foojay-toolchain-resolver", "org.gradle.toolchains.foojay-resolver-convention").version("0.7.0")
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version("1.9.21")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

include("day1")
include("day2")
