package studytrackerapp.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joelhassan
 */
public class UserTest {

  public UserTest() {
  }

  @Test
  public void usersWithSameContentAreEqual() {
    User u1 = new User("person", "user", "pass");
    User u2 = new User("person", "user", "pass");
    assertTrue(u1.equals(u2));
  }

  @Test
  public void usersWithDifferentContentAreNotEqual() {
    User u1 = new User("person", "user", "password");
    User u2 = new User("second", "user", "password");
    assertFalse(u1.equals(u2));
  }

  @Test
  public void userNotEqualToObjectOfDifferentType() {
    User u = new User("person", "user", "password");
    Object o = new Object();
    assertFalse(u.equals(o));
  }
}
