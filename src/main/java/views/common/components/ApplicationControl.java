package views.common.components;

import literals.Icons;
import org.apache.log4j.Logger;
import program.PersonalFinance;
import views.common.VerifyAccess;

import javax.swing.*;
import java.awt.*;

public class ApplicationControl {

    private static final Logger logger = Logger.getLogger(ApplicationControl.class);

    public static JPanel closeAndLogout(final JFrame frame) {
        final JButton close = new PrimaryButton("Close", Icons.EXIT_ICON);
        final JButton logOut = new PrimaryButton("Log off", Icons.LOGOFF_ICON);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(logOut, BorderLayout.WEST);
        panel.add(Box.createHorizontalGlue());
        panel.add(close, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        final JPanel panelWrapper = new JPanel(new BorderLayout());
        panelWrapper.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);
        panelWrapper.add(panel, BorderLayout.SOUTH);

        close.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                logger.info("Closed by user");
                PersonalFinance.appLogger.logFooter();
                System.exit(0);
            }
        });

        logOut.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to log out?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                frame.dispose();
                logger.info("Closed by user");

                new VerifyAccess();
            }
        });
        return panelWrapper;
    }
}
