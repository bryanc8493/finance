package views.common;

import beans.Transaction;
import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.Connect;
import persistence.finance.BalanceData;
import persistence.finance.Transactions;
import views.accounts.AccountTab;
import views.address.AddressTab;
import views.common.components.MultiLabelButton;
import utilities.ReadConfig;
import views.common.components.ApplicationControl;
import views.common.components.Title;
import views.finance.ModifyRecords;
import views.finance.NewTransaction;
import views.finance.TransactionRecord;
import views.investments.InvestmentTab;
import views.reminders.RemindersTab;
import views.reporting.CustomReport;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

public class MainMenu {

    private static final ImageIcon appIcon = Icons.APP_ICON;
    private static Logger logger = Logger.getLogger(MainMenu.class);
    private static NumberFormat decimal = ApplicationLiterals.getNumberFormat();
    private static JFrame frame;

    private static boolean isFutureBalancePositive = false;

    public static void modeSelection(int persistedTab) {
        logger.debug("Initializing and generating main menu GUI...");
        Loading.update("Initializing main menu", 27);
        frame = new JFrame("Finance Utility");

        String viewingAmount = ReadConfig.getConfigValue(ApplicationLiterals.VIEWING_AMOUNT_MAX);

        final JButton insert = new MultiLabelButton("New",
                MultiLabelButton.BOTTOM, Icons.INSERT_ICON);
        final JButton modify = new MultiLabelButton("Modify",
                MultiLabelButton.BOTTOM, Icons.DELETE_ICON);
        final JButton report = new MultiLabelButton("Reporting",
                MultiLabelButton.BOTTOM, Icons.REPORT_ICON);
        final JButton lastRecords = new MultiLabelButton("Last "
                + viewingAmount, MultiLabelButton.BOTTOM, Icons.QUERY_ICON);

        // Get current balance as of current date and time
        Loading.update("Determining account balances", 36);
        String amount = BalanceData.getTodaysBalance();
        amount = decimal.format(Double.parseDouble(amount));

        // Get full balance (todays balance minus future payments excluding unpaid credits)
        String futureBalance = BalanceData.getFutureBalance();
        futureBalance = decimal.format(Double.parseDouble(futureBalance));

        // Get actual balance
        String trueBalance = BalanceData.getTrueBalance();
        trueBalance = decimal.format(Double.parseDouble(trueBalance));

        // Get data for last specified (in config) past entries and put in scroll pane for table
        int entriesToRetrieve = Integer.parseInt(ReadConfig.getConfigValue(ApplicationLiterals.VIEWING_AMOUNT_MAX));
        Loading.update("Gathering last " + entriesToRetrieve + " entries", 45);
        Object[][] previousRecords = Transactions.getPastEntries(entriesToRetrieve);
        Object[] columnNames = { "ID", "TITLE", "TYPE", "DATE", "AMOUNT", "STORE" };
        final JTable table = new JTable(previousRecords, columnNames);
        final JScrollPane entriesScrollPane = new JScrollPane(table);
        entriesScrollPane.setViewportView(table);
        entriesScrollPane.setVisible(true);
        Dimension d = table.getPreferredSize();
        entriesScrollPane.setPreferredSize(new Dimension(d.width * 2, table.getRowHeight() * 15));

        Loading.update("Looking for future payments", 54);
        String futurePayments = BalanceData.getFuturePayments();
        final JButton futureBalBtn = new JButton();
        if (futurePayments.equals("0.00")) {
            futureBalBtn.setText(ApplicationLiterals.EMPTY);
            futureBalBtn.setVisible(false);
        } else {
            Double amt = Double.parseDouble(futurePayments);
            futurePayments = decimal.format(amt);
            isFutureBalancePositive = amt > 0.00;
            futureBalBtn.setText("<html><u>($ " + futurePayments
                    + ")</u></html>");
        }

        futureBalBtn.setBorderPainted(false);
        futureBalBtn.setContentAreaFilled(false);
        futureBalBtn.setFocusPainted(false);
        futureBalBtn.setHorizontalAlignment(SwingConstants.LEFT);
        futureBalBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isFutureBalancePositive) {
            futureBalBtn.setForeground(ApplicationLiterals.APP_GREEN);
        } else {
            futureBalBtn.setForeground(Color.RED);
        }

