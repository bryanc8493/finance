package views.settings;

import domain.beans.SystemSettings;
import literals.Icons;
import services.ViewService;
import utilities.settings.SettingsService;
import views.common.components.JLeftLabel;
import views.common.components.LabelValue;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SystemSettingsModal {

    private JFrame frame;
    private JLabel title;

    private JLabel rootUserLabel;
    private JLabel adminUserLabel;
    private JLabel reportTypesLabel;
    private JLabel deploymentLabel;
    private JLabel developmentLabel;
    private JLabel dbServerLabel;

    private JTextField rootUser;
    private JTextField adminUser;
    private JTextField reportTypes;
    private JTextField deployment;
    private JTextField development;
    private JTextField dbServer;

    private JPanel contentPanel;
    private JPanel mainPanel;

    private PrimaryButton close;

    public SystemSettingsModal() {
        initializeComponents();

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(new LabelValue(rootUserLabel, rootUser));
        contentPanel.add(new LabelValue(adminUserLabel, adminUser));
        contentPanel.add(new LabelValue(reportTypesLabel, reportTypes));
        contentPanel.add(new LabelValue(deploymentLabel, deployment));
        contentPanel.add(new LabelValue(developmentLabel, development));
        contentPanel.add(new LabelValue(dbServerLabel, dbServer));

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(close);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(title);
        mainPanel.add(contentPanel);
        mainPanel.add(buttonPanel);

        packInitialFrame();

        calculateLabelOffsets();

        setUpdatedFrame();

        close.addActionListener(e -> frame.dispose());
    }

    private List<JLabel> getLabelList() {
        List<JLabel> labels = new ArrayList<>();
        labels.add(rootUserLabel);
        labels.add(adminUserLabel);
        labels.add(reportTypesLabel);
        labels.add(deploymentLabel);
        labels.add(developmentLabel);
        labels.add(dbServerLabel);

        return labels;
    }

    private void calculateLabelOffsets() {
        List<Integer> offsets = ViewService.getLabelValueOffsets(getLabelList());

        frame.remove(mainPanel);
        contentPanel.removeAll();

        contentPanel.add(new LabelValue(rootUserLabel, rootUser, offsets.get(0)));
        contentPanel.add(new LabelValue(adminUserLabel, adminUser,offsets.get(1)));
        contentPanel.add(new LabelValue(reportTypesLabel, reportTypes,offsets.get(2)));
        contentPanel.add(new LabelValue(deploymentLabel, deployment,offsets.get(3)));
        contentPanel.add(new LabelValue(developmentLabel, development,offsets.get(4)));
        contentPanel.add(new LabelValue(dbServerLabel, dbServer,offsets.get(5)));
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

    private void initializeComponents() {
        initializeLabels();

        initializeValues(SettingsService.getSystemSettings());

        lockValues();
    }

    private void initializeLabels() {
        frame = new JFrame("Global System Settings");
        close = new PrimaryButton("Close");

        title = new Title("Current System Settings:");
        rootUserLabel = new JLeftLabel("Root User:");
        adminUserLabel = new JLeftLabel("Administrator:");
        reportTypesLabel = new JLeftLabel("Report Types:");
        deploymentLabel = new JLeftLabel("Deployment Location:");
        developmentLabel = new JLeftLabel("Development Location:");
        dbServerLabel = new JLeftLabel("Database Server:");
    }

    private void initializeValues(SystemSettings settings) {
        rootUser = new JTextField(settings.getRootUser());
        adminUser = new JTextField(settings.getAdminUser());
        setReportTypesText(settings.getReportTypes().toString());
        deployment = new JTextField(settings.getDeploymentLocation());
        development =  new JTextField(settings.getDevelopmentLocation());
        dbServer = new JTextField(settings.getDatabaseServerLocation());
    }

    private void lockValues() {
        rootUser.setEditable(false);
        adminUser.setEditable(false);
        reportTypes.setEditable(false);
        deployment.setEditable(false);
        development.setEditable(false);
        dbServer.setEditable(false);
    }

    private void setReportTypesText(String input) {
        reportTypes = new JTextField(input
            .replace("[", "")
            .replace("]", "")
        );
    }
}
