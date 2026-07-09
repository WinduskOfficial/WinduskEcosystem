// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.mavenPublish) apply false
    id("org.jetbrains.dokka") version "2.2.0" apply false
    id("org.jetbrains.dokka-javadoc") version "2.2.0" apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    id("signing")
}

val localProperties = java.util.Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

localProperties.forEach { (key, value) ->
    extra.set(key as String, value)
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jetbrains.dokka-javadoc")

    // Настройка публикации для всех модулей, где применен плагин
    plugins.withType<com.vanniktech.maven.publish.MavenPublishPlugin> {
        configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
            val artifactId = project.name.let { 
                // Превращаем CamelCase в kebab-case для Maven, если нужно
                // Или используем явно заданный PUBLISH_ARTIFACT_ID
                (project.findProperty("PUBLISH_ARTIFACT_ID") as? String) ?: it
            }

            coordinates(
                rootProject.ext["PUBLISH_GROUP_ID"] as String,
                artifactId,
                rootProject.ext["PUBLISH_VERSION"] as String
            )

            pom {
                name.set(artifactId)
                description.set("Windusk Ecosystem Library")
                inceptionYear.set("2025")
                url.set("https://github.com/WinduskOfficial/WinduskEcosystem")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("Windusk")
                        name.set("Aitov Ruslan")
                        url.set("https://github.com/WinduskOfficial/")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/WinduskOfficial/WinduskEcosystem.git")
                    developerConnection.set("scm:git:ssh://git@github.com/WinduskOfficial/WinduskEcosystem.git")
                    url.set("https://github.com/WinduskOfficial/WinduskEcosystem")
                }
            }

            publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
        }

        // Общая логика подписи для всех публикуемых модулей
        apply(plugin = "signing")
        configure<org.gradle.plugins.signing.SigningExtension> {
            val keyId = rootProject.ext["signing.keyId"] as? String
            val password = rootProject.ext["signing.password"] as? String
            val secretKeyFile = rootProject.file("secret.gpg")
            
            if (keyId != null && password != null && secretKeyFile.exists()) {
                useInMemoryPgpKeys(keyId, secretKeyFile.readText(), password)
            }
            
            val publishing = extensions.getByType<PublishingExtension>()
            sign(publishing.publications)
        }
    }
}

ext["PUBLISH_GROUP_ID"] = "io.github.winduskofficial"
ext["PUBLISH_VERSION"] = "1.0.0"

tasks.register("loadOutputs") {
    group = "build"
    description = "Copy all .aar files to ../allOutputs without 'debug' suffix"

    doLast {
        val outputDir = File("../allOutputs")

        delete(outputDir.listFiles().orEmpty())
        outputDir.mkdirs()

        subprojects {
            val buildDir = layout.buildDirectory.asFile.get()
            val aarDir = File(buildDir, "outputs/aar")

            if(!aarDir.exists()) return@subprojects

            val aarFiles = aarDir.listFiles { file ->
                file.name.endsWith(".aar")
            }?.toList().orEmpty()

            aarFiles.forEach { aarFile ->
                val moduleName = name
                val targetName = "${moduleName}.aar"
                val targetFile = File(outputDir, targetName)

                copy {
                    from(aarFile)
                    into(outputDir)
                    rename { targetFile.name }
                }
            }
        }
    }
}
