package studytrackerapp.domain;

import java.util.ArrayList;
import java.util.List;

import studytrackerapp.dao.CourseDao;

/**
 * A class for creating and modifying Course objects through its corresponding
 * DAO.
 */
public class CourseService {

  private CourseDao courseDao;
  private List<Course> courses;

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
   * Interacts with courseDao to add a course to the db User not included since
   * its stored in CourseDao
   * 
   * @return true if course was created, false otherwise
   */

  public boolean createCourse(String name, int credits, int isCompulsory, int period, String status,
      String courseLink) {
    try {
      Course newCourse = courseDao.create(new Course(name, credits, isCompulsory, period, status, courseLink));

      if (newCourse != null)
        return true;

    } catch (Exception ex) {
      return false;
    }
    return true;
  }

}
