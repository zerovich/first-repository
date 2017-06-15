package task;

import java.sql.SQLException;

/**
 * Created by Тарас on 21.05.2017.
 */
public interface OperationsDB {
    void add(String command)  throws SQLException;
    void list(String command) throws SQLException;
    void clear(String command)throws SQLException;
    String getCurrency(String command)throws SQLException;
    void total(String command)throws SQLException;
    void orderByDate()throws SQLException;
}
