# Speak Touch
A open source screen reader for android.

## Build
For the ***release*** **buildType**, it's necessary to create a [keystore](https://developer.android.com/training/articles/keystore) and specify its properties in the `keystore.properties` file (use `keystore.properties.example` as a base).

### Android Studio
To build you need Android Studio Flamingo | 2022.2.1 or later. The latest stable version is always prefered.

1. Clone the repository to your local machine.
2. Open project in [the latest version of Android Studio](https://developer.android.com/studio) IDE.
3. Run the **app** module.

### Command line
1. Download and install JDK 17 or newer.
2. Download and install [Android Command Line Tools](https://developer.android.com/studio#command-tools).
3. set `ANDROID_HOME` environment variable.
4. Clone the repository to your local machine.
5. `cd` to directory of the newly cloned repository (`cd SpeakTouch`).
6. Build with `./gradlew build`. Alternatively, you can `./gradlew assembleDebug` or `./gradlew assembleRelease` if you need only debug or release build type, respectively.

## License
[![GNU GPLv3 logo](https://www.gnu.org/graphics/gplv3-127x51.png)](https://www.gnu.org/licenses/gpl-3.0.html)

This project is licensed under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html), a copyleft open-source license. You are permitted to copy, modify, and redistribute the code, provided that the redistribution is accompanied by the corresponding source code under the same license in order to maintain its open-source nature. You can find a copy of the license in the project's [LICENSE file](/LICENSE) or the original version at https://www.gnu.org/licenses/gpl-3.0.html.
