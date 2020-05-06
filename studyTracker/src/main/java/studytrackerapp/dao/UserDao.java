package studytrackerapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import studytrackerapp.domain.User;

/**
 * DAO mapping of User object to User database entity
 *
 */
public class UserDao implements Dao<User, String> {

  private final Database database;

  /**
   * Constructs DAO for User objects and initializes User table in db
   *
   * @param database (database to be accessed)
   */
  public UserDao(final Database database) {
    this.database = database;
  }

  /**
   * @param user - the user to be mirrored in the db
   * @return User - the User object provided as an argument, null if user could
   *         not be created
   * @throws SQLException if there was an issue inserting a user to the db
   * 
   */

  @Override
  public User create(final User user) throws SQLException {
    // define query
    final String sql = "INSERT INTO " + "User(username, name, password, program_name, target_credits ) "
        + "VALUES (?, ?, ?, ?, ?)";

    final Connection connection = database.getConnection();
    final PreparedStatement statement = connection.prepareStatement(sql);
    // set values
    statement.setString(1, user.getUsername());
    statement.setString(2, user.getName());
    statement.setString(3, user.getPassword());
    statement.setString(4, user.getStudyProgram());
    statement.setInt(5, user.getTarget());
    statement.executeUpdate();

    return user;
  }

  /**
   * Fetch a single user from the database and create a User object using the data
   *
   * @param username - this is the primary key in the db
   * @return User object if found in database, null otherwise
   * @throws SQLException if there was an issue reading a user from the db
   */
  @Override
  public User read(final String username) throws SQLException {
    // object to hold the user, no user found by default
    User found = null;

    // define query
    final String sql = "SELECT username, name, password, program_name, target_credits FROM User WHERE username = ?";

    // attempt to form a connection
    final Connection connection = database.getConnection();
    final PreparedStatement statement = connection.prepareStatement(sql);

    // inject query params
    statement.setString(1, username);

    // execute query & store result
    final ResultSet resultSet = statement.executeQuery();

    if (resultSet.isClosed())
      return null;

    // create user
    found = new User(resultSet.getString("name"), resultSet.getString("username"), resultSet.getString("password"),
        resultSet.getString("program_name"), resultSet.getInt("target_credits"));

    System.out.println(found.getUsername() + " retrieved successfully");

    return found;
  }

  @Override
  public User update(final User user) {
    return null;
  }

  @Override
  public List<User> list() {
    return null;
  }

  @Override
  public boolean delete(final String key) {
    return false;
  }

}
