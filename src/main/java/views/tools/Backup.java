package views.tools;

import domain.beans.UserSettings;
import literals.ApplicationLiterals;
import literals.Icons;
import literals.enums.Databases;
import org.apache.log4j.Logger;
import utilities.ReadConfig;
import utilities.SystemUtility;
import utilities.exceptions.AppException;
import utilities.security.Encoding;
import utilities.settings.SettingsService;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Backup {

    private final Logger logger = Logger.getLogger(Backup.class);
    private UserSettings settings;

    private final String MYSQL_DIR = ReadConfig.getConfigValue(ApplicationLiterals.MY_SQL_DIR);

    public Backup() {
        logger.debug("showing backup modal");
        settings = SettingsService.getCurrentUserSettings();

        final JFrame frame = new JFrame("Backup Status");
        JLabel title = new Title("Backup Status");

        String lastBackupTime = getLastBackupTime(ApplicationLiterals.FULL_DATE);

        JLabel backupTime = new JLabel("<html><b>Last Backed Up:</b>&emsp;"
                + lastBackupTime + "</html>", SwingConstants.CENTER);
        backupTime.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton close = new PrimaryButton("Close");
        JButton backup = new PrimaryButton("Backup Now");

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(backup);
        backup.setVisible(enableBackup(ApplicationLiterals.YEAR_MONTH_DAY_CONDENSED));
        buttons.add(close);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(backupTime, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JRootPane rp = SwingUtilities.getRootPane(close);
        rp.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        rp.setDefaultButton(close);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        close.addActionListener(e -> frame.dispose());

        backup.addActionListener(e -> {
            frame.dispose();
            performBackup(settings.getBackupLocation());
        });
    }

    private String getLastBackupTime(SimpleDateFormat returnFormat) {
        File[] array = new File(settings.getBackupLocation()).listFiles();
        long lastBackupDate = 0;

        if (array == null || array.length == 0) {
            return "Never Backed Up";
        }

        for (File x : array) {
            long temp = x.lastModified();
            if (temp > lastBackupDate) {
                lastBackupDate = temp;
            }
        }
        return returnFormat.format(lastBackupDate);
    }

    private void performBackup(String backupDir) {
        String todaysDir = "backup_" + getToday();
        File fullDir = new File(backupDir + ApplicationLiterals.SLASH
                + todaysDir);
        fullDir.mkdirs();

        try {
            String financialBackupScript = generateBackupCommand(fullDir,
                    Databases.FINANCIAL);
            String accountsBackupScript = generateBackupCommand(fullDir,
                    Databases.ACCOUNTS);

            Runtime.getRuntime().exec("cmd /c " + financialBackupScript);
            Runtime.getRuntime().exec("cmd /c " + accountsBackupScript);

            int olderBackups = deleteOtherBackups();

            String msg = "Successfully backed up databases!";
            if (olderBackups > 0) {
                msg = msg + "\nIncluding removing " + olderBackups
                        + " older backups";
            }

            JOptionPane.showMessageDialog(null, msg, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            new Backup();
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    private String generateBackupCommand(File backupDir, Databases database)
            throws GeneralSecurityException, IOException {
        String port = ReadConfig.getConfigValue(ApplicationLiterals.DB_PORT);
        return "\"" + MYSQL_DIR + "mysqldump.exe\" -e -uroot " + "-p"
                + Encoding.decrypt(ApplicationLiterals.getRootPassword())
                + " -hlocalhost " + "-P" + port + ApplicationLiterals.SPACE
                + database + " > " + backupDir + "\\" + database + ".sql";
    }

    private boolean enableBackup(SimpleDateFormat format) {
        String lastBackup = getLastBackupTime(format);

        return (!getToday().equals(lastBackup));
    }

    private String getToday() {
        return ApplicationLiterals.YEAR_MONTH_DAY_CONDENSED.format(new Date());
    }

    private int deleteOtherBackups() {
        File[] files = new File(settings.getBackupLocation()).listFiles();

        int deletedBackups = 0;
        for (File f : files) {
            if (!f.getName().contains(getToday())) {
                if (SystemUtility.deleteDirectory(f)) {
                    deletedBackups++;
                }
            }
        }
        return deletedBackups;
    }
}
