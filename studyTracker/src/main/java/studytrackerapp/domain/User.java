package studytrackerapp.domain;

import java.util.Objects;

/**
 * Class representing a user There's only one type of user
 */

public class User {
  private String name;
  private String username;
  private String password;
  private String studyProgram;
  private int targetCredits;

  public User() {
  }

  /**
   * 
   * @param name         - full name of the user
   * @param username     - the user's username
   * @param password     - the user's password
   * @param studyProgram - name of the program the user is studying
   * @param target       - target number of credits for the user
   */

  public User(final String name, final String username, final String password, final String studyProgram,
      final int target) {
    this.name = name;
    this.username = username;
    this.password = password;
    this.studyProgram = studyProgram;
    this.targetCredits = target;
  }

  public User(final String name, final String username, final String password) {
    this.name = name;
    this.username = username;
    this.password = password;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getStudyProgram() {
    return this.studyProgram;
  }

  public void setStudyProgram(final String studyProgram) {
    this.studyProgram = studyProgram;
  }

  public int getTarget() {
    return this.targetCredits;
  }

  public void setTarget(final int target) {
    this.targetCredits = target;
  }

  public User name(final String name) {
    this.name = name;
    return this;
  }

  public User username(final String username) {
    this.username = username;
    return this;
  }

  public User password(final String password) {
    this.password = password;
    return this;
  }

  public User studyProgram(final String studyProgram) {
    this.studyProgram = studyProgram;
    return this;
  }

  public User target(final int target) {
    this.targetCredits = target;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (!(o instanceof User)) {
      return false;
    }
    final User user = (User) o;
    return Objects.equals(name, user.name) && Objects.equals(username, user.username)
        && Objects.equals(password, user.password) && Objects.equals(studyProgram, user.studyProgram)
        && targetCredits == user.targetCredits;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, username, password, studyProgram, targetCredits);
  }

  @Override
  public String toString() {
    return "{" + " name='" + getName() + "'" + ", username='" + getUsername() + "'" + ", password='" + getPassword()
        + "'" + ", studyProgram='" + getStudyProgram() + "'" + ", target='" + getTarget() + "'" + "}";
  }

}
