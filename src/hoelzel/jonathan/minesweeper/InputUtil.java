package hoelzel.jonathan.minesweeper;

import java.util.Map;
import java.util.Scanner;

public class InputUtil {
    public static final Scanner in = new Scanner(System.in);

    public static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            if (in.hasNextInt()) {
                int ret = in.nextInt();
                in.nextLine();
                return ret;
            }
            in.nextLine();
        }
    }

    public static boolean getBool(String prompt) {
        while (true) {
            System.out.print(prompt + "? ");
            String rsp = in.nextLine();
            if (rsp.equalsIgnoreCase("yes") || rsp.equalsIgnoreCase("true") || rsp.equalsIgnoreCase("y") || rsp.equalsIgnoreCase("t")) {
                return true;
            } else if (rsp.equalsIgnoreCase("no") || rsp.equalsIgnoreCase("false") ||rsp.equalsIgnoreCase("n") || rsp.equalsIgnoreCase("f")) {
                return false;
            }
        }
    }

    public static <T> T getChoice(String prompt, Map<String, T> m) {
        StringBuilder builder = new StringBuilder();
        builder.append(prompt);
        builder.append("(");
        for (String s : m.keySet()) {
            builder.append(s);
            builder.append(", ");
        }
        builder.replace(builder.length() - 2, builder.length(), "");
        builder.append("): ");
        String fullPrompt = builder.toString();

        while (true) {
            System.out.print(fullPrompt);
            String r = in.nextLine();

            if (m.containsKey(r)) {
                return m.get(r);
            }
        }
    }
}
