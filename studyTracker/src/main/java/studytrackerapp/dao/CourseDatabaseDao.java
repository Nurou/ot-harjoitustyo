package studytrackerapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;

/**
 * Interface between Course objects and db courses
 * 
 */
public class CourseDatabaseDao implements CourseDao {

  private Database database;
  private User user;

  public CourseDatabaseDao(Database database) {
    this.database = database;
    createTable();
  }

  /**
   * Sets current user
   * 
   * @param user (owner of Todo objects to be queried)
   */
  @Override
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Adds a new course into the database
   * 
   * @param course - the Course to be added to the database (holds all the
   *               necessary info)
   * @return created Course object
   */
  @Override
  public Course create(Course course) throws Exception {
    // define query
    String sql = "INSERT INTO " + "Course(name, credits, compulsory, period, status, courseLink ) "
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
      statement.executeUpdate();
    } catch (SQLException e) {
      // nothing returned if creation unsuccessful
      return null;
    }

    return course;

  }

  @Override
  public List<Course> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Course getOne() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void modify(int id) throws Exception {
    // TODO Auto-generated method stub

  }

  private void createTable() {

    String sql = "CREATE TABLE IF NOT EXISTS Course (\n" + "id INTEGER PRIMARY KEY,\n" + "name TEXT,\n"
        + "credits TEXT NOT NULL,\n" + "compulsory INTEGER NOT NULL,\n" + "period INTEGER NOT NULL,\n"
        + "status INTEGER NOT NULL,\n" + "courseLink TEXT,\n" + "FOREIGN KEY (username) REFERENCES User (username)\n"
        + ");";

    try (Connection connection = database.getConnection(); Statement statement = connection.createStatement()) {
      statement.execute(sql);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public boolean delete(Integer key) throws Exception {
    return false;
  }

  @Override
  public boolean changeStatus(Integer key) throws Exception {
    return false;
  }

}