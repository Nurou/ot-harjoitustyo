package studytrackerapp.dao;

import java.util.List;

import studytrackerapp.domain.Course;

public interface CourseDao {

  Course create(Course course) throws Exception;

  List<Course> getAll();

  void modify(int id) throws Exception;

}
