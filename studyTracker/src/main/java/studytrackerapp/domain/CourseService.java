package studytrackerapp.domain;

import studytrackerapp.dao.CourseDao;
import studytrackerapp.dao.UserDao;

/**
 * A class for creating and modifying Course objects through its corresponding DAO.
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
   * Adding a new course for a logged-in user
   * input provided through the GUI
   *
   * @param name
   * @param credits
   * @param period
   * @param isCompulsory
   * 
   */

  public boolean createCourse(String name, int credits, int period, boolean isCompulsory, String courseLink) {
    Course course = new Course(name, credits, period, isCompulsory, courseLink, loggedIn);
    try {
      courseDao.create(course);
    } catch (Exception ex) {
      return false;
    }
    return true;
  }

}
