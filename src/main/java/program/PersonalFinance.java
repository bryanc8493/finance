package program;

import org.apache.log4j.Logger;
import utilities.AppLogger;
import utilities.SystemUtility;
import views.common.Loading;
import views.common.VerifyAccess;
import views.reminders.ModifyReminders;

import javax.swing.UIManager;
import java.util.Arrays;

public class PersonalFinance {

    private static Logger logger;
    public static AppLogger appLogger;

    public static void main(String[] args) {
        appLogger = new AppLogger();
        logger = Logger.getLogger(PersonalFinance.class);

        if (args.length > 0) {
            if (isValidArgs(args)) {
                new ModifyReminders(true);
            }
        } else{
            runApp();
        }
    }

    private static boolean isValidArgs(String[] args) {
        if (args.length != 1) {
            logger.fatal("Invalid args passed: " + Arrays.toString(args));
        } else if (args[0].equalsIgnoreCase("-checkReminders")) {
            logger.info("Programmatically checking for reminders...");
            return true;
        }
        logger.fatal("Invalid arg passed in first position: " + args[0]);
        return false;
    }

    public static void runApp() {
        boolean isValidationRequired = true;
        setLookAndFeel();

        if (SystemUtility.inDevelopment())
            isValidationRequired = false;

        if (isValidationRequired) {
            new VerifyAccess();
        } else {
            logger.debug("Skipping authentication - working from dev workspace");
            new Loading("ROOT");
        }
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            logger.warn("Unable to set nimbus theme");
        }
    }
}
