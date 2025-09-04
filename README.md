FalconEye (3rd Eye)
FalconEye (a.k.a. 3rd Eye) is a GPS tracking and monitoring Android application built with Kotlin & Java using MVP architecture. It enables real-time location tracking, order management, push notifications, and offline data sync.

✨ Features
📍 GPS Tracking – Background service to track and send live location

🔔 Push Notifications – Firebase Cloud Messaging integration

📦 Order & Product Management – Dealer, orders, and product catalog views

🗄 Local Database – Room persistence for offline usage

🖼 Custom UI Components – ToastyLayout for styled notifications

🚀 Auto Start/Stop Tracking with broadcast receivers

🛠 Tech Stack
Language: Kotlin + Java

Architecture: MVP

Database: Room (SQLite)

Push Notifications: Firebase Cloud Messaging (FCM)

UI: RecyclerView, Custom Adapters

Build System: Gradle

📂 Project Structure
app/
 ┣ src/main/java/com/teknopole/track3rdeye/
 ┃ ┣ App/ → Application setup, database  
 ┃ ┣ DAOs/ → Room DAOs  
 ┃ ┣ Fcm/ → Firebase messaging service  
 ┃ ┣ GpsTracking/ → GPS services & receivers  
 ┃ ┣ MVP/ → Adapters, Contracts, Presenters, Views  
 ┣ res/ → Layouts, Drawables, Values  
 
🚀 Getting Started
Prerequisites
Android Studio (latest version)

JDK 11+

Firebase project (add your own google-services.json)

Installation
# Clone the repository
git clone https://github.com/USERNAME/falconeye.git

# Open in Android Studio and sync Gradle
Run
Connect an Android device or emulator

Hit Run ▶ from Android Studio


📜 License
This project is licensed under the MIT License
