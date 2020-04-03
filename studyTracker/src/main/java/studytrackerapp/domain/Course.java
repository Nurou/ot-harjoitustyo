
package studytrackerapp.domain;

import java.util.Objects;

/**
 * Class representing a course studied by the user
 */

public class Course {

  private String name;
  private int credits;
  private int isCompulsory;
  private int period;
  private String status;
  private String courseLink;

  private User user;

  public Course() {
  }

  public Course(String name, int credits, int isCompulsory, int period, String courseLink, User user) {
    this.name = name;
    this.credits = credits;
    this.isCompulsory = isCompulsory;
    this.period = period;
    this.courseLink = courseLink;
    this.user = user;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCredits() {
    return this.credits;
  }

  public void setCredits(int credits) {
    this.credits = credits;
  }

  public int getIsCompulsory() {
    return this.isCompulsory;
  }

  public void setIsCompulsory(int isCompulsory) {
    this.isCompulsory = isCompulsory;
  }

  public int getPeriod() {
    return this.period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public String getCourseLink() {
    return this.courseLink;
  }

  public void setCourseLink(String courseLink) {
    this.courseLink = courseLink;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Course)) {
      return false;
    }
    Course course = (Course) o;
    return Objects.equals(name, course.name) && credits == course.credits && isCompulsory == course.isCompulsory
        && period == course.period && Objects.equals(courseLink, course.courseLink)
        && Objects.equals(user, course.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, credits, isCompulsory, period, courseLink, user);
  }

  @Override
  public String toString() {
    return "{" + " name='" + getName() + "'" + ", credits='" + getCredits() + "'" + ", isCompulsory='"
        + getIsCompulsory() + "'" + ", period='" + getPeriod() + "'" + ", courseLink='" + getCourseLink() + "'"
        + ", user='" + getUser() + "'" + "}";
  }

}