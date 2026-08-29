# LunyaAssistant — Nana Banana character companion

Android assistant for Lunya with a floating overlay, character reactions, wardrobe systems and Nana Banana image-task integration.

## Current integration

- Android 8.0+ / API 26
- JDK 17
- Nana Banana `generate-2` + `record-info` integration
- Canonical Lunya reference and persistent reaction state
- Persistent unified Lunya runtime coordinator
- Event-driven reactions, wardrobe and overlay support
- API credentials are runtime-only and are **not committed** to Git
- Existing overlay, accessibility and notification services remain part of the app
- Unified feature registry preserves the existing AI, animation, physics, tamagotchi, proactive, voice and interaction modules

## Build verification

The last recorded GitHub Actions run failed during Android resource linking because `activity_main.xml` contained unsupported `android:hintTextColor` attributes. The current layout no longer contains those attributes; a new commit is being pushed to trigger a clean verification build.

## Nana Banana

See `docs/NANA_BANANA.md` for the API contract and secure credential handling.

The requested master ZIP is not currently mounted as a conversation file, so the repository does not invent replacement binary assets.

## Build

```bash
gradle assembleDebug --no-daemon
```

GitHub Actions is configured to build `app-debug.apk` and upload it as `LunyaAssistant-Debug-APK`.
