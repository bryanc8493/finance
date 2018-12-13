package persistence.settings;

import domain.beans.SystemSettings;
import domain.beans.UserSettings;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
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

        if (userHasSettings(user)) {
            query = "SELECT * FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                    + Tables.USER_SETTINGS + " WHERE USERNAME = '" + user + "'";
        } else {
            query = "SELECT * FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                    + Tables.USER_SETTINGS + " WHERE USERNAME = 'DEFAULT'";
        }

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            UserSettings userSettings = SettingsService.mapUserSettings(rs);
            con.close();
            return userSettings;
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
