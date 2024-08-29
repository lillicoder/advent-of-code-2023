dependencies {
    implementation(project(":grids"))
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
