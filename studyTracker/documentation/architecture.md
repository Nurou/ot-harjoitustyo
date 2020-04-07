# Architecture Overview

## Structure

The program structure follows a layered architecture with three tiers. The package hierarchy is as follows:

![Application Package Structure](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/documentation/images/package_structure.png)

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

## Data Persistence

The application is embedded with a SQLite RDBMS, which maintains a database that's used to keep records of users and their courses. The data is stored in a file that comes with the jar that the application is packaged in, and that the user downloads.

The application makes use of the Data Access Object (DAO) or data mapper design pattern, where a set of DAO classes handle all the interaction with the database. A generic [Dao interface](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/src/main/java/studytrackerapp/dao/Dao.java) offers methods create, read, update, delete (CRUD) operations, and is implemented by both the userDao and courseDao classes.

This way, all of the database operations are abstracted into these DAO classes, and do not have to be exposed and/or repeated throughout the program.

### Database/Files

The application stores user and course data into a single database file. Each entity has a table of its own. A course references a user instance through a foreign key.

### Main Features

- User Login
- New User Creation
- Addition/Deletion of Courses

## Remaining Structural Weaknesses

### UI

### DAO
