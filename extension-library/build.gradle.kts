plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

group = "app.morphe"
base.archivesName = "instagram-morphe-extensions-library"

android {
    namespace = "app.morphe.extension.library"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    compileOnly(libs.annotation)
    compileOnly(libs.morphe.extensions.library)
}

afterEvaluate {
    publishing {
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

        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "app.morphe"
                artifactId = "instagram-morphe-extensions-library"
                version = project.version.toString()

                pom {
                    name = "Morphe Extensions Library for Instagram"
                    description = "Common extension utilities for Instagram Morphe patch bundles"
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
}

