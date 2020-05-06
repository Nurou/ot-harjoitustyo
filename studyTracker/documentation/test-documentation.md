# Testing Documentation

The application has been tested with unit and integration tests with the JUnit library, and the system manually.

## Unit & Integration Testing

### Application Logic

The bulk of the tests are concerned with the application logic - the functionality in the domain package classes. The following classes tests integration:

- [CourseServiceTest](https://github.com/Nurou/studyTracker/blob/master/studyTracker/src/test/java/studytrackerapp/domain/CourseServiceTest.java)
- [UserServiceTest](https://github.com/Nurou/studyTracker/blob/master/studyTracker/src/test/java/studytrackerapp/domain/UserServiceTest.java)

These integration tests utilise the corresponding DAO interfaces.

### DAO Classes

The DAO classes have been tested through the integration tests. The database class has been tested separately: [DatabaseTest](https://github.com/Nurou/studyTracker/blob/master/studyTracker/src/test/java/studytrackerapp/dao/DatabaseTest.java)

### Test Coverage

Excluding the user interface the application's test coverage is 86% for lines and 75% for branches.
![](https://github.com/Nurou/studyTracker/blob/master/studyTracker/documentation/images/test-coverage.jpg)

## System testing

E2E testing has been performed manually.

### Installation

The application has been installed on macOS and Linux as per [instructions](https://github.com/Nurou/studyTracker/blob/master/studyTracker/documentation/user-guide.md). No configuration is required on the part of the user.

### Functionality

The functionality listed in the [User Guide](https://github.com/Nurou/studyTracker/blob/master/studyTracker/documentation/test-documentation.md) and [Requirements Specification](https://github.com/Nurou/studyTracker/blob/master/studyTracker/documentation/requirements-specifcication.md) has been tested in various, e.g. through invalid inputs and unexpected user behaviour.

## Unresolved Quality Issues

The application lacks in the following areas:

- proper error reporting to the user of database errors
- the release jar does not work on some macOS environments
