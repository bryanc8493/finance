package domain.beans;

import java.util.Set;

public class SystemSettings {

    private String rootUser;
    private String adminUser;
    private Set<String> reportTypes;
    private String deploymentLocation;
    private String developmentLocation;
    private String databaseServerLocation;

    public SystemSettings() {}

    public String getRootUser() {
        return rootUser;
    }

    public void setRootUser(String rootUser) {
        this.rootUser = rootUser;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public Set<String> getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(Set<String> reportTypes) {
        this.reportTypes = reportTypes;
    }

    public String getDeploymentLocation() {
        return deploymentLocation;
    }

    public void setDeploymentLocation(String deploymentLocation) {
        this.deploymentLocation = deploymentLocation;
    }

    public String getDevelopmentLocation() {
        return developmentLocation;
    }

    public void setDevelopmentLocation(String developmentLocation) {
        this.developmentLocation = developmentLocation;
    }

    public String getDatabaseServerLocation() {
        return databaseServerLocation;
    }

    public void setDatabaseServerLocation(String databaseServerLocation) {
        this.databaseServerLocation = databaseServerLocation;
    }
}
