package studytrackerapp.domain;

import studytrackerapp.dao.CourseDao;
import studytrackerapp.dao.UserDao;

/**
 * A class for creating and modifying Course objects through its corresponding
 * DAO.
 */
public class CourseService {

  private CourseDao courseDao;
  private UserDao userDao;
  private User loggedIn;

  public CourseService(CourseDao courseDao, UserDao userDao) {
    // initialise DAO
    this.courseDao = courseDao;
    this.userDao = userDao;
  }

  /**
   * Adding a new course for a logged-in user input provided through the GUI
   *
   * @param name         - name of user
   * @param credits      - number of credits course is worth
   * @param isCompulsory - is the course compulsory
   * @param period       - the period the course takes place in
   * @param status       - current status of the course
   * @param courseLink   - url linking to the course
   * 
   */

  public boolean createCourse(String name, int credits, int isCompulsory, int period, String status,
      String courseLink) {
    Course course = new Course(name, credits, isCompulsory, period, status, courseLink, loggedIn);

    try {
      courseDao.create(course);
    } catch (Exception ex) {
      return false;
    }
    return true;
  }

}
