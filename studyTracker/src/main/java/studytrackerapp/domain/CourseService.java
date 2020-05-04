package studytrackerapp.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import studytrackerapp.dao.CourseDao;

/**
 * A class for creating and modifying Course objects through its corresponding
 * DAO.
 */
public class CourseService {

  private final CourseDao courseDao;

  public CourseService(final CourseDao courseDao) {
    this.courseDao = courseDao;
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
   * 
   * @return true if course was created, false otherwise
   */
  public boolean createCourse(final String name, final int credits, final int isCompulsory, final int status) {

    for (final Course course : getCourses()) {
      if (course.getName().equals(name)) {
        System.out.println("The course '" + name + "' has already been added.");
        return false;
      }
    }

    try {
      final Course newCourse = courseDao.create(new Course(name, credits, isCompulsory, status));

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
    return this.courseDao.list().stream().collect(Collectors.toList());
  }

  /**
   * 
   * @param course - name of the course to be deleted
   * @return true if successful, false if student studies no such course
   */

  public boolean deleteCourse(final String course) {

    final var existingCourseNames = getCourses().stream().map(c -> c.getName()).collect(Collectors.toList());
    if (!existingCourseNames.contains(course)) {
      return false;
    }

    try {
      courseDao.delete(course);
    } catch (final SQLException e) {
      e.printStackTrace();
    }

    return true;

  }

  /**
   * 
   * @param courseName - name of the course to be modified
   * @param status     - the status to which the course should be updated
   * @return course if update successful
   */
  public Course changeCourseStatus(final String courseName, final int status) throws SQLException {

    var course = courseDao.read(courseName);
    course.setStatus(status);
    return courseDao.update(course);

  }

  /**
   * 
   * @param courseName - name of the course to be modified
   * @param grade
   * @return course if update successful
   */
  public Course changeCourseGrade(String courseName, int grade) throws SQLException {

    var course = courseDao.read(courseName);
    if (course.getStatus() == 2) {
      course.setGrade(grade);
      return courseDao.update(course);
    }
    return course;

  }

}
