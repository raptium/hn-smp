# Example Project for Developing Mini Program in Kotlin/JS + Taro

## Source Layout

- `buildSrc/` Gradle build scripts
- `app/` Taro project
- `components/` Kotlin project

## Build

- all `./gradlew build`
- Kotlin components `./gradlew :components:build`
    - This builds the Kotlin/JS project and link the output as a dependency to the Taro project(
      as `@${project.name}/components`)
- Taro project `./gradlew taroBuild`
    - This invokes `taro build --type=weapp`
