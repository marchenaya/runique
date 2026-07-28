# Runique

Runique is a modular Android running-tracker app, built as part of the Android Essentials course
bundle by Philipp Lackner. Users authenticate, record runs with live GPS tracking on a map, review
their run history, and view an analytics dashboard delivered on demand via Play Feature Delivery.

## Design

- **Figma:
  ** [Runique Course](https://www.figma.com/design/NNSWHCD7WMpzks7xfKp35c/Runique-Course?node-id=0-1&p=f)

## Features

- **Authentication** — intro/onboarding, login, and registration with password validation.
- **Run tracking** — active run screen with real-time location on a Google Map, run overview (
  history), and background data sync via WorkManager.
- **Analytics dashboard** — run metrics visualization, shipped as an on-demand dynamic feature
  module (Play Feature Delivery) backed by a Room-based local repository.

## Architecture

Runique follows **Clean Architecture** with strict layer separation enforced at module boundaries:

- **domain** — JVM-only, zero Android dependencies, defines interfaces and business logic.
- **data** — implements domain interfaces, owns networking, database, and storage.
- **presentation** — MVI ViewModels + Compose UI, no business logic.

Key conventions:

- **MVI** — ViewModels expose UI state via Compose state / `StateFlow` and emit one-shot events
  through a `Channel`; actions flow in as sealed classes.
- **Error handling** — a `Result<Success, Error>` sealed interface propagates typed errors, mapped
  to `UiText` at the presentation boundary.
- **Session storage** — `SessionStorage` observed as a `Flow<AuthInfo?>`, backed by encrypted
  DataStore (Tink).
- **Navigation** — Navigation 3 with type-safe `@Serializable` route objects.
- **Dependency injection** — Koin, one module per layer, aggregated in `:app`.
- **Convention plugins** — shared Gradle logic in `build-logic/convention/`.

## Module structure

| Module                            | Purpose                                                    |
|-----------------------------------|------------------------------------------------------------|
| `:app`                            | App entry point, DI wiring, navigation root                |
| `:auth:domain`                    | Auth interfaces & validators (JVM-only)                    |
| `:auth:data`                      | Ktor HTTP client, auth repository, token handling          |
| `:auth:presentation`              | Intro / login / register screens (Compose)                 |
| `:run:domain`                     | Run data models, location observer, calculators            |
| `:run:data`                       | Run tracking data layer (WorkManager sync)                 |
| `:run:presentation`               | Active run & run overview screens (Compose + Maps)         |
| `:run:location`                   | Play Services location wrapper                             |
| `:run:network`                    | Ktor remote source for run data                            |
| `:analytics:domain`               | Analytics repository interface & models                    |
| `:analytics:data`                 | Room-based analytics repository                            |
| `:analytics:presentation`         | Analytics dashboard screen (Compose)                       |
| `:analytics:analytics_feature`    | Dynamic feature module (Play Feature Delivery)             |
| `:core:domain`                    | Shared `Result`, `DataError`, `SessionStorage`, `AuthInfo` |
| `:core:data`                      | `HttpClientFactory`, encrypted session storage             |
| `:core:database`                  | Room database setup                                        |
| `:core:presentation:designsystem` | Material3 theme & design tokens                            |
| `:core:presentation:ui`           | Reusable Composables & formatters                          |

## Tech stack

- **UI** — Jetpack Compose, Material3
- **Navigation** — Jetpack Navigation 3 (type-safe routes)
- **Networking** — Ktor Client 3, Kotlinx Serialization
- **Dependency injection** — Koin 4
- **Persistence** — Room, DataStore, Tink encryption
- **Location & maps** — Play Services Location, Google Maps Compose
- **Dynamic delivery** — Play Feature Delivery
- **Background work** — WorkManager
- **Async** — Kotlin Coroutines
- **Logging** — Timber

## Requirements

- **Min SDK** 24 · **Target / Compile SDK** 37
- **Kotlin** 2.3.21 · **AGP** 9.2.1
- A Google Maps API key, provided via the Secrets Gradle plugin (e.g. in `local.properties` /
  `secrets.properties`).

## Build & test

```powershell
# Build
.\gradlew build

# Install debug APK
.\gradlew :app:installDebug

# Run all unit tests
.\gradlew test

# Run tests for a single module
.\gradlew :auth:domain:test

# Run instrumentation tests
.\gradlew connectedAndroidTest
```

## App identity

- **Application ID:** `com.marchenaya.runique`
- **Version:** 1.0
