# Architecture Overview

## Structure

The program structure follows a layered architecture with three tiers. The package hierarchy is as follows:

![Application Package Structure](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/documentation/images/package_structure.png)

The studytracker.ui package contains the application's user interface implemented in JavaFX, studytracker.domain the application logic, and studytracker.dao the functionality used to interact with the database.

## User Interface

The user interface comprises of four separate views:

- Login (initial)
- Account creation
- Main tracking view
- Course addition

each of which has been implemented as a Scene object. The view that's displayed to the user at any given point depends on the actions taken by said user.

The UI is separate from the application logic and is situated in the [studytrackerapp.ui.StudyTrackerUi class](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/src/main/java/studytrackerapp/ui/StudyTrackerUi.java)

The user interacts with the UI, and any events are handled by the courseService and userService classes, which in turn invoke method calls from their corresponding DAO classes.

## Application logic

The application data model consists of two classes: User and Course. There exists a one-to-many relationship between the two:

![Course-User](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/documentation/images/course_user.jpg)

All functionality is provided through two classes: courseService & userService. They handle all actions performed by the user, such as login, or adding a course. They do this by invoking the corresponding methods in the DAO classes, which are injected into the service classes as dependencies.

Here's a high-level representation of the application's logic.

![Class Diagram](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/documentation/images/class_diagram.png)

## Data Persistence

The application is embedded with a SQLite RDBMS, which maintains a database that's used to keep records of users and their courses. The data is stored in a file that comes with the jar that the application is packaged in, and that the user downloads.

The application makes use of the Data Access Object (DAO) or data mapper design pattern, where a set of DAO classes handle all the interaction with the database. A generic [DAO interface](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/src/main/java/studytrackerapp/dao/DAO.java) offers standard CRUD operations, and is implemented by both the userDao and courseDao classes.

These DAO classes abstract away all the database operations. This means that they are not exposed to, nor repeated throughout the rest of the application.

### Database Files

The application stores user and course data into a single database file called tracker.db. Each entity has a table of its own. A course references a user instance through a foreign key.

### Database Schema

Each DAO represents a table in the database. The statements used to create the tables can be viewed [here](https://github.com/Nurou/ot-harjoitustyo/blob/master/studyTracker/documentation/misc/create-table-statements).


## Main Features

The following sequence diagrams demonstrate the main elements of the application's logic. The general flow involves an event being triggered in the user interface causing a service-class method to be invoked, which then interacts with a DAO class as necessary. Any data fetched from the database through the DAO then flows back in the reverse direction. 

### User Login

When the button for logging in is clicked, the following sequence occurs:

![](https://github.com/Nurou/studyTracker/blob/master/studyTracker/documentation/images/login-sequence.png).


### New User Creation

When the button for creating a new user is clicked, the following sequence occurs:

![](https://github.com/Nurou/studyTracker/blob/master/studyTracker/documentation/images/create-new-user-sequence.png).


### Adding Courses

New courses can be added by clicking the button for adding courses on the main view, which opens up a dedicated view for the process. The following sequence occurs when course details are provided and the _add course_ button is clicked: 

![](https://github.com/Nurou/studyTracker/blob/master/studyTracker/documentation/images/add-course-sequence.png).

A similar sequence of logic occurs for the rest of the functionality of the application, e.g. when courses are added or removed.

## Remaining Structural Weaknesses

### UI
The UI implementation is implemented in its entirety in the StudyTrackerUi class. A cleaner and more maintanable soltuion would be to refactor the application to use FXML, or at least seperate the current class into smaller ones.

Inline CSS has been mixed with JavaFX styling, which is an inconsistent approach. More optimally, there would be separate stylesheets that take care of all the layout needs of the application. 

The UI has also not been tested, and is skipped in checkstyle tests.

### DAO

Testing of the DAO classes could have been automated. Also, error handling could and should have been passed all the way to the UI.

### Tests

The tests could perhaps have been more extensive and covered more unexpected events. More importantly, the tests could have been automated.
