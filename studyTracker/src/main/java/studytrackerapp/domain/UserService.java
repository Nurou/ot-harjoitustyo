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

  public boolean login(String username) {
    // attempt to fetch user
    User user = userDao.getOne(username);

    // no such user?
    if (user == null) {
      return false;
    }

    // otherwise,
    this.currentlyLoggedIn = user;
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
  public void logout() {
    currentlyLoggedIn = null;
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
  public boolean newUser(String name, String username, String password) {
    if (userDao.getOne(username) != null) {
      return false;
    }
    userDao.create(new User(name, username, password));
    return true;
  }

}
