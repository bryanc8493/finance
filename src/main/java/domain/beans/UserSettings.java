package domain.beans;

import java.util.Set;

public class UserSettings {

    private String backupLocation;
    private Set<String> expenseCategories;
    private Set<String> incomeCategories;
    private Double savingsSafetyAmount;
    private Integer viewingRecords;
    private String deploymentLocation;
    private Set<String> creditCards;
    private String templateFileLocation;
    private String chartOutputLocation;
    private String reportsOutputLocation;
    private String username;
    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public UserSettings(String username) {
        setUsername(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBackupLocation() {
        return backupLocation;
    }

    public void setBackupLocation(String backupLocation) {
        this.backupLocation = backupLocation;
    }

    public Set<String> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(Set<String> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    public Set<String> getIncomeCategories() {
        return incomeCategories;
    }

    public void setIncomeCategories(Set<String> incomeCategories) {
        this.incomeCategories = incomeCategories;
    }

    public Double getSavingsSafetyAmount() {
        return savingsSafetyAmount;
    }

    public void setSavingsSafetyAmount(Double savingsSafetyAmount) {
        this.savingsSafetyAmount = savingsSafetyAmount;
    }

    public Integer getViewingRecords() {
        return viewingRecords;
    }

    public void setViewingRecords(Integer viewingRecords) {
        this.viewingRecords = viewingRecords;
    }

    public String getDeploymentLocation() {
        return deploymentLocation;
    }

    public void setDeploymentLocation(String deploymentLocation) {
        this.deploymentLocation = deploymentLocation;
    }

    public Set<String> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(Set<String> creditCards) {
        this.creditCards = creditCards;
    }

    public String getTemplateFileLocation() {
        return templateFileLocation;
    }

    public void setTemplateFileLocation(String templateFileLocation) {
        this.templateFileLocation = templateFileLocation;
    }

    public String getChartOutputLocation() {
        return chartOutputLocation;
    }

    public void setChartOutputLocation(String chartOutputLocation) {
        this.chartOutputLocation = chartOutputLocation;
    }

    public String getReportsOutputLocation() {
        return reportsOutputLocation;
    }

    public void setReportsOutputLocation(String reportsOutputLocation) {
        this.reportsOutputLocation = reportsOutputLocation;
    }
}
