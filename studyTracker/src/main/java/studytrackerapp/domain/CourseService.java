package studytrackerapp.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import studytrackerapp.dao.CourseDao;

// TODO: refactor this class - courses list

/**
 * A class for creating and modifying Course objects through its corresponding
 * DAO.
 */
public class CourseService {

  private final CourseDao courseDao;
  private List<Course> courses;

  public CourseService(final CourseDao courseDao) {
    this.courseDao = courseDao;
    this.courses = new ArrayList<>();
  }

  /**
   * 
   * @param user - the user to be assigned as the owner of the courses
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
   * 
   * @param name         - name of the course
   * @param credits      - the number of credits the course is worth
   * @param isCompulsory - whether the course is compulsory or not
   * @param status       - completion status
   * @param courseLink   - a link to the course
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
   * @return list of courses
   */

  public List<Course> getCourses() {
    final List<Course> courses = this.courseDao.list().stream().collect(Collectors.toList());

    setCourses(courses);

    return this.courses;
  }

  /**
   * 
   * @param course - name of the course to be deleted
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
