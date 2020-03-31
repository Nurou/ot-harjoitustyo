package studytrackerapp.domain;

import java.util.Objects;

/**
 * Class representing a user There's only one type of user
 */

public class User {
  private String name;
  private String username;
  private String password;
  private int credits;

  public User(String name, String username, String password) {
    this.name = name;
    this.username = username;
    this.password = password;
  }

  public User() {
  }

  public User(String name, String username, String password, int credits) {
    this.name = name;
    this.username = username;
    this.password = password;
    this.credits = credits;
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

  public int getCredits() {
    return this.credits;
  }

  public void setCredits(int credits) {
    this.credits = credits;
  }

  public User name(String name) {
    this.name = name;
    return this;
  }

  public User username(String username) {
    this.username = username;
    return this;
  }

  public User password(String password) {
    this.password = password;
    return this;
  }

  public User credits(int credits) {
    this.credits = credits;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(name, user.name) && Objects.equals(username, user.username)
        && Objects.equals(password, user.password) && credits == user.credits;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, username, password, credits);
  }

  @Override
  public String toString() {
    return "{" + " name='" + getName() + "'" + ", username='" + getUsername() + "'" + ", password='" + getPassword()
        + "'" + ", credits='" + getCredits() + "'" + "}";
  }

}
