import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("jvm") version "1.9.21"
    application
}

kotlin {
    jvmToolchain(21)
}

group = "me.nagy"
version = "1.0-SNAPSHOT"

tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.ExperimentalStdlibApi")
}
