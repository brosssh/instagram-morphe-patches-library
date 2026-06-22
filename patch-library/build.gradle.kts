plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

group = "app.morphe"
base.archivesName = "instagram-morphe-patches-library"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withSourcesJar()
    withJavadocJar()
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        freeCompilerArgs = listOf("-Xcontext-parameters")
    }
}

dependencies {
    // Used by JsonGenerator.
    implementation(libs.gson)

    implementation(libs.morphe.patcher)
    implementation(libs.smali)

    compileOnly(libs.morphe.patches.library)
}

publishing {
    publications {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/brosssh/instagram-morphe-patches-library")
                credentials {
                    username = providers.gradleProperty("gpr.user").getOrElse(System.getenv("GITHUB_ACTOR"))
                    password = providers.gradleProperty("gpr.key").getOrElse(System.getenv("GITHUB_TOKEN"))
                }
            }
        }

        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = "app.morphe"
            artifactId = "instagram-morphe-patches-library"
            version = project.version.toString()

            pom {
                name = "Instagram Morphe Patches Library"
                description = "Common patch utilities for Instagram Morphe patch bundles"
                url = ""
                licenses {
                    license {
                        name = "GNU General Public License v3.0"
                    }
                }
                developers {
                    developer {
                        name = "brosssh"
                    }
                }
                scm {
                    url = "https://github.com/brosssh/instagram-morphe-patches-library"
                }
            }
        }
    }
}
