package utilities.settings;

import domain.beans.UserSettings;
import literals.ApplicationLiterals;
import persistence.Connect;
import persistence.settings.SettingsData;
import utilities.MapperUtil;
import utilities.exceptions.AppException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class SettingsService {

    public static UserSettings getCurrentUserSettings() {
        String user = Connect.getCurrentUser();

        return SettingsData.getUserSettingsData(user);
    }

    public static UserSettings mapUserSettings(ResultSet rs) {
        UserSettings settings = new UserSettings();

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
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return settings;
    }
}
