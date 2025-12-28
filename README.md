# 📱 NewCalculator

A modern, clean, and efficient Calculator App for Android built with **Jetpack Compose**.
In addition to standard arithmetic operations, it features a **real-time Euro (EUR) to Japanese Yen (JPY) currency converter** with offline support.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-purple) ![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-green) ![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-blue)

## ✨ Features

* **Standard Calculation**: Supports addition, subtraction, multiplication, division, and percentages.
* **Expression Evaluation**: Handles complex expressions with operator priority using a recursive descent parser.
* **Currency Conversion**:
    * Real-time conversion between **EUR** and **JPY**.
    * Fetches the latest rates from a remote API.
    * **Offline Mode**: Automatically caches the latest rate locally using DataStore, allowing conversion even without internet access.
* **Modern UI**: Fully built with **Material 3** components and a clean, intuitive design.
* **Smart Formatting**: Auto-formats numbers with thousands separators and decimal limits for better readability.

## 🛠 Tech Stack & Libraries

* **Language**: Kotlin
* **UI Toolkit**: Jetpack Compose (Material 3)
* **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture principles
* **Asynchronous Processing**: Coroutines & Flow
* **Networking**: Retrofit2 & Gson
* **Local Storage**: Jetpack DataStore Preferences
* **Testing**: JUnit 4 (Unit tests for domain logic)

## 📂 Project Structure

The project follows a clean separation of concerns:

```text
com.mika.newcalculator
 ┣ 📂 data               # Data Layer
 ┃ ┣ 📂 local            # Local Storage
 ┃ ┃ ┗ 📜 ExchangeRatePreferences.kt
 ┃ ┗ 📂 remote           # Network (API)
 ┃   ┣ 📜 ExchangeResponse.kt
 ┃   ┣ 📜 FrankfurterApi.kt
 ┃   ┗ 📜 RetrofitInstance.kt
 ┣ 📂 domain             # Domain Layer
 ┃ ┗ 📜 CalculatorLogic.kt
 ┣ 📂 repository         # Repository Layer
 ┃ ┗ 📜 CurrencyRepository.kt
 ┗ 📂 ui                 # UI Layer
   ┣ 📜 MainActivity.kt
   ┣ 📂 calculator       # Calculator Screen & ViewModel
   ┃ ┣ 📜 CalculatorScreen.kt
   ┃ ┗ 📜 CalculatorViewModel.kt
   ┗ 📂 theme            # App Theme
     ┣ 📜 Color.kt
     ┗ 📜 Theme.kt

## 🚀 How to Run

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/YourUsername/NewCalculator.git](https://github.com/YourUsername/NewCalculator.git)
    ```
2.  **Open in Android Studio**
    * Open Android Studio and select "Open an existing project".
    * Select the cloned folder.
3.  **Sync Gradle**
    * Wait for the project to sync dependencies.
4.  **Run the App**
    * Select an emulator or connect a physical device.
    * Click the green "Run" button (▶).

## 🧪 Testing

The core calculation logic is covered by Unit Tests. To run them:

1.  Open `src/test/java/com/mika/newcalculator/domain/CalculatorLogicTest.kt`.
2.  Click the run icon next to the class name.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.