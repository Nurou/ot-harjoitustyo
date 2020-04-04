package studytrackerapp.dao;

import studytrackerapp.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Interface between User objects and db users
 * 
 */
public class UserDao implements Dao<User, String> {

  private Database database;

  /**
   * Constructs DAO for User objects and creates table for object type if it
   * doesn't exist
   * 
   * @param database (database to be accessed)
   * @throws SQLException
   */
  public UserDao(Database database) throws SQLException {
    this.database = database;
    createTable();
  }

  /**
   * Adds a new user into the database
   * 
   * @param user - the User to be added to the database (holds all the necessary
   *             info)
   * @return created User object
   */
  @Override
  public User create(User user) {
    // define query
    String sql = "INSERT INTO " + "User(username, name, password) " + "VALUES (?, ?, ?)";

    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      // set values
      statement.setString(1, user.getName());
      statement.setString(2, user.getUsername());
      statement.setString(3, user.getPassword());
      statement.executeUpdate();
    } catch (SQLException e) {
    }

    return user;
  }

  /**
   * Fetch a single user from the database & create a User object using the data
   * 
   * @param key - username (primary key)
   * @return User object if found in database, null otherwise
   */
  @Override
  public User read(String key) {
    // object to hold the user, no user found by default
    User found = null;

    // define query
    String sql = "SELECT username, name, password, credits " + "FROM User WHERE username = ?";

    // attempt to form a connection
    try (Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      // inject query params
      statement.setString(1, key);

      // execute query & store result
      ResultSet resultSet = statement.executeQuery();

      // create user
      found = new User(resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("password"));

    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }

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

  @Override
  public void createTable() throws SQLException {

    String sql = "CREATE TABLE IF NOT EXISTS User (\n" + "username TEXT PRIMARY KEY,\n" + "name TEXT NOT NULL,\n"
        + "password TEXT NOT NULL,\n" + ");";

    try (Connection connection = database.getConnection(); Statement statement = connection.createStatement()) {
      statement.execute(sql);

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
