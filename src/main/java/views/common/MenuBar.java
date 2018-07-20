package views.common;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.Connect;
import persistence.finance.Transactions;
import utilities.DateUtility;
import views.accounts.UserManagement;
import views.common.components.Title;
import views.finance.Savings;
import views.salary.SalaryCalculator;
import views.settings.AppSettings;
import views.settings.DatabaseSettings;
import views.tools.Backup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuBar {

    private final Logger logger = Logger.getLogger(MenuBar.class);

    private JMenuItem userMgmt = new JMenuItem("Lock/Unlock Users",
            KeyEvent.VK_L);
    private JMenuItem salary = new JMenuItem("Salary", KeyEvent.VK_A);
    private JMenuItem modifyAppSettings = new JMenuItem("Modify");
    private JMenuItem modifyDBSettings = new JMenuItem("Modify");

    private JMenuBar menuBar = new JMenuBar();

    JMenuBar getMenu() {
        return menuBar;
    }

    MenuBar(final JFrame frame, final JTabbedPane menuTabs) {
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);

        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_S);

        String releaseDate = DateUtility.getDeploymentDate();
        JMenu versionAndDate = new JMenu();

        versionAndDate.setIcon(Icons.GREEN_DOT);
        versionAndDate.setText("v" + ApplicationLiterals.VERSION + "  (Released: " + releaseDate + ")");

        if (ApplicationLiterals.isFromWorkspace()) {
            versionAndDate.setIcon(Icons.RED_DOT);
            versionAndDate.setText("v" + ApplicationLiterals.VERSION + "  (Development)");
        } else {
            versionAndDate.setIcon(Icons.GREEN_DOT);
            versionAndDate.setText("v" + ApplicationLiterals.VERSION + "  (Released: " + releaseDate + ")");
        }
        versionAndDate.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JMenuItem backup = new JMenuItem("Backup Databases", KeyEvent.VK_B);
        backup.setIcon(Icons.BACKUP_ICON);
        JMenuItem monthReports = new JMenuItem("Monthly Summaries",
                KeyEvent.VK_M);
        monthReports.setIcon(Icons.SUMMARY_ICON);
        JMenuItem savings = new JMenuItem("View Savings", KeyEvent.VK_S);
        savings.setIcon(Icons.SAVINGS_ICON);
        salary.setIcon(Icons.SALARY_ICON);
        userMgmt.setIcon(Icons.USER_MGMT_ICON);
        JMenuItem changePass = new JMenuItem("Change Password", KeyEvent.VK_C);
        changePass.setIcon(Icons.CHANGE_PASS_ICON);
        JMenuItem refresh = new JMenuItem("Refresh");
        refresh.setIcon(Icons.REFRESH_ICON);
        toolsMenu.add(backup);
        toolsMenu.addSeparator();
        toolsMenu.add(changePass);
        toolsMenu.add(userMgmt);
        toolsMenu.addSeparator();
        toolsMenu.add(monthReports);
        toolsMenu.add(salary);
        toolsMenu.add(savings);
        toolsMenu.addSeparator();
        toolsMenu.add(refresh);

        JMenu databaseSettings = new JMenu("Database");
        JMenuItem viewDBSettings = new JMenuItem("View");
        databaseSettings.add(viewDBSettings);
        databaseSettings.add(modifyDBSettings);

        JMenu appSettings = new JMenu("Application");
        JMenuItem viewAppSettings = new JMenuItem("View");
        appSettings.add(viewAppSettings);
        appSettings.add(modifyAppSettings);

        settingsMenu.add(appSettings);
        settingsMenu.addSeparator();
        settingsMenu.add(databaseSettings);

        setPermissions(Connect.getUsersPermission());

        menuBar.add(toolsMenu);
        menuBar.add(settingsMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(versionAndDate);

        backup.addActionListener((e) -> new Backup());

        refresh.addActionListener(e -> {
            int currentTabIndex = menuTabs.getSelectedIndex();
            frame.dispose();
            MainMenu.modeSelection(currentTabIndex);
        });

        changePass.addActionListener(e -> {
            if (!Connect.getCurrentUser().equalsIgnoreCase("root")) {
                UserManagement.changePassword(false,
                        Connect.getCurrentUser());
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Password cannot be changed for root user",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        userMgmt.addActionListener((e) -> new UserManagement());

        salary.addActionListener((e) -> new SalaryCalculator());

        monthReports.addActionListener(e -> {
            JFrame f = new JFrame("Monthly Summary Data");
            JPanel p = new JPanel(new BorderLayout(10, 0));
            JLabel label = new Title("Monthly Summary Data (Since January 2016)");
            p.add(label, BorderLayout.NORTH);
            p.add(getMonthReportsData(), BorderLayout.SOUTH);
            p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            f.add(p);
            f.setIconImage(Icons.APP_ICON.getImage());
            f.pack();
            f.setVisible(true);
            f.setLocationRelativeTo(null);
        });

        savings.addActionListener((e) -> new Savings());

        viewAppSettings.addActionListener(e -> {
            logger.debug("Displaying app settings");
            new AppSettings(false);
        });

        modifyAppSettings.addActionListener(e -> {
            logger.debug("Modifying app settings");
            new AppSettings(true);
        });

        viewDBSettings.addActionListener(e -> {
            logger.debug("Dislaying database settings");
            new DatabaseSettings(false);
        });

        modifyDBSettings.addActionListener(e -> {
            logger.debug("Modifying database settings");
            new DatabaseSettings(true);
        });
    }

    private void setPermissions(char permission) {
        if (permission == ApplicationLiterals.VIEW_ONLY) {
            userMgmt.setEnabled(false);
            salary.setEnabled(false);
            modifyAppSettings.setEnabled(false);
            modifyDBSettings.setEnabled(false);
        }
    }

    private static JScrollPane getMonthReportsData() {
        Object[][] records = Transactions.getMonthlySummaries();
        Object[] columnNames = { "MONTH", "YEAR", "TOTAL EXPENSES", "TOTAL INCOME", "CASH FLOW" };
        JTable table = new JTable(records, columnNames);
        final JScrollPane sp = new JScrollPane(table);
        sp.setViewportView(table);
        sp.setVisible(true);
        Dimension d = table.getPreferredSize();
        sp.setPreferredSize(new Dimension(d.width + 250, table.getRowHeight() * 15));
        return sp;
    }
}
