# Android News App

A mobile news reader built with **Java** and **Android Studio** that consumes a WordPress REST API to display category previews, article lists, and full‑story pages. The project is designed as a clean reference for anyone who wants to learn how to build a modern content‑driven Android application.

> **Why this project?** It demonstrates a practical MVVM architecture, efficient image loading with Picasso, navigation with Jetpack Fragments, and seamless pagination of remote data—everything you need for a solid production foundation.

---

## ✨ Features

| Area                 | Highlights                                                                                      |
| -------------------- | ----------------------------------------------------------------------------------------------- |
| **Dynamic data**     | Fetches categories and posts from any WordPress site via `/wp-json/wp/v2/*` endpoints.          |
| **Category preview** | Horizontal list of categories with thumbnails—tap to drill down (see `CategoryPreviewAdapter`). |
| **Article feed**     | Infinite‑scroll RecyclerView of posts with images and excerpts.                                 |
| **Detail screen**    | Renders full article content in a WebView or native layout.                                     |
| **Image handling**   | Uses **Picasso** for caching, placeholders, errors, and resizing.                               |
| **Offline caching**  | Room database (optional) for offline reading and faster cold starts.                            |
| **Search & filter**  | Query WordPress posts by keyword, author, or tag.                                               |
| **Dark mode**        | Automatic theme switch based on system setting.                                                 |
| **CI / CD ready**    | Gradle scripts configured for lint, unit tests, and GitHub Actions.                             |

---

## 🏗️ Tech Stack

- **Language:** Java 17 (Android Gradle Plugin 8+)
- **Build:** Gradle   `./gradlew assembleDebug`
- **UI:** AndroidX, Material Components, RecyclerView, ConstraintLayout
- **Images:** Picasso
- **Networking:** Retrofit + OkHttp + Gson
- **Persistence (optional):** Room
- **Architecture:** MVVM, Repository pattern, LiveData / StateFlow

---

## 📂 Project Structure (high‑level)

```
app/
└── src/main/
    ├── java/com/data/cloner/newapp/
    │   ├── adapters/         # RecyclerView adapters
    │   │   └── CategoryPreviewAdapter.java
    │   ├── fragments/        # UI fragments (home, list, detail)
    │   ├── modelclass/       # POJOs mapping the WP JSON
    │   ├── network/          # Retrofit service & interceptors
    │   ├── repository/       # Single source of truth
    │   └── viewmodel/        # Lifecycle‑aware view models
    └── res/                  # Layouts, drawables, themes
```

Feel free to rename packages to match your organization.

---

## 🚀 Getting Started

1. **Clone the repo**
   ```bash
   git clone https://github.com/your‑username/android‑news‑app.git
   cd android‑news‑app
   ```
2. **Open in Android Studio (Flamingo or newer)**
3. **Configure the API base URL** *In **`network/ApiConstants.java`** (or your Repository)*
   ```java
   public static final String BASE_URL = "https://<your‑wordpress‑site>.com/wp-json/wp/v2/";
   ```
4. **Run on a device/emulator** (API 24+). `Shift + F10` or the run button.
5. **(Optional) Enable Room cache** – set `ENABLE_ROOM_CACHE=true` in `local.properties`.

---

## 🔌 API Endpoints Used

| Endpoint                 | Purpose                    |
| ------------------------ | -------------------------- |
| `/posts?per_page=...`    | Paginated list of posts    |
| `/categories`            | List all categories        |
| `/posts?categories=<id>` | Posts filtered by category |
| `/search?search=<q>`     | Search posts by keyword    |

> The app parses the `title.rendered`, `excerpt.rendered`, `content.rendered`, and `better_featured_image.source_url` fields.

---

## 🖼️ Screenshots

Add PNGs to `/screenshots` and link them here. For example:

| Home | Category | Article |
| ---- | -------- | ------- |
|      |          |         |

---

## 📈 Roadmap

-

---

## 🤝 Contributing

1. Fork the project
2. Create a feature branch `git checkout -b feature/someName`
3. Commit your changes `git commit -m "Add AwesomeFeature"`
4. Push to origin `git push origin feature/someName`
5. Open a Pull Request

All contributions, big or small, are welcome 🎉

---

## 📝 License

This project is released under the **MIT License**. See `LICENSE` for details.

---

## 🙏 Acknowledgements

- [Picasso](https://github.com/square/picasso)
- [WordPress REST API](https://developer.wordpress.org/rest-api/)
- [Android Open Source Project](https://source.android.com)

> Happy coding! ✌️

