dependencies {
    implementation(libs.advent.of.code.kotlin.collections)
    implementation(libs.advent.of.code.kotlin.graphs)
    implementation(libs.advent.of.code.kotlin.io)
    implementation(libs.advent.of.code.kotlin.math)
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
