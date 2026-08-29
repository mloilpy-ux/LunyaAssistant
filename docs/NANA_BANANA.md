# Nana Banana integration

The Android client contains a small, runtime-key-only client for the Nana Banana task API.

## Task

Default example task:
`1e099185c5d9ac033ce9678225fb46a4`

The app queries:
`GET https://api.nanobananaapi.ai/api/v1/nanobanana/record-info?taskId=<taskId>`

with:
`Authorization: Bearer <NANABANANA_API_KEY>`

When `resultImageUrl` is present, `NanaBananaTaskPoller` returns it for the UI/image loader.

## Security

Do **not** commit an API key to Kotlin, Gradle, GitHub Actions, or the APK. Supply it at runtime through a secure backend or local secret/configuration mechanism. The previously supplied key is intentionally not embedded in this repository.

## Assets

The master asset ZIP referenced by the project request was not available as a mounted conversation file, so binary/vector assets have not been fabricated or substituted. Add the real assets under `app/src/main/assets/` when available.
