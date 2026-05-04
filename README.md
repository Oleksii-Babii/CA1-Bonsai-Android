# Bonsai Collection — Android App

An Android application for browsing bonsai species and managing a personal bonsai tree collection. Built with Java, Retrofit 2, and Material Design 3.

---

## Features

### Species Browser
- Browse all available bonsai species
- View detailed species info: origin country, difficulty level, description, and image
- Featured species showcase on the home screen
- Difficulty level colour-coded chips (Easy / Intermediate / Hard / Expert)

### Tree Collection
- Add trees with a photo, nickname, age, height, notes, and species
- Edit or delete existing trees
- Search and filter trees by nickname (300 ms debounce)
- Auto-populated "last watered" date on creation

### UI / UX
- Material Design 3 with a forest-green and earth-tone palette
- Card layouts with rounded corners
- Gradient backgrounds and hero overlays
- Progress indicators, empty states, and confirmation dialogs

---

## Screenshots

> _Add screenshots here_

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 11 |
| UI | Material Design 3, ViewBinding |
| Networking | Retrofit 2.11 + OkHttp (GSON converter) |
| Image loading | Glide 4.16 |
| Image upload | Base64 encoding + background ExecutorService |
| Lists | RecyclerView + DiffUtil |
| Backend | Azure-hosted REST API |

---

## Architecture

The app follows a simple **activity-centric** architecture with no Fragments:

```
MainActivity
├── SpeciesListActivity
│   └── SpeciesDetailActivity
└── TreeListActivity
    ├── TreeDetailActivity
    └── AddEditTreeActivity
```

- **Singleton** `RetrofitClient` provides a shared `BonsaiApiService` instance.
- Activities handle UI and Retrofit callbacks directly (no ViewModel / LiveData).
- Adapters use `DiffUtil.Callback` for efficient list diffing.
- Image compression runs on a single-threaded `ExecutorService` off the main thread.

---

## Project Structure

```
app/src/main/java/org/tudublin/bonsaiapp/
├── MainActivity.java
├── SpeciesListActivity.java
├── SpeciesDetailActivity.java
├── TreeListActivity.java
├── TreeDetailActivity.java
├── AddEditTreeActivity.java
├── adapter/
│   ├── SpeciesAdapter.java
│   └── TreeAdapter.java
├── api/
│   ├── BonsaiApiService.java
│   └── RetrofitClient.java
├── model/
│   ├── Species.java
│   └── Tree.java
└── util/
    ├── DifficultyUtils.java
    └── ImageUtils.java
```

---

## Data Models

### Species
| Field | Type | Description |
|---|---|---|
| id | int | Unique identifier |
| name | String | Species name |
| originCountry | String | Country of origin |
| description | String | Detailed description |
| difficultyLevel | String | Easy / Intermediate / Hard / Expert |
| imageUrl | String | Remote image URL |

### Tree
| Field | Type | Description |
|---|---|---|
| id | int | Unique identifier |
| nickname | String | User-given name |
| age | int | Age in years |
| height | double | Height in centimetres |
| lastWateredDate | String | ISO date string |
| notes | String | Optional freeform notes |
| imageData | String | Base64-encoded photo |
| speciesId | int | FK to Species |
| species | Species | Populated from API |

---

## API

Backend: `https://bonsaiapi-ewe7gdd0hfd8a8dv.westeurope-01.azurewebsites.net`

Interactive docs: [Swagger UI](https://bonsaiapi-ewe7gdd0hfd8a8dv.westeurope-01.azurewebsites.net/swagger/index.html)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/Species` | List all species |
| GET | `/api/Species/{id}` | Get species by ID |
| GET | `/api/Trees` | List all trees |
| GET | `/api/Trees/{id}` | Get tree by ID |
| POST | `/api/Trees` | Create a tree |
| PUT | `/api/Trees/{id}` | Update a tree |
| DELETE | `/api/Trees/{id}` | Delete a tree |
| GET | `/api/Trees/search` | Search trees by name |
| POST | `/api/Trees/{id}/image` | Upload an image for a tree |

OkHttp timeouts: connect 10 s · read 30 s · write 20 s.

---

## Requirements

- Android 8.0 (API 26) or higher
- Internet connection (HTTPS only — cleartext disabled)

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd CA1-Bonsai-Android
   ```

2. Open the project in **Android Studio** (Hedgehog or later recommended).

3. Sync Gradle and run on a device or emulator running API 26+.

No local configuration is required — the app connects to the hosted Azure backend out of the box.

---

## Dependencies

```groovy
// UI
implementation 'androidx.appcompat:appcompat:1.7.0'
implementation 'com.google.android.material:material:1.12.0'
implementation 'androidx.constraintlayout:constraintlayout:2.2.0'
implementation 'androidx.recyclerview:recyclerview:1.3.2'

// Networking
implementation 'com.squareup.retrofit2:retrofit:2.11.0'
implementation 'com.squareup.retrofit2:converter-gson:2.11.0'

// Images
implementation 'com.github.bumptech.glide:glide:4.16.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'
```

---

## Testing

The project includes instrumented UI tests using Espresso:

```bash
./gradlew connectedAndroidTest
```

Test dependencies: JUnit 4.13.2, Espresso Core 3.6.1, Espresso Intents 3.6.1.

---

## Build Configuration

| Property | Value |
|---|---|
| Compile SDK | 35 |
| Target SDK | 35 |
| Min SDK | 26 |
| Java compatibility | VERSION_11 |
| View Binding | Enabled |
| AGP | 8.7.3 |

---

## Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## License

This project was created as a college assignment (CA1) at TU Dublin.
