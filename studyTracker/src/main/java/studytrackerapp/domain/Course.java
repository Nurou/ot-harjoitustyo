
package studytrackerapp.domain;

import java.util.Objects;

public class Course {

  private String name;
  private int Credits;
  private boolean compulsory;
  private int period;
  private String courseLink;

  public Course() {
  }

  public Course(String name, int Credits, boolean compulsory, int period, String courseLink) {
    this.name = name;
    this.Credits = Credits;
    this.compulsory = compulsory;
    this.period = period;
    this.courseLink = courseLink;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCredits() {
    return this.Credits;
  }

  public void setCredits(int Credits) {
    this.Credits = Credits;
  }

  public boolean isCompulsory() {
    return this.compulsory;
  }

  public boolean getCompulsory() {
    return this.compulsory;
  }

  public void setCompulsory(boolean compulsory) {
    this.compulsory = compulsory;
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

  public Course Credits(int Credits) {
    this.Credits = Credits;
    return this;
  }

  public Course compulsory(boolean compulsory) {
    this.compulsory = compulsory;
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
    return Objects.equals(name, course.name) && Credits == course.Credits && compulsory == course.compulsory
        && period == course.period && Objects.equals(courseLink, course.courseLink);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, Credits, compulsory, period, courseLink);
  }

  @Override
  public String toString() {
    return "{" + " name='" + getName() + "'" + ", Credits='" + getCredits() + "'" + ", compulsory='" + isCompulsory()
        + "'" + ", period='" + getPeriod() + "'" + ", courseLink='" + getCourseLink() + "'" + "}";
  }

}