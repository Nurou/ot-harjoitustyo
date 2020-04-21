package studytrackerapp.domain;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;

import studytrackerapp.dao.Database;
import studytrackerapp.dao.UserDao;

public class UserServiceTest {

  private UserService userService;
  private UserDao userDao;
  private Database database;

  public UserServiceTest() {

  }

  /**
   * runs before each test creates a db adds a single test user to it
   * 
   * @throws SQLException
   */

  @Before
  public void setUp() throws SQLException {

    database = new Database();
    database.createDatabase("test.db");

    userDao = new UserDao(database);
    userService = new UserService(userDao);

    User user = new User("tester", "username", "testpass");
    userDao.create(user);

    System.out.println("Set up successful");
  }

  /* runs after each test method */
  @After
  public void tearDown() {
    // wrap db resource in a File object
    File file = new File("test.db");
    // db can now be removed
    file.delete();
  }

  @Test
  public void noLoggedInUserBeforeLogin() {
    assertEquals(null, userService.getLoggedUser());
  }

  @Test
  public void userCanLogInWithValidCredentials() {
    userService.login("username", "testpass");
    assertEquals("username", userService.getLoggedUser().getUsername());
    assertEquals("tester", userService.getLoggedUser().getName());
  }

  @Test
  public void userCannotLogInWithInvalidCredentials() {
    assertFalse(userService.login("username", "wrong password"));
  }

  @Test
  public void userCanBeLoggedOut() {
    userService.login("username", "testpass");
    assertTrue(userService.logout());
    assertNull(userService.getLoggedUser());
  }

  @Test
  public void cannotLogOutAUserNotLoggedIn() {
    assertFalse(userService.logout());
  }

  @Test
  public void loggedUsersNameCanBeFetched() {
    var username = "unique";
    var name = "unique-username-guy";
    var password = "password";
    userService.createUser(username, name, password, "tkt", 180);
    userService.login(username, password);
    assertEquals("unique-username-guy", userService.getLoggedUser().getName());
  }

  @Test
  public void canCreateNewUserWithUniqueUsername() {
    assertTrue(userService.createUser("unique", "unique-username-guy", "password", "tkt", 180));
    assertFalse(userService.createUser("unique", "non-unique-username-guy", "password", "tkt", 180));
  }

}