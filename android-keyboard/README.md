# iOS 26 Keyboard - Android

Bàn phím Android Kotlin với phong cách iOS 26, bao gồm hiệu ứng Glassmorphism, animation mượt mà, và hỗ trợ Light/Dark Mode.

## Features

- Hiệu ứng Glassmorphism (kính mờ) như iOS 26
- Animation khi nhấn phím (spring effect)
- Hỗ trợ Light Mode và Dark Mode
- Bố cục QWERTY, số, và ký tự đặc biệt
- Gợi ý từ thông minh
- Haptic feedback
- Hỗ trợ đa ngôn ngữ

## Build APK

### Build Local

```bash
# Build Debug APK
./gradlew assembleDebug

# Build Release APK
./gradlew assembleRelease
```

APK được tạo tại:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

### Build với GitHub Actions

1. Push code lên GitHub repository
2. GitHub Actions sẽ tự động build APK
3. Download APK từ tab **Actions** → **Artifacts**

### Cấu hình Signed Release APK

Để build APK đã ký cho Play Store:

1. Tạo keystore:
```bash
keytool -genkey -v -keystore release.keystore -alias ios26keyboard -keyalg RSA -keysize 2048 -validity 10000
```

2. Encode keystore thành base64:
```bash
base64 -w 0 release.keystore > keystore_base64.txt
```

3. Thêm GitHub Secrets:
   - `KEYSTORE_BASE64` - Nội dung file keystore_base64.txt
   - `KEYSTORE_PASSWORD` - Mật khẩu keystore
   - `KEY_ALIAS` - Alias của key (ios26keyboard)
   - `KEY_PASSWORD` - Mật khẩu key

## Installation

1. Download APK từ GitHub Actions
2. Cài đặt APK trên thiết bị Android
3. Vào **Settings** → **System** → **Languages & Input** → **On-screen keyboard**
4. Bật **iOS 26 Keyboard**
5. Chọn làm bàn phím mặc định

## Project Structure

```
app/
├── src/main/
│   ├── java/com/ios26keyboard/
│   │   ├── MainActivity.kt
│   │   ├── SettingsActivity.kt
│   │   ├── model/
│   │   │   ├── KeyboardState.kt
│   │   │   └── KeyData.kt
│   │   ├── service/
│   │   │   └── iOS26KeyboardService.kt
│   │   ├── utils/
│   │   │   ├── KeyAnimationHelper.kt
│   │   │   └── HapticHelper.kt
│   │   └── view/
│   │       └── KeyboardView.kt
│   └── res/
│       ├── drawable/
│       ├── layout/
│       ├── values/
│       └── xml/
├── build.gradle.kts
└── proguard-rules.pro
```

## Requirements

- Android 7.0 (API 24) trở lên
- JDK 17
- Gradle 8.4+

## License

MIT License
