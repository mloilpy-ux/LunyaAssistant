# LunyaAssistant — Nana Banana character companion

Android assistant for Lunya with a floating overlay, character reactions, wardrobe systems and Nana Banana image-task integration.

## Current integration

- Android 8.0+ / API 26
- JDK 17
- Nana Banana `record-info` task lookup
- Polling helper that waits for `resultImageUrl`
- Example task ID: `1e099185c5d9ac033ce9678225fb46a4`
- API credentials are runtime-only and are **not committed** to Git
- Existing overlay, accessibility and notification services remain part of the app

The existing project already has a GitHub Actions debug-APK workflow and the Android module is configured for Java/Kotlin 17. fileciteturn10file0 fileciteturn7file0

## Nana Banana

See `docs/NANA_BANANA.md` for the API contract and secure credential handling.

The requested master ZIP is not currently mounted as a conversation file, so the repository does not invent replacement assets. Add the supplied master assets to `app/src/main/assets/` and wire their real filenames into the existing wardrobe/skin catalog.

## Build

```bash
gradle assembleDebug --no-daemon
```

GitHub Actions is configured to build `app-debug.apk` and upload it as `LunyaAssistant-Debug-APK`. fileciteturn10file0
