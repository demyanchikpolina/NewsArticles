plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.annotation)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    //implementation(libs.retrofit.kotlinx.serialization.converter)
    //implementation(libs.retrofit.adapters.result)
}