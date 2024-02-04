dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(project(":grids"))
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
