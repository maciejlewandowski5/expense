# Expense App

Welcome to the Expense project. It's an application allowing for easy and effortless group budgets management.


## Table of content
1. [Technologies & Dependencies](#paragraph1)
2. [Project setup & Configuration](#paragraph2)
3. [Project guide](#paragraph3)
    - [General project architecture](#projectGuide1)
    - [Build types](#projectGuide2)
    - [Build flavors](#projectGuide3)

## Technologies & Dependencies <a name="paragraph1"></a>

- Kotlin (v 1.6.10)
- Gradle Android Plugin (v 7.1.2)
- KotlinX Coroutines (v 1.6.0)
- Jetpack Compose Navigation (v 2.4.2)
- Dagger + Hilt (v 2.41)
- Annotation (v 1.3.0)
- Material Design (v 1.5.0), 
- AndroidX Lifecycle (v 2.4.1), Compose(v 1.1.1)
- Room (v 2.4.2)

## Project setup & Configuration <a name="paragraph2"></a>
1. Download repository and open it with Android Studio.

## Project guide <a name="paragraph3"></a>
Expense is a single-module app structured with use of MVVM archtecture pattern. Components are connected with the use od `Dagger + Hilt`. UI layer is based on signle activity, compose navigation. Views are based on compose. Asnychronus operations are performed with `Kotlin Coroutines`. Local storage is build with `SharedPreferences` and `Room`.

The project is divided into multiple packages. Packages reflects specific app feature.
