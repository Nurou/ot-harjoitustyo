package studytrackerapp.domain;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;

import studytrackerapp.dao.CourseDao;
import studytrackerapp.dao.Database;
import studytrackerapp.dao.UserDao;

public class CourseServiceTest {

  private CourseDao courseDao;
  private CourseService courseService;
  private Database database;

  /**
   * runs before each test creates a db adds a single course to it
   * 
   * @throws SQLException
   */

  @Before
  public void setUp() throws SQLException {

    database = new Database();
    database.createDatabase("test.db");

    courseDao = new CourseDao(database);
    courseService = new CourseService(courseDao);

    System.out.println("CourseService test set up successful.");
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
  public void courseCanBeCreated() {
    User user = new User("username", "test-user", "password", "tkt", 180);
    courseService.assignUser(user);
    assertNotNull(courseService.retrieveUser());

    assertTrue(courseService.createCourse("test-course", 3, 1, 2, "https://mooc.helsinki.fi/"));

  }

  @Test
  public void usersCoursesCanBeListed() {
    User user = new User("username", "test-user", "password", "tkt", 180);
    courseService.assignUser(user);

    courseService.createCourse("first course", 3, 1, 2, "https://mooc.helsinki.fi/");
    courseService.createCourse("second course", 3, 1, 2, "https://mooc.helsinki.fi/");

    assertEquals("first course", courseService.getCourses().get(0).getName());
    assertEquals("second course", courseService.getCourses().get(1).getName());
  }

  @Test
  public void noCoursesIfNoneAdded() {
    assertNull(courseService.getCourses());
  }

  @Test
  public void sameCourseCannotBeAddedTwiceForSameUser() {
    User user = new User("username", "test-user", "password", "tkt", 180);
    courseService.assignUser(user);

    assertTrue(courseService.createCourse("test-course", 3, 1, 2, "https://mooc.helsinki.fi/"));

    courseService.getCourses();

    assertFalse(courseService.createCourse("test-course", 3, 1, 2, "https://mooc.helsinki.fi/"));

  }

}