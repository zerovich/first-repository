package task;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Тарас on 21.05.2017.
 */
public class Main {

    static {
        System.out.println("You can use 'add', 'clear', 'total', 'list' and 'exit' commands." +
                "\nAnd remember you need internet connection!");
    }

    public static void main(String[] args)  throws ClassNotFoundException, SQLException {
        Expression expr = new TerminalExpression();

        while (true) {
            DBHelper.connect();
            DBHelper.creatDB();
            String word = EnterCommand.enterCommand();
            expr.interpret(word);
            DBHelper.closeDB();
        }
        }
    }

