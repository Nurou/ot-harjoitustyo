package studytrackerapp.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import studytrackerapp.dao.CourseDao;

/**
 * A class for creating and modifying Course objects through its corresponding
 * DAO.
 */
public class CourseService {

  private final CourseDao courseDao;
  private List<Course> courses;

  public CourseService(final CourseDao courseDao) {
    // initialise DAO
    this.courseDao = courseDao;
    this.courses = new ArrayList<>();
  }

  /**
   * Assigns a user to the given course through the courseDao
   * 
   * @param user
   */

  public void assignUser(final User user) {
    courseDao.setUser(user);
  }

  /**
   * 
   * @return the user whose courses are being queried/manipulated
   */
  public User retrieveUser() {
    return courseDao.getUser();
  }

  /**
   * Interacts with courseDao to add a course to the db User not included since
   * its stored in CourseDao
   * 
   * @return true if course was created, false otherwise
   */

  public boolean createCourse(final String name, final int credits, final int isCompulsory, final int status,
      final String courseLink) {

    for (final Course course : courses) {
      if (course.getName().equals(name)) {
        System.out.println("The course '" + name + "' has already been added.");
        return false;
      }
    }

    try {
      final Course newCourse = courseDao.create(new Course(name, credits, isCompulsory, status, courseLink));

      if (newCourse != null) {
        System.out.println("Course '" + name + "' added");
        return true;
      }

    } catch (final Exception e) {
      System.err.println("Error in adding course: " + e.getMessage());
      return false;
    }

    return false;
  }

  /**
   * fetches all courses for a user through the Dao
   * 
   * @return
   */

  public List<Course> getCourses() {
    final List<Course> courses = this.courseDao.list().stream().collect(Collectors.toList());

    setCourses(courses);

    return this.courses;
  }

  /**
   * 
   * @param the name of the course to be deleted
   * @return true if successful, false if student studies no such course
   */

  public boolean deleteCourse(final String course) {

    final var currentCourses = getCourses().stream().map(c -> c.getName()).collect(Collectors.toList());

    if (!currentCourses.contains(course)) {
      return false;
    }

    try {
      courseDao.delete(course);
    } catch (final SQLException e) {
      e.printStackTrace();
    }

    return true;

  }

  public Course changeCourseStatus(final String courseName, final int status) throws SQLException {
    // find course

    var course = courseDao.read(courseName);
    course.setStatus(status);
    return courseDao.update(course);
  }

  public void setCourses(final List<Course> courses) {
    this.courses = courses;
  }
}
