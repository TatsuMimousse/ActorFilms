package DAO;

import java.util.List;
import java.sql.SQLException;

public interface IDAO <T, K> {


    int create(T t) throws SQLException;

    T find(K id) throws SQLException;

    List<T> getAll() throws SQLException;

    // pas de suppression prevue non demande...
}

