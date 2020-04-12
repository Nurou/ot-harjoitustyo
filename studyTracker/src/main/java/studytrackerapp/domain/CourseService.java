package studytrackerapp.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import studytrackerapp.dao.CourseDao;

/**
 * A class for creating and modifying Course objects through its corresponding
 * DAO.
 */
public class CourseService {

  private CourseDao courseDao;
  private List<Course> courses;
  private int courseCount;

  public CourseService(CourseDao courseDao) {
    // initialise DAO
    this.courseDao = courseDao;
    this.courses = new ArrayList<>();
  }

  /**
   * Assigns a user to the given course through the courseDao
   * 
   * @param user
   */

  public void assignUser(User user) {
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

  public boolean createCourse(String name, int credits, int isCompulsory, int status, String courseLink) {

    for (Course course : courses) {
      if (course.getName().equals(name)) {
        System.out.println("The course '" + name + "' has already been added.");
        return false;
      }
    }

    try {
      Course newCourse = courseDao.create(new Course(name, credits, isCompulsory, status, courseLink));

      if (newCourse != null) {
        System.out.println("Course '" + name + "' added");
        courseCount++;
        return true;
      }

    } catch (Exception e) {
      System.err.println("Error in adding course - " + e.getMessage());
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
    if (courseCount == 0) {
      return null;
    }
    setCourses(this.courseDao.list().stream().collect(Collectors.toList()));
    return this.courses;
  }

  public void setCourses(List<Course> courses) {
    this.courses = courses;
  }
}
