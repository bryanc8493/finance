package views.payments;

import domain.dto.FinancingSummary;
import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import services.FinancingService;
import views.common.MainMenu;
import views.common.components.ApplicationControl;
import views.common.components.MultiLabelButton;
import views.common.components.Title;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class FinancedTab extends JPanel {

    private Logger logger = Logger.getLogger(FinancedTab.class);
    private List<FinancingSummary> summaryData;
    private JTable summaryTable;

    public FinancedTab() {
        logger.debug("Initializing and populating Financed Tab");
        summaryData = FinancingService.getFinancingSummaryList();

        final JButton view = new MultiLabelButton(" View Details ", MultiLabelButton.BOTTOM, Icons.VIEW_ICON);
        final JButton add = new MultiLabelButton("New Purchase", MultiLabelButton.BOTTOM, Icons.ADD_ICON);
        final JButton pay = new MultiLabelButton("Make Payment", MultiLabelButton.BOTTOM, Icons.BANK_ICON);

        JLabel title = new Title("Financed Purchases");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(view);
        buttons.add(add);
        buttons.add(pay);

        JPanel content = new JPanel(new BorderLayout());
        content.add(buttons, BorderLayout.NORTH);
        content.add(getListPanel());
        content.setBorder(ApplicationLiterals.PADDED_SPACE);

        this.setLayout(new BorderLayout(10, 10));
        this.add(title, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
        this.add(ApplicationControl.closeAndLogout((JFrame) SwingUtilities.getRoot(this)), BorderLayout.SOUTH);

        view.addActionListener(e ->  viewDetails());

        pay.addActionListener(e -> makePayment());

        add.addActionListener(e -> newPurchase());
    }

    private JScrollPane getListPanel() {
        Object[][] records = FinancingService.transformDataForSummaryTable(summaryData);
        Object[] columnNames = { "ID", "Title", "Total", "Remaining Balance"};

        DefaultTableModel model = new DefaultTableModel(records, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        summaryTable = new JTable(model);
        final TableColumn idColumn = summaryTable.getColumnModel().getColumn(0);
        summaryTable.getColumnModel().removeColumn(idColumn);

        JScrollPane scrollPane = new JScrollPane(summaryTable);
        scrollPane.setViewportView(summaryTable);
        scrollPane.setVisible(true);
        Dimension d = summaryTable.getPreferredSize();
        scrollPane.setPreferredSize(new Dimension(d.width * 2,
                summaryTable.getRowHeight() * 3));

        return scrollPane;
    }

    private void viewDetails() {
        new FinancedPurchaseDetails();
    }

    private void makePayment() {
        MainMenu.closeWindow();
        new FinancedPayment();
    }

    private void newPurchase() {
        MainMenu.closeWindow();
        new FinancedPurchase();
    }
}
