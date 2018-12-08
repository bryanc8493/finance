package utilities;

import literals.ApplicationLiterals;

import java.text.NumberFormat;

public class StringUtility {

    public static boolean isEmpty(String input) {
        return input
            .trim()
            .replace(ApplicationLiterals.WHITESPACE, ApplicationLiterals.EMPTY)
            .equals(ApplicationLiterals.EMPTY);
    }

    public static String formatAsCurrency(double input) {
        NumberFormat numberFormat = ApplicationLiterals.getNumberFormat();
        return "$ " + numberFormat.format(input);
    }

    public static String formatAsCurrency(String input) {
        return "$ " + input;
    }
}
