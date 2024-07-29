# :page_with_curl: TODO App

Client-server app that provides CRUD for notes.

![screenshots](screenshot.png)

**Tech stack:**
- **Server-side**: Spring Boot, Kotlin, H2 and Liquibase.
- **Client-side**: Android SDK, Kotlin, Retrofit, Jetpack Compose, Paging and Hilt.

## :wrench: Requirements

- Installed JDK 17 and set `JAVA_HOME` environment variable.
- Installed Android SDK and set `ANDROID_HOME` environment variable.

## :package: Build

First of all, replace arguments in `dev.properties`:
- `server.base.url` - server address accessible from client
- `server.address`, `server.port` - server host
- `db.h2.address` - URL to embedded H2 DB (file will be created if not exists)

### Composite build

1. Run `./gradlew build`.
2. Go to `/build` (here you can find server as `.jar` and Android app as `.apk`).

- I would recommend to execute `./gradlew client-android:app:clean` each time you change base url, cause' sometimes BuildConfig doesn't update it.

### Build server only

1. Open `server` folder.
2. Run `./gradlew bootJar` (JAR will be located in `/server/build/libs`).

### Build client only

1. Open `client-android` folder.
2. Run `./gradlew assembleDebug` (APK locates in `/client-android/app/build/outputs/apk/debug`).

## :rocket: Deploy

### Migrations

1. Open `server` folder.
2. Run DB migrations by `./gradlew update` (you can override `dev.properties` value with `"-Pdb.h2.address=URL"`).

- Run `./gradlew dropAll` to drop all DB entities.
- Run `./gradlew dropAll update` to remove all records from DB.

### Deploy with Gradle

1. Open `server` folder.
2. Run `./gradlew bootRun` (you can override values from `dev.properties` with `--args="--KEY=VALUE..."`).

### Deploy from JAR

Run `java -jar server.jar` (you can override values from `dev.properties` with `--KEY=VALUE...`).