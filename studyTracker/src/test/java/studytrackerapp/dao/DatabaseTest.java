package studytrackerapp.dao;

import java.io.File;
import java.sql.Connection;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseTest {

  public DatabaseTest() {
  }

  @After
  public void tearDown() {
    File file = new File("some.db");
    file.delete();
  }

  @Test
  public void creatingDbSucceeds() {
    Database testDatabase = new Database();
    testDatabase.createDatabase("some.db");
    File file = new File("some.db");
    assertTrue(file.exists());
    assertTrue(file.isFile());
  }

  @Test
  public void connectionToDbSucceeds() {
    Database testDatabase = new Database();
    testDatabase.createDatabase("some.db");
    Connection connection = null;
    try {
      connection = testDatabase.getConnection();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertTrue(connection != null);
  }

}
