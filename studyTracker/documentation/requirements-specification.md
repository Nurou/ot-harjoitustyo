# Requirements Specification

## Purpose

This application helps students manage progress in a study program consisting of multiple courses. Since it comes with a built-in login system, the application can be used by multiple users, each having control of their own study progress.
A student may login, see what courses

## Users

The application comes with a single user role - normal user. A user with equipped with more rights may be added later on.

## User Interface Draft

The initial design of the application has 5 views in total.

<img src="https://raw.githubusercontent.com/nurou/ot-harjoitustyo/master/studyTracker/documentaatio/studytracker-uidraft.png" width="750">

The image shows the connections between the links.

## Functionality Offered By The Basic Version

### Pre-login

- The user's able to create a user account on the system

  - The username must be unique and at least 3 characters long
  - The password must be unique and at least 3 characters long

- The user's able to login to the system
  - Login is done by entering username and password on the login form
  - If the given user does not exist the user is notified

### Post-login

- The user see's an overview of their own profile with an option to navigate to _manage courses_

- The user can add a new course

  - the user fill's out information with regard to the course to be added
  - courses added by a user are only visible to that user

- courses added by the user are automatically placed in the appropriate column - _ongoing_, _done_ and _backlog_

- The _manage courses_ view also has a link for the user to go browse the course catalogue

- The user is able to logout of the system

## Further Development Ideas

The application could be improved by expanding it to have the ability to:

- add notes to courses
- add links to courses that take the student to the course page online
- keep removed courses in a trash bin
- delete users
- categorise courses by faculty (e.g. math, computer science)
- categorise courses by priority (using colours, for instance)
- share the progress by generating a pdf, web link, etc.
