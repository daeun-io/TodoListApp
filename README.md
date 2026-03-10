# TodoListApp ✅
TodoListApp is a task management application that helps you organize your to-do lists and send push notifications at a scheduled time.

## Overview
- version1: Add, update, or delete to-do data in RoomDB or Firebase Database, depending on the user’s login state, and can complete a to-do by clicking the check box.
- version2(main): Implemented new features like being able to choose a storage and a category, pushing notifications, etc.

## Key Features
- Implemented social login using Kakao SDK
- Managed local and cloud data using Room Database and Firebase Realtime Database
- Implemented scheduled push notifications<br>
<p>
  <img src="https://github.com/user-attachments/assets/6af665ae-7c49-47ae-a9c8-2186b53f8211" width="24%" />
  <img src="https://github.com/user-attachments/assets/49b1ce64-9e48-4c2c-824b-5f914466b6bd" width="24%" />
  <img src="https://github.com/user-attachments/assets/ab03cd6c-ab1a-4688-9a56-3c8eeaa78690" width="24%" />
  <img src="https://github.com/user-attachments/assets/51992895-76e1-4e2a-90e2-0070840f86a2" width="24%" />
</p>

## Tech Stack
- Language: Android(Kotlin)
- Tools: Firebase + Kakao Sdk: applied Firebase Database for storing data in the cloud and Kakao Sdk for social login

## Improvements (version1 → main)
- Changed overall UI to support new and revised features
- **New Features**
    - Created some properties in the Todo data model for various actions and events
    - Created category data model for filtering and grouping todos
    - Implemented push notifications at a scheduled time
- **Revised Features**
    - Changed login method from email/password authentication to social login
    - Fixed logout error by navigating users to the login screen
- **Refactoring**
    - Refactored ViewModels and Repository layers by merging two instances into one
    - Replaced data type from LiveData to StateFlow for immediate state updates

## Current Issues
- The screen is not updated instantly when the user clicks the check box of a to-do in the Firebase Database. It requires an additional action, like clicking another button.
- Can’t use access restriction, _request.auth != null_, in Firebase Database rules because Firebase Authentication is not used. For now, reading and writing are allowed to all.
