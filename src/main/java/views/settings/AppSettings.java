package views.settings;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import utilities.ReadConfig;
import utilities.exceptions.AppException;
import views.common.components.Title;
import views.common.components.PrimaryButton;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AppSettings {

    private JTextField expenseCategories = new JTextField(30);
    private JTextField incomeCategories = new JTextField(30);
    private JTextField savingsSafeAmt = new JTextField(30);
    private JTextField viewingRecords = new JTextField(30);
    private JTextField htmlTemplate = new JTextField(30);
    private JTextField chartOutput = new JTextField(30);

    public AppSettings(boolean isModifiable) {
        final JFrame frame = new JFrame("Application Settings");

        JLabel title = new Title("Current Application Settings");

        Map<String, String> props = ReadConfig.getAllProperties();
        setCurrentAppSettings(props);

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
//            updateProperties();
            frame.dispose();
        });
    }

    private void setCurrentAppSettings(Map<String, String> props) {
        expenseCategories.setText(props.get(ApplicationLiterals.EXPENSE_CATEGORIES));
        incomeCategories.setText(props.get(ApplicationLiterals.INCOME_CATEGORIES));
        savingsSafeAmt.setText(props.get(ApplicationLiterals.SAVINGS_SAFE_AMT));
        viewingRecords.setText(props.get(ApplicationLiterals.VIEWING_AMOUNT_MAX));
        htmlTemplate.setText(props.get(ApplicationLiterals.HTML_TEMPLATE));
        chartOutput.setText(props.get(ApplicationLiterals.CHART_OUTPUT));
    }

//    private void updateProperties() {
//        String configFile = ReadConfig.getConfigFile(ApplicationLiterals.getLaunchPath());
//        configFile = configFile.replace("bin/", ApplicationLiterals.EMPTY)
//                .replace(ApplicationLiterals.DOUBLE_SLASH,
//                        ApplicationLiterals.SLASH);
//
//        try {
//            PropertiesConfiguration config = new PropertiesConfiguration(configFile);
//
//            config.setProperty(ApplicationLiterals.EXPENSE_CATEGORIES, expenseCategories.getText()
//                    .trim());
//            config.setProperty(ApplicationLiterals.INCOME_CATEGORIES, incomeCategories.getText()
//                    .trim());
//            config.setProperty(ApplicationLiterals.SAVINGS_SAFE_AMT, savingsSafeAmt.getText()
//                    .trim());
//            config.setProperty(ApplicationLiterals.VIEWING_AMOUNT_MAX, viewingRecords.getText()
//                    .trim());
//            config.setProperty(ApplicationLiterals.HTML_TEMPLATE, htmlTemplate.getText().trim());
//            config.setProperty(ApplicationLiterals.CHART_OUTPUT, chartOutput.getText().trim());
//            config.save();
//
//            JOptionPane.showMessageDialog(null,
//                    "Settings Updated Successfully!", "Complete",
//                    JOptionPane.INFORMATION_MESSAGE);
//        } catch (ConfigurationException e) {
//            new AppException(e);
//        }
//    }
}
