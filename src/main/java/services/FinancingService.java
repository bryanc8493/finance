package services;

import domain.dto.FinancingPurchase;
import domain.dto.FinancingSummary;
import literals.ApplicationLiterals;
import persistence.payments.FinancingData;
import utilities.exceptions.AppException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinancingService {

    public static List<FinancingSummary> getFinancingSummaryList() {
        return FinancingData.getFinancingSummaryData();
    }

    public static boolean addNewPurchase(FinancingPurchase purchase) {
        return FinancingData.newPurchase(purchase);
    }

    public static List<FinancingSummary> mapFinancingPurchaseData(ResultSet rs) {
        final List<FinancingSummary> summaryData = new ArrayList<>();

        try {
            while (rs.next()) {
                FinancingSummary data = new FinancingSummary();
                data.setTitle(rs.getString(1));
                data.setTotal(rs.getDouble(2));
                data.setRemaining(rs.getDouble(2));
                data.setUniqueId(rs.getInt(3));

                summaryData.add(data);
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return summaryData;
    }

    public static List<FinancingSummary> mapFinancingSummaryData(ResultSet rs) {
        final List<FinancingSummary> summaryData = new ArrayList<>();

        try {
            while (rs.next()) {
                FinancingSummary data = new FinancingSummary();
                data.setTitle(rs.getString(1));
                data.setTotal(rs.getDouble(2));
                data.setRemaining(rs.getDouble(4));
                data.setUniqueId(rs.getInt(7));

                summaryData.add(data);
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return summaryData;
    }

    public static Object[][] transformDataForTable(List<FinancingSummary> data) {
        Object[][] tableData = new Object[data.size()][4];

        for (int i=0; i<data.size(); i++) {
            FinancingSummary currentInstance = data.get(i);

            tableData[i][0] = currentInstance.getUniqueId();
            tableData[i][1] = currentInstance.getTitle();
            tableData[i][2] = ApplicationLiterals.DOUBLE_FORMAT.format(
                    currentInstance.getTotal()
            );
            tableData[i][3] = ApplicationLiterals.DOUBLE_FORMAT.format(
                    currentInstance.getRemaining()
            );
        }

        return tableData;
    }
}
