package repositories;

import java.util.List;

public interface GenericRepository<T> {
    void create(T entity);
    T read(int id);
    void update(T entity);
    void delete(int id);
    List<T> readAll();
}
