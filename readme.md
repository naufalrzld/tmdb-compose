# Installation Guide

## Prerequisites

Before building and installing the application, you need to configure the `local.properties` file with the required API keys.

## Setup Instructions

### 1. Create local.properties File

Create or edit the `local.properties` file in the root directory of the project and add the following keys:

```properties
TMDB_BASE_URL=https://api.themoviedb.org/3/
TMDB_BASE_URL_IMAGE=https://image.tmdb.org/t/p/w500
TMDB_AUTH=<YOUR_TMDB_API_TOKEN>
```

### 2. Get TMDB API Token

1. Go to [The Movie Database (TMDB)](https://www.themoviedb.org/)
2. Create an account or sign in
3. Navigate to Settings > API
4. Request an API key
5. Copy your API Read Access Token (Bearer Token)
6. Paste it as the value for `TMDB_AUTH` in `local.properties`

### 3. Build and Install

Once you've configured `local.properties`, you can build and install the app:

- **Using Android Studio**: Click the Run button or press `Shift + F10`
- **Using Gradle**: Run `./gradlew installDebug` in the terminal

## Tech Stack

This application is built using the following technologies:

- **UI**: Jetpack Compose with Material3
- **Networking**: Retrofit
- **Architecture**: Clean Architecture with MVVM (Model-View-ViewModel)
- **Dependency Injection**: Koin
- **Video Player**: YouTube Player
- **Language**: Kotlin

## Important Note

⚠️ **Never commit `local.properties` to version control** as it contains sensitive API keys and local configuration.