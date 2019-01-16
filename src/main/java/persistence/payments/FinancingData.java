package persistence.payments;

import domain.beans.Transaction;
import domain.dto.FinancingPurchase;
import domain.dto.FinancingSummary;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import literals.enums.Views;
import org.apache.log4j.Logger;
import persistence.Connect;
import persistence.finance.Transactions;
import services.FinancingService;
import utilities.exceptions.AppException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class FinancingData {

    private static Logger logger = Logger.getLogger(FinancingData.class);

    public static List<FinancingSummary> getFinancingSummaryData() {
        logger.debug("Getting all financing summary data");

        List<FinancingSummary> unpaidPurchases = getNewPurchases();

        String query = "SELECT * FROM " + Databases.FINANCIAL
            + ApplicationLiterals.DOT + Views.FINANCED
            + " WHERE PAID_OFF = '0'";

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            List<FinancingSummary> data = FinancingService.mapFinancingSummaryData(rs);
            unpaidPurchases.addAll(data);
            con.close();
            return unpaidPurchases;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private static List<FinancingSummary> getNewPurchases() {
        logger.debug("Getting all new financing purchases");

        String query = "SELECT * FROM " + Databases.FINANCIAL
                + ApplicationLiterals.DOT + Views.NEW_FINANCED_PURCHASES;

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            List<FinancingSummary> data = FinancingService.mapFinancingPurchaseData(rs);
            con.close();
            return data;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public static boolean newPurchase(FinancingPurchase purchase) {
        logger.debug("Adding new financed purchase");

        String query = "INSERT INTO " + Databases.FINANCIAL + ApplicationLiterals.DOT
                + Tables.FINANCED_PURCHASES + " (DATE, TOTAL_AMOUNT, STORE, DESCRIPTION) "
                + "VALUES (?, ?, ?, ?)";

        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            Date convertedDate = new Date(purchase.getDate().getTime());
            ps.setDate(1, convertedDate);
            ps.setDouble(2, purchase.getAmount());
            ps.setString(3, purchase.getStore());
            ps.setString(4, purchase.getDescription());
            ps.executeUpdate();

            con.close();
            return true;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public static boolean newPayment(String id, Double amount) {
        logger.debug("Adding new financed payment");

        Transaction transaction = new Transaction();
        transaction.setTitle("financed payment");
        transaction.setDescription("financed payment");
        transaction.setDate(ApplicationLiterals.YEAR_MONTH_DAY.format(new java.util.Date()));
        transaction.setType(ApplicationLiterals.EXPENSE);
        transaction.setCategory("Financed Payment");
        transaction.setAmount(String.valueOf(amount));

        Transactions.addTransaction(transaction);

        final int lastTransactionId = getLastTransactionId();

        String query = "INSERT INTO " + Databases.FINANCIAL + ApplicationLiterals.DOT
            + Tables.FINANCED_PAYMENTS + " (PURCHASE_ID, TRANSACTION_ID, DATE, AMOUNT) "
            + "VALUES (?, ?, ?, ?)";

        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(id));
            ps.setInt(2, lastTransactionId);
            ps.setDate(3, new Date(new java.util.Date().getTime()));
            ps.setDouble(4, amount);

            ps.executeUpdate();
            con.close();
            return true;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private static Integer getLastTransactionId() {
        String query = "SELECT MAX(TRANSACTION_ID) FROM " + Databases.FINANCIAL
            + ApplicationLiterals.DOT + Tables.MONTHLY_TRANSACTIONS;

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            rs.next();
            final int maxId = rs.getInt(1);
            con.close();
            return maxId;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}
