# SendMoneyApp Demo

## Overview

SendMoneyApp is a demo Android application designed to showcase a simplified fintech flow, focusing on modern Android development practices, clean architecture, and robust design patterns. The app allows users to log in (using mock credentials) and navigate to a "send money" screen and a "saved requests" screen.

## Features

*   User Authentication (Mock Login)
*   Navigation to Send Money Screen
*   Navigation to Saved Requests Screen
*   Localization Support:
    *   US English (en-US) - Default
    *   Arabic (ar)

## Architecture & Design Choices

This application adheres to the following architectural and design principles:

### 1. Architecture: MVVM (Model-View-ViewModel)
The app employs the **MVVM architecture** as recommended by Android Jetpack.
*   **View (Composables):** UI is built entirely with **Jetpack Compose**, representing the View layer. Screens like `LoginScreen`, `SendMoneyScreen`, and `SavedRequestsScreen` are Composables that observe state from ViewModels.
*   **ViewModel (`LoginViewModel`, `SendMoneyViewModel`, etc.):** ViewModels are responsible for holding and managing UI-related data in a lifecycle-conscious way. They expose UI states using Kotlin **`StateFlow`** and handle user interactions, delegating business logic and data operations.
*   **Model (Data Layer):** This layer includes components like `UserSessionManager` which interacts with **Jetpack DataStore** for persisting user login state. For more complex data operations, Repositories and UseCases would be introduced here.

### 2. Dependency Injection: Hilt
**Hilt** is used for dependency injection to manage dependencies throughout the app (e.g., injecting `UserSessionManager` into ViewModels or `MainActivity`). This simplifies DI and reduces boilerplate compared to manual injection or Dagger.

### 3. UI State Management: Kotlin StateFlow
UI states are exposed from ViewModels using Kotlin **`StateFlow`**, enabling reactive programming. Composables collect these flows to update the UI in response to state changes.

### 4. UI Development: Jetpack Compose
The entire UI is built using **Jetpack Compose**, leveraging modern declarative UI development.

### 5. Modularity
While currently a single-module application (app module), the architecture is designed to be scalable. For more complex features, the app could be further modularized (e.g., separating UI, data, and domain layers into distinct library modules).

### 6. Design Principles: Material Design
The app aims to follow **Material Design 3** principles for UI/UX. This includes:
*   **Theming:** A custom theme (`SendMoneyAppTheme`) is defined (colors, typography).
*   **Accessibility:** (Placeholder - For a production app, ensuring content descriptions, sufficient contrast, and navigable elements for accessibility tools would be a priority).

### 7. Configuration Changes & Performance
*   **Configuration Changes:** Gracefully handled by ViewModels (which survive configuration changes) and state management in Compose (e.g., `rememberSaveable`).
*   **Performance:**
    *   Lifecycle-aware components (`ViewModel`, `LaunchedEffect`, etc.) are used to prevent memory leaks.
    *   `viewModelScope` ensures coroutines are managed within the ViewModel's lifecycle.
    *   (For large lists, Paging 3 would be implemented - not currently applicable in this demo).

## Code Quality & Best Practices

*   **Language:** The application is written entirely in **Kotlin**.
*   **Asynchronous Operations:** **Coroutines** are used for asynchronous operations (e.g., interacting with DataStore in `UserSessionManager`, handling delays) with **structured concurrency** (`viewModelScope`, `lifecycleScope`).
*   **Error Handling:** Implemented robustly, for example, in `LoginViewModel`, where error states are managed with `StateFlow` and communicated to the UI to provide user-friendly feedback.
*   **Naming Conventions & Readability:** Standard Kotlin naming conventions are followed (CamelCase for classes, camelCase for functions/variables). Code aims to be self-documenting, with comments added for clarity where necessary.
*   **Thread Safety:** UI updates are ensured to run on the main thread (Compose handles this inherently when observing StateFlows), and background work is managed by coroutine dispatchers (e.g., `Dispatchers.IO` for DataStore operations).
*   **Navigation:** **Navigation Compose** is used for routing between screens, with routes defined in a centralized `Routes` object for type safety and maintainability.

## Security & Privacy

*   **Secure Storage:** (Placeholder - For a production fintech app, `EncryptedSharedPreferences` would be used for storing any sensitive data like auth tokens. The current `UserSessionManager` uses DataStore for a non-sensitive login flag).
*   **Best Practices:** (Placeholder - Features like preventing screen capture on sensitive screens (`FLAG_SECURE`) would be implemented in a production fintech app).
*   **Permissions:** (Placeholder - Runtime permissions with rationale dialogs would be implemented if the app required any sensitive permissions. This demo currently does not require special permissions).

## Testing

*   **Unit Tests:** JUnit5 is used for writing unit tests for ViewModels (e.g., `LoginViewModelTest`). Dependencies are mocked using **Mockito** (or MockK as per guidelines). The focus is on testing business logic, state changes, and interactions.
*   **Running Tests:** To run all unit tests from the command line, navigate to the project root and execute:
*   Test Coverage Report (JaCoCo): If JaCoCo is configured for test coverage reporting, you can generate a report using:
     (Note: Replace jacocoTestReport with the appropriate task for your project if it's named differently or for another build variant.) The HTML report can usually be found in app/build/reports/jacoco/jacocoTestReportDebug/html/index.html.

## Build and Run Instructions

1.  **Prerequisites:**
    *   Android Studio (latest stable version, e.g., Iguana or newer).
    *   Android SDK (as specified in `build.gradle.kts`, typically API 34+).
    *   Git.
2.  **Clone the Repository:**

3.  **Open in Android Studio:**
    *   Launch Android Studio.
    *   Select "Open" and choose the cloned `<project-directory>`.
    *   Allow Android Studio to sync Gradle and download dependencies.
4.  **Run the App:**
    *   Connect an Android device (with USB debugging enabled) or start an Android Emulator.
    *   Click the "Run 'app'" button (green play icon) in Android Studio, or select **Run > Run 'app'**.

## Screenshots

*   *Screenshot 1: Login Screen*
    * 1 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/login_1.jpeg?raw=true) 
    * 2 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/login_2.jpeg?raw=true) 
    * 3 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/login_3.jpeg?raw=true) 
*   *Screenshot 2: Send Money Screen*
    * 1 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/send_money_1.jpeg?raw=true) 
    * 2 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/send_money_2.jpeg?raw=true) 
    * 3 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/send_money_3.jpeg?raw=true) 
    * 4 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/send_money_4.jpeg?raw=true) 
    * 5 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/send_money_5.jpeg?raw=true) 
    * 6 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/send_money_6.jpeg?raw=true) 
    * 7 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/send_money_7.jpeg?raw=true) 
*   *Screenshot 3: Saved Requests Screen*
    * 1 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/saved_requestes_1.jpeg?raw=true) 
    * 2 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/saved_requestes_2.jpeg?raw=true) 
    * 3 ![image_alt](https://github.com/mkrtchyanmnatsakan/SendMoney/blob/master/saved_requestes_3.jpeg?raw=true) 



    
