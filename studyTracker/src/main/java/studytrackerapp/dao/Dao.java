package studytrackerapp.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * The DAO API keeps the domain model completely decoupled from the persistence
 * layer
 * 
 * It defines CRUD operations for the DAOs to implement
 * 
 * @param <T> - objects of type T
 * @param <K> - keys of type K
 * 
 *            The params are specified in the specific DAO implementations of
 *            this interface
 */

public interface Dao<T, K> {
  T create(T object) throws SQLException;

  T read(K key) throws SQLException;

  T update(T object) throws SQLException;

  void delete(K key) throws SQLException;

  List<T> list() throws SQLException;

  void createTable() throws SQLException;
}