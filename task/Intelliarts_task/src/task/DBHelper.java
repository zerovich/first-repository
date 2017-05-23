package task;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Тарас on 21.05.2017.
 */
public class DBHelper implements OperationsDB {
    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;


    public static void connect() throws ClassNotFoundException, SQLException {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:test.s3db");
    }

    public static void creatDB() throws ClassNotFoundException, SQLException {
        statement = connection.createStatement();
        statement.execute("create table if not exists 'expenses' " +
                "('id' integer primary key autoincrement," +
                "'name' text, 'date' text, 'amount' text, 'currency' text, 'convertToEur' text);");
    }

    public static void closeDB() throws ClassNotFoundException, SQLException {
        connection.close();
        statement.close();
        try {
            resultSet.close();
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void add(String command) throws SQLException {

        String afterAdd = command.substring(4);
        Pattern p = Pattern.compile("[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])\\s(([1-9](\\d{0,5}))|(([0-9]{0,6}[.])\\d{0,2}))\\s[a-zA-Z]{3}\\s([a-zA-Z]{0,20})");
        Matcher m = p.matcher(afterAdd);
        boolean check = m.matches();
        if (!check) {
            System.err.println("Incorrect format for 'add'!");
            return;
        }else {
            String resValue = "";
            String[] arr = command.split(" ");
            String currencyAmount = arr[2];
            String currencyName = arr[3].toUpperCase();
            if (currencyName.equals("EUR")) {
                resValue = currencyAmount;
                statement.execute("insert into 'expenses' ('name', 'date', 'amount', 'currency', 'convertToEur') " +
                        "values ('" + arr[4] + "', " + "'" + arr[1] + "', " + "'" + arr[2] + "', " + "'" + currencyName + "', " +
                        "'" + resValue + "'); ");
                orderByDate();
            } else {
                resValue = getCurrency(currencyName);

                try {
                    Double a = Double.parseDouble(resValue);
                    Double b = Double.parseDouble(currencyAmount);
                    double c = (b / a) * 100;
                    double round = Math.round(c);
                    Double finalRes = (round / 100.0);
                    String finalValue = finalRes.toString();
                    statement.execute("insert into 'expenses' ('name', 'date', 'amount', 'currency', 'convertToEur') " +
                            "values ('" + arr[4] + "', " + "'" + arr[1] + "', " + "'" + arr[2] + "', " + "'" + currencyName + "', " +
                            "'" + finalValue + "'); ");
                    orderByDate();
                } catch (NumberFormatException e) {
                }
            }
        }



    }

    @Override
    public void list(String command) throws SQLException {
        if (command.trim().toLowerCase().matches("list")) orderByDate();
        else System.err.println("Incorrect format for 'list'!");
    }

    @Override
    public void clear(String command) throws SQLException {
        if(command.trim().toLowerCase().matches("clear"))
            statement.execute("delete from expenses;");
        else {
            String date = "";
            try {
                date = command.trim().substring(6,16);
                Pattern p = Pattern.compile("[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])");
                Matcher m = p.matcher(date);
                boolean res = m.matches();
                if (res) {
                    statement.execute("delete from expenses " +
                            "where date = " + "'" + date + "';");
                    orderByDate();
                } else {System.err.println("Incorrect format for 'clear'!");}
            } catch (Exception e) {
                System.err.println("Incorrect format for 'clear'!");
            }

        }
    }

    @Override
    public String getCurrency(String currency)  {
             String value = "";
        try {
            URLConnection connection = new URL("http://api.fixer.io/latest").openConnection();

            try {

                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                char[] buffer = new char[256];
                int rc;

                StringBuilder sb = new StringBuilder();

                while ((rc = reader.read(buffer)) != -1)
                    sb.append(buffer, 0, rc);
                String res = sb.toString();
                reader.close();


                JsonParser parser = new JsonParser();
                JsonObject root = parser.parse(res).getAsJsonObject();

                value = root.getAsJsonObject("rates")
                        .get(currency).toString();
            } catch (UnknownHostException uh) {
                System.err.println("You must have internet connection!");
            } catch (SocketTimeoutException e) {
                System.err.println("Time is over!");
            } catch (NullPointerException e) {
                System.err.println("There is no such currency!");
            }
        } catch (IOException ioe) {
        }
        return value;

    }

    @Override
    public void total(String command) throws SQLException {

        //String total = command.substring(0);
        Pattern p = Pattern.compile("[a-zA-Z]{5}\\s([a-zA-Z]{3})");
        Matcher m = p.matcher(command);
        boolean check = m.matches();
        if (!check) {
            System.err.println("Incorrect format for 'total'!");
            return;
        }
        else {

            String subTotal = command.substring(0, 5);
            try {
                String currency = command.substring(6, 9).toUpperCase();
                String ifEur = "";
                if (currency.equals("EUR")) {
                    resultSet = statement.executeQuery("select sum(convertToEur) from expenses;");
                    while (resultSet.next()) {
                        ifEur = resultSet.getString("sum(convertToEur)");
                    }
                    System.out.println(ifEur + " " + currency);
                } else {
                    String value = getCurrency(currency);
                    Double a = Double.parseDouble(value);

                    resultSet = statement.executeQuery("select sum(convertToEur) from expenses;");
                    String amount = "";
                    while (resultSet.next()) {
                        amount = resultSet.getString("sum(convertToEur)");
                    }
                    Double b = Double.parseDouble(amount);
                    double c = (b * a) * 100;
                    double round = Math.round(c);
                    Double finalRes = (round / 100.0);

                    System.out.println(finalRes + " " + currency);
                }
            } catch (Exception e) {
                System.err.println("DB is empty!");
            }
        }
    }


    @Override
    public void orderByDate() throws SQLException {
        resultSet = statement.executeQuery("select date from expenses group by date having count(*)>=1;");
        List<String> arrDate = new ArrayList<>();
        while(resultSet.next()) {
            String nextDate = resultSet.getString("date");
            arrDate.add(nextDate);
        }

        for (int i = 0; i < arrDate.size(); i++) {
            resultSet = statement.executeQuery("select name, amount, currency from expenses where date = " + "'" + arrDate.get(i) + "';");
            System.out.println(arrDate.get(i));
            while(resultSet.next())
            {
                String  name = resultSet.getString("name");
                String  amount = resultSet.getString("amount");
                String  currency = resultSet.getString("currency");
                System.out.print(name + " " + amount + " " + currency.toUpperCase());
                System.out.println();
            }
            System.out.println();
        }
    }
}
