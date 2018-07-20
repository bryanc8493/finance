package persistence.finance;

import beans.MonthlyRecord;
import beans.Transaction;
import beans.UpdatedRecord;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import utilities.exceptions.AppException;
import utilities.DateUtility;

import javax.swing.*;
import java.sql.*;
import java.util.List;
import java.util.Set;

public class Transactions {

    private static Logger logger = Logger.getLogger(Transactions.class);

    public static Object[][] getPastEntries(int entriesToRetrieve) {
        final Connection con = Connect.getConnection();
        Object[][] records = new Object[entriesToRetrieve][5];
        logger.debug("Getting past " + entriesToRetrieve + " transaction records...");

        String SQL_TEXT = "SELECT TRANSACTION_ID, TITLE, TYPE, TRANSACTION_DATE, AMOUNT FROM "
                + Databases.FINANCIAL + ApplicationLiterals.DOT + Tables.MONTHLY_TRANSACTIONS
                + " ORDER BY TRANSACTION_ID desc limit " + entriesToRetrieve;
        Statement statement;
        ResultSet rs;
        int recordCount = 0;

        try {
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);

            while (rs.next()) {
                for (int i=0; i<5; i++) {
                    records[recordCount][i] = rs.getString(i+1);
                }
                recordCount++;
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }
        return records;
    }

    public static Object[][] getMonthlySummaries() {
        logger.debug("Generating Monthly Summaries...");
        final Connection con = Connect.getConnection();
        Object[][] records = new Object[DateUtility.getMonthsSinceJan2016()][5];

        String SQL_TEXT = "SELECT MONTH, YEAR, TOTAL_EXPENSES, TOTAL_INCOME, MONTHLY_CASH_FLOW FROM "
                + Databases.FINANCIAL + ApplicationLiterals.DOT + Tables.MONTHLY_TOTALS;
        Statement statement;
        ResultSet rs;
        int recordCount = 0;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);

