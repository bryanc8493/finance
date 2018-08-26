package persistence.salary;

import beans.Salary;
import beans.SalaryConfiguration;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import utilities.exceptions.AppException;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class SalaryData {

    private static final Logger logger = Logger.getLogger(SalaryData.class);

    public static Set<Salary> getSalaryData() {
        logger.debug("Getting all salary data");
        String SQL_TEXT = "SELECT * FROM " + Databases.FINANCIAL + ApplicationLiterals.DOT
                + Tables.PAY_GRADES;
        Set<Salary> data = new LinkedHashSet<>();

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            while (rs.next()) {
                Salary salary = new Salary();
                salary.setGrade(rs.getInt(1));
                salary.setMinPay(rs.getInt(2));
                salary.setMidPay(rs.getInt(3));
                salary.setMaxPay(rs.getInt(4));
                salary.setStiTarget(rs.getInt(5));
                salary.setStiMax(rs.getInt(6));
                salary.setMtiTarget(rs.getInt(7));
                salary.setMtiMax(rs.getInt(8));
                data.add(salary);
            }
            con.close();
        } catch (SQLException e) {
            throw new AppException(e);
        }
        return data;
    }

    public static void addSalarySetting(String user, SalaryConfiguration config) {
        logger.debug("add new salary configuration for " + user);
        String SQL_TEXT = "INSERT INTO " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                + Tables.PAY_SETTINGS + " (USER, GRADE, COMP_RATIO, STI, MTI) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_TEXT);
            ps.setString(1, user);
            ps.setInt(2, config.getGrade());
            ps.setDouble(3, config.getCompRatio());
            ps.setDouble(4, config.getSti());

            if(config.getMti() == null) {
                ps.setNull(5, 1);
            }else {
                ps.setDouble(5, config.getMti());
            }

            ps.executeUpdate();

            con.close();
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static void updateSalarySetting(String user, SalaryConfiguration config) {
        logger.debug("add new salary configuration for " + user);
        String SQL_TEXT = "UPDATE " + Databases.ACCOUNTS + ApplicationLiterals.DOT + Tables.PAY_SETTINGS +
                " SET GRADE = ?, COMP_RATIO = ?, STI = ?, MTI = ? " +
                "WHERE USER = ?";

        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_TEXT);
            ps.setInt(1, config.getGrade());
            ps.setDouble(2, config.getCompRatio());
            ps.setDouble(3, config.getSti());

            if(config.getMti() == null) {
                ps.setNull(4, 1);
            }else {
                ps.setDouble(4, config.getMti());
            }

            ps.setString(5, user);

            ps.executeUpdate();

            con.close();
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static SalaryConfiguration getSalarySettings(String user) {
        logger.debug("getting salary configuration for " + user);
        SalaryConfiguration config = new SalaryConfiguration();

        String SQL_TEXT = "SELECT GRADE, COMP_RATIO, STI, MTI "
            + "FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT + Tables.PAY_SETTINGS;
        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            while(rs.next()) {
                config.setGrade(rs.getInt(1));
                config.setCompRatio(rs.getDouble(2));
                config.setSti(rs.getDouble(3));
                config.setMti(rs.getDouble(4));
            }
            con.close();
        } catch (SQLException e) {
            throw new AppException(e);
        }
        return config;
    }

    public static boolean userSettingsExist(String user) {
        logger.debug("checking if user salary default config exists");
        String SQL_TEXT = "SELECT COUNT(*) "
                + "FROM " + Databases.ACCOUNTS + ApplicationLiterals.DOT + Tables.PAY_SETTINGS
                + " WHERE USER = '" + user + "'";
        int records;

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            rs.next();
            records = rs.getInt(1);
            con.close();
        } catch (SQLException e) {
            throw new AppException(e);
        }
        return records == 1;
    }
}
