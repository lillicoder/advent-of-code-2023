plugins {
    kotlin("jvm") version "1.9.10"
    application
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.kotlin.stdlib)
}
