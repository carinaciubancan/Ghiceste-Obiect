package joc.persistance;


import java.util.List;

public interface CrudRepository<ID, T> {
    void add(T elem);
    T save(T elem);
    List<T> findAll();
    void update(T elem);
    void delete(ID id);
    T findById(ID id);

}