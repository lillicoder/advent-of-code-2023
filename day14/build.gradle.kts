dependencies {
    implementation(project(":grids"))
    implementation(project(":io"))
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
