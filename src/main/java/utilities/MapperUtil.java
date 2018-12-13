package utilities;

import literals.ApplicationLiterals;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class MapperUtil {

    public static Set<String> mapCommaSeparatedList(String rawData) {
        String[] splitData = rawData.split(ApplicationLiterals.COMMA);

        return new LinkedHashSet<>(Arrays.asList(splitData));
    }
}
