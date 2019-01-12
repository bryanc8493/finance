package utilities.settings;

import domain.beans.SystemSettings;
import domain.beans.UserSettings;
import persistence.Connect;
import persistence.settings.SettingsData;
import utilities.MapperUtil;
import utilities.exceptions.AppException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsService {

    public static UserSettings getCurrentUserSettings() {
        String user = Connect.getCurrentUser();

        return SettingsData.getUserSettingsData(user);
    }

    public static boolean saveUserSettings(UserSettings updatedSettings) {
        return SettingsData.saveUserSettings(updatedSettings);
    }

    public static SystemSettings getSystemSettings() {
        return SettingsData.getSystemSettingsData();
    }

    public static UserSettings mapUserSettings(ResultSet rs, String user, boolean isDefault) {
        UserSettings settings = new UserSettings(user);

        try {
            rs.next();
            settings.setBackupLocation(rs.getString(3));
            settings.setExpenseCategories(MapperUtil.mapCommaSeparatedList(rs.getString(4)));
            settings.setIncomeCategories(MapperUtil.mapCommaSeparatedList(rs.getString(5)));
            settings.setSavingsSafetyAmount(rs.getDouble(6));
            settings.setViewingRecords(rs.getInt(7));
            settings.setDeploymentLocation(rs.getString(8));
            settings.setCreditCards(MapperUtil.mapCommaSeparatedList(rs.getString(9)));
            settings.setTemplateFileLocation(rs.getString(10));
            settings.setChartOutputLocation(rs.getString(11));
            settings.setReportsOutputLocation(rs.getString(12));
            settings.setDefault(isDefault);
        } catch (SQLException e) {
            throw new AppException(e);
        }
System.out.println("");
        return settings;
    }

    public static SystemSettings mapSystemSettings(ResultSet rs) {
        SystemSettings settings = new SystemSettings();

        try {
            rs.next();
            settings.setRootUser(rs.getString(2));
            settings.setAdminUser(rs.getString(3));
            settings.setReportTypes(MapperUtil.mapCommaSeparatedList(rs.getString(4)));
            settings.setDeploymentLocation(rs.getString(5));
            settings.setDevelopmentLocation(rs.getString(6));
            settings.setDatabaseServerLocation(rs.getString(7));
            settings.setEncryptionKey(rs.getString(8));
            settings.setRootPassword(rs.getString(9));
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return settings;
    }
}
