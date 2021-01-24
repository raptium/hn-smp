import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.file.Files
import java.nio.file.Path

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

repositories {
    jcenter()
    mavenLocal()
}

kotlin {
    js {
        nodejs()
        useCommonJs()
        binaries.library()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("me.hguan:kotlin-taro:${version("kotlin-taro")}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
            }
        }
        all {
            languageSettings.useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
            languageSettings.useExperimentalAnnotation("kotlin.js.ExperimentalJsExport")
        }
    }
}

open class LinkJsModuleTask : DefaultTask() {
    @get:Input
    var entries: List<Pair<Path, Path>> = emptyList()

    @TaskAction
    fun link() {
        for (entry in entries) {
            val src = entry.first
            val dest = entry.second
            Files.deleteIfExists(dest)
            Files.createDirectories(dest.parent)
            Files.createSymbolicLink(dest, src)
            logger.info("Linking $src to $dest")
        }
    }
}

fun registerLinkComponentsTask() {
    val taskName: String
    val parent = project.parent!!
    val app = project.projectDir.toPath().resolveSibling("app")
    val module = parent.buildDir.toPath().resolve("js/packages/${parent.name}-${project.name}")
    val nodeModule = app.resolve("node_modules/@${parent.name}/${project.name}")
    if (System.getProperty("os.name").startsWith("Windows")) {
        // symbolic link on Windows does not work
        taskName = "copyComponentsJsModule"
        tasks.register<Copy>(taskName) {
            dependsOn("assemble")
            from(module)
            into(nodeModule)
        }
    } else {
        taskName = "linkComponentsJsModule"
        tasks.register<LinkJsModuleTask>(taskName) {
            entries = listOf(
                module to nodeModule
            )
        }
    }
    tasks.getByName("build") {
        dependsOn(taskName)
    }
}

registerLinkComponentsTask()
