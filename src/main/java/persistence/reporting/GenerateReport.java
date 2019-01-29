package persistence.reporting;

import domain.beans.UserSettings;
import domain.dto.CategorySummary;
import domain.dto.MonthlyRecord;
import domain.beans.ReportRecord;
import literals.ApplicationLiterals;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import persistence.finance.BalanceData;
import persistence.finance.Transactions;
import utilities.exceptions.AppException;
import utilities.settings.SettingsService;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.List;

public class GenerateReport {

    private static final String DATE = "TRANSACTION_DATE";
    private static Logger logger = Logger.getLogger(GenerateReport.class);

    public static List<ReportRecord> createMonthlyReport(int year, int month) {

        Connection con;
        try {
            con = Connect.getConnection();
        } catch (Exception e1) {
            throw new AppException(e1);
        }

        logger.debug("Creating monthly report for " + year + " " + month);

        // if month is between 1-9 add a leading 0 before month in the query
        String monthPrefix = "";
        if (month < 10) {
            monthPrefix = "0";
        }

        updateMonthlySummaryTable(year, month, monthPrefix + month);

        String SQL_TEXT = "select * FROM " + Tables.MONTHLY_TRANSACTIONS
                + " WHERE " + DATE + " like '" + year + "-" + monthPrefix
                + month + "%' order by " + DATE + " ASC";

        Statement statement = null;
        ResultSet rs = null;
        logger.debug("Running report query: " + SQL_TEXT);
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);
        } catch (SQLException e) {
            logger.error(e.toString() + Arrays.toString(e.getStackTrace()));
        }

        List<ReportRecord> data = new ArrayList<ReportRecord>();
        try {
            while (rs.next()) {
                ReportRecord record = new ReportRecord();
                record.setID(rs.getInt(1));
                record.setTitle(rs.getString(2));
                record.setType(rs.getString(3));
                record.setCategory(rs.getString(4));
                record.setDate(rs.getString(5));
                record.setAmount(rs.getDouble(6));
                record.setCombinedAmount(rs.getDouble(7));
                record.setDescription(rs.getString(8));
                data.add(record);
            }
            con.close();
        } catch (SQLException e) {
            logger.error(e.toString() + Arrays.toString(e.getStackTrace()));
        }
        return data;
    }

    public static List<ReportRecord> createCustomReport(String start, String end) {
        Connection con = Connect.getConnection();

        logger.debug("Creating custom report for period from " + start + " to "
                + end);
        String SQL_TEXT = "select * FROM " + Tables.MONTHLY_TRANSACTIONS
                + " WHERE " + DATE + " between '" + start + "' and '" + end
                + "' order by " + DATE + " ASC";
        Statement statement = null;
        ResultSet rs = null;
        logger.debug("Running custom report query: " + SQL_TEXT);
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);
        } catch (SQLException e) {
            logger.error(e.toString() + Arrays.toString(e.getStackTrace()));
        }

        List<ReportRecord> data = new ArrayList<ReportRecord>();
        try {
            while (rs.next()) {
                ReportRecord record = new ReportRecord();
                record.setID(rs.getInt(1));
                record.setTitle(rs.getString(2));
                record.setType(rs.getString(3));
                record.setCategory(rs.getString(4));
                record.setDate(rs.getString(5));
                record.setAmount(rs.getDouble(6));
                record.setCombinedAmount(rs.getDouble(7));
                record.setDescription(rs.getString(8));
                data.add(record);
            }
            con.close();
        } catch (SQLException e) {
            logger.error(e.toString() + Arrays.toString(e.getStackTrace()));
        }
        return data;
    }

    private static void updateMonthlySummaryTable(int year, int month, String monthSQL) {
        MonthlyRecord record = new MonthlyRecord();

        record.setYear(year);
        record.setMonth(month);
        record.setMonthInt(month);

        record.setExpenses(BalanceData.getMonthlyExpenseSum(year, monthSQL));
        record.setIncome(BalanceData.getMonthlyIncomeSum(year, monthSQL));
        record.setCashFlow(record.getIncome(), record.getExpenses());

        Transactions.insertMonthlySummaryData(record);
    }

    public static Map<String, List<CategorySummary>> categoryReportT(
            String start, String end) {
        Connection con = Connect.getConnection();

        String sql = "select * from "
                + Tables.MONTHLY_EXP_CATGR
                + " where MONTH between '"
                + start
                + "' and '"
                + end
                + "' "
                + "AND not CATEGORY IN ('Cable', 'Insurance - Auto', 'Insurance - Life', "
                + "'Internet', 'Mortgage/Rent', 'Student Loans', 'Services/Membership', 'Savings', 'Wedding') "
                + "order by MONTH, CATEGORY ASC";
        Statement statement = null;
        ResultSet rs = null;
        Map<String, List<CategorySummary>> dataset = new LinkedHashMap<String, List<CategorySummary>>();

        try {
            statement = con.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                String month = rs.getString(1);
                String category = rs.getString(2);
                Double cost = rs.getDouble(3);

                CategorySummary categoryAndCost = new CategorySummary(category,
                        cost);
                List<CategorySummary> x = dataset.get(month);
                if (x == null) {
                    dataset.put(
                            month,
                            new ArrayList<CategorySummary>(Arrays
                                    .asList(categoryAndCost)));
                } else {
                    x.add(categoryAndCost);
                    dataset.put(month, x);
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, List<CategorySummary>> map : dataset.entrySet()) {
            Set<String> distinctCategories = getDistinctCategories(dataset);
            dataset.put(map.getKey(),
                    addMissingCategories(map.getValue(), distinctCategories));
        }

        return dataset;
    }

    private static List<CategorySummary> addMissingCategories(
            List<CategorySummary> list, Set<String> set) {

        List<String> originalCategories = new ArrayList<String>();
        for (CategorySummary c : list) {
            originalCategories.add(c.getCategory());
        }
        set.removeAll(originalCategories);

        for (String x : set) {
            CategorySummary c = new CategorySummary(x, 0);
            list.add(c);
        }

        Collections.sort(list, new Comparator<CategorySummary>() {
            @Override
            public int compare(CategorySummary o1, CategorySummary o2) {
                return o1.getCategory().compareTo(o2.getCategory());
            }
        });
        return list;
    }

    public static Map<String, String> categoryReport(String start, String end) {
        Connection con = Connect.getConnection();

        String sql = "select * from " + Tables.MONTHLY_EXP_CATGR
                + " where MONTH between '" + start + "' and '" + end + "' "
                + "order by MONTH, CATEGORY ASC";
        Statement statement = null;
        ResultSet rs = null;
        Map<String, String> dataset = new LinkedHashMap<String, String>();
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                String month = rs.getString(1);
                String category = rs.getString(2);
                Double cost = rs.getDouble(3);

                String merged = month + ApplicationLiterals.COMMA
                        + String.valueOf(cost);
                String existing = dataset.get(category);
                if (existing == null) {
                    dataset.put(category, merged);
                } else {
                    existing = existing + ApplicationLiterals.COMMA + merged;
                    dataset.put(category, existing);
                }
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private static String readTemplateFile() {
        UserSettings settings = SettingsService.getCurrentUserSettings();

        String fileAsString = "";
        try {
            InputStream is = new FileInputStream(settings.getTemplateFileLocation());
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                sb.append(line).append(ApplicationLiterals.NEW_LINE);
                line = buf.readLine();
            }

            fileAsString = sb.toString();
            buf.close();
            is.close();
        } catch (Exception e) {
            throw new AppException(e);
        }
        return fileAsString;
    }

    public static void createCategoryChart(
            Map<String, List<CategorySummary>> data) {

        String contents = readTemplateFile();

        contents = contents.replace("${TITLE}", "Expenses By Category");
        contents = contents.replace("${SUBTITLE}", "Month by Month");

        // Generate String for creating category columns, and arrays for holding
        // data
        String columns = "data.addColumn('date', 'Date');\r\n";
        Set<String> distinctCategories = getDistinctCategories(data);

        for (String category : distinctCategories) {
            columns = columns + "data.addColumn('number', '" + category
                    + "');\r\n";
        }
        contents = contents.replace("${COLUMNS}", columns);

        String dataValues = "";
        int count = 0;
        for (Map.Entry<String, List<CategorySummary>> entry : data.entrySet()) {
            if (count == 0) {
                dataValues = "[new Date('" + entry.getKey() + "'), "
                        + commaSeparateCategoryValues(entry.getValue()) + "]";
            } else {
                dataValues = dataValues + ",\r\n[new Date('" + entry.getKey()
                        + "'), "
                        + commaSeparateCategoryValues(entry.getValue()) + "]";
            }
            count++;
        }
        contents = contents.replace("${DATA}", dataValues);

        // Write to new file
        UserSettings settings = SettingsService.getCurrentUserSettings();

        File f = new File(settings.getChartOutputLocation());
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(contents);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            throw new AppException(e);
        }

        int choice = JOptionPane.showConfirmDialog(null,
                "Report was generated successfully!"
                        + ApplicationLiterals.NEW_LINE
                        + "Do you want to view it now?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                Desktop.getDesktop().browse(f.toURI());
            } catch (IOException e) {
                throw new AppException(e);
            }
        }
    }

    private static String commaSeparateCategoryValues(List<CategorySummary> list) {

        String str = "";
        int count = 0;
        for (CategorySummary c : list) {
            if (count == 0) {
                str = String.valueOf(c.getAmount());
            } else {
                str = str + ApplicationLiterals.COMMA
                        + String.valueOf(c.getAmount());
            }
            count++;
        }
        return str;
    }

    private static Set<String> getDistinctCategories(
            Map<String, List<CategorySummary>> data) {

        Set<String> set = new TreeSet<>();
        for (Map.Entry<String, List<CategorySummary>> m : data.entrySet()) {
            for (CategorySummary c : m.getValue()) {
                set.add(c.getCategory());
            }
        }
        return set;
    }
}
