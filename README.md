<img width="1080" height="2408" alt="image" src="https://github.com/user-attachments/assets/3060366b-fab2-4dcb-a40d-2d04baa00479" />
<img width="1080" height="2408" alt="image" src="https://github.com/user-attachments/assets/07817691-c233-43dc-bbb4-99a76c315c21" />
<img width="1080" height="2408" alt="image" src="https://github.com/user-attachments/assets/f5f77cc8-de57-4f9b-b919-960ecb60e148" />
# Vidya-Vahini 
**Smart Student Transport Tracker | Rural Student Transport & Safety Platform**

Vidya-Vahini is a lightweight application designed to improve transport reliability and safety for students in rural areas. Functioning as a collaborative **"Student Commute Buddy,"** the platform empowers students to crowdsource real-time transport updates for school and college routes, minimizing unsafe waiting times at isolated transit points.

Vidya-Vahini is an Android-based student transport tracking application designed specifically for rural students who depend on public buses and school transportation. The application enables students to share real-time transport updates, receive arrival notifications, report vehicle breakdowns, and notify parents when they safely reach their destination.

The project aims to improve student safety, reduce transportation uncertainty, and enhance educational accessibility in rural communities.

Problem Statement

Students in rural areas often rely on limited transportation options to travel to schools and colleges. Delayed buses, route disruptions, and vehicle breakdowns can lead to:

Missed classes and examinations
Long waiting times at isolated bus stops
Safety concerns, especially for female students
Lack of real-time transport information

Vidya-Vahini addresses these challenges through a community-driven transport tracking system.

Objectives
Provide real-time transport updates for students.
Improve student safety during daily commutes.
Reduce uncertainty regarding bus arrival times.
Enable collaborative transport monitoring.
Notify parents and guardians when students safely reach their destination.
Minimize absenteeism caused by transportation issues.
Features
Bus Ping System

Students can instantly update the bus location by sending a "Ping" when the bus crosses a landmark.

ETA Prediction

Provides estimated bus arrival times based on predefined route checkpoints.

📢 Live Route Updates

Broadcasts transport updates to all students using the same route.

⚠ Breakdown Reporting

Allows users to report bus breakdowns or route cancellations.

Safe Reach Notification

Students can notify parents or friends upon safely reaching home or college.

Route Visualization

Displays the latest known bus position on a simplified route map.

Push Notifications

Real-time alerts for delays, ETA updates, breakdown reports, and route activity.

System Architecture
Frontend
Android Studio
Kotlin / Java
XML UI Design
Backend
Firebase Realtime Database
Authentication
Firebase Authentication
Notifications
Firebase Cloud Messaging (FCM)
Data Processing
ETA calculation using average travel duration between route checkpoints.
📱 User Flow
User opens the application.
Student registers or logs in.
Student selects their transport route.
Current bus status and ETA are displayed.
Student sends a Bus Ping when spotting the vehicle.
Route users receive instant updates.
Student marks Safe Reach after arriving at the destination.
🛠 Technology Stack
Technology	Purpose
Android Studio	Mobile App Development
Kotlin / Java	Application Logic
Firebase Realtime Database	Real-Time Data Synchronization
Firebase Authentication	User Authentication
Firebase Cloud Messaging	Push Notifications
XML	User Interface Design
Target Users
Rural School Students
College Students
Parents and Guardians
Educational Institutions
Non-Functional Requirements
Lightweight application
Low battery consumption
Offline-friendly design
Fast synchronization
Secure authentication
Low-end smartphone compatibility
Expected Impact
Educational Access

Reduces absenteeism caused by transportation uncertainty.

Student Safety

Minimizes waiting time at remote bus stops.

Time Management

Helps students plan their commute effectively.

Community Collaboration

Encourages students to share transport information for mutual benefit.

Future Enhancements
AI-based ETA prediction
GPS-enabled live bus tracking
Voice-assisted updates
Multi-language support
Institution Admin Dashboard
Advanced analytics and reporting
Challenges
Limited internet connectivity in rural areas
Dependence on crowdsourced updates
Preventing false route information
Device and battery limitations on older smartphones
Screenshots

Add screenshots of:

Login Screen
Route Selection Screen
Bus Tracking Dashboard
Bus Ping Feature
Safe Reach Notification Screen
Author

Pavitra
Android Application Developer

License

This project was developed as part of the MindMatrix Internship Program for educational and academic purposes only. Commercial use requires proper authorization.
