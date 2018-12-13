package views.settings;

import domain.beans.UserSettings;
import literals.Icons;
import utilities.settings.SettingsService;
import views.common.components.Title;
import views.common.components.PrimaryButton;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Set;

public class AppSettings {

    private JTextField expenseCategories = new JTextField(30);
    private JTextField incomeCategories = new JTextField(30);
    private JTextField savingsSafeAmt = new JTextField(30);
    private JTextField viewingRecords = new JTextField(30);
    private JTextField htmlTemplate = new JTextField(30);
    private JTextField chartOutput = new JTextField(30);

    private UserSettings userSettings;

    public AppSettings(boolean isModifiable) {
        userSettings = SettingsService.getCurrentUserSettings();

        final JFrame frame = new JFrame("Application Settings");

        JLabel title = new Title("Current Application Settings");

        setCurrentAppSettings();

        JLabel expenseCategoriesLabel = new JLabel("Expense Categories");
        JLabel incomeCategoriesLabel = new JLabel("Income Categories");
        JLabel savingsSafeAmtLabel = new JLabel("Savings Emergency Amount");
        JLabel viewingRecordsLabel = new JLabel("Last Viewing Records");
        JLabel htmlTemplateLabel = new JLabel("HTML Template File");
        JLabel chartOutputLabel = new JLabel("HTML Chart Output File");

        JPanel contentLabels = new JPanel(new GridLayout(6, 1, 10, 10));
        contentLabels.add(expenseCategoriesLabel);
        contentLabels.add(incomeCategoriesLabel);
        contentLabels.add(savingsSafeAmtLabel);
        contentLabels.add(viewingRecordsLabel);
        contentLabels.add(htmlTemplateLabel);
        contentLabels.add(chartOutputLabel);

        JPanel contentItems = new JPanel(new GridLayout(6, 1, 10, 10));
        contentItems.add(expenseCategories);
        contentItems.add(incomeCategories);
        contentItems.add(savingsSafeAmt);
        contentItems.add(viewingRecords);
        contentItems.add(htmlTemplate);
        contentItems.add(chartOutput);

        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.add(contentLabels, BorderLayout.WEST);
        content.add(contentItems, BorderLayout.EAST);
        content.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton close = new PrimaryButton("Close");

        JButton update = new PrimaryButton("Apply & Save");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(update);
        update.setVisible(isModifiable);
        bottom.add(close);

        JPanel main = new JPanel(new BorderLayout());
        main.add(title, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        frame.add(main);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JRootPane rp = SwingUtilities.getRootPane(close);
        rp.setDefaultButton(close);
        rp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        close.addActionListener(e -> frame.dispose());

        update.addActionListener(e -> {
            frame.dispose();
        });
    }

    private void setCurrentAppSettings() {
        expenseCategories.setText(setExpenseText());
        incomeCategories.setText(setIncomeText());
        savingsSafeAmt.setText(userSettings.getSavingsSafetyAmount().toString());
        viewingRecords.setText(userSettings.getViewingRecords().toString());
        htmlTemplate.setText(userSettings.getTemplateFileLocation());
        chartOutput.setText(userSettings.getChartOutputLocation());
    }

    private String setExpenseText() {
        Set<String> expenseSet = userSettings.getExpenseCategories();

        String text = Arrays.toString(expenseSet.toArray(new String[expenseSet.size()]));

        return text.replace("[", "").replace("]", "");
    }

    private String setIncomeText() {
        Set<String> incomeSet = userSettings.getIncomeCategories();

        String text = Arrays.toString(incomeSet.toArray(new String[incomeSet.size()]));

        return text.replace("[", "").replace("]", "");
    }
}
