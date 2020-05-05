package studytrackerapp.domain;

import java.sql.SQLException;

import studytrackerapp.dao.UserDao;

/**
 * A class for creating and modifying User objects through its corresponding
 * DAO.
 */
public class UserService {

  private final UserDao userDao;
  private User currentlyLoggedIn;

  public UserService(final UserDao userDao) {
    this.userDao = userDao;
  }

  /**
   * Checks if user exists and sets user as logged in.
   *
   * @param username - username input from the user
   * @param password - password input from the user
   * @return true if login successful, false if unsuccessful
   * @throws SQLException
   */

  public boolean login(final String username, final String password) throws SQLException {
    // attempt to fetch user
    final User user = userDao.read(username);

    if (user == null) {
      return false;
    }

    if (!(password.equals(user.getPassword()))) {
      return false;
    }

    // otherwise,
    System.out.println(user.getUsername() + " has been read successfully from the db");
    System.out.println("User details: " + user);
    // set them as logged in
    this.currentlyLoggedIn = user;
    System.out.println();
    System.out.println(username + " has logged in.");

    return true;
  }

  /**
   * @return logged in user
   */
  public User getLoggedUser() {
    if (this.currentlyLoggedIn == null) {
      return null;
    }
    return this.currentlyLoggedIn;
  }

  /**
   * Logs user out
   * 
   * @return true if successful, false otherwise
   */
  public boolean logout() {

    if (currentlyLoggedIn == null) {
      System.out.println("No user has logged in.");
      return false;
    }

    System.out.println(currentlyLoggedIn.getUsername() + " has logged out.");
    currentlyLoggedIn = null;
    return true;
  }

  /**
   * Creates a new User object and sends it to be stored it in the database
   * parameter are provided through GUI user input
   * 
   * @param name          - name of the new user
   * @param username      - username of the new user
   * @param password      - password of the new user
   * @param programName   - programName of the new user's study program
   * @param targetCredits - targetCredits for the new user
   * @return true if no issues in creating user
   * @throws SQLException
   */
  public boolean createUser(final String username, final String name, final String password, final String programName,
      final int targetCredits) throws SQLException {
    // see if there exists a user with the same username
    if (userDao.read(username) != null) {
      System.out.println("Username taken.");
      return false;
    }

    final User user = userDao.create(new User(name, username, password, programName, targetCredits));

    if (user != null) {
      System.out.println("The user: " + username + " was created!");
      return true;
    }

    return false;
  }

}
