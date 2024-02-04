dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
