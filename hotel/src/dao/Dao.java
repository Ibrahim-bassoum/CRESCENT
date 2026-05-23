package dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique CRUD pour tous les DAOs.
 */
public interface Dao<T, ID> {

    void save(T entity);
    void update(T entity);
    void delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
}
