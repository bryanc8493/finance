package utilities;

import literals.ApplicationLiterals;

public class StringUtility {

    public static boolean isEmpty(String input) {
        return input
            .trim()
            .replace(ApplicationLiterals.WHITESPACE, ApplicationLiterals.EMPTY)
            .equals(ApplicationLiterals.EMPTY);
    }
}
