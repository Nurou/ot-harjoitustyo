package studytrackerapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import studytrackerapp.domain.User;

/**
 * Interface between User objects and db users
 *
 */
public class UserDao implements Dao<User, String> {

  private Database database;

  /**
   * Constructs DAO for User objects and initializes User table in db
   *
   * @param database (database to be accessed)
   * @throws SQLException
   */
  public UserDao(Database database) throws SQLException {
    this.database = database;
    createTable();
  }

  /**
   * @param user - the user to be mirrored in the db
   * @return User - the User object provided as an argument, null if user could
   *         not be created
   */
  @Override
  public User create(User user) {
    // define query
    String sql = "INSERT INTO " + "User(username, name, password) " + "VALUES (?, ?, ?)";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      // set values
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getName());
      statement.setString(3, user.getPassword());
      statement.executeUpdate();
    } catch (SQLException e) {
      return null;
    }

    return user;
  }

  /**
   * Fetch a single user from the database & create a User object using the data
   *
   * @param username - this is the primary key in the db
   * @return User object if found in database, null otherwise
   */
  @Override
  public User read(String username) {
    // object to hold the user, no user found by default
    User found = null;

    // define query
    String sql = "SELECT username, name, password FROM User WHERE username = ?";

    // attempt to form a connection
    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // inject query params
      statement.setString(1, username);

      // execute query & store result
      ResultSet resultSet = statement.executeQuery();

      // create user
      found = new User(resultSet.getString("name"), resultSet.getString("username"), resultSet.getString("password"));

    } catch (SQLException e) {
      System.out.println();
      System.err.println("Error in UserDao - unable to retrieve user \n" + e.getMessage());
      return found;
    }

    System.out.println(found.getUsername() + " retrieved successfully");
    return found;
  }

  /**
   * Updates a user's details
   *
   * @param user - User object to be updated
   * @return true if setting succeeded; otherwise false
   */
  @Override
  public User update(User user) {
    // String sql = "UPDATE User SET credits = ? WHERE username = ?";

    // try (Connection connection = database.getConnection();
    // PreparedStatement statement = connection.prepareStatement(sql)) {
    // statement.setInt(1, user.getCredits());
    // statement.setString(2, user.getUsername());
    // statement.executeUpdate();
    // } catch (SQLException e) {
    // return null;
    // }
    return null;
    // return user;
  }

  @Override
  public List list() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(String key) {
    // TODO Auto-generated method stub

  }

  /**
   * Creates a table for the object type in the db if one does not already exist
   *
   * @throws SQLException
   */
  @Override
  public void createTable() throws SQLException {
    String sql = "CREATE TABLE IF NOT EXISTS User (username TEXT PRIMARY KEY, name TEXT, password TEXT)";

    try (Connection connection = database.getConnection(); Statement statement = connection.createStatement()) {
      statement.execute(sql);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

}
