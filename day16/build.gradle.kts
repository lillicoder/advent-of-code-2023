dependencies {
    implementation(project(":grids"))
    implementation(project(":math"))
    implementation(libs.kotlin.stdlib)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
