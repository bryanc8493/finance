package views.settings;

import domain.beans.UserSettings;
import literals.Icons;
import services.ViewService;
import utilities.MapperUtil;
import utilities.settings.SettingsService;
import views.common.components.LabelValue;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApplicationSettingsModal {

    private JFrame frame;
    private JLabel title;

    private UserSettings applicationSettings;

    private JPanel contentPanel;
    private JPanel mainPanel;

    private PrimaryButton close;
    private PrimaryButton save;

    private List<LabelValue> initialLabelValueList;
    private List<LabelValue> updatedLabelValueList;

    public ApplicationSettingsModal(Boolean isEditable) {
        applicationSettings = SettingsService.getCurrentUserSettings();

        initializeComponents();
        generateLabelValueList();
        generateContent(initialLabelValueList);

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(close);
        if (isEditable) {
            buttonPanel.add(save);
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(title);
        mainPanel.add(contentPanel);
        mainPanel.add(buttonPanel);

        packInitialFrame();

        setLabelOffsets(isEditable);

        setUpdatedFrame();

        close.addActionListener(e -> frame.dispose());

        save.addActionListener(e -> saveSettings());
    }

    private void packInitialFrame() {
        frame.add(mainPanel);
        frame.pack();
    }

    private void setUpdatedFrame() {
        frame.add(mainPanel);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JRootPane rp = SwingUtilities.getRootPane(close);
        rp.setDefaultButton(close);
        rp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void setLabelOffsets(boolean isEditable) {
        frame.remove(mainPanel);
        contentPanel.removeAll();

        updatedLabelValueList = ViewService.updateLabelValueOffsets(initialLabelValueList, isEditable);
        generateContent(updatedLabelValueList);
    }

    private void initializeComponents() {
        frame = new JFrame("Application Settings");
        close = new PrimaryButton("Close");
        save = new PrimaryButton("Save");
        initialLabelValueList = new ArrayList<>();

        title = new Title("Application Settings for " + applicationSettings.getUsername());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    }

    private void generateLabelValueList() {
        initialLabelValueList.add(new LabelValue("Backup Location:", applicationSettings.getBackupLocation()));
        initialLabelValueList.add(new LabelValue("Expense Categories:", MapperUtil.mapSetToCommaSeparatedString(applicationSettings.getExpenseCategories())));
        initialLabelValueList.add(new LabelValue("Income Categories:", MapperUtil.mapSetToCommaSeparatedString(applicationSettings.getIncomeCategories())));
        initialLabelValueList.add(new LabelValue("Savings Amount:", applicationSettings.getSavingsSafetyAmount().toString()));
        initialLabelValueList.add(new LabelValue("Viewing Records:", applicationSettings.getViewingRecords().toString()));
        initialLabelValueList.add(new LabelValue("Credit Cards:", MapperUtil.mapSetToCommaSeparatedString(applicationSettings.getCreditCards())));
        initialLabelValueList.add(new LabelValue("Template File:", applicationSettings.getTemplateFileLocation()));
        initialLabelValueList.add(new LabelValue("Chart Output:", applicationSettings.getChartOutputLocation()));
        initialLabelValueList.add(new LabelValue("Reports Output:", applicationSettings.getReportsOutputLocation()));
    }

    private void generateContent(List<LabelValue> list) {
        for (LabelValue labelValue : list) {
            contentPanel.add(labelValue);
        }
    }

    private void saveSettings() {
        final List<String> updatedValues = new ArrayList<>();
        for (LabelValue list : updatedLabelValueList) {
            updatedValues.add(list.getValue().getText().trim());
        }

        applicationSettings.setBackupLocation(updatedValues.get(0));
        applicationSettings.setExpenseCategories(
            MapperUtil.mapCommaSeparatedList(updatedValues.get(1))
        );
        applicationSettings.setIncomeCategories(
            MapperUtil.mapCommaSeparatedList(updatedValues.get(2))
        );
        applicationSettings.setSavingsSafetyAmount(
            Double.parseDouble(updatedValues.get(3))
        );
        applicationSettings.setViewingRecords(
                Integer.parseInt(updatedValues.get(4))
        );
        applicationSettings.setCreditCards(
                MapperUtil.mapCommaSeparatedList(updatedValues.get(5))
        );
        applicationSettings.setTemplateFileLocation(updatedValues.get(6));
        applicationSettings.setChartOutputLocation(updatedValues.get(7));
        applicationSettings.setReportsOutputLocation(updatedValues.get(8));

        if (SettingsService.saveUserSettings(applicationSettings)) {
            JOptionPane.showMessageDialog(
                frame,
        "Your settings have been saved successfully",
            "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
