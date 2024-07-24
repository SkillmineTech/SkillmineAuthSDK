# Integrating `SkillmineAuth` Library in Your Android App

## Introduction

The `skillmineAuth` Library enables seamless integration of authentication in Android applications. It facilitates handling OAuth2 flows, allowing developers to manage user authentication and token retrieval within their apps easily.

## Prerequisites

Ensure you have the following before starting:

- Android Studio installed.
- An existing Android project.
- Minimum SDK version 21 or higher.
- Internet permission in your `AndroidManifest.xml` file:
  ```xml
  <uses-permission android:name="android.permission.INTERNET" />

# SkillmineAuth Library

## Installation
To use the AuthWeb library, add it to your project dependencies.Open your build.gradle file (Module: app) and add the following:

Sync your project to download and include the dependency.

```gradle
auth_web_version = "1.0.4"
implementation "com.github.SkillmineTech:SkillmineAuthSDK:${auth_web_version}

```

## Authentication Flow
To initiate the authentication process, create an intent to open the Authentication Activity provided by the library. Use an ActivityResultLauncher to handle the result of the authentication process.

Define authActivityResultLauncher in the Activity
Here, we want to open the Authentication Activity from LoginActivity.

```gradle
// Define the BASE_URL and CLIENT_ID
const val BASE_URL = "https://nightly-accounts-api.complyment.com/authz-srv/authz"
const val CLIENT_ID = "236b91c8-b2f0-4891-a83c-f358a109a843"
```
Initialize the authActivityResultLauncher:

```val authActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        // Handle the result here
        val accessToken = data?.getStringExtra("accessToken")
        // Use the access token as needed
    }
}
```
Open the Library’s Authentication Activity:
```loginButton.setOnClickListener {
    val intent = Intent(this@LoginActivity, AuthenticationActivity::class.java).apply {
        putExtra("baseUrl", BASE_URL)
        putExtra("clientId", CLIENT_ID)
    }
    authActivityResultLauncher.launch(intent)
}
```
## Conclusion
This guide has covered the basic steps to integrate and use the auth_webview_sdk Package in your Flutter app. For more advanced configurations and troubleshooting, contact Skillmine Technologies.
