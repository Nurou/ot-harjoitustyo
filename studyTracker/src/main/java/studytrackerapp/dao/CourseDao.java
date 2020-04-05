package studytrackerapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    createTable();
  }

  /**
   * Sets current user
   * 
   * @param user (owner of Todo objects to be queried)
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Adds a new course into the database each course belongs to a specific user
   * 
   * @param course - the Course to be added to the database (holds all the
   *               necessary info)
   * @return created Course object
   */
  @Override
  public Course create(Course course) {
    // define query
    String sql = "INSERT INTO " + "Course(name, credits, compulsory, period, status, course_link, username ) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      // set values
      statement.setString(1, course.getName());
      statement.setInt(2, course.getCredits());
      statement.setInt(3, course.getIsCompulsory());
      statement.setInt(4, course.getPeriod());
      statement.setString(5, course.getStatus());
      statement.setString(6, course.getCourseLink());
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
   * @return List<Course>
   */
  @Override
  public List<Course> list() {

    List<Course> courses = new ArrayList<>();

    String sql = "SELECT * FROM Course WHERE username = ?";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, this.user.getUsername());

      // get query results
      ResultSet resultSet = statement.executeQuery();

      // extract query results & add to the course list
      while (resultSet.next()) {
        System.out.println(resultSet.next());
        courses.add(new Course(resultSet.getString("name"), resultSet.getInt("credits"),
            resultSet.getInt("isCompulsory"), resultSet.getInt("period"), resultSet.getString("status"),
            resultSet.getString("course_link"), this.user));
      }

    } catch (Exception e) {
      System.err.println(e.getMessage());
    }

    // return courses
    return null;
  }

  @Override
  public Course read(String key) throws SQLException {
    // TODO Auto-generated method stub
    return null;
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

  /**
   * Creates a table for the object type in the db if one does not already exist
   * 
   * @throws SQLException
   */

  @Override
  public void createTable() throws SQLException {

    String sql = "CREATE TABLE IF NOT EXISTS Course (\n" + "id INTEGER PRIMARY KEY,\n" + "name TEXT,\n"
        + "credits INTEGER NOT NULL,\n" + "compulsory INTEGER NOT NULL,\n" + "period INTEGER NOT NULL,\n"
        + "status INTEGER NOT NULL,\n" + "course_link TEXT,\n" + "FOREIGN KEY (username) REFERENCES User (username)\n"
        + ");";

    try (Connection connection = database.getConnection(); Statement statement = connection.createStatement()) {
      statement.execute(sql);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

}