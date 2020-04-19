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
 * Interface between Course objects and db courses
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
    String sql = "INSERT INTO Course(name, credits, compulsory, status, course_link, username) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      // set values
      statement.setString(1, course.getName());
      statement.setInt(2, course.getCredits());
      statement.setInt(3, course.getIsCompulsory());
      statement.setInt(4, course.getStatus());
      statement.setString(5, course.getCourseLink());
      statement.setString(6, this.user.getUsername());
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
   * @return List<Course>
   */

  @Override
  public List<Course> list() {
    List<Course> courses = new ArrayList<>();

    String sql = "SELECT name, credits, compulsory, status, course_link FROM Course WHERE username = ?";

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
            resultSet.getInt("status"), resultSet.getString("course_link"), this.user));
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return List.of();
    }

    return courses;
  }

  @Override
  public Course read(String courseName) throws SQLException {
    // object to hold the course, no course found by default
    Course found = null;

    // define query
    String sql = "SELECT * FROM Course WHERE name = ?";

    // attempt to form a connection
    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // inject query params
      statement.setString(1, courseName);

      // execute query & store result
      ResultSet resultSet = statement.executeQuery();

      // create course
      found = new Course(resultSet.getString("name"), resultSet.getInt("credits"), resultSet.getInt("compulsory"),
          resultSet.getInt("status"), resultSet.getString("course_link"));

    } catch (SQLException e) {
      System.out.println();
      System.err.println("Error in UserDao - unable to retrieve user \n" + e.getMessage());
      return found;
    }

    System.out.println(found.getName() + " retrieved successfully");
    return found;
  }

  @Override
  public Course update(Course object) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(String key) throws SQLException {
    // TODO Auto-generated method stub

  }

}
