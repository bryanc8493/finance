package persistence.payments;

import domain.dto.FinancingPurchase;
import domain.dto.FinancingSummary;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import literals.enums.Views;
import org.apache.log4j.Logger;
import persistence.Connect;
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
}
