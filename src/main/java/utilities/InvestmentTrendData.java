package utilities;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class InvestmentTrendData {

    public static Double determineTrendAmount(int days, Map<LocalDate, Double> data) {
        LocalDate dateMinusTrend = LocalDate.now().minusDays(days);
        LocalDate closestDate = getClosestDateValue(dateMinusTrend, data.keySet());
System.out.println("date minust trend " + dateMinusTrend);
System.out.println("closest " + closestDate);
System.out.println("data " + data.get(closestDate));
        return data.get(closestDate);
    }

    private static LocalDate getClosestDateValue(LocalDate date, Set<LocalDate> dateEntries) {
        LocalDate closest = null;
        LocalDate now = LocalDate.now();
        long daysDifference = Long.MAX_VALUE;

        for (LocalDate entry : dateEntries) {
            int comp = entry.compareTo(now);
            long diff = Math.abs(comp);

            if (diff < daysDifference) {
                daysDifference = diff;
                closest = entry;
            }
        }

        return closest;
    }
}
