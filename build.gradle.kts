@file:Suppress("VulnerableLibrariesLocal")

import org.gradle.jvm.tasks.Jar


plugins {
    kotlin("jvm") version "1.9.21"
}

group = "yu17"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    // https://mvnrepository.com/artifact/com.nfeld.jsonpathkt/jsonpathkt
    implementation("com.nfeld.jsonpathkt:jsonpathkt:2.0.1")
    // https://mvnrepository.com/artifact/io.ktor/ktor-client-core
    implementation("io.ktor:ktor-client-core:2.3.7")
    // https://mvnrepository.com/artifact/io.ktor/ktor-client-cio
    implementation("io.ktor:ktor-client-cio:2.3.7")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.9")
    // https://mvnrepository.com/artifact/io.ktor/ktor-client-apache5-jvm
    // implementation("io.ktor:ktor-client-apache5-jvm:2.3.7")
    // https://mvnrepository.com/artifact/io.netty/netty-all
    implementation("io.netty:netty-all:4.1.101.Final")
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    implementation("org.jsoup:jsoup:1.17.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

//tasks.register("fatJar", Jar::class) {
//    /*manifest {
//        attributes("Main-Class" to "Main")
//    }
//    archiveBaseName = "all-in-one-jar"
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE*/
////    from { configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) } }
////    from({ configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) } })
//
//
//}

tasks.register<Jar>("fatJar") {
    manifest {
        attributes["Main-Class"] = "yu17.MainKt"
    }
    archiveBaseName = "fat"
    version = ""
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get()
        .onEach { println("add from dependencies: ${it.name}") }
        .map { if (it.isDirectory()) it else zipTree(it) }
    )
    sourceSets.main.get().allSource.forEach { println("add from sources: ${it.name}") }
    with(tasks.jar.get() as CopySpec)
}
