package studytrackerapp.domain;

public class User {
  private String name;
  private String username;
  private int credits;

  public User(String username, String name) {
    this.name = name;
    this.username = username;

  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public int getCredits() {
    return this.credits;
  }

  public void setCredits(int credits) {
    this.credits = credits;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof User)) {
      return false;
    }

    User other = (User) obj;
    return username.equals(other.username);
  }

}
