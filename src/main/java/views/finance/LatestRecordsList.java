package views.finance;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;

public class LatestRecordsList {

    private Logger logger = Logger.getLogger(LatestRecordsList.class);

    public LatestRecordsList(Integer viewingAmount, JTable table) {
        logger.debug("Displaying last " + viewingAmount + " records");

        JFrame frame = new JFrame("Past " + viewingAmount + " Records");
        frame.setIconImage(Icons.APP_ICON.getImage());

        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JLabel label = new Title("Last " + viewingAmount + " Transactions");
        panel.add(label, BorderLayout.NORTH);
        panel.add(getEntriesPane(table), BorderLayout.SOUTH);
        panel.setBorder(ApplicationLiterals.PADDED_SPACE);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private JScrollPane getEntriesPane(JTable table) {
        final JScrollPane entriesScrollPane = new JScrollPane(table);
        entriesScrollPane.setViewportView(table);
        entriesScrollPane.setVisible(true);
        Dimension d = table.getPreferredSize();
        entriesScrollPane.setPreferredSize(new Dimension(d.width * 2, table.getRowHeight() * 20));

        return entriesScrollPane;
    }
}