        Loading.update("Looking for credit card payments", 63);
        String credits = BalanceData.getCreditBalance();
        final JButton creditBalBtn = new JButton();
        if (credits == null) {
            creditBalBtn.setText(ApplicationLiterals.EMPTY);
            creditBalBtn.setVisible(false);
        } else {
            credits = decimal.format(Double.parseDouble(credits));
            creditBalBtn.setText("<html><u>($ " + credits + ")</u></html>");
        }

        creditBalBtn.setBorderPainted(false);
        creditBalBtn.setContentAreaFilled(false);
        creditBalBtn.setFocusPainted(false);
        creditBalBtn.setHorizontalAlignment(SwingConstants.LEFT);
        creditBalBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        creditBalBtn.setForeground(Color.RED);

        JPanel grid = new JPanel(new GridLayout(3, 3));
        grid.add(new JLabel("Available Balance:"));
        grid.add(new JLabel("$ " + amount));
        grid.add(new JLabel());
        grid.add(new JLabel("Future Balance:"));
        grid.add(new JLabel("$ " + futureBalance));
        grid.add(futureBalBtn);
        grid.add(new JLabel("Balance (- Credits):"));
        grid.add(new JLabel("$ " + trueBalance));
        grid.add(creditBalBtn);
        grid.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.add(grid, BorderLayout.NORTH);
        top.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
        top.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JPanel dbBtnContent = new JPanel(new GridLayout(1, 4, 5, 5));
        dbBtnContent.add(insert);
        dbBtnContent.add(modify);
        dbBtnContent.add(report);
        dbBtnContent.add(lastRecords);
        dbBtnContent.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.add(dbBtnContent, BorderLayout.NORTH);

        Border space = BorderFactory.createEmptyBorder(10, 25, 15, 25);
        content.setBorder(space);

        JPanel center = new JPanel(new BorderLayout());
        center.add(content, BorderLayout.NORTH);
        content.add(getLatestRecordsPane(entriesToRetrieve), BorderLayout.SOUTH);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(ApplicationControl.closeAndLogout(frame));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(top, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        JTabbedPane screen = new JTabbedPane(JTabbedPane.TOP);
        screen.add("Finance", mainPanel);
        if (Connect.getUsersPermission() == ApplicationLiterals.FULL_ACCESS) {
            screen.add("Investments", new InvestmentTab());
            screen.add("Accounts", new AccountTab());
        }
        screen.add("Addresses", new AddressTab());
        screen.add("Reminders", new RemindersTab());
        screen.setSelectedIndex(persistedTab);

        MenuBar m = new MenuBar(frame, screen);
        JMenuBar b = m.getMenu();

        frame.add(screen);
        frame.setIconImage(appIcon.getImage());
        frame.setJMenuBar(b);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JRootPane rp = SwingUtilities.getRootPane(insert);
        rp.setDefaultButton(insert);
        frame.pack();
        Loading.terminate();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // If user permission is read only (0) then disable all GUI elements for
        // possible editing and deleting
        if (Connect.getUsersPermission() == ApplicationLiterals.VIEW_ONLY) {
            logger.warn("Read Permission only - Editing and deleting disabled");
            modify.setEnabled(false);
            InvestmentTab.fidelity.setEnabled(false);
            InvestmentTab.janus.setEnabled(false);
        }

        insert.addActionListener(e -> {
            frame.dispose();
            NewTransaction.InsertFrame();
        });

        modify.addActionListener(e -> {
            frame.dispose();
            ModifyRecords.editData();
        });

        report.addActionListener(e -> {
            frame.dispose();
            CustomReport.selectReport();
        });

        lastRecords.addActionListener(e -> {
            logger.debug("Displaying last " + viewingAmount + " records");
            JFrame f = new JFrame("Past " + viewingAmount + " Records");
            f.setIconImage(appIcon.getImage());
            JPanel p = new JPanel(new BorderLayout(10, 0));
            JLabel label = new Title("Last " + viewingAmount + " Transactions");
            p.add(label, BorderLayout.NORTH);
            p.add(entriesScrollPane, BorderLayout.SOUTH);
            f.add(p);
            f.pack();
            f.setVisible(true);
            f.setLocationRelativeTo(null);
        });

        futureBalBtn.addActionListener(e -> {
            logger.debug("Displaying future transaction records");
            JFrame f = new JFrame("Future Transactions");
            JPanel p = new JPanel(new BorderLayout(10, 0));
            JLabel label = new Title("Future Transactions");
            p.add(label, BorderLayout.NORTH);
            p.add(getFutureRecordsPane(), BorderLayout.SOUTH);
            f.add(p);
            f.setIconImage(Icons.APP_ICON.getImage());
            f.pack();
            f.setVisible(true);
            f.setLocationRelativeTo(null);
        });

        creditBalBtn.addActionListener(e -> {
            logger.debug("Displaying unpaid credit records");
            JFrame f = new JFrame("Unpaid Credits");
            JPanel p = new JPanel(new BorderLayout(10, 0));
            JLabel label = new Title("Unpaid Credit Card Transactions");
            p.add(label, BorderLayout.NORTH);
            p.add(getCreditRecordsPane(), BorderLayout.SOUTH);
            f.add(p);
            f.setIconImage(Icons.APP_ICON.getImage());
            f.pack();
            f.setVisible(true);
            f.setLocationRelativeTo(null);
        });
    }

