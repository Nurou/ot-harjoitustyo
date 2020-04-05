package studytrackerapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
    } catch (Exception e) {
      System.out.println("Error here!");
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

  private void setPath(String path) {
    this.path = path;
  }

}
