package services;

import domain.dto.FinancingDetail;
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

    public static boolean addNewPayment(String id, Double amount) {
        return FinancingData.newPayment(id, amount);
    }

    public static List<FinancingDetail> getFinancingDetailsList() {
        return FinancingData.getFinancingDetailsData();
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
                data.setLastPaymentDate(rs.getDate(5));
                data.setUniqueId(rs.getInt(7));

                summaryData.add(data);
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return summaryData;
    }

    public static List<FinancingDetail> mapFinancingDetailData(ResultSet rs) {
        final List<FinancingDetail> detailData = new ArrayList<>();

        try {
            while (rs.next()) {
                FinancingDetail data = new FinancingDetail();
                data.setDescription(rs.getString(1));
                data.setTotal(rs.getDouble(2));
                data.setTotalPayments(rs.getDouble(3));
                data.setTotalRemaining(rs.getDouble(4));
                data.setLastPayment(rs.getDate(5));
                data.setPaidOff(rs.getInt(6) == 1);

                detailData.add(data);
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }

        return detailData;
    }

    public static List<FinancingDetail> mapSummaryToDetails(List<FinancingSummary> input) {
        List<FinancingDetail> output = new ArrayList<>();

        for (FinancingSummary data : input) {
            final FinancingDetail detail = new FinancingDetail();
            detail.setDescription(data.getTitle());
            detail.setTotal(data.getTotal());
            detail.setTotalRemaining(data.getTotal());
            detail.setPaidOff(false);

            output.add(detail);
        }

        return output;
    }

    public static Object[][] transformDataForSummaryTable(List<FinancingSummary> data) {
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

    public static Object[][] transformDataForDetailTable(List<FinancingDetail> data) {
        Object[][] tableData = new Object[data.size()][6];

        for (int i=0; i<data.size(); i++) {
            FinancingDetail currentInstance = data.get(i);

            tableData[i][0] = currentInstance.getDescription();
            tableData[i][1] = ApplicationLiterals.DOUBLE_FORMAT.format(
                    currentInstance.getTotal()
            );

            Double totalPayments = currentInstance.getTotalPayments();

            if (totalPayments == null) {
                tableData[i][2] = ApplicationLiterals.NULL_PLACEHOLDER;
            } else {
                tableData[i][2] = ApplicationLiterals.DOUBLE_FORMAT.format(
                        currentInstance.getTotalPayments()
                );
            }

            tableData[i][3] = ApplicationLiterals.DOUBLE_FORMAT.format(
                    currentInstance.getTotalRemaining()
            );
            tableData[i][4] = nullCheck(currentInstance.getLastPayment());
            tableData[i][5] = currentInstance.isPaidOff();
        }

        return tableData;
    }

    private static Object nullCheck(Object input) {
        return input != null ? input : ApplicationLiterals.NULL_PLACEHOLDER;
    }
}
