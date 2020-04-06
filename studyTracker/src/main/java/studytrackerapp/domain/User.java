package studytrackerapp.domain;

import java.util.Objects;

/**
 * Class representing a user There's only one type of user
 */

public class User {
  private String name;
  private String username;
  private String password;

  public User(String name, String username, String password) {
    this.name = name;
    this.username = username;
    this.password = password;
  }

  public User() {
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(name, user.name) && Objects.equals(username, user.username)
        && Objects.equals(password, user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, username, password);
  }

  @Override
  public String toString() {
    return "{\n" + " name='" + getName() + "', \n" + " username='" + getUsername() + "', \n" + " password='"
        + getPassword() + "'\n" + "}";
  }

}
