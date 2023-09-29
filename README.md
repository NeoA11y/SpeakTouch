# Speak Touch
A open source screen reader for android.

## Build
In development, choose the *debug build type*, because for the *release build type*, it's necessary to create a [keystore](https://developer.android.com/training/articles/keystore) and specify its properties in the `keystore.properties` file (use `keystore.properties.example` as a base).

### Android Studio

1. Download and install [Android Studio](https://developer.android.com/studio).
2. Clone the repository to your local machine.
3. Open the project in the Android Studio.
4. Run the **app** module.

### IntelliJ IDEA

1. Download and install [IntelliJ IDEA](https://www.jetbrains.com/idea/).
2. Clone the repository to your local machine.
3. Open the project in the IntelliJ IDEA.
4. Run the **app** module.

### Command line
1. Download and install [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or newer.
2. Download and install [Android Command Line Tools](https://developer.android.com/studio#command-tools).
3. Set android sdk path on `ANDROID_HOME` environment variable.
4. Clone the repository to your local machine.
5. Open the project in the terminal with `cd path/SpeakTouch`.
6. Run `./gradlew assembleDebug` to build the debug version.
7. The apk will be generated in `app/build/outputs/apk/debug`.

## License
[![GNU GPLv3 logo](https://www.gnu.org/graphics/gplv3-127x51.png)](https://www.gnu.org/licenses/gpl-3.0.html)

This project is licensed under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html), a copyleft open-source license. You are permitted to copy, modify, and redistribute the code, provided that the redistribution is accompanied by the corresponding source code under the same license in order to maintain its open-source nature. You can find a copy of the license in the project's [LICENSE file](/LICENSE) or the original version at https://www.gnu.org/licenses/gpl-3.0.html.
