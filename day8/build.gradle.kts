dependencies {
    implementation(project(":io"))
    implementation(project(":math"))
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
