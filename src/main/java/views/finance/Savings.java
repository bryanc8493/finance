package views.finance;

import domain.beans.UserSettings;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.finance.BalanceData;
import utilities.settings.SettingsService;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Savings {

    private JFrame frame;

    private JLabel totalLbl = new JLabel("Total Amount");
    private JLabel safetyLbl = new JLabel("Emergency");
    private JLabel houseLbl = new JLabel("House");

    private JLabel total = new JLabel();
    private JLabel safety = new JLabel();
    private JLabel house = new JLabel();

    private UserSettings userSettings;

    private Locale locale = new Locale("en", "US");
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    private final Logger logger = Logger.getLogger(Savings.class);

    public Savings() {
        userSettings = SettingsService.getCurrentUserSettings();
        logger.debug("Displaying Savings account data");
        frame = new JFrame("Savings Summary");

        setAmounts();
        alignRight();
        setFont();

        JLabel title = new Title("Savings Account Details");
        JButton close = new PrimaryButton("Close");

        JPanel content = new JPanel(new GridLayout(3,2, 0, 10));
        content.add(totalLbl); content.add(total);
        content.add(safetyLbl); content.add(safety);
        content.add(houseLbl); content.add(house);
        content.setBorder(BorderFactory.createEmptyBorder(20,30,20,30));

        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER));
        button.add(close);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JRootPane rp = SwingUtilities.getRootPane(close);
        rp.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
        rp.setDefaultButton(close);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        close.addActionListener((e) -> frame.dispose());
    }

    private void setAmounts() {
        double totalSavings = BalanceData.getSavingsBalance();
        double safetyAmt = userSettings.getSavingsSafetyAmount();
        double remainder = totalSavings - safetyAmt;

        total.setText(currencyFormatter.format(totalSavings));
        safety.setText(currencyFormatter.format(safetyAmt));
        house.setText(currencyFormatter.format(remainder));

        safety.setForeground(Color.RED);
    }

    private void alignRight() {
        total.setHorizontalAlignment(JLabel.RIGHT);
        safety.setHorizontalAlignment(JLabel.RIGHT);
        house.setHorizontalAlignment(JLabel.RIGHT);
    }

    private Set<JLabel> getLabelList() {
        Set<JLabel> labels = new HashSet<>();
        labels.add(total);
        labels.add(totalLbl);
        labels.add(safety);
        labels.add(safetyLbl);
        labels.add(house);
        labels.add(houseLbl);
        return labels;
    }

    private void setFont() {
        final Font font = new Font("sans serif", Font.BOLD, 16);
        for (JLabel l : getLabelList()) {
            l.setFont(font);
        }
    }
}
