package studytrackerapp.dao;

import java.util.List;

import studytrackerapp.domain.Course;
import studytrackerapp.domain.User;

public interface CourseDao {

  Course create(Course course) throws Exception;

  List<Course> getAll();

  Course getOne();

  void setUser(User user);

  boolean delete(Integer key) throws Exception;

  boolean changeStatus(Integer key) throws Exception;

}
