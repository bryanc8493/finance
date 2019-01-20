package views.payments;

import domain.dto.FinancingDetail;
import literals.ApplicationLiterals;
import literals.Icons;
import services.FinancingService;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FinancedPurchaseDetails extends JFrame {

    private List<FinancingDetail> detailListData;
    private JTable detailsTable;

    public FinancedPurchaseDetails() {
        detailListData = FinancingService.getFinancingDetailsList();

        final JButton close = new PrimaryButton("Close");
        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(close);

        final JPanel content = new JPanel(new BorderLayout());
        content.add(new Title("Financed Purchase Details"), BorderLayout.NORTH);
        content.add(getListPanel(), BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);
        setTitle("Financing Details");
        setIconImage(Icons.APP_ICON.getImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        JRootPane rp = SwingUtilities.getRootPane(close);
        rp.setDefaultButton(close);
        rp.setBorder(ApplicationLiterals.PADDED_SPACE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

        close.addActionListener(e -> dispose());
    }

    private JScrollPane getListPanel() {
        Object[][] records = FinancingService.transformDataForDetailTable(detailListData);
        Object[] columnNames = {"Title", "Total", "Total Payments", "Remaining Balance",
            "Last Payment", "Paid Off"};

        DefaultTableModel model = new DefaultTableModel(records, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailsTable = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(detailsTable);
        scrollPane.setViewportView(detailsTable);
        scrollPane.setVisible(true);
        Dimension d = detailsTable.getPreferredSize();
        scrollPane.setPreferredSize(new Dimension(d.width * 2,
                detailsTable.getRowHeight() * 12));

        return scrollPane;
    }

}
