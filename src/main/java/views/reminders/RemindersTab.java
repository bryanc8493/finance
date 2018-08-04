package views.reminders;

import beans.Reminder;
import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.reminders.ReminderData;
import views.common.Loading;
import views.common.components.ApplicationControl;
import views.common.components.MultiLabelButton;
import views.common.components.TabButton;
import views.common.components.Title;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RemindersTab extends JPanel {

    private Logger log = Logger.getLogger(RemindersTab.class);
    private JScrollPane activeTable = getActiveReminderData("ACTIVE");
    private JScrollPane futureTable = getActiveReminderData("FUTURE");
    private JScrollPane dismissedTable = getActiveReminderData("DISMISSED");

    public RemindersTab() {
        log.debug("Initializing and populating Reminders Tab");
        Loading.update("Retrieving reminder data", 100);

        final JButton add = new MultiLabelButton(" New Reminder ", MultiLabelButton.BOTTOM, Icons.ADD_ICON);
        final JButton edit = new MultiLabelButton(" Edit Reminders ", MultiLabelButton.BOTTOM, Icons.EDIT_ICON);

        JLabel title = new Title("Reminders");

        JLabel noReminders = new JLabel("You currently have no reminders!", JLabel.CENTER);
        noReminders.setFont(ApplicationLiterals.APP_FONT);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(add);
        buttons.add(edit);
        buttons.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));

        TabButton active = new TabButton("   Active   ");
        TabButton future = new TabButton("   Future   ");
        TabButton dismissed = new TabButton("   Dismissed   ");
        setActiveTab(active);

        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.CENTER, 30,10));
        tabs.add(active);
        tabs.add(future);
        tabs.add(dismissed);

        JPanel tables = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tables.add(activeTable);
        tables.add(dismissedTable);
        tables.add(futureTable);
        futureTable.setVisible(false);
        dismissedTable.setVisible(false);

        JPanel content = new JPanel(new BorderLayout());
        content.add(buttons, BorderLayout.NORTH);
        content.add(tabs, BorderLayout.CENTER);
        if (ReminderData.getTotalNonDismissedReminders() == 0) {
            content.add(noReminders, BorderLayout.SOUTH);
        } else {
            content.add(tables, BorderLayout.SOUTH);
        }
        content.setBorder(BorderFactory.createEmptyBorder(0,25,0,25));

        this.setLayout(new BorderLayout(10, 10));
        this.add(title, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
        this.add(ApplicationControl.closeAndLogout((JFrame) SwingUtilities.getRoot(this)), BorderLayout.SOUTH);

        add.addActionListener(e -> NewReminder.addNewReminder());
        edit.addActionListener(e -> new ModifyReminders(false));

        active.addActionListener(e -> {
            setActiveTab(active);
            setInactiveTabs(future, dismissed);
            activeTable.setVisible(true);
            dismissedTable.setVisible(false);
            futureTable.setVisible(false);
        });

        future.addActionListener(e -> {
            setActiveTab(future);
            setInactiveTabs(active, dismissed);
            futureTable.setVisible(true);
            activeTable.setVisible(false);
            dismissedTable.setVisible(false);
        });

        dismissed.addActionListener(e -> {
            setActiveTab(dismissed);
            setInactiveTabs(future, active);
            dismissedTable.setVisible(true);
            futureTable.setVisible(false);
            activeTable.setVisible(false);
        });
    }

    private void setActiveTab(TabButton tab) {
        log.debug("displaying reminder data for: " + tab.getText());
        tab.setActive(true);
        tab.setFont(ApplicationLiterals.ACTIVE_TAB_FONT);
        tab.setForeground(Color.BLACK);
        Border active = BorderFactory.createMatteBorder(0,0,3,0, ApplicationLiterals.APP_COLOR);
        tab.setBorder(new CompoundBorder(active, ApplicationLiterals.TAB_BORDER));
    }

    private void setInactiveTabs(TabButton tabOne, TabButton tabTwo) {
        tabOne.setFont(ApplicationLiterals.TAB_FONT);
        tabTwo.setFont(ApplicationLiterals.TAB_FONT);

        tabOne.setForeground(ApplicationLiterals.GREY_TAB);
        tabTwo.setForeground(ApplicationLiterals.GREY_TAB);

        tabOne.setBorder(ApplicationLiterals.TAB_BORDER);
        tabTwo.setBorder(ApplicationLiterals.TAB_BORDER);
    }

    private JScrollPane getActiveReminderData(String dataType) {
        Object[] columnNames = { "Id", "Reminder Text", "Reminder Date" };
        Object[][] records;

        switch (dataType) {
            case "ACTIVE":
                records = ReminderData.getActiveReminders();
                break;
            case "FUTURE":
                records = ReminderData.getFutureReminders();
                break;
            default:
                records = ReminderData.getDismissedReminders();
                break;
        }

        DefaultTableModel model = new DefaultTableModel(records, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        final JTable table = new JTable(model);
        final JScrollPane remSP = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        remSP.setViewportView(table);
        remSP.setVisible(true);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        Dimension d = table.getPreferredSize();
        remSP.setPreferredSize(new Dimension(d.width,
                table.getRowHeight() * 12));

        TableColumn idColumn = table.getColumnModel().getColumn(0);
        table.getColumnModel().removeColumn(idColumn);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String id = table.getModel().getValueAt(table.getSelectedRow(),0).toString();

                    Reminder selectedReminder = ReminderData.getReminder(id);
                    new ReminderRecord(selectedReminder);
                }
            }
        });

        return remSP;
    }
}
