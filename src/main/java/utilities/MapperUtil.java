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

    public static String mapSetToCommaSeparatedString(Set<String> rawData) {
        StringBuffer sb = new StringBuffer();

        for (String data : rawData) {
            sb.append(data.trim() + ApplicationLiterals.COMMA);
        }

        String returnData = sb.toString();
        return returnData.substring(0, returnData.length() - 1);
    }
}
