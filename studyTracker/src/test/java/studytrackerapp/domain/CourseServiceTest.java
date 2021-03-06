package studytrackerapp.domain;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;

import studytrackerapp.dao.CourseDao;
import studytrackerapp.dao.Database;

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

    User user = new User("username", "test-user", "password", "tkt", 180);
    courseService.assignUser(user);

    System.out.println("CourseService test set up successful.");
  }

  @After
  public void tearDown() {
    // wrap db resource in a File object
    File file = new File("test.db");
    // db can now be removed
    file.delete();
  }

  @Test
  public void courseCanBeCreated() throws SQLException {
    assertNotNull(courseService.retrieveUser());
    assertTrue(courseService.createCourse("test-course", 3, 1, 2));
  }

  @Test
  public void courseAddedCanBeDeleted() throws SQLException {
    courseService.createCourse("test-course", 3, 1, 2);
    assertTrue(courseService.deleteCourse("test-course"));
  }

  @Test
  public void nonExistentCourseCannotBeDeleted() throws SQLException {
    courseService.retrieveUser();
    assertFalse(courseService.deleteCourse("test-course"));
  }

  @Test
  public void usersCoursesCanBeListed() throws SQLException {
    courseService.createCourse("first course", 3, 1, 2);
    courseService.createCourse("second course", 3, 1, 2);
    assertEquals("first course", courseService.getCourses().get(0).getName());
    assertEquals("second course", courseService.getCourses().get(1).getName());
  }

  @Test
  public void noCoursesIfNoneAdded() throws SQLException {
    assertTrue(courseService.getCourses().isEmpty());
  }

  @Test
  public void sameCourseCannotBeAddedTwiceForSameUser() throws SQLException {
    assertTrue(courseService.createCourse("test-course", 3, 1, 2));
    courseService.getCourses();
    assertFalse(courseService.createCourse("test-course", 3, 1, 2));
  }

  @Test
  public void courseStatusCanBeModified() throws SQLException {
    courseService.createCourse("test-course", 5, 1, 1);
    courseService.changeCourseStatus("test-course", 2);

    assertEquals(2, courseDao.read("test-course").getStatus());

  }

  @Test
  public void canAddAGradeToCourse() throws SQLException {
    courseService.createCourse("test-course", 5, 1, 2);
    courseService.changeCourseGrade("test-course", 5);
    assertEquals(5, courseDao.read("test-course").getGrade());
  }

  @Test
  public void gradeCannotBeAddedToUncompletedCourses() throws SQLException {
    courseService.createCourse("test-course", 5, 1, 1);
    courseService.changeCourseGrade("test-course", 5);
    assertEquals(0, courseDao.read("test-course").getGrade());
  }

}