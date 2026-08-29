# LunyaAssistant

Cyber-style Android assistant with floating overlay avatar, accessibility services,wardrobe system, Google Workspace integration and root/system control features.

## Features
- Floating Lunya overlay character
- Modular avatar (hair, horns, glasses, outfits, cans)
- Accessibility service
- Notification listener
- Google Calendar / Tasks integration
- Screen automation & root actions (libsu)

## Build

```bash
./gradlew assembleDebug
```

APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

GitHub Actions automatically builds debug APK on every push to `main`.

## Requirements
- Android 8.0+ (API 26)
- JDK 17
