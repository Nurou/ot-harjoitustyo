package studytrackerapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides shared functionality for the DAO classes and is used to initialize
 * the database
 * 
 * @author joelhassan
 */

public class Database {

  private String path;
  private Connection connection;

  public Database() {
    this.connection = null;
  }

  /**
   * Opens a connection to the database using JDBC DriverManager
   * 
   * @return connection object representing a connection to a database
   * @throws SQLException if connection cannot be expected
   */

  public Connection getConnection() throws SQLException {
    if (connection == null) {
      connection = DriverManager.getConnection("jdbc:sqlite:" + path);
    }
    return connection;
  }

  /**
   * Creates new database in root of application JDBC provides standardized
   * communication with the db
   * 
   * @param fileName (name of the database file as a string)
   */

  public void createDatabase(final String fileName) {

    Connection connection = null;

    try {
      Class.forName("org.sqlite.JDBC");
      // creating a connection over jdbc
      connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
      System.out.println("Connection to SQLite has been established.");

      createTables(connection);

    } catch (final Exception e) {
      System.err.println("Error: " + e.getMessage());
    } finally {
      this.setPath(fileName);
      if (connection != null)
        try {
          connection.close();
        } catch (final SQLException ex) {
          System.out.println(ex.getMessage());
        }
    }
  }

  /**
   * Adds relations for the entities defined in the schema
   * 
   * @param connection the context in which the queries are executed
   * @throws SQLException throws an error if there was an issue creating the
   *                      tables
   * @return true if successful, false otherwise
   */
  public boolean createTables(final Connection connection) throws SQLException {

    final String userTableSql = "CREATE TABLE IF NOT EXISTS User (username TEXT PRIMARY KEY, name TEXT, password TEXT, program_name TEXT, target_credits INTEGER)";

    try (Statement statement = connection.createStatement()) {
      statement.execute(userTableSql);
    } catch (final Exception e) {
      System.err.println(e.getMessage());
      return false;
    }

    final String courseTableSql = "CREATE TABLE IF NOT EXISTS Course (id INTEGER PRIMARY KEY, name TEXT, credits INTEGER NOT NULL, compulsory INTEGER NOT NULL, status INTEGER NOT NULL, grade INTEGER, username TEXT NOT NULL, FOREIGN KEY (username) REFERENCES User (username));";

    try (Statement statement = connection.createStatement()) {
      statement.execute(courseTableSql);

    } catch (final Exception e) {
      System.err.println(e.getMessage());
      return false;
    }
    return true;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public String getPath() {
    return this.path;
  }

}
