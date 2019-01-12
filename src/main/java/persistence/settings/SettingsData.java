package persistence.settings;

import domain.beans.SystemSettings;
import domain.beans.UserSettings;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import utilities.MapperUtil;
import utilities.exceptions.AppException;
import utilities.settings.SettingsService;

import java.sql.*;

public class SettingsData {

    private static Logger logger = Logger.getLogger(SettingsData.class);

    public static SystemSettings getSystemSettingsData() {
        logger.debug("getting system settings");
        String query = "SELECT * FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                + Tables.SYSTEM_SETTINGS + " WHERE ID = '1'";

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            SystemSettings settings = SettingsService.mapSystemSettings(rs);
            con.close();
            return settings;
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static UserSettings getUserSettingsData(String user) {
        logger.debug("getting settings for user: " + user);
        String query;
        boolean isDefault = false;

        if (userHasSettings(user)) {
            query = "SELECT * FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                    + Tables.USER_SETTINGS + " WHERE USERNAME = '" + user + "'";
        } else {
            query = "SELECT * FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                    + Tables.USER_SETTINGS + " WHERE USERNAME = 'DEFAULT'";
            isDefault = true;
        }

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            UserSettings userSettings = SettingsService.mapUserSettings(rs, user, isDefault);
            con.close();
            return userSettings;
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static boolean saveUserSettings(UserSettings settings) {
        return settings.isDefault() ? insertNewUserSettings(settings) : updateExistingUserSettings(settings);
//        return insertNewUserSettings(settings);
    }

    private static boolean insertNewUserSettings(UserSettings settings) {
        String query = "INSERT INTO " + Databases.ACCOUNTS + ApplicationLiterals.DOT
            + Tables.USER_SETTINGS + " (USERNAME, BACKUP_LOCATION, EXPENSE_CATEGORIES, "
            + "INCOME_CATEGORIES, SAVING_AMOUNT, VIEWING_RECORDS, DEPLOYMENT_LOCATION, "
            + "CREDIT_CARDS, TEMPLATE_FILE, CHART_OUTPUT, REPORTS_OUTPUT) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, settings.getUsername());
            ps.setString(2, settings.getBackupLocation());
            ps.setString(3, MapperUtil.mapSetToCommaSeparatedString(settings.getExpenseCategories()));
            ps.setString(4, MapperUtil.mapSetToCommaSeparatedString(settings.getIncomeCategories()));
            ps.setDouble(5, settings.getSavingsSafetyAmount());
            ps.setInt(6, settings.getViewingRecords());
            ps.setString(7, settings.getDeploymentLocation());
            ps.setString(8, MapperUtil.mapSetToCommaSeparatedString(settings.getCreditCards()));
            ps.setString(9, settings.getTemplateFileLocation());
            ps.setString(10, settings.getChartOutputLocation());
            ps.setString(11, settings.getReportsOutputLocation());

            ps.executeUpdate();
            con.close();
            return true;
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    private static boolean updateExistingUserSettings(UserSettings settings) {
        String query = "DELETE FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                + Tables.USER_SETTINGS + " WHERE USERNAME = '" + settings.getUsername() + "'";

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            con.close();

            insertNewUserSettings(settings);
            return true;
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    private static boolean userHasSettings(String user) {
        String query = "SELECT COUNT(*) FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                + Tables.USER_SETTINGS + " WHERE USERNAME = '" + user + "'";

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();

            int result = rs.getInt(1);
            con.close();
            return result == 1;
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }
}
