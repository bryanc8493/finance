package views.finance;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.finance.Transactions;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;

public class FutureBalanceList {

    private Logger logger = Logger.getLogger(FutureBalanceList.class);
    private Object[] columns = { "Title", "Type", "Category", "Transaction_Date", "Amount" };

    public FutureBalanceList() {
        logger.debug("Displaying future transaction records");

        JFrame frame = new JFrame("Future Transactions");

        JPanel panel = new JPanel(new BorderLayout(10, 0));

        JLabel label = new Title("Future Transactions");

        panel.add(label, BorderLayout.NORTH);
        panel.add(getFutureRecordsPane(columns), BorderLayout.SOUTH);
        panel.setBorder(ApplicationLiterals.PADDED_SPACE);

        frame.add(panel);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private JScrollPane getFutureRecordsPane(Object[] columns) {
        JTable table = new JTable(Transactions.getFutureRecords(), columns);
        JScrollPane sp = new JScrollPane(table);
        sp.setViewportView(table);
        sp.setVisible(true);
        Dimension d = table.getPreferredSize();
        sp.setPreferredSize(new Dimension((d.width * 2) - 150, table
                .getRowHeight() * 10));

        return sp;
    }
}
