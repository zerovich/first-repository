package task;

import java.sql.SQLException;

/**
 * Created by Тарас on 21.05.2017.
 */
public class TerminalExpression implements Expression {

    private OperationsDB operationsDB = null;

    @Override
    public void interpret(String context) {

        try {
            if (context.toLowerCase().contains("add")) {
                operationsDB = new DBHelper();
                operationsDB.add(context);
            } else if (context.toLowerCase().contains("clear")) {
                operationsDB = new DBHelper();
                operationsDB.clear(context);
            } else if (context.toLowerCase().contains("list")) {
                operationsDB = new DBHelper();
                operationsDB.list(context);
            } else if (context.toLowerCase().contains("total")) {
                operationsDB = new DBHelper();
                operationsDB.total(context);
            } else if (context.toLowerCase().contains("exit")) {
                System.exit(0);
            } else {
                System.err.println("Wrong command!!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        }
    }
