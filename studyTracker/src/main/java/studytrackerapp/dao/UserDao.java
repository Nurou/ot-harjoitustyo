package studytrackerapp.dao;

import studytrackerapp.domain.User;

/**
 * DAO interface for User objects
 */
public interface UserDao {

  public User getOne(String key);

  public User create(User object);

  public boolean updateUser(User object);
}
