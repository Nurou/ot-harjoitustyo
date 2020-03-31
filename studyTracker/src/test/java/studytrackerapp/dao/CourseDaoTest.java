package studytrackerapp.dao;

import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joel Hassan
 */
public class CourseDaoTest {

  private Database db;
  private CourseDao courseDao;

  public CourseDaoTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
    db = new Database();
    db.createDatabase("some.db");
  }

  @After
  public void tearDown() {
    File file = new File("some.db");
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  public void courseCreationSuccessful() {
    // ...
  }

}
