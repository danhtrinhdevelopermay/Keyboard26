# iOS 26 Keyboard - Android Kotlin Project

## Overview
Ứng dụng bàn phím Android Kotlin với phong cách iOS 26, bao gồm:
- Hiệu ứng Glassmorphism (kính mờ)
- Animation khi nhấn phím
- Hỗ trợ Light Mode và Dark Mode
- Bố cục QWERTY, số, và ký tự đặc biệt
- Gợi ý từ thông minh

## Project Structure

### Web Demo
Demo trực quan để xem trước giao diện bàn phím:
- `index.html` - Giao diện demo
- `style.css` - CSS với hiệu ứng glassmorphism
- `script.js` - Logic bàn phím tương tác
- `server.js` - Server Node.js

### Android Kotlin Source Code
Mã nguồn đầy đủ cho Android Studio:

```
android-keyboard/
├── .github/workflows/
│   └── android-build.yml      # GitHub Actions workflow
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/ios26keyboard/
│       │   ├── MainActivity.kt
│       │   ├── SettingsActivity.kt
│       │   ├── model/
│       │   │   ├── KeyboardState.kt
│       │   │   └── KeyData.kt
│       │   ├── service/
│       │   │   └── iOS26KeyboardService.kt
│       │   ├── utils/
│       │   │   ├── KeyAnimationHelper.kt
│       │   │   └── HapticHelper.kt
│       │   └── view/
│       │       └── KeyboardView.kt
│       └── res/
│           ├── drawable/
│           ├── layout/
│           ├── values/
│           └── xml/
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

## GitHub Actions - Build APK

### Cách sử dụng
1. Push code lên GitHub repository
2. GitHub Actions tự động chạy khi push lên main/master/develop
3. Download APK từ tab **Actions** → **Artifacts**

### Cấu hình Secrets cho Release APK
Vào **Settings** → **Secrets and variables** → **Actions** → **New repository secret**:

| Secret Name | Mô tả |
|-------------|-------|
| `KEYSTORE_BASE64` | Keystore file encoded base64 |
| `KEYSTORE_PASSWORD` | Mật khẩu keystore |
| `KEY_ALIAS` | Alias của key |
| `KEY_PASSWORD` | Mật khẩu key |

### Tạo Keystore mới
```bash
keytool -genkey -v -keystore release.keystore -alias ios26keyboard -keyalg RSA -keysize 2048 -validity 10000
base64 -w 0 release.keystore > keystore_base64.txt
```

## Features Implemented

### 1. Glassmorphism Background
- Nền bán trong suốt với blur effect
- Gradient tinh tế từ trên xuống dưới
- Bo góc 22px ở góc dưới

### 2. Key Animations
- Spring animation khi nhấn phím (80ms)
- Scale up 115% khi nhấn
- Ripple light effect màu xanh lam
- Elevation tăng khi nhấn

### 3. iOS 26 Dark Style
- Phím tối trong suốt (#424247) với chữ trắng
- Phím đặc biệt màu tối hơn (#323235)
- Phím Search/Return màu xám sáng (#8E8E93)
- Nền bán trong suốt (#232326) với blur effect
- Tự động theo system theme

### 4. Keyboard Layouts
- QWERTY cho chữ cái
- Bàn phím số với ký tự phụ
- Bàn phím ký tự đặc biệt

### 5. Special Keys
- Shift (toggle/caps lock)
- Backspace với animation
- Space bar
- Return/Search key
- Globe (chuyển ngôn ngữ)
- Emoji button
- Microphone button

## How to Use

### Web Demo
1. Chạy server với `node server.js`
2. Mở trình duyệt tại `http://localhost:5000`
3. Nhấn các phím để test
4. Chuyển đổi Light/Dark mode

### Android Studio (Local Build)
1. Mở thư mục `android-keyboard` trong Android Studio
2. Sync Gradle
3. Build và cài đặt lên thiết bị
4. Vào Settings > Language & Input
5. Bật iOS 26 Keyboard
6. Chọn làm bàn phím mặc định

### GitHub Actions Build
1. Fork hoặc push repository lên GitHub
2. Workflow tự động build APK
3. Download từ Actions > Artifacts

## Recent Changes
- 2025-11-28: Added navigation bar hiding when keyboard opens
  - Uses WindowInsetsController for Android 11+ (API 30+)
  - Falls back to SYSTEM_UI_FLAG for older Android versions
  - Immersive sticky mode for smooth UX
- 2025-11-28: Updated to iOS 26 dark keyboard style
  - Dark semi-transparent keys (#424247) with white text
  - Special keys with darker background (#323235)
  - Search/Return key with lighter gray (#8E8E93)
  - Preserved gaussian blur background effect
  - Updated web demo with matching dark colors
- 2025-11-28: Fixed app crash on launch
  - Added missing CardView dependency (cause of InflateException)
  - Changed theme to Theme.AppCompat.DayNight.NoActionBar for better compatibility
  - Removed unused imports (android.renderscript.*)
  - Removed unused Blurry library
  - Added values-night resources for proper dark mode support
  - Fixed Kotlin scope resolution in KeyboardView.kt
- 2025-11-28: Initial project setup
- 2025-11-28: Added GitHub Actions workflow for APK build
- Created full Android Kotlin keyboard implementation
- Created interactive web demo with glassmorphism effects
- Implemented all iOS 26 design specifications

## User Preferences
- Vietnamese language interface
- iOS 26 design style
- Glassmorphism effects
- Animation-focused UX
