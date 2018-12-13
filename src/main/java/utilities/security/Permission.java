package utilities.security;

import domain.beans.SystemSettings;
import persistence.Connect;
import utilities.settings.SettingsService;

import javax.swing.JOptionPane;

public class Permission {

    public static boolean isUserAllowedToChangePass() {
        SystemSettings settings = SettingsService.getSystemSettings();
        String user = Connect.getCurrentUser();

        String root = settings.getRootUser();
        String admin = settings.getAdminUser();

        if (user.equalsIgnoreCase(root) || user.equalsIgnoreCase(admin)) {
            JOptionPane.showMessageDialog(null,
                    "Cannot use forgot password for this type of user!",
                    "Unauthorized", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
