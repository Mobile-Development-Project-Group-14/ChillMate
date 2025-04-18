# 🌤️ ChillMate – Smart Weather & Outfit Companion 🌦️👕

[![GitHub License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-brightgreen)](https://developer.android.com/jetpack/compose)

## Description

**ChillMate** is a mobile app designed to provide **real-time weather updates** along with **personalized outfit recommendations** to help users dress appropriately for rapidly changing weather conditions. Beyond weather insights, the app offers **shopping guidance** for the best clothing and outfit suppliers, suggests **outdoor activities** suited to the weather, and even helps users **pre-plan their outfits** for vacations or trips.

## Features ✨

- ✅ **Weather-Based Outfit Suggestions** – Receive real-time outfit recommendations tailored to live weather conditions.
- ✅ **Shopping Assistance** – Find the best stores for weather-appropriate outfits and gear.
- ✅ **Outdoor Activity Suggestions** – Discover activities that suit the day’s weather conditions.
- ✅ **Trip & Vacation Preparation** – Plan your wardrobe ahead of time for upcoming trips.

### 🌦️ Smart Weather Integration
- Real-time weather updates using device GPS
- 7-day forecast with temperature/precipitation
- Severe weather alerts system

### 👗 Context-Aware Outfit System
- Temperature-based clothing recommendations
- Layering suggestions for changing conditions
- Direct shopping links for recommended items

### 🎯 Activity Suggestions
- Weather-appropriate outdoor/indoor activities
- Detailed equipment checklists
- Price level indicators (Free/€/€€/€€€)

### ✈️ Travel Preparation
- Vacation wardrobe planner
- Packing list generator

## Flowchart

![Flowchart](docs/Flow%20Chart.png)


## Team

| Name               | GitHub Name      | Role                           |
| ------------------ | ---------------- | ------------------------------ |
| Sujeewa            | SampathHM        | Developer/Project Management   |
| Nadeesha           | t3rana00         | Developer/Designer             |
| Hasitha            | hasiya89         | Developer/Tester               |

## 🙏 Credits

Special thanks to **jochang** for sharing public Lottie animations used in ChillMate.  
Animations sourced from: [LottieFiles - vdr0uy2wwsoljqtc](https://lottiefiles.com/vdr0uy2wwsoljqtc)

All assets are used under the license provided by LottieFiles and the respective creators.

## Tech Stack 🛠️

- **Development:** Android Studio + Kotlin (Jetpack Compose)
- **Weather Data:** Integrated API for real-time weather updates

### Core Components
- **Frontend**: Jetpack Compose (100% Kotlin)
- **Architecture**: MVVM with Clean Architecture
- **Animations**: Lottie for weather visualizations

### Key Libraries
- **Retrofit 2** - Weather API integration
- **Coil** - Image loading
- **Geocoder** - Location processing
- **Material 3** - Modern UI components

### APIs Used
- **Open-Meteo** - Global weather data
    - https://open-meteo.com/
- **GeoNames** - City database for travel planning
    - http://geodb-free-service.wirefreethought.com/v1/geo/cities?namePrefix=oul&limit=10

## Device Features Used 📱

| Feature               | Usage                                  |
|-----------------------|----------------------------------------|
| GPS Location          | Real-time weather data collection      |
| Network Status        | API call management                    |

## 🔐 Permissions

ChillMate requests the following permissions:

- `ACCESS_FINE_LOCATION` – To fetch accurate weather data based on user location
- `INTERNET` – To communicate with external APIs
- `ACCESS_NETWORK_STATE` – To check network availability

## WireFrames

![Home Screen Wireframe](docs/Chillmate%20readme2.jpeg)
![Home Screen Wireframe](docs/Chillmate%20readme1.jpeg)

## 📸 Screenshots

_Coming soon... UI previews of ChillMate in action!_

## ⚙️ Installation

1. Clone the repository:
```bash
git clone https://github.com/Mobile-Development-Project-Group-14/ChillMate