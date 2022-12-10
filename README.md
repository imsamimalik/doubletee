# doubleTee

doubletee is a university timetable management system that provides a range of features for students, faculty, and administrators. With doubletee, users can login or sign up, add and remove courses, rooms, and faculty, register for courses, assign, modify, delete, and view their timetables, and even download their timetable as a CSV file. Additionally, doubletee offers an automatic timetable generation feature based on registered and assigned courses, as well as a clashless timetable feature and the ability to view faculty, student, and room availability. Users can also create personalized timetables to fit their specific needs.


![screenshot of timetable (admin view)](https://i.ibb.co/FnFyhZg/screenshot.png "screenshot of timetable")

## Features
  
- Login/Sign up User
  - Student
  - Faculty
  - Admin
- Delete Users (Admin only)
- Add Faculty/Admin (faculty registration only allowed if added by admin)
- Add/Remove Courses
- Add/Remove Rooms
- Add/Remove Faculty
- Register Courses (Student Only)
- Assign/Modify/Delete/View TimeTable
- Auto TimeTable for Stuent/Faculty based on Registered/Assigned Coures
- Clashless TimeTable
- View Faculty/Student/Room Availability
- Make personalized TimeTable
- Download TimeTable as CSV

## Requiements

- Java v.11 or higher
- Java sdk v.17 or higher
- intelliJ Idea (preferable)
- MySql Database

## Steps to run the Project

1. Open the project in intelliJ Idea Idea
   1. It should automatically install dependencies. If not then go to pom.xml file and install Maven dependencies from right click menu
2. Make a database named "dobuletee" in MySql database
3. Open the file "application.properties" in the path "src/main/resources"
4. Change the database username and password to your database username and password
5. Run the project from intelliJ Idea
6. Open the browser and type "localhost:8080" in the address bar
7. By default, the application comes with an allowed user (admin with employee id 1) in the database. First of all, register for admin with the id 1 and other required details
8. After registering, login with the same credentials
9. Now you can add other users and manage the timetable
