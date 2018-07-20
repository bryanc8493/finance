package views.finance;

import beans.Transaction;
import literals.ApplicationLiterals;
import literals.Icons;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import persistence.finance.Transactions;
import utilities.exceptions.AppException;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CreditPayments {

    private JFrame frame = new JFrame("Credit Card Transactions");
    private Set<JCheckBox> records;
    private static Logger logger = Logger.getLogger(CreditPayments.class);

    public CreditPayments() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        JLabel label = new Title("Select Paid Credit Card Transactions");

        JButton update = new PrimaryButton("Mark As Paid");
        JButton close = new PrimaryButton("Close");
        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER));
        button.add(close);
        button.add(update);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(label, BorderLayout.NORTH);
        p.add(getCreditPanel(), BorderLayout.CENTER);
        p.add(button, BorderLayout.SOUTH);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.add(p);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        close.addActionListener((e) -> frame.dispose());

        update.addActionListener(e ->  {
            Set<Transaction> credits = new HashSet<>();

            for (JCheckBox box : records) {
                if (box.isSelected()) {
                    Transaction t = new Transaction();
                    String boxText = box.getText();
                    String idString = boxText.substring(boxText.indexOf("(")+1, boxText.indexOf(")"));
                    t.setTransactionID(idString);
                    credits.add(t);
                }
            }

            // Call query to mark selected credits as paid
            logger.debug("Marking " + credits.size() + " credit transactions as paid");
            Transactions.markCreditsPaid(credits);
            frame.dispose();
        });
    }

    private JPanel getCreditPanel() {
        records = new LinkedHashSet<>();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        String SQL_TEXT = "SELECT count(*) from " + Databases.FINANCIAL
                + ApplicationLiterals.DOT + Tables.MONTHLY_TRANSACTIONS
                + " where CREDIT = '1' AND CREDIT_PAID = '0'";
        Statement statement;
        ResultSet rs;

        try {
            Connection con = Connect.getConnection();
            statement = con.createStatement();
            rs = statement.executeQuery(SQL_TEXT);
            rs.next();

            SQL_TEXT = "SELECT TRANSACTION_ID, TITLE, CATEGORY, TRANSACTION_DATE, AMOUNT, CARD_USED "
                    + "from " + Databases.FINANCIAL + ApplicationLiterals.DOT
                    + Tables.MONTHLY_TRANSACTIONS
                    + " where CREDIT = '1' AND CREDIT_PAID = '0'";
            rs = statement.executeQuery(SQL_TEXT);

            while (rs.next()) {
                String id = rs.getString(1);
                String title = rs.getString(2);
                String category = rs.getString(3);
                String date = rs.getString(4);
                String amount = rs.getString(5);
                String card = rs.getString(6);

                JCheckBox box = new JCheckBox();
                box.setText("(" + id + ") " + title + "  |  " + category + "  |  "
                        + date + "  |  " + card + "  |  " + amount);
                records.add(box);
            }
            for (JCheckBox x : records) {
                panel.add(x);
            }

            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        } catch (Exception e) {
            throw new AppException(e);
        }
        return panel;
    }
}
