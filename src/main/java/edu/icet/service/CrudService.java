package edu.icet.service;

import java.sql.SQLException;
import java.util.List;

public interface CrudService<T, ID> {
    boolean save(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(ID id) throws SQLException;
    T search(ID id) throws SQLException;
    List<T> getAll() throws SQLException;
}
