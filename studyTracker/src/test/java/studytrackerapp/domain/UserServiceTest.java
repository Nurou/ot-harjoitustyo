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
  public void canCreateNewUserWithUniqueUsername() {
    assertTrue(userService.createNewUser("unique", "unique-username-guy", "password"));
    assertFalse(userService.createNewUser("unique", "non-unique-username-guy", "password"));
  }

  @Test
  public void cannotCreateNewUserWithNonUniqueUsername() {
  }

  // @Test
  // public void canAddExperience() {
  // userService.login("tester");
  // userService.addExp(1);
  // assertEquals(25, userService.getLoggedUser().getExperience());

  // userService.addExp(2);
  // assertEquals(75, userService.getLoggedUser().getExperience());

  // userService.addExp(3);
  // assertEquals(175, userService.getLoggedUser().getExperience());
  // }

  // @Test
  // public void canRemoveExperience() {
  // userService.login("tester");
  // userService.addExp(3);
  // userService.removeExp(75);
  // assertEquals(25, userService.getLoggedUser().getExperience());
  // }

  // @Test
  // public void levelIncreaseWithEnoughExperience() {
  // userService.login("tester");
  // userService.getLoggedUser().setExperience(1950);
  // userService.addExp(3);
  // assertEquals(50, userService.getLoggedUser().getExperience());
  // assertEquals(2, userService.getLoggedUser().getLevel());
  // assertEquals(400, userService.getLoggedUser().getHealth());
  // }

  // @Test
  // public void levelDecreaseByPenalty() {
  // userService.login("tester");
  // userService.getLoggedUser().setExperience(0);
  // userService.getLoggedUser().setLevel(2);
  // userService.getLoggedUser().setHealth(400);
  // userService.experiencePenalty();
  // assertEquals(0, userService.getLoggedUser().getExperience());
  // assertEquals(1, userService.getLoggedUser().getLevel());
  // assertEquals(200, userService.getLoggedUser().getHealth());
  // }

  // @Test
  // public void levelNotGetLowerThanOne() {
  // userService.login("tester");
  // userService.getLoggedUser().setExperience(50);
  // userService.getLoggedUser().setLevel(1);
  // userService.getLoggedUser().setHealth(200);
  // userService.experiencePenalty();
  // assertEquals(0, userService.getLoggedUser().getExperience());
  // assertEquals(1, userService.getLoggedUser().getLevel());
  // assertEquals(200, userService.getLoggedUser().getHealth());
  // }

  // @Test
  // public void invalidDifficultyWillNotChangeLevelOrExp() {
  // userService.login("tester");
  // userService.getLoggedUser().setHealth(200);
  // userService.addExp(0);
  // assertEquals(0, userService.getLoggedUser().getExperience());
  // assertEquals(1, userService.getLoggedUser().getLevel());
  // assertEquals(200, userService.getLoggedUser().getHealth());
  // }

}