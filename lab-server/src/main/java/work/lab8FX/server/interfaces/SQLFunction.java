package work.lab8FX.server.interfaces;

import work.lab8FX.common.exceptions.DatabaseException;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLFunction<T, R> {
    R apply(T t) throws SQLException, DatabaseException;
}
