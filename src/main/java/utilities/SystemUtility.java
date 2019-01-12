package utilities;

import domain.beans.SystemSettings;
import literals.ApplicationLiterals;
import org.apache.log4j.Logger;
import utilities.settings.SettingsService;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemUtility {

    private static Logger logger = Logger.getLogger(SystemUtility.class);

    public static String[] getSystemInfo() {
        InetAddress ip;
        String hostname;
        String username;
        String[] data = new String[3];
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            username = System.getProperty(ApplicationLiterals.USER_NAME);
            data[0] = ip.toString();
            data[1] = hostname;
            data[2] = username;
        } catch (UnknownHostException e) {
            logger.warn("Unable to log system info");
        }
        return data;
    }

    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDirectory(new File(dir, child));
                if (!success)
                    return false;
            }
        }
        return dir.delete();
    }

    public static boolean inDevelopment() {
        SystemSettings settings = SettingsService.getSystemSettings();

        String startDir = System.getProperty(ApplicationLiterals.USER_DIR)
                .replace("\\", "/");

        return startDir.equalsIgnoreCase(settings.getDevelopmentLocation());
    }
}
