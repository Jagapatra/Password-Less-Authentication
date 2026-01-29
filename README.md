# Passwordless OTP Authentication App

## Overview
This Android application demonstrates a **passwordless authentication system** using **email-based One-Time Password (OTP)**.  
The app is built using **Kotlin**, **Jetpack Compose**, and **MVVM architecture**, focusing on clean separation of concerns, lifecycle safety, and clear business logic.

The entire authentication flow is implemented **locally**, without any backend.

---

## Core Features

### 1. Passwordless Login
- User logs in using an **email address only**
- No passwords are stored or required

### 2. OTP Generation & Validation
- **6-digit numeric OTP**
- **Valid for 60 seconds**
- **Maximum 3 verification attempts**
- OTP data is stored **per email**
- Generating a new OTP:
  - Invalidates the old OTP
  - Resets the attempt counter

### 3. OTP Retry & Error Handling
- Displays **remaining attempts** on incorrect OTP entry
- Redirects user to Login screen when:
  - OTP expires
  - Attempts are exhausted

### 4. Live OTP Expiry Countdown
- Real-time countdown shown on OTP screen
- Countdown derived from OTP expiry timestamp
- Automatically updates every second

### 5. Session Management
- On successful login, a **session screen** is shown
- Displays **live session duration (mm:ss)**
- Session timer:
  - Starts after successful OTP verification
  - Stops and resets on logout

### 6. Logging & SDK Integration
- Integrated **Timber** logging SDK
- Logged events:
  - OTP generation
  - OTP verification success/failure
  - Logout event

---

## Architecture

The app follows **MVVM (Model–View–ViewModel)** architecture:

- **UI (Jetpack Compose)**  
  Stateless composables that react to state changes

- **ViewModel**  
  Manages authentication state, OTP flow, and session timer using `StateFlow`

- **Data Layer**  
  Handles OTP generation, expiry, validation, and retry logic

This ensures:
- Clean separation of concerns
- Lifecycle-safe state management
- Easy testing and maintainability

---

## Technologies Used

- **Kotlin**
- **Jetpack Compose**
- **Android ViewModel**
- **StateFlow & Coroutines**
- **Material 3**
- **Timber (Logging SDK)**

---

## OTP Logic Summary

- OTPs are generated and stored **per email**
- Each OTP contains:
  - Code
  - Expiry timestamp
  - Remaining attempts
- OTP validation returns structured results:
  - Success
  - Failure with remaining attempts
  - Attempts exhausted
  - Expired

This allows precise UI feedback and strict enforcement of rules.

---

## Conclusion

This project demonstrates a complete, clean, and scalable **passwordless authentication flow** using modern Android development practices.  
It emphasizes correctness, clarity, and maintainability over UI complexity.

