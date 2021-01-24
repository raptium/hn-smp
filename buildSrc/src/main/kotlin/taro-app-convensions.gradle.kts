import org.gradle.api.tasks.TaskAction

open class TaroBuildTask : DefaultTask() {
    @get:Input
    var type: String = "weapp"

    @TaskAction
    fun build() {
        logger.info("Building Taro application")
        val commands = listOf("taro", "build", "--type", type)
        val process = ProcessBuilder()
            .directory(project.projectDir)
            .command(commands)
            .start()
        process.waitFor()
        if (process.exitValue() != 0) {
            logger.error("Failed to build Taro application")
        }
    }
}

tasks.register<TaroBuildTask>("taroBuild") {
    dependsOn(":components:build")
    group = "taro"
}

tasks.register<DefaultTask>("build") {
    dependsOn("taroBuild")
    group = "build"
}
