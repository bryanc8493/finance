package views.reminders;

import beans.Reminder;
import literals.Icons;
import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePickerImpl;
import persistence.reminders.ReminderData;
import utilities.DateUtility;
import views.common.MainMenu;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class NewReminder {

    private static Logger logger = Logger.getLogger(NewReminder.class);
    private static JLabel missingField = new JLabel();

    public static void addNewReminder() {
        logger.debug("Displaying GUI to insert new Reminder");
        final JFrame frame = new JFrame("New Reminder");

        final JLabel reminderLabel = new JLabel("Remind me to:");
        final JLabel dateLabel = new JLabel("Remind me on:");
        final JLabel notesLabel = new JLabel("Further notes:");

        final JTextField reminder = new JTextField(17);
        JDatePickerImpl datePicker = DateUtility.getDatePicker();
        final JTextArea notes = new JTextArea(3,17);

        final JButton insert = new PrimaryButton("    Add    ");
        final JButton close = new PrimaryButton("    Close    ");

        missingField.setForeground(Color.RED);
        missingField.setVisible(false);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topRow.add(reminderLabel);
        topRow.add(reminder);

        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        secondRow.add(dateLabel);
        secondRow.add(datePicker);

        JPanel thirdRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        thirdRow.add(notesLabel);
        thirdRow.add(notes);

        JPanel missing = new JPanel();
        missing.setLayout(new FlowLayout(FlowLayout.CENTER));
        missing.add(missingField);

        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.add(topRow);
        middle.add(secondRow);
        middle.add(thirdRow);
        middle.add(missing);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttons.add(close);
        buttons.add(insert);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        JLabel frameTitle = new Title("Add New Reminder");
        main.add(frameTitle, BorderLayout.NORTH);
        main.add(middle, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        frame.add(main);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        JRootPane rp = SwingUtilities.getRootPane(insert);
        rp.setDefaultButton(insert);
        rp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        close.addActionListener(e -> frame.dispose());

        insert.addActionListener(e -> {
            Date inputDate = (Date) datePicker.getModel().getValue();
            String inputReminder = reminder.getText().trim();
            String inputNotes = notes.getText().trim();

            if (validateInput(inputReminder, inputDate)) {
                Reminder reminderData = new Reminder();
                reminderData.setText(inputReminder);
                reminderData.setDate(inputDate);
                reminderData.setIsDismissed(false);
                reminderData.setNotes(inputNotes);

                ReminderData.addReminder(reminderData);
                MainMenu.closeWindow();
                frame.dispose();
                MainMenu.modeSelection(4);
            } else {
                logger.warn("invalid input");
                frame.pack();
            }
        });
    }

    private static boolean validateInput(String reminder, Date date) {
        if (reminder.equals("")) {
            missingField.setText("Reminder field cannot be empty!");
            missingField.setVisible(true);
            return false;
        }else if (date.before(new Date())) {
            missingField.setText("Date cannot be in the past or today!");
            missingField.setVisible(true);
            return false;
        }
        return true;
    }
}
