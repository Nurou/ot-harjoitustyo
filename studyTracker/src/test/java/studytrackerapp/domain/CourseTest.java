
package studytrackerapp.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joelhassan
 */

public class CourseTest {

  @Test
  public void coursesWithSameContentAreEqual() {
    Course c1 = new Course("course", 5, 0, 1, "link", new User("person", "user", "pass"));
    Course c2 = new Course("course", 5, 0, 1, "link", new User("person", "user", "pass"));
    assertTrue(c1.equals(c2));
  }

  @Test
  public void coursesWithDifferentContentAreNotEqual() {
    Course c1 = new Course("course", 5, 0, 1, "link", new User("person", "user", "pass"));
    Course c2 = new Course("another course", 5, 0, 1, "link", new User("person", "user", "pass"));
    assertFalse(c1.equals(c2));
  }

  @Test
  public void courseNotEqualToObjectOfDifferentType() {
    Course c = new Course("course", 5, 0, 1, "link", new User("person", "user", "pass"));
    Object o = new Object();
    assertFalse(c.equals(o));
  }

}
