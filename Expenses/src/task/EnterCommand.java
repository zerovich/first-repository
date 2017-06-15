package task;

import java.util.Scanner;

/**
 * Created by Тарас on 22.05.2017.
 */
public class EnterCommand {
    public static String enterCommand() {
        Scanner sc = new Scanner(System.in);
        String res = sc.nextLine();
        return res;
    }
}
