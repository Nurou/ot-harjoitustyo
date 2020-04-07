# Architecture Overview

## Structure

The program structure follows a layered architecture with three tiers. The package hierarchy is as follows:

The studytracker.ui package contains the application's user interface implemented in JavaFX, studytracker.domain the application logic, and studytracker.dao the functionality used to interact with the database.

## User Interface

The user interface comprises of four separate views:

- Login (initial)
- Account creation
- Progress/courses overview
- Course addition

each of which has been implemented as a Scene object. The view that's displayed to the user at any given point depends on the actions taken by said user.

The UI is separate from the application logic and is situated in the [studytrackerapp.ui.StudyTrackerUi class](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/src/main/java/studytrackerapp/ui/StudyTrackerUi.java)

The user interacts with the UI, and any events are handled by the courseService and userService classes, which in turn invoke method calls from their corresponding Dao classes.

## Application logic

The application data model consists of two classes: User and Course. There exists a one-to-many relationship between the two:

[](documentation/images/package_structure.png)

## Data Persistence

### Database/Files

### Main Features

- User Login
- New User Creation
- Addition/Deletion of Courses

## Remaining Architectural Weaknesses

### UI

### DAO
