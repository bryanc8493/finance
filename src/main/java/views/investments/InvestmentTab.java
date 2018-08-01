package views.investments;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.Connect;
import persistence.finance.InvestmentData;
import utilities.InvestmentTrendData;
import views.common.Loading;
import views.common.components.ApplicationControl;
import views.common.components.MultiLabelButton;
import views.common.components.RequestFocusListener;
import views.common.components.Title;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.Connection;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Map;

public class InvestmentTab extends JPanel {

    private static final long serialVersionUID = -3456368332519377049L;
    private static Logger logger = Logger.getLogger(InvestmentTab.class);

    public final static JButton fidelity = new MultiLabelButton("Update 401K", MultiLabelButton.BOTTOM, Icons.FIDELITY_ICON);
    public final static JButton janus = new MultiLabelButton("Update Janus", MultiLabelButton.BOTTOM, Icons.JANUS_ICON);

    private final static JLabel thirtyDayLabel = new JLabel("30-Day:");
    private final static JLabel sixtyDayLabel = new JLabel("60-Day:");
    private final static JLabel ninetyDayLabel = new JLabel("90-Day:");
    private final static JLabel yearLabel = new JLabel("1 Year:");

    private static JLabel thirtyDay = new JLabel();
    private static JLabel sixtyDay = new JLabel();
    private static JLabel ninetyDay = new JLabel();
    private static JLabel year = new JLabel();

    private static NumberFormat currency = ApplicationLiterals.getNumberFormat();

    private Connection con;

    public InvestmentTab() {
        logger.debug("Initializing and population Investments Tab");
        Loading.update("Retrieving investment data", 72);

        con = Connect.getConnection();
        final JButton fidelityV = new MultiLabelButton("View 401K", MultiLabelButton.BOTTOM, Icons.FIDELITY_ICON);
        final JButton janusV = new MultiLabelButton("View Janus", MultiLabelButton.BOTTOM, Icons.JANUS_ICON);
        janusV.setEnabled(false);
        janus.setEnabled(false);

        JPanel investContent = new JPanel(new GridLayout(1, 4, 5, 5));
        investContent.add(fidelityV);
        investContent.add(fidelity);
        investContent.add(janusV);
        investContent.add(janus);
        investContent.setBorder(createCompoundBorder("Investment Actions:"));

        setTrendValues();

        JPanel trendContent = new JPanel(new GridLayout(4,2,5,5));
        trendContent.add(thirtyDayLabel);
        trendContent.add(thirtyDay);
        trendContent.add(sixtyDayLabel);
        trendContent.add(sixtyDay);
        trendContent.add(ninetyDayLabel);
        trendContent.add(ninetyDay);
        trendContent.add(yearLabel);
        trendContent.add(year);
        trendContent.setBorder(createCompoundBorder("Historical Trends:"));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(investContent, BorderLayout.NORTH);
        wrapper.add(trendContent, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(new Title("Investments"), BorderLayout.NORTH);
        this.add(wrapper, BorderLayout.CENTER);
        this.add(ApplicationControl.closeAndLogout(con,
                    (JFrame) SwingUtilities.getRoot(this)),
                    BorderLayout.SOUTH);

        fidelity.addActionListener(e -> {
            JFormattedTextField tf = new JFormattedTextField(ApplicationLiterals.getCurrencyFormat());
            tf.setColumns(10);
            tf.setValue(0.0);
            tf.setFont(ApplicationLiterals.APP_FONT);
            tf.addAncestorListener(new RequestFocusListener());
            int input = JOptionPane.showConfirmDialog(null, tf,
                    "Updated Fidelity Balance",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (input == JOptionPane.OK_OPTION) {
                String balance = tf.getText().replace(ApplicationLiterals.DOLLAR, ApplicationLiterals.EMPTY)
                        .replace(ApplicationLiterals.COMMA, ApplicationLiterals.EMPTY);

                InvestmentData.updateInvestmentAccount(con, ApplicationLiterals.FIDELITY, balance);
                JOptionPane.showMessageDialog(null,
                        "Updated Fidelity Table", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        janus.addActionListener(e -> {
            JFormattedTextField tf = new JFormattedTextField(ApplicationLiterals.getCurrencyFormat());
            tf.setColumns(10);
            tf.setValue(0.0);
            tf.setFont(ApplicationLiterals.APP_FONT);
            tf.addAncestorListener(new RequestFocusListener());
            int input = JOptionPane.showConfirmDialog(null, tf,
                    "Updated Janus Balance", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (input == JOptionPane.OK_OPTION) {
                String balance = tf.getText().replace("$", "")
                        .replace(",", "");

                InvestmentData.updateInvestmentAccount(con, ApplicationLiterals.JANUS, balance);
                JOptionPane.showMessageDialog(null, "Updated Janus Table",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        fidelityV.addActionListener(e -> InvestmentData.getLatestFidelityBalance(con));

        janusV.addActionListener(e -> InvestmentData.getLatestJanusBalance(con));
    }

    private Border createCompoundBorder(String label) {
        return BorderFactory.createCompoundBorder(
            ApplicationLiterals.PADDED_SPACE,
            BorderFactory.createTitledBorder(label)
        );
    }

    private void setTrendValues() {
        Map<LocalDate, Double> trendData = InvestmentData.getInvestmentData(ApplicationLiterals.FIDELITY);

        Double thirtyData = InvestmentTrendData.determineTrendAmount(30, trendData);
        Double sixtyData = InvestmentTrendData.determineTrendAmount(60, trendData);
        Double ninetyData = InvestmentTrendData.determineTrendAmount(90, trendData);
        Double yearData = InvestmentTrendData.determineTrendAmount(365, trendData);

        thirtyDay.setText("$ " + currency.format(thirtyData));
        sixtyDay.setText("$ " + currency.format(sixtyData));
        ninetyDay.setText("$ " + currency.format(ninetyData));
        year.setText("$ " + currency.format(yearData));
    }
}
