package studytrackerapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class provides shared functionality for the DAO classes that interface
 * with the db
 * 
 * @author joelhassan
 */

public class Database {

  private String path;

  public Database() {
  }

  /**
   * Opens a connection to the database using JDBC DriverManager
   * 
   * @return connection object representing a connection to a database
   * @throws SQLException (connection cannot be established)
   */

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:sqlite:" + path);
  }

  /**
   * Creates new database in root of application JDBC provides standardized
   * communication with the db
   * 
   * @param fileName (name of the database file as a string)
   */

  public void createDatabase(String fileName) {

    Connection connection = null;

    try {
      Class.forName("org.sqlite.JDBC");
      // creating a connection over jdbc
      connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
      System.out.println("Connection to SQLite has been established.");

      createTables(connection);

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    } finally {
      this.setPath(fileName);
      try {
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

  /**
   * Adds the required tables to the db
   * 
   * @param connection - the context in which the queries are executed
   * @throws SQLException - throws an error if there was an issue creating the
   *                      tables
   * @return true if successful, false otherwise
   */
  public boolean createTables(Connection connection) throws SQLException {

    String userTableSql = "CREATE TABLE IF NOT EXISTS User (username TEXT PRIMARY KEY, name TEXT, password TEXT, program_name TEXT, target_credits INTEGER)";

    try (Statement statement = connection.createStatement()) {
      statement.execute(userTableSql);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return false;
    }

    String courseTableSql = "CREATE TABLE IF NOT EXISTS Course (id INTEGER PRIMARY KEY, name TEXT, credits INTEGER NOT NULL, compulsory INTEGER NOT NULL, status INTEGER NOT NULL, course_link TEXT, username TEXT NOT NULL, FOREIGN KEY (username) REFERENCES User (username));";

    try (Statement statement = connection.createStatement()) {
      statement.execute(courseTableSql);

    } catch (Exception e) {
      System.err.println(e.getMessage());
      return false;
    }
    return true;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getPath() {
    return this.path;
  }

}
