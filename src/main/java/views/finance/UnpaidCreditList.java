package views.finance;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.finance.BalanceData;
import persistence.finance.Transactions;
import utilities.CommonConfigValues;
import utilities.StringUtility;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UnpaidCreditList {

    private Logger logger = Logger.getLogger(UnpaidCreditList.class);
    private Object[] columns = { "Title", "Category", "Date", "Amount", "Card Used" };

    public UnpaidCreditList() {
        logger.debug("Displaying unpaid credit records");

        JFrame frame = new JFrame("Unpaid Credits");
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JLabel title = new Title("Unpaid Credit Card Transactions");

        panel.add(title, BorderLayout.NORTH);
        panel.add(getCreditRecordsPane(columns), BorderLayout.CENTER);
        panel.add(generateCreditCardsSummaryPanel(), BorderLayout.SOUTH);
        panel.setBorder(ApplicationLiterals.PADDED_SPACE);

        frame.add(panel);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private JScrollPane getCreditRecordsPane(Object[] headerColumns) {
        JTable table = new JTable(Transactions.getUnpaidCreditRecords(), headerColumns);
        JScrollPane sp = new JScrollPane(table);
        sp.setViewportView(table);
        sp.setVisible(true);
        Dimension d = table.getPreferredSize();
        sp.setPreferredSize(new Dimension(d.width * 2, table.getRowHeight() * 15));

        return sp;
    }

    private JPanel generateCreditCardsSummaryPanel() {
        Set<String> cards = CommonConfigValues.getCreditCards();
        Map<String, Double> cardData = getCreditCardsSummary(cards);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));

        for (Map.Entry<String, Double> card : cardData.entrySet()) {
            cardsPanel.add(new JLabel(card.getKey()));

            Double amount = card.getValue();
            amountPanel.add(new JLabel(StringUtility.formatAsCurrency(amount)));
        }

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25,0));
        panel.add(cardsPanel);
        panel.add(amountPanel);
        panel.setBorder(BorderFactory.createEmptyBorder(10,2,10,0));

        return panel;
    }

    private Map<String, Double> getCreditCardsSummary(Set<String> cards) {
        Map<String, Double> cardData = new HashMap<>();

        for (String card : cards) {
            Double amount = BalanceData.getOutstandingCredit(card);
            cardData.put(card, amount);
        }

        return cardData;
    }
}
