# Vidya-Vahini — Complete Build Guide

## Prerequisites (Install These First)
1. Android Studio Ladybug (2024.2.1) or later — https://developer.android.com/studio
2. JDK 17+ (bundled with Android Studio)
3. A Google account (for Firebase)
4. Git (optional but recommended)

---

## STEP 1 — Create the Android Project

1. Open Android Studio → New Project
2. Choose: **Empty Activity** (with Compose support)
3. Set these values:
   - Name: `VidyaVahini`
   - Package: `com.vidyavahini`
   - Language: `Kotlin`
   - Min SDK: `API 24 (Android 7.0)`
   - Build config: Kotlin DSL (build.gradle.kts)
4. Click **Finish**

---

## STEP 2 — Set Up Firebase

1. Go to https://console.firebase.google.com
2. Click **Add Project** → Name it "VidyaVahini" → Continue
3. Disable Google Analytics (keep it simple) → Create Project
4. In the Firebase Console, click **Android icon** to add an Android app
   - Package name: `com.vidyavahini`
   - App nickname: VidyaVahini
   - Click **Register App**
5. Download `google-services.json` → Place it in `app/` folder of your project
6. Enable these Firebase services:
   - **Authentication** → Sign-in method → Enable **Email/Password** and **Anonymous**
   - **Realtime Database** → Create database → Start in **test mode**
   - **Cloud Messaging** (for notifications) — already enabled by default

### Firebase Database Rules (paste in Console → Realtime Database → Rules):
```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid"
      }
    },
    "routes": {
      ".read": "auth != null",
      ".write": "auth != null"
    },
    "admins": {
      ".read": "auth != null",
      ".write": "root.child('admins').child(auth.uid).exists()"
    }
  }
}
```

---

## STEP 3 — Copy the Code Files

Copy each file from the `code/` folder of this project into the matching path in Android Studio.

### File Mapping:
```
code/app/build.gradle.kts                          → app/build.gradle.kts
code/gradle/libs.versions.toml                     → gradle/libs.versions.toml

code/app/src/main/java/com/vidyavahini/
  data/model/User.kt                               → (same path in your project)
  data/model/Route.kt
  data/model/BusStatus.kt
  data/repository/AuthRepository.kt
  data/repository/RouteRepository.kt
  ui/theme/Theme.kt
  ui/theme/Color.kt
  ui/components/CommonComponents.kt
  ui/screens/SplashScreen.kt
  ui/screens/LoginScreen.kt
  ui/screens/RegisterScreen.kt
  ui/screens/RouteSelectionScreen.kt
  ui/screens/LiveStatusScreen.kt
  ui/screens/AdminScreen.kt
  ui/screens/SafeReachScreen.kt
  viewmodel/AuthViewModel.kt
  viewmodel/RouteViewModel.kt
  viewmodel/AdminViewModel.kt
  utils/NotificationHelper.kt
  utils/Constants.kt
  MainActivity.kt
  VidyaVahiniApp.kt
```

---

## STEP 4 — Build and Run

1. Sync Gradle: **File → Sync Project with Gradle Files**
2. Connect your Android device (enable USB debugging) or use an emulator
3. Click **Run** (green triangle)

---

## STEP 5 — Create Your First Admin User

1. Open Firebase Console → Authentication → Add User manually
2. Email: `admin@vidyavahini.com`, Password: (your choice)
3. Copy the User UID shown in Authentication tab
4. Go to Realtime Database → manually add:
```json
{
  "admins": {
    "<paste-uid-here>": true
  }
}
```
5. Add some sample routes in the database:
```json
{
  "routes": {
    "route1": {
      "routeId": "route1",
      "routeName": "Route 1 — City Centre",
      "currentLocation": "Main Bus Stand",
      "eta": "10 mins",
      "isDelayed": false,
      "isCancelled": false,
      "lastUpdated": 0
    },
    "route2": {
      "routeId": "route2",
      "routeName": "Route 2 — North Campus",
      "currentLocation": "Checkpoint A",
      "eta": "20 mins",
      "isDelayed": false,
      "isCancelled": false,
      "lastUpdated": 0
    }
  }
}
```

---

## App Flow Summary

```
Launch → SplashScreen (2s)
  ├─ Not logged in → LoginScreen → RegisterScreen
  └─ Logged in
       ├─ Student → RouteSelectionScreen → LiveStatusScreen → SafeReachScreen
       └─ Admin   → AdminScreen (update routes, send alerts)
```

---

## Troubleshooting

| Problem | Fix |
|---|---|
| `google-services.json` missing | Re-download from Firebase Console and place in `app/` |
| Gradle sync fails | Check `libs.versions.toml` versions match exactly |
| Firebase permission denied | Check DB rules are in Test Mode |
| Notifications not received | Ensure FCM is enabled in Firebase Console |
| App crashes on login | Verify Email/Password auth is enabled in Firebase |
