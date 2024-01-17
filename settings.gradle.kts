@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

include("day1")
include("day2")
include("day3")
include("day4")
include("day5")
include("day6")
include("day7")
include("day8")
include("day9")
include("day10")
include("day11")
include("day12")
include("grids")
