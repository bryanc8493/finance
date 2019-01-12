package domain.dto;

import literals.enums.InvestmentAccount;
import persistence.finance.InvestmentData;

public class InvestmentTrend {

    private InvestmentAccount account;
    private Integer expectedTrendPeriod;
    private Long actualTrendPeriod;
    private Double trendPeriodAmount;
    private Double currentAmount;
    private Double trendPercent;

    public InvestmentTrend(InvestmentAccount account, Integer trendPeriod) {
        this.account = account;
        this.expectedTrendPeriod = trendPeriod;
        this.currentAmount = InvestmentData.getAccountBalance(account);
    }

    public InvestmentAccount getAccount() {
        return account;
    }

    public Integer getExpectedTrendPeriod() {
        return expectedTrendPeriod;
    }

    public Long getActualTrendPeriod() {
        return actualTrendPeriod;
    }

    public void setActualTrendPeriod(Long actualTrendPeriod) {
        this.actualTrendPeriod = actualTrendPeriod;
    }

    public Double getTrendPeriodAmount() {
        return trendPeriodAmount;
    }

    public void setTrendPeriodAmount(Double trendPeriodAmount) {
        this.trendPeriodAmount = trendPeriodAmount;
    }

    public Double getCurrentAmount() {
        return currentAmount;
    }

    public Double getTrendPercent() {
        return trendPercent;
    }

    public void setTrendPercent(Double trendPercent) {
        this.trendPercent = trendPercent;
    }
}
