package utilities;

import literals.ApplicationLiterals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommonConfigValues {

    public static Set<String> getCreditCards() {
        String cardsString = ReadConfig.getConfigValue(ApplicationLiterals.CREDIT_CARDS);
        String[] values = cardsString.split(ApplicationLiterals.COMMA);

        return new HashSet<>(Arrays.asList(values));
    }
}
