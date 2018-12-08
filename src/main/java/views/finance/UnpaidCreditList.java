package views.finance;

import literals.Icons;
import org.apache.log4j.Logger;
import persistence.finance.Transactions;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;

public class UnpaidCreditList {

    private Logger logger = Logger.getLogger(UnpaidCreditList.class);
    private Object[] columns = { "Title", "Category", "Date", "Amount", "Card Used" };

    public UnpaidCreditList() {
        logger.debug("Displaying unpaid credit records");

        JFrame frame = new JFrame("Unpaid Credits");
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JLabel title = new Title("Unpaid Credit Card Transactions");

        panel.add(title, BorderLayout.NORTH);
        panel.add(getCreditRecordsPane(columns), BorderLayout.SOUTH);

        frame.add(panel);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static JScrollPane getCreditRecordsPane(Object[] headerColumns) {
        JTable table = new JTable(Transactions.getUnpaidCreditRecords(), headerColumns);
        JScrollPane sp = new JScrollPane(table);
        sp.setViewportView(table);
        sp.setVisible(true);
        Dimension d = table.getPreferredSize();
        sp.setPreferredSize(new Dimension(d.width * 2, table.getRowHeight() * 15));

        return sp;
    }
}
