package studytrackerapp.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DAOs not tested individually since the services are, and the services utilise
 * the DAOs
 * 
 */

public class DatabaseTest {

  Database testDatabase;

  @Before
  public void setUp() throws SQLException {

    testDatabase = new Database();
    testDatabase.createDatabase("test.db");

    System.out.println("Test db set up successful.");
  }

  @After
  public void tearDown() {
    File file = new File("test.db");
    file.delete();
    System.out.println("Test db has been torn down.");
  }

  @Test
  public void createdDatabaseExists() {
    File file = new File("test.db");
    assertTrue(file.exists());
    assertTrue(file.isFile());
  }

  @Test
  public void databasePathCanBeSet() {
    testDatabase.setPath("test.db");
    assertEquals("test.db", testDatabase.getPath());
  }

  @Test
  public void canConnectToDatabase() {
    Connection connection = null;
    try {
      connection = testDatabase.getConnection();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertNotNull(connection);
  }

  @Test
  public void canCreateTables() throws SQLException {
    assertTrue(testDatabase.createTables(DriverManager.getConnection("jdbc:sqlite:test.db")));
  }

}
