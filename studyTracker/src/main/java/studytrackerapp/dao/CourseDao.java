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

  private final Database database;
  private User user;

  public CourseDao(final Database database) throws SQLException {
    this.database = database;
  }

  /**
   *
   * @param user - user that the course is attached to
   */

  public void setUser(final User user) {
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
  public Course create(final Course course) throws SQLException {
    // define query
    final String sql = "INSERT INTO Course(name, credits, compulsory, status, grade, username) VALUES (?, ?, ?, ?, ?, ?)";

    final Connection connection = database.getConnection();
    final PreparedStatement statement = connection.prepareStatement(sql);

    // set values
    statement.setString(1, course.getName());
    statement.setInt(2, course.getCredits());
    statement.setInt(3, course.getIsCompulsory());
    statement.setInt(4, course.getStatus());
    statement.setInt(5, course.getGrade());
    statement.setString(6, this.user.getUsername());
    statement.executeUpdate();

    return course;

  }

  /**
   * Returns a list of all the courses in the database for a given user
   * 
   * @return List of courses, an empty one if a user has yet to add courses
   */

  @Override
  public List<Course> list() throws SQLException {

    final List<Course> courses = new ArrayList<>();

    final String sql = "SELECT name, credits, compulsory, status, grade FROM Course WHERE username = ?";

    final Connection connection = database.getConnection();
    final PreparedStatement statement = connection.prepareStatement(sql);
    System.out.println(this.user.getUsername());
    statement.setString(1, this.user.getUsername());
    final ResultSet resultSet = statement.executeQuery();

    // checking if there are any results
    if (!resultSet.isBeforeFirst()) {
      System.out.println("No course data");
      return List.of();
    }

    while (resultSet.next()) {
      courses.add(new Course(resultSet.getString("name"), resultSet.getInt("credits"), resultSet.getInt("compulsory"),
          resultSet.getInt("status"), resultSet.getInt("grade"), this.user));
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
  public Course read(final String courseName) throws SQLException {
    // object to hold the course, no course found by default
    Course found = null;

    // define query
    final String sql = "SELECT * FROM Course WHERE name = ? AND username = ?";

    // attempt to form a connection
    final Connection connection = database.getConnection();
    final PreparedStatement statement = connection.prepareStatement(sql);

    // inject query params
    statement.setString(1, courseName);
    statement.setString(2, this.user.getUsername());

    // execute query & store result
    final ResultSet resultSet = statement.executeQuery();

    if (!resultSet.isBeforeFirst())
      return found;

    // create course
    found = new Course(resultSet.getString("name"), resultSet.getInt("credits"), resultSet.getInt("compulsory"),
        resultSet.getInt("status"), resultSet.getInt("grade"), this.user);

    return found;
  }

  /**
   * updates a course with the properties in the object passed as an argument
   * 
   * @param course - the course to be updated in the db
   * @return the course if update successful, null otherwise
   */
  @Override
  public Course update(final Course course) throws SQLException {
    final String sql = "UPDATE Course SET status = ?, grade = ? WHERE username = ? AND name = ?";

    final Connection connection = database.getConnection();
    final PreparedStatement statement = connection.prepareStatement(sql);

    statement.setInt(1, course.getStatus());
    statement.setInt(2, course.getGrade());
    statement.setString(3, this.user.getUsername());
    statement.setString(4, course.getName());
    statement.executeUpdate();

    return course;

  }

  @Override
  public boolean delete(final String courseName) throws SQLException {
    final String sql = "DELETE FROM Course WHERE name = ? AND username = ?";

    final Connection connection = database.getConnection();
    final PreparedStatement statement = connection.prepareStatement(sql);

    statement.setString(1, courseName);
    statement.setString(2, this.user.getUsername());
    statement.executeUpdate();

    return true;
  }

}
