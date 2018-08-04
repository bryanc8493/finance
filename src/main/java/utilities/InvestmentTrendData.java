package utilities;

import beans.InvestmentTrend;
import literals.enums.InvestmentAccount;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.time.temporal.ChronoUnit;

public class InvestmentTrendData {

    public static InvestmentTrend determineTrendAmount(int days, Map<LocalDate, Double> data,
                                                       InvestmentAccount account) {
        LocalDate today = LocalDate.now();
        LocalDate dateMinusTrend = today.minusDays(days);
        LocalDate closestDate = getClosestDateValue(dateMinusTrend, data.keySet());

        long daysAgo = Math.abs(ChronoUnit.DAYS.between(today, closestDate));

        InvestmentTrend investmentTrendData = new InvestmentTrend(account, days);
        investmentTrendData.setActualTrendPeriod(daysAgo);
        investmentTrendData.setTrendPeriodAmount(data.get(closestDate));
        investmentTrendData.setTrendPercent(calculateGrowthPercent(investmentTrendData));

        return investmentTrendData;
    }

    private static LocalDate getClosestDateValue(LocalDate date, Set<LocalDate> dateEntries) {
        LocalDate closest = null;
        long daysDifference = Long.MAX_VALUE;

        for (LocalDate entry : dateEntries) {
            long diff = Math.abs(ChronoUnit.DAYS.between(entry, date));

            if (diff < daysDifference) {
                daysDifference = diff;
                closest = entry;
            }
        }

        return closest;
    }

    private static double calculateGrowthPercent(InvestmentTrend investmentData) {
        double difference = investmentData.getCurrentAmount() - investmentData.getTrendPeriodAmount();
        return (difference / investmentData.getTrendPeriodAmount()) * 100;
    }
}
