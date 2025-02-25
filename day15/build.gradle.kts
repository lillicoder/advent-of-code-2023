dependencies {
    implementation(libs.advent.of.code.kotlin.io)
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
