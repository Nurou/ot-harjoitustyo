
package studytrackerapp.domain;

import java.util.Objects;

/**
 * Class representing a course studied by the user
 */

public class Course {

  private String name;
  private int credits;
  private int isCompulsory;
  private int status;
  private int grade;

  private User user;

  /**
   * Constructor for when all the params are known, inc. user
   * 
   * @param name         - name of user
   * @param credits      - number of credits course is worth
   * @param isCompulsory - is the course compulsory. 0: No, 1: Yes
   * @param status       - current status of the course: 0: backlog, 1: ongoing,
   *                     2: completed
   * @param user         - the user the course belongs to
   */

  public Course(final String name, final int credits, final int isCompulsory, final int status, final User user) {
    this.name = name;
    this.credits = credits;
    this.isCompulsory = isCompulsory;
    this.status = status;
    this.user = user;
  }

  public Course(final String name, final int credits, final int isCompulsory, final int status, final int grade,
      final User user) {
    this.name = name;
    this.credits = credits;
    this.isCompulsory = isCompulsory;
    this.status = status;
    this.user = user;
    this.grade = grade;
  }

  public Course(final String name, final int credits, final int isCompulsory, final int status) {
    this.name = name;
    this.credits = credits;
    this.isCompulsory = isCompulsory;
    this.status = status;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(final int status) {
    this.status = status;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getCredits() {
    return this.credits;
  }

  public void setCredits(final int credits) {
    this.credits = credits;
  }

  public int getIsCompulsory() {
    return this.isCompulsory;
  }

  public void setIsCompulsory(final int isCompulsory) {
    this.isCompulsory = isCompulsory;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  public int getGrade() {
    return this.grade;
  }

  public void setGrade(final int grade) {
    this.grade = grade;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Course)) {
      return false;
    }
    final Course course = (Course) o;
    return Objects.equals(name, course.name) && credits == course.credits && isCompulsory == course.isCompulsory
        && status == course.status && grade == course.grade && Objects.equals(user, course.user);
  }

  @Override
  public String toString() {
    return "{" + " name='" + getName() + "'" + ", credits='" + getCredits() + "'" + ", isCompulsory='"
        + getIsCompulsory() + "'" + ", status='" + getStatus() + "'" + ", grade='" + getGrade() + "'" + ", user='"
        + getUser() + "'" + "}";
  }

}