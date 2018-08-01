package persistence.finance;

import literals.ApplicationLiterals;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import utilities.exceptions.AppException;
import utilities.DateUtility;

import javax.swing.*;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

public class InvestmentData {

    private static Logger logger = Logger.getLogger(InvestmentData.class);

    public static void updateInvestmentAccount(Connection con, String account,
                                               String balance) {
        logger.debug("Updating " + account + " invesment account to " + balance);
        PreparedStatement ps = null;
        balance = balance.replace(ApplicationLiterals.DOLLAR,
                ApplicationLiterals.EMPTY).replace(ApplicationLiterals.COMMA,
                ApplicationLiterals.EMPTY);
        Double amount = Double.parseDouble(balance);
        account = account.toUpperCase();
        String sql = "INSERT into " + Tables.INVESTMENTS
                + " (ACCOUNT_NAME, DATE, BALANCE) " + "Values(\"" + account
                + "\", \"" + DateUtility.getToday() + "\", \"" + amount + "\");";
        try {
            logger.debug("Investment Update Query: " + sql);
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static void getLatestFidelityBalance(Connection con) {
        logger.debug("Getting latest Fidelity Balance...");
        double balance = 0.0;
        String date = null;
        String SQL_TEXT = "select t1.DATE, t1.BALANCE from "
                + Tables.INVESTMENTS + " t1"
                + " where ACCOUNT_NAME = 'FIDELITY'"
                + " and t1.DATE = (select MAX(t2.DATE)" + " from "
                + Tables.INVESTMENTS + " t2"
                + " where t2.ACCOUNT_NAME = 'FIDELITY')";
        Statement statement;
        ResultSet rs;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);

            while (rs.next()) {
                date = rs.getString(1);
                balance = rs.getDouble(2);
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat
                .getCurrencyInstance(locale);
        String strBal = currencyFormatter.format(balance);
        JOptionPane.showMessageDialog(null,
                "<html>The most recent Fidelity 401k account balance details:<br><ul><li>"
                        + date + ":&emsp;<b>" + strBal + "</b></html>",
                "Fidelity Balance", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void getLatestJanusBalance(Connection con) {
        logger.debug("Getting latest Janus Balance...");
        double balance = 0.0;
        String date = null;

        String SQL_TEXT = "select t1.DATE, t1.BALANCE from "
                + Tables.INVESTMENTS + " t1" + " where ACCOUNT_NAME = 'JANUS'"
                + " and t1.DATE = (select MAX(t2.DATE)" + " from "
                + Tables.INVESTMENTS + " t2"
                + " where t2.ACCOUNT_NAME = 'JANUS')";
        Statement statement;
        ResultSet rs;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);

            while (rs.next()) {
                date = rs.getString(1);
                balance = rs.getDouble(2);
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String strBal = currencyFormatter.format(balance);
        JOptionPane.showMessageDialog(null,
                "<html>The most recent Janus Investment Accounts balance details:<br><ul><li>"
                        + date + ":&emsp;<b>" + strBal + "</b></html>",
                "Janus Balance", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Map<LocalDate, Double> getInvestmentData(String account) {
        logger.debug("Getting dates for investments");

        Map<LocalDate, Double> dates = new LinkedHashMap<>();

        String SQL_TEXT = "SELECT DATE, BALANCE FROM "
                + Tables.INVESTMENTS + " where ACCOUNT_NAME = '" + account.toUpperCase() + "'";
        Statement statement;
        ResultSet rs;
        Connection con;

        try {
            con = Connect.getConnection();

            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);

            while (rs.next()) {
                String dateString = rs.getString(1);
                Double amount = rs.getDouble(2);

                LocalDate date = LocalDate.parse(dateString, ApplicationLiterals.DATABASE_DATE);
                dates.put(date, amount);
            }

            con.close();
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return dates;
    }
}
