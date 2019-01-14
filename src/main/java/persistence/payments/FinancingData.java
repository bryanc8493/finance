package persistence.payments;

import domain.dto.FinancingSummary;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Views;
import org.apache.log4j.Logger;
import persistence.Connect;
import services.FinancingService;
import utilities.exceptions.AppException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class FinancingData {

    private static Logger logger = Logger.getLogger(FinancingData.class);

    public static List<FinancingSummary> getFinancingSummaryData() {
        logger.debug("Getting all financing summary data");

        String query = "SELECT * FROM " + Databases.FINANCIAL
            + ApplicationLiterals.DOT + Views.FINANCED
            + " WHERE PAID_OFF = '0'";

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            List<FinancingSummary> data = FinancingService.mapFinancingSummaryData(rs);
            con.close();
            return data;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}
