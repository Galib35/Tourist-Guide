# Tourist Guide

**Tourist Guide** is a comprehensive Android application developed to assist tourists in discovering and exploring popular spots in Bangladesh. The app provides a centralized platform for tourists to access location-specific recommendations, weather updates, chatbot assistance, and spot details, all in one place.

## Features

### 1. **Spot Search**
- Tourists can search for tourist spots by **name**, **tag**, or **city**.
- The app is designed with an advanced search system that allows up to **30% typing mismatch**, making the search process more flexible.
- **Substring matching** and **dynamic programming algorithms** are implemented to optimize the search process and provide accurate results.

### 2. **Tour Recommendations**
- Based on the user’s **current location**, the app recommends tourist spots nearby.
- The **Geocoder class** is used to convert latitude and longitude into localities, ensuring precise recommendations.
- Location services continuously track the user’s position to offer up-to-date spot suggestions.

### 3. **Weather Forecast**
- Integrated with the **OpenWeather API**, the app provides:
  - **Current weather conditions** based on zip code.
  - **5-day weather forecasts** using latitude and longitude coordinates.
- The API responses are in **JSON format**, which is parsed using the **Volley library** for efficient data handling.

### 4. **Virtual Tourist Guide Chatbot**
- A basic-level chatbot is integrated to assist users with their queries about tourist spots and provide general travel guidance.
- The chatbot is designed to improve user engagement by simulating conversation-like interactions.

### 5. **Google Map Services**
- The app uses **Google Maps API** to display:
  - **Nearby hotels**, **restaurants**, **attractions**, **hospitals**, and **police stations** on a map view.
- Tourists can navigate the city and access essential services easily, helping them plan their trips more effectively.


### 8. **Admin Dashboard**
- An admin interface allows authorized personnel to:
  - **Add new tourist spots**.

### 9. **Agency Dashboard**
- Various travel agencies can manage their own dashboards, offering tour packages and managing bookings.
- Agencies can post promotional content and manage reservations made by tourists through the app.

## System Architecture

The app follows a modular system architecture that enables each of the key functionalities (search, recommendations, chatbot, etc.) to function independently while interacting with each other as needed. This architecture enhances scalability and maintainability, allowing for future expansions.

## System Implementation

### 1. **Optimized Searching**
- Search functionality is made robust with advanced algorithms for substring matching.
- A **30% mismatch tolerance** ensures that users can find results even with typing errors.

### 2. **Tour Recommendations**
- Using **GPS coordinates**, the app suggests relevant spots based on proximity.
- The **Geocoder class** converts geographic coordinates into recognizable location names.

### 3. **Weather Forecast**
- Integrated with the **OpenWeather API** to provide real-time weather information.
- The app supports both **current weather data** and **5-day forecasts**, ensuring tourists can plan their trips efficiently.

### 4. **Google Map Services**
- **Google Map’s Place API** allows users to discover essential services like restaurants, hotels, and hospitals near tourist spots.
- The app’s map view is interactive and highlights points of interest.

### 5. **Chatbot Integration**
- A simple chatbot interface is implemented to help users get answers to common questions.
- The chatbot’s functionality is basic but can be improved for more complex interactions in future versions.


## Future Work

- **Enhanced Chatbot**: Further development of the chatbot to handle more complex queries and interactions.
- **Real-Time Transportation Information**: Adding real-time transport updates to help tourists plan their journeys better.
- **Database Enrichment**: Expanding the database with more spots, user reviews, and detailed information.
- **Advanced Features**: Implementing dynamic features such as **paid APIs** for a richer user experience.


## Technologies Used

- **Android Studio**
- **Google Maps API**
- **Firebase Storage**
- **OpenWeather API**
- **Java** and **XML** for Android development