            while (rs.next()) {
                records[recordCount][0] = rs.getString(1);
                records[recordCount][1] = rs.getString(2);
                records[recordCount][2] = "$  " + rs.getString(3);
                records[recordCount][3] = "$  " + rs.getString(4);
                String tempCashFlow = rs.getString(5);
                if (Double.parseDouble(tempCashFlow) >= 0.00) {
                    records[recordCount][4] = "$  " + tempCashFlow;
                } else {
                    records[recordCount][4] = "$  ( " + tempCashFlow + " )";
                }

                recordCount++;
            }
        } catch (SQLException e) {
            throw new AppException(e);
        }
        return records;
    }

    public static Transaction getSpecifiedTransaction(String tranId) {
        String SQL_TEXT = "SELECT TRANSACTION_ID, TITLE, TYPE, CATEGORY, TRANSACTION_DATE, AMOUNT, DESCRIPTION, CREDIT, CREDIT_PAID, CARD_USED "
                + "FROM " + Databases.FINANCIAL + ApplicationLiterals.DOT
                + Tables.MONTHLY_TRANSACTIONS + " WHERE TRANSACTION_ID = " + tranId;
        Statement statement;
        ResultSet rs;
        Transaction tran = new Transaction();
        try {
            Connection con = Connect.getConnection();
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);
            while (rs.next()) {
                tran.setTransactionID(rs.getString(1));
                tran.setTitle(rs.getString(2));
                tran.setType(rs.getString(3));
                tran.setCategory(rs.getString(4));
                tran.setDate(rs.getString(5));
                tran.setAmount(rs.getString(6));
                tran.setDescription(rs.getString(7));
                tran.setCredit(rs.getString(8).charAt(0));
                tran.setCreditPaid(rs.getString(9).charAt(0));
                tran.setCreditCard(rs.getString(10));
            }
            con.close();
        } catch (StringIndexOutOfBoundsException e) {
            tran.setCreditPaid(' ');
        } catch (SQLException sqlE) {
            throw new AppException(sqlE);
        }
        return tran;
    }

    public static void markCreditsPaid(Set<Transaction> records) {
        try {
            Connection con = Connect.getConnection();

            PreparedStatement ps;
            String SQL_TEXT = "UPDATE " + Databases.FINANCIAL + ApplicationLiterals.DOT
                    + Tables.MONTHLY_TRANSACTIONS + " SET CREDIT_PAID = '1' "
                    + "where TRANSACTION_ID = ?";

            for (Transaction t : records) {
                ps = con.prepareStatement(SQL_TEXT);
                ps.setString(1, t.getTransactionID());
                ps.executeUpdate();
            }

            con.close();
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private static int getFutureRecordCount() {
        String SQL_TEXT = "SELECT COUNT(*) FROM " + Databases.FINANCIAL
                + ApplicationLiterals.DOT + Tables.MONTHLY_TRANSACTIONS
                + " WHERE TRANSACTION_DATE > now()";

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public static Object[][] getFutureRecords() {
        Object[][] data = new Object[getFutureRecordCount()][5];
        String SQL_TEXT = "SELECT TITLE, TYPE, CATEGORY, TRANSACTION_DATE, AMOUNT "
                + "FROM " + Databases.FINANCIAL
                + ApplicationLiterals.DOT + Tables.MONTHLY_TRANSACTIONS
                + " WHERE TRANSACTION_DATE > now()";
        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            int count = 0;

            while (rs.next()) {
                data[count][0] = rs.getString(1);
                data[count][1] = rs.getString(2);
                data[count][2] = rs.getString(3);
                data[count][3] = rs.getString(4);
                if(data[count][1].toString().equalsIgnoreCase("Expense")) {
                    data[count][4] = "( -" + rs.getString(5) + " )";
                }else{
                    data[count][4] = rs.getString(5);
                }

                count++;
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return data;
    }

    private static int getNumberOfUnpaidCredits() {
        String SQL_TEXT = "SELECT COUNT(*) FROM " + Databases.FINANCIAL
                + ApplicationLiterals.DOT + Tables.MONTHLY_TRANSACTIONS
                + " where CREDIT = '1' AND CREDIT_PAID = '0'";

        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public static Object[][] getUnpaidCreditRecords() {
        Object[][] data = new Object[getNumberOfUnpaidCredits()][5];
        String SQL_TEXT = "SELECT TITLE, CATEGORY, TRANSACTION_DATE, AMOUNT, CARD_USED "
                + "FROM " + Databases.FINANCIAL
                + ApplicationLiterals.DOT + Tables.MONTHLY_TRANSACTIONS
                + " where CREDIT = '1' AND CREDIT_PAID = '0'";
        try {
            Connection con = Connect.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);
            int count = 0;

            while (rs.next()) {
                for (int i=0; i<5; i++) {
                    data[count][i] = rs.getString(i+1);
                }
                count++;
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return data;
    }

    public static void addTransaction(Transaction tran) {
        try {
            Connection con = Connect.getConnection();

            PreparedStatement ps;
            String SQL_TEXT = "INSERT INTO " + Databases.FINANCIAL + ApplicationLiterals.DOT
                    + Tables.MONTHLY_TRANSACTIONS  + " (TITLE, TYPE, CATEGORY, TRANSACTION_DATE, "
                    + "AMOUNT, DESCRIPTION, CREDIT, CREDIT_PAID, CARD_USED) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(SQL_TEXT);
            ps.setString(1, tran.getTitle());
            ps.setString(2, tran.getType());
            ps.setString(3, tran.getCategory());
            ps.setString(4, tran.getDate());
            ps.setString(5, tran.getAmount());
            ps.setString(6, tran.getDescription());
            ps.setString(7, String.valueOf(tran.getCredit()));
            ps.setString(8, String.valueOf(tran.getCreditPaid()));
            ps.setString(9, tran.getCreditCard());
            ps.executeUpdate();

            if(tran.getCategory().equals(ApplicationLiterals.SAVINGS) ||
                    tran.getCategory().equals(ApplicationLiterals.SAVINGS_TRANSFER) ||
                    tran.getCategory().equals(ApplicationLiterals.HOUSE_SAVINGS))
                addSavingsTransaction(tran, con);

            con.close();
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private static void addSavingsTransaction(Transaction tran, Connection con) {
        try {
            PreparedStatement ps;
            String SQL_TEXT = "INSERT INTO " + Databases.FINANCIAL + ApplicationLiterals.DOT
                    + Tables.SAVINGS  + " (TRANS_TYPE, TRANS_DATE, AMOUNT, DESCRIPTION, SUM_AMOUNT) "
                    + "VALUES (?, ?, ?, ?, ?)";

            String sumAmount = tran.getType().equals(ApplicationLiterals.INCOME) ?
                    "-" + tran.getAmount() :
                    tran.getAmount();
            String tranType = tran.getType().equals(ApplicationLiterals.EXPENSE) ?
                    ApplicationLiterals.INCOME :
                    ApplicationLiterals.EXPENSE;

            ps = con.prepareStatement(SQL_TEXT);
            ps.setString(1, tranType);
            ps.setString(2, tran.getDate());
            ps.setString(3, tran.getAmount());
            ps.setString(4, tran.getDescription());
            ps.setString(5, sumAmount);

            ps.executeUpdate();

            if(tran.getCategory().equals(ApplicationLiterals.HOUSE_SAVINGS))
                addHouseSavings(con);

        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private static void addHouseSavings(Connection con) {
        try {
            PreparedStatement ps;
            String SQL_TEXT = "INSERT INTO " + Databases.FINANCIAL + ApplicationLiterals.DOT
                    + Tables.HOUSE_SAVINGS  + " (SAVINGS_TRAN_ID) "
                    + "VALUES (?)";

            int tranId = getLastSavingsRecordId();

            ps = con.prepareStatement(SQL_TEXT);
            ps.setInt(1, tranId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private static int getLastSavingsRecordId() {
        Statement statement;
        ResultSet rs ;

        String sql = "SELECT MAX(TRANS_ID) FROM " + Databases.FINANCIAL +
                ApplicationLiterals.DOT + Tables.SAVINGS;
        try {
            Connection con = Connect.getConnection();

            statement = con.createStatement();
            rs = statement.executeQuery(sql);
            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static void insertMonthlySummaryData(MonthlyRecord record) {
        String insertSQL = "insert into "
                + Databases.FINANCIAL + ApplicationLiterals.DOT + Tables.MONTHLY_TOTALS
                + " (YEAR, MONTH_INT, MONTH, TOTAL_EXPENSES, TOTAL_INCOME, MONTHLY_CASH_FLOW) "
                + "values (?, ?, ?, ?, ?, ?)";

        try {
            Connection con = Connect.getConnection();
            PreparedStatement ps = con.prepareStatement(insertSQL);
            ps.setInt(1, record.getYear());
            ps.setInt(2, record.getMonthInt());
            ps.setString(3, record.getMonth());
            ps.setDouble(4, record.getExpenses());
            ps.setDouble(5, record.getIncome());
            ps.setDouble(6, record.getCashFlow());
            ps.execute();

            JOptionPane.showMessageDialog(null,
                    "Data successfully added for " + record.getMonth() + ", " + record.getYear()
                            + ApplicationLiterals.NEW_LINE + ApplicationLiterals.NEW_LINE
                            + "Total Income:             $" + record.getIncome()
                            + ApplicationLiterals.NEW_LINE
                            + "Total Expenses:       $" + record.getExpenses()
                            + ApplicationLiterals.NEW_LINE
                            + "Cash Flow:                 $" + record.getCashFlow(),
                    "Monthly Summary", JOptionPane.INFORMATION_MESSAGE);

        } catch (MySQLIntegrityConstraintViolationException pk) {
            JOptionPane.showMessageDialog(null,
                    "Data already exists for "
                            + record.getMonth() + " " + record.getYear() + "!",
                    "Duplicate Selection", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            throw new AppException(e);
        }
    }

    public static void changeTransactions(List<UpdatedRecord> updates) {
        logger.debug("Updating address changes..");
        final Connection con = Connect.getConnection();
        for (UpdatedRecord a : updates) {
            String query = "UPDATE " + Databases.FINANCIAL
                    + ApplicationLiterals.DOT + Tables.MONTHLY_TRANSACTIONS
                    + " set attr = ? where TRANSACTION_ID = ?";
            query = query.replace("attr", a.getAttribute());
            try {
                PreparedStatement preparedStmt = con.prepareStatement(query);
                preparedStmt.setString(1, a.getData());
                preparedStmt.setString(2, a.getID());
                preparedStmt.executeUpdate();
            } catch (SQLException e) {
                throw new AppException(e);
            }
        }
        int updatedCount = updates.size();
        logger.debug("Made " + updatedCount + " updates");
        JOptionPane.showMessageDialog(null, "Successfully updated "
                        + updatedCount + " transaction records", "Updated!",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void deleteTransaction(Transaction tran) {
        logger.debug("Deleting Transactions...");
        final Connection con = Connect.getConnection();

        String query = "DELETE from " + Tables.MONTHLY_TRANSACTIONS
                + " WHERE TRANSACTION_ID = " + tran.getTransactionID();
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new AppException(e);
        }

        logger.debug("Deleted transaction");
        JOptionPane.showMessageDialog(null,
                "Deleted record successfully",
                "Deleted!",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
