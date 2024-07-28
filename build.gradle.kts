tasks.register<Copy>("build") {
    dependsOn(gradle.includedBuild("server").task(":bootJar"))
    dependsOn(gradle.includedBuild("client-android").task(":app:assembleDebug"))
    from(
        "client-android/app/build/outputs/apk/debug/app-debug.apk",
        "server/build/libs"
    )
    into("build")
}