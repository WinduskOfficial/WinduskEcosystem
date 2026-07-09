// Code was made with Gemini
val localProps = java.util.Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localPropsFile.inputStream().use { localProps.load(it) }
}

localProps.forEach { (key, value) ->
    extra.set(key as String, value as String)
    System.setProperty(key as String, value as String)
}

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
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jetbrains.dokka-javadoc")
    
    localProps.forEach { (key, value) ->
        extra.set(key as String, value as String)
    }
}

ext["PUBLISH_GROUP_ID"] = "io.github.winduskofficial"
ext["PUBLISH_VERSION"] = "1.0.0-SNAPSHOT"

tasks.register("loadOutputs") {
    group = "build"
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
