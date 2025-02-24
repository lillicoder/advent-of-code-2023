dependencies {
    implementation(libs.advent.of.code.kotlin.grids)
    implementation(libs.advent.of.code.kotlin.io)
    implementation(libs.advent.of.code.kotlin.math)
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
