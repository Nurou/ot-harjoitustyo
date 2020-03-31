
package studytrackerapp.domain;

import java.util.Objects;

/**
 * Class representing a course studied by the user
 */

public class Course {

  private String name;
  private int credits;
  private boolean isCompulsory;
  private int period;
  private String courseLink;

  private User user;

  public Course() {
  }

  public Course(String name, int credits, int period, boolean isCompulsory, String courseLink, User user) {
    this.name = name;
    this.credits = credits;
    this.isCompulsory = isCompulsory;
    this.period = period;
    this.courseLink = courseLink;
    this.user = user;
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

  public boolean isCompulsory() {
    return this.isCompulsory;
  }

  public boolean getCompulsory() {
    return this.isCompulsory;
  }

  public void setCompulsory(boolean isCompulsory) {
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

  public Course name(String name) {
    this.name = name;
    return this;
  }

  public Course credits(int credits) {
    this.credits = credits;
    return this;
  }

  public Course isCompulsory(boolean isCompulsory) {
    this.isCompulsory = isCompulsory;
    return this;
  }

  public Course period(int period) {
    this.period = period;
    return this;
  }

  public Course courseLink(String courseLink) {
    this.courseLink = courseLink;
    return this;
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
        && period == course.period && Objects.equals(courseLink, course.courseLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, credits, isCompulsory, period, courseLink);
  }

  @Override
  public String toString() {
    return "{" + " name='" + getName() + "'" + ", credits='" + getCredits() + "'" + ", isCompulsory='" + isCompulsory()
        + "'" + ", period='" + getPeriod() + "'" + ", courseLink='" + getCourseLink() + "'" + "}";
  }

}