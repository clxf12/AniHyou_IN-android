# <img alt="app-icon" height="50" src="https://github.com/axiel7/AniHyou-android/blob/master/app/src/main/res/mipmap-hdpi/ic_launcher_round.webp"/>AniHyou

[![Donate](https://img.shields.io/badge/buy%20me%20a%20coffee-donate-yellow.svg)](https://ko-fi.com/axiel7)

Klien unofficial Android AniList lainnya

[<img alt="Google Play" height="80" src="https://play.google.com/intl/en_US/badges/images/generic/en_badge_web_generic.png"/>](https://play.google.com/store/apps/details?id=com.axiel7.anihyou)

Ikuti perkembangan di server Discord resmi kami:

[![Discord Banner 3](https://discordapp.com/api/guilds/741059285122940928/widget.png?style=banner2)](https://discord.gg/CTv3WdfxHh)

# Tampilan
![Screenshots](https://github.com/axiel7/AniHyou-android/blob/master/screenshots.webp)

## Libraries used
* [AniList GraphQL API](https://github.com/AniList/ApiV2-GraphQL-Docs)
* [Apollo Kotlin](https://github.com/apollographql/apollo-kotlin)
* [Material3 Components](https://github.com/material-components/material-components-android)
* [Jetpack Compose](https://developer.android.com/jetpack/compose)
* [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
* [Coil](https://github.com/coil-kt/coil)

# Building
Buatlah file `app/scr/main/java/com/axiel7/anihyou/ClientId.kt` dan seperti contoh berikut:

```kotlin
package com.axiel7.anihyou

const val CLIENT_ID = 1234 //Ganti dengan AniList API client ID kamu disini
```
