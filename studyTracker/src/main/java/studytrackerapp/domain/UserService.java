package studytrackerapp.domain;

import studytrackerapp.dao.UserDao;

/**
 * This UserService class provides methods to the UI and triggers database
 * actions by interfacing with UserDao.
 */
public class UserService {

  private UserDao userDao;
  private User currentlyLoggedIn;

  public UserService(UserDao userDao) {
    // initialise DAO
    this.userDao = userDao;
  }

  /**
   * Checks if user exists and sets user as logged in.
   *
   * @param username (user input)
   * @return true if login successful, false if unsuccessful
   */

  public boolean login(String username, String password) {
    // attempt to fetch user
    User user = userDao.read(username);

    System.out.println(user.getUsername() + " has been read successfully from the db.\n");
    System.out.println("User details: " + user);

    if (user == null || !password.equals(user.getPassword())) {
      System.out.println("Db pass: " + user.getPassword());
      System.out.println("Param pass: " + password);
      System.out.println("Can't login!");
      return false;
    }

    // otherwise,
    this.currentlyLoggedIn = user;
    System.out.println();
    System.out.println(username + " has logged in.");
    return true;
  }

  /**
   * @return logged in user
   */
  public User getLoggedUser() {
    return this.currentlyLoggedIn;
  }

  /**
   * Logs user out
   *
   */
  public boolean logout() {

    if (currentlyLoggedIn == null) {
      System.out.println("No user has logged in.");
      return false;
    }

    System.out.println(currentlyLoggedIn.getUsername() + " is logging out...");
    currentlyLoggedIn = null;
    return true;
  }

  /**
   * Creates a new User object and sends it to be stored it in the database
   * parameter are provided through GUI user input
   * 
   * @param name
   * @param username
   * @param password
   * @return true if no issues in creating user
   */
  public boolean createUser(String username, String name, String password) {
    // see if there exists a user with the same username
    if (userDao.read(username) != null) {
      System.out.println("Username taken.");
      return false;
    }
    userDao.create(new User(name, username, password));
    System.out.println("The user: " + username + " was created!");
    return true;
  }

}