    public static void closeWindow() {
        frame.dispose();
    }

    private static JScrollPane getFutureRecordsPane() {
        Object[] columns = { "Title", "Type", "Category", "Transaction_Date", "Amount" };

        JTable table = new JTable(Transactions.getFutureRecords(), columns);
        JScrollPane sp = new JScrollPane(table);
        sp.setViewportView(table);
        sp.setVisible(true);
        Dimension d = table.getPreferredSize();
        sp.setPreferredSize(new Dimension((d.width * 2) - 150, table
                .getRowHeight() * 10));

        return sp;
    }

    private static JScrollPane getCreditRecordsPane() {
        Object[] columns = { "Title", "Category", "Date", "Amount", "Card Used" };

        JTable table = new JTable(Transactions.getUnpaidCreditRecords(), columns);
        JScrollPane sp = new JScrollPane(table);
        sp.setViewportView(table);
        sp.setVisible(true);
        Dimension d = table.getPreferredSize();
        sp.setPreferredSize(new Dimension(d.width * 2,
                table.getRowHeight() * 15));

        return sp;
    }

    private static JScrollPane getLatestRecordsPane(int entries) {
        Object[][] records = Transactions.getPastEntries(entries);
        Object[][] partialRecords = getPartialDataColumns(records);
        Object[] columnNames = { "Transactions ID", "Title", "Type", "Amount" };

        DefaultTableModel model = new DefaultTableModel(partialRecords,
                columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        final JTable table = new JTable(model);
        final JScrollPane addrSP = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addrSP.setViewportView(table);
        addrSP.setVisible(true);
        Dimension d = table.getPreferredSize();
        addrSP.setPreferredSize(new Dimension(d.width,
                table.getRowHeight() * 12));

        TableColumn tranIdColumn = table.getColumnModel().getColumn(0);
        table.getColumnModel().removeColumn(tranIdColumn);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String id = table.getModel().getValueAt(table.getSelectedRow(),0).toString();

                    Transaction selectedTran = Transactions.getSpecifiedTransaction(id);
                    new TransactionRecord(selectedTran);
                }
            }
        });

        return addrSP;
    }

    private static Object[][] getPartialDataColumns(Object[][] data) {

        Object[][] returnData = new Object[data.length][4];
        for (int i = 0; i < data.length; i++) {
            returnData[i][0] = data[i][0];
            returnData[i][1] = data[i][1];
            returnData[i][2] = data[i][2];
            returnData[i][3] = data[i][4];
        }
        return returnData;
    }
}
