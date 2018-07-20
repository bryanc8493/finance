package views.settings;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import utilities.ReadConfig;
import utilities.exceptions.AppException;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DatabaseSettings {

    private JTextField url = new JTextField(30);
    private JTextField mySqlClass = new JTextField(30);
    private JTextField username = new JTextField(30);
    private JTextField password = new JTextField(30);
    private JTextField port = new JTextField(30);
    private JTextField directory = new JTextField(30);
    private JTextField backup = new JTextField(30);

    public DatabaseSettings(boolean isModifable) {
        final JFrame frame = new JFrame("Database Settings");

        JLabel title = new Title("Current Database Settings");

        Map<String, String> props = ReadConfig.getAllProperties();
        setCurrentAppSettings(props);

        JLabel urlLabel = new JLabel("Database Connection URL");
        JLabel mySqlClassLabel = new JLabel("MySQL Class Name");
        JLabel usernameLabel = new JLabel("Database User");
        JLabel passwordLabel = new JLabel("Database Password");
        JLabel portLabel = new JLabel("Database Port");
        JLabel directoryLabel = new JLabel("MySQL bin Directory");
        JLabel backupLabel = new JLabel("Backup Directory");

        JPanel contentLabels = new JPanel(new GridLayout(7, 1, 10, 10));
        contentLabels.add(urlLabel);
        contentLabels.add(mySqlClassLabel);
        contentLabels.add(usernameLabel);
        contentLabels.add(passwordLabel);
        contentLabels.add(portLabel);
        contentLabels.add(directoryLabel);
        contentLabels.add(backupLabel);

        JPanel contentItems = new JPanel(new GridLayout(7, 1, 10, 10));
        contentItems.add(url);
        contentItems.add(mySqlClass);
        contentItems.add(username);
        contentItems.add(password);
        contentItems.add(port);
        contentItems.add(directory);
        contentItems.add(backup);

        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.add(contentLabels, BorderLayout.WEST);
        content.add(contentItems, BorderLayout.EAST);
        content.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton close = new PrimaryButton("Close");
        JButton update = new PrimaryButton("Apply & Save");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(update);
        update.setVisible(isModifable);
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
        url.setText(props.get(ApplicationLiterals.DB_URL));
        mySqlClass.setText(props.get(ApplicationLiterals.MY_SQL_CLASS));
        username.setText(props.get(ApplicationLiterals.DB_USER));
        password.setText(props.get(ApplicationLiterals.DB_PASS));
        port.setText(props.get(ApplicationLiterals.DB_PORT));
        directory.setText(props.get(ApplicationLiterals.MY_SQL_DIR));
        backup.setText(props.get(ApplicationLiterals.MY_SQL_BACKUP));
    }

//    private void updateProperties() {
//        String configFile = ReadConfig.getConfigFile(ApplicationLiterals.getLaunchPath());
//        configFile = configFile
//                .replace("bin/", ApplicationLiterals.EMPTY)
//                .replace(ApplicationLiterals.DOUBLE_SLASH, ApplicationLiterals.SLASH);
//
//        try {
//            PropertiesConfiguration config = new PropertiesConfiguration(configFile);
//
//            config.setProperty(ApplicationLiterals.DB_URL, url.getText().trim());
//            config.setProperty(ApplicationLiterals.MY_SQL_CLASS, mySqlClass.getText().trim());
//            config.setProperty(ApplicationLiterals.DB_USER, username.getText().trim());
//            config.setProperty(ApplicationLiterals.DB_PASS, password.getText().trim());
//            config.setProperty(ApplicationLiterals.DB_PORT, port.getText().trim());
//            config.setProperty(ApplicationLiterals.MY_SQL_DIR, directory.getText().trim());
//            config.setProperty(ApplicationLiterals.MY_SQL_BACKUP, backup.getText().trim());
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
