package studytrackerapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;

/**
 * DAO mapping of Course object to Course database entity
 *
 */

public class CourseDao implements Dao<Course, String> {

  private Database database;
  private User user;

  public CourseDao(Database database) throws SQLException {
    this.database = database;
  }

  /**
   *
   * @param user - user that the course is attached to
   */

  public void setUser(User user) {
    this.user = user;
  }

  /**
   *
   * @return user that is to be assigned to the course
   */

  public User getUser() {
    return this.user;
  }

  /**
   * Adds a new course entity into the database
   *
   * @param course - the Course to be added to the database (holds all the
   *               necessary info)
   * @return created Course object
   */

  @Override
  public Course create(Course course) {
    // define query
    String sql = "INSERT INTO Course(name, credits, compulsory, status, course_link, grade, username) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      // set values
      statement.setString(1, course.getName());
      statement.setInt(2, course.getCredits());
      statement.setInt(3, course.getIsCompulsory());
      statement.setInt(4, course.getStatus());
      statement.setString(5, course.getCourseLink());
      statement.setInt(6, course.getGrade());
      statement.setString(7, this.user.getUsername());
      statement.executeUpdate();
    } catch (SQLException e) {
      // nothing returned if a course was not created
      return null;
    }

    return course;

  }

  /**
   * Returns a list of all the courses in the database for a given user
   * 
   * @return List of courses, an empty one if a user has yet to add courses
   */

  @Override
  public List<Course> list() {
    List<Course> courses = new ArrayList<>();

    String sql = "SELECT name, credits, compulsory, status, course_link, grade FROM Course WHERE username = ?";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      System.out.println(this.user.getUsername());
      statement.setString(1, this.user.getUsername());
      ResultSet resultSet = statement.executeQuery();

      // checking if there are any results
      if (!resultSet.isBeforeFirst()) {
        System.out.println("No course data");
        return List.of();
      }

      while (resultSet.next()) {
        courses.add(new Course(resultSet.getString("name"), resultSet.getInt("credits"), resultSet.getInt("compulsory"),
            resultSet.getInt("status"), resultSet.getString("course_link"), resultSet.getInt("grade"), this.user));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return List.of();
    }

    return courses;
  }

  /**
   * reads a single course from the db
   * 
   * @param courseName - name of the course being fetched
   * @return the course that was fetched from db
   */
  @Override
  public Course read(String courseName) throws SQLException {
    // object to hold the course, no course found by default
    Course found = null;

    // define query
    String sql = "SELECT * FROM Course WHERE name = ? AND username = ?";

    // attempt to form a connection
    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // inject query params
      statement.setString(1, courseName);
      statement.setString(2, this.user.getUsername());

      // execute query & store result
      ResultSet resultSet = statement.executeQuery();

      // create course
      found = new Course(resultSet.getString("name"), resultSet.getInt("credits"), resultSet.getInt("compulsory"),
          resultSet.getInt("status"), resultSet.getString("course_link"), resultSet.getInt("grade"), this.user);

    } catch (SQLException e) {
      System.out.println();
      System.err.println("Error in UserDao - unable to retrieve user \n" + e.getMessage());
      return found;
    }

    System.out.println(found.getName() + " retrieved successfully");
    return found;
  }

  /**
   * updates a course with the properties in the object passed as an argument
   * 
   * @param course - the course to be updated in the db
   * @return the course if update successful, null otherwise
   */
  @Override
  public Course update(Course course) throws SQLException {
    String sql = "UPDATE Course SET status = ?, grade = ? WHERE username = ? AND name = ?";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, course.getStatus());
      statement.setInt(2, course.getGrade());
      statement.setString(3, this.user.getUsername());
      statement.setString(4, course.getName());
      statement.executeUpdate();
      return course;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public boolean delete(String courseName) throws SQLException {
    String sql = "DELETE FROM Course WHERE name = ? AND username = ?";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // set value
      statement.setString(1, courseName);
      statement.setString(2, this.user.getUsername());
      statement.executeUpdate();

    } catch (SQLException e) {
      System.err.println(e.getMessage());
      return false;
    }

    return true;
  }

}
