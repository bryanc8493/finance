package views.accounts;

import domain.beans.SystemSettings;
import domain.beans.UpdatedRecord;
import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.accounts.AccountData;
import utilities.exceptions.AppException;
import utilities.security.Encoding;
import utilities.settings.SettingsService;
import views.common.Loading;
import views.common.MainMenu;
import views.common.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountTab extends JPanel {

    private Logger logger = Logger.getLogger(AccountTab.class);
    private JScrollPane acctSP;
    private JTable table;
    private JTable fullTable;
    private TableColumn passwordColumn;
    private List<UpdatedRecord> updates;

    private SystemSettings settings;

    private boolean passVerified = false;
    private int attempts = 1;

    public AccountTab() {
        logger.debug("Initializing and populating Accounts Tab");
        settings = SettingsService.getSystemSettings();

        Loading.update("Retrieving account data", 81);

        getAccountData(false);

        final JButton view = new MultiLabelButton("View Passwords", MultiLabelButton.BOTTOM, Icons.VIEW_ICON);
        final JButton add = new MultiLabelButton(" New Accounts ", MultiLabelButton.BOTTOM, Icons.ADD_ICON);
        final JButton edit = new MultiLabelButton(" Edit Accounts ", MultiLabelButton.BOTTOM, Icons.EDIT_ICON);

        JLabel title = new Title("Current Accounts");
        JLabel clipPassword = new JLabel("* Single-Click to copy password to the clipboard");
        clipPassword.setForeground(Color.blue);
        clipPassword.setVisible(false);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(view);
        buttons.add(add);
        buttons.add(edit);
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel instructions = new JPanel(new BorderLayout());
        instructions.add(new JLabel("* Double-click an account to be navigated to the login page"), BorderLayout.NORTH);
        instructions.add(clipPassword, BorderLayout.SOUTH);

        JPanel content = new JPanel(new BorderLayout());
        content.add(buttons, BorderLayout.NORTH);
        content.add(acctSP, BorderLayout.CENTER);
        content.add(instructions, BorderLayout.SOUTH);

        content.setBorder(ApplicationLiterals.PADDED_SPACE);

        this.setLayout(new BorderLayout(10, 10));
        this.add(title, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
        this.add(ApplicationControl.closeAndLogout((JFrame) SwingUtilities.getRoot(this)), BorderLayout.SOUTH);

        view.addActionListener(e ->  {
            if (checkEncryptionKey()) {
                logger.debug("Displaying passwords");
                table.getColumnModel().addColumn(passwordColumn);
                Dimension d = table.getPreferredSize();
                acctSP.setPreferredSize(new Dimension(d.width * 3, table
                        .getRowHeight() * 12));
                view.setEnabled(false);
                clipPassword.setVisible(true);

            } else {
                logger.warn("Invalid encryption key - attempt: "
                        + getAttempts());
                setAttempts(getAttempts() + 1);
                if (getAttempts() > 3) {
                    view.setEnabled(false);
                    edit.setEnabled(false);
                }
            }
        });

        add.addActionListener((e) -> InsertAccount.addNewAccount());

        edit.addActionListener(e ->  {
            if (!checkEncryptionKey()) {
                logger.warn("Invalid encryption key - attempt: "
                        + getAttempts());
                setAttempts(getAttempts() + 1);
                if (getAttempts() > 3) {
                    edit.setEnabled(false);
                    view.setEnabled(false);
                }
            } else {
                logger.debug("User editing account data");
                final JFrame f = new JFrame("Edit Accounts");
                JPanel p = new JPanel(new BorderLayout(10, 0));
                JLabel label = new Title("Edit Accounts");
                JButton update = new JButton("Update");
                JButton delete = new JButton("Delete");
                delete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                update.setCursor(new Cursor(Cursor.HAND_CURSOR));
                JPanel buttonPanel = new JPanel(new FlowLayout(
                        FlowLayout.CENTER));
                buttonPanel.add(delete);
                buttonPanel.add(update);
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0,
                        10, 0));
                p.add(label, BorderLayout.NORTH);
                p.add(getFullAccountData(), BorderLayout.CENTER);
                p.add(buttonPanel, BorderLayout.SOUTH);
                f.add(p);
                f.setIconImage(Icons.APP_ICON.getImage());
                f.pack();
                f.setVisible(true);
                f.setLocationRelativeTo(null);

                updates = new ArrayList<>();

                update.addActionListener(exc ->  {
                    f.dispose();
                    AccountData.changeAccounts(updates);
                    MainMenu.closeWindow();
                    JOptionPane.showMessageDialog(null, "Successfully updated account records",
                            "Updated!", JOptionPane.INFORMATION_MESSAGE);
                    MainMenu.modeSelection(3);
                });

                delete.addActionListener(ex ->  {
                    int row = fullTable.getSelectedRow();
                    if (row != -1) {
                        String ID = (String) fullTable.getValueAt(row,
                                0);
                        int choice = JOptionPane
                                .showConfirmDialog(
                                        null,
                                        "Are you sure you want to delete the selected record?",
                                        "Confirm",
                                        JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            f.dispose();
                            AccountData.deleteAccount(ID);
                            MainMenu.closeWindow();
                            JOptionPane.showMessageDialog(null, "Record deleted successfully",
                                    "Deleted!", JOptionPane.INFORMATION_MESSAGE);
                            MainMenu.modeSelection(3);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please select a record to delete",
                                "No Selection",
                                JOptionPane.WARNING_MESSAGE);
                    }
                });
            }
        });
    }

    private void getAccountData(boolean showPassword) {
        Object[][] records = AccountData.getAccounts();
        Object[] columnNames = { "ACCOUNT", "USERNAME", "PASSWORD" };
        DefaultTableModel model = new DefaultTableModel(records, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        passwordColumn = table.getColumnModel().getColumn(2);
        if (!showPassword) {
            table.getColumnModel().removeColumn(passwordColumn);
        }

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && isPassVerified()) {
                    String clickedAccount = table.getModel().getValueAt(table.getSelectedRow(),0).toString();
                    String pass = AccountData.getPassword(clickedAccount);
                    StringSelection selection = new StringSelection(pass);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                }
                if (e.getClickCount() == 2) {
                    String clickedAccount = table.getModel().getValueAt(table.getSelectedRow(),0).toString();
                    String loginUrl = AccountData.getUrl(clickedAccount);
                    openSiteLogin(loginUrl, clickedAccount);
                }
            }
        });

        acctSP = new JScrollPane(table);
        acctSP.setViewportView(table);
        acctSP.setVisible(true);
        Dimension d = table.getPreferredSize();
        acctSP.setPreferredSize(new Dimension(d.width * 3, table.getRowHeight() * 12));
    }

    private void openSiteLogin(String url, String site) {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI(url);
            desktop.browse(oURL);
        } catch (NullPointerException e) {
            logger.error("No URL defined for " + site);
            JOptionPane.showMessageDialog(this,
                    site + " does not have a login site URL defined! Please edit the Account to add one",
                    "No Site Defined", JOptionPane.ERROR_MESSAGE);
        } catch  (IOException ex) {
            logger.error("IO Exception for " + site + ": " + ex.toString());
            JOptionPane.showMessageDialog(this,
                    "Unhandled IO Exception - site: " + site + " - error: " + ex.toString(),
                    "IO Exception Error", JOptionPane.ERROR_MESSAGE);
        } catch (URISyntaxException ux) {
            logger.error("URI Exception for " + site + ": " + ux.toString());
            JOptionPane.showMessageDialog(this,
                    "Unhandled URI Exception - site: " + site + " - error: " + ux.toString(),
                    "URI Exception Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JScrollPane getFullAccountData() {
        Object[][] records = AccountData.getFullAccounts();
        Object[] columnNames = { "ID", "Accounts", "Username", "Password", "Site URL" };

        DefaultTableModel model = new DefaultTableModel(records, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return (column != 0);
            }
        };
        fullTable = new JTable(model);

        final JScrollPane sp = new JScrollPane(fullTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setViewportView(fullTable);
        sp.setVisible(true);
        Dimension d = fullTable.getPreferredSize();
        sp.setPreferredSize(new Dimension(d.width * 2,
                fullTable.getRowHeight() * 15));
        final Map<Integer, String> map = getAttributeMap();

        fullTable.getModel().addTableModelListener(e ->  {
            String changedData;
            String ID;
            int row = fullTable.getSelectedRow();
            int column = fullTable.getSelectedColumn();
            changedData = (String) fullTable.getValueAt(row, column);
            ID = (String) fullTable.getValueAt(row, 0);

            UpdatedRecord changedRecord = new UpdatedRecord();
            changedRecord.setID(ID);
            changedRecord.setAttribute(map.get(column));
            changedRecord.setData(changedData);
            updates.add(changedRecord);
        });
        return sp;
    }

    private boolean checkEncryptionKey() {
        if (isPassVerified()) {
            return true;
        }
        JPasswordField pf = new HintPassField("Verify Encryption Key", false);
        pf.addAncestorListener(new RequestFocusListener());
        int okCxl = JOptionPane.showConfirmDialog(null, pf,
                "Verify Encryption Key", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (okCxl == JOptionPane.OK_OPTION) {
            String inputKey;
            try {
                inputKey = Encoding.encrypt(new String(pf.getPassword()));
            } catch (UnsupportedEncodingException | GeneralSecurityException e1) {
                throw new AppException(e1);
            }
            if (inputKey.equals(settings.getEncryptionKey())) {
                logger.debug("Encryption Key is correct");
                setPassVerified(true);
                return true;
            }
        }
        JOptionPane.showMessageDialog(null, "Invalid key provided", "Invalid",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    private static Map<Integer, String> getAttributeMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "ACCOUNT");
        map.put(2, "USERNAME");
        map.put(3, "PASS");
        map.put(4, "URL");
        return map;
    }

    private boolean isPassVerified() {
        return passVerified;
    }

    private void setPassVerified(boolean passVerified) {
        this.passVerified = passVerified;
    }

    private int getAttempts() {
        return attempts;
    }

    private void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
