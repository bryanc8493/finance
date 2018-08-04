package views.investments;

import beans.InvestmentTrend;
import literals.ApplicationLiterals;
import literals.Icons;
import literals.enums.InvestmentAccount;
import org.apache.log4j.Logger;
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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Map;

public class InvestmentTab extends JPanel {

    private static final long serialVersionUID = -3456368332519377049L;
    private static Logger logger = Logger.getLogger(InvestmentTab.class);

    public final static JButton fidelity = new MultiLabelButton("Update 401K", MultiLabelButton.BOTTOM, Icons.FIDELITY_ICON);
    public final static JButton janus = new MultiLabelButton("Update Janus", MultiLabelButton.BOTTOM, Icons.JANUS_ICON);

    private final static JLabel thirtyDayLabel = new JLabel();
    private final static JLabel sixtyDayLabel = new JLabel();
    private final static JLabel ninetyDayLabel = new JLabel();
    private final static JLabel yearLabel = new JLabel("YTD:");

    private final static JLabel thirtyDayPercent = new JLabel();
    private final static JLabel sixtyDayPercent = new JLabel();
    private final static JLabel ninetyDayPercent = new JLabel();
    private final static JLabel yearPercent = new JLabel();

    private static JLabel thirtyDay = new JLabel();
    private static JLabel sixtyDay = new JLabel();
    private static JLabel ninetyDay = new JLabel();
    private static JLabel year = new JLabel();

    private static NumberFormat currency = ApplicationLiterals.getNumberFormat();

    public InvestmentTab() {
        logger.debug("Initializing and population Investments Tab");
        Loading.update("Retrieving investment data", 72);

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

        JPanel trendContent = new JPanel(new GridLayout(4,3,0,5));
        trendContent.add(thirtyDayLabel);
        trendContent.add(thirtyDayPercent);
        trendContent.add(thirtyDay);
        trendContent.add(sixtyDayLabel);
        trendContent.add(sixtyDayPercent);
        trendContent.add(sixtyDay);
        trendContent.add(ninetyDayLabel);
        trendContent.add(ninetyDayPercent);
        trendContent.add(ninetyDay);
        trendContent.add(yearLabel);
        trendContent.add(yearPercent);
        trendContent.add(year);
        trendContent.setBorder(createCompoundBorder("Historical Trends:"));

        JPanel dataContent = new JPanel(new GridLayout(1,2));
        dataContent.add(trendContent);
        dataContent.add(new JPanel());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(investContent, BorderLayout.NORTH);
        wrapper.add(dataContent, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(new Title("Investments"), BorderLayout.NORTH);
        this.add(wrapper, BorderLayout.CENTER);
        this.add(ApplicationControl.closeAndLogout((JFrame) SwingUtilities.getRoot(this)), BorderLayout.SOUTH);

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

                InvestmentData.updateInvestmentAccount(InvestmentAccount.FIDELITY, balance);
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

                InvestmentData.updateInvestmentAccount(InvestmentAccount.JANUS, balance);
                JOptionPane.showMessageDialog(null, "Updated Janus Table",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        fidelityV.addActionListener(e -> InvestmentData.getLatestAccountBalance(InvestmentAccount.FIDELITY));

        janusV.addActionListener(e -> InvestmentData.getLatestAccountBalance(InvestmentAccount.JANUS));
    }

    private Border createCompoundBorder(String label) {
        return BorderFactory.createCompoundBorder(
            ApplicationLiterals.PADDED_SPACE,
            BorderFactory.createTitledBorder(label)
        );
    }

    private void setTrendValues() {
        Map<LocalDate, Double> trendData = InvestmentData.getInvestmentData(InvestmentAccount.FIDELITY);

        InvestmentTrend thirtyData = InvestmentTrendData.determineTrendAmount(30, trendData, InvestmentAccount.FIDELITY);
        InvestmentTrend sixtyData = InvestmentTrendData.determineTrendAmount(60, trendData, InvestmentAccount.FIDELITY);
        InvestmentTrend ninetyData = InvestmentTrendData.determineTrendAmount(90, trendData, InvestmentAccount.FIDELITY);
        InvestmentTrend yearData = InvestmentTrendData.determineTrendAmount(365, trendData, InvestmentAccount.FIDELITY);

        thirtyDay.setText("$ " + currency.format(thirtyData.getTrendPeriodAmount()));
        sixtyDay.setText("$ " + currency.format(sixtyData.getTrendPeriodAmount()));
        ninetyDay.setText("$ " + currency.format(ninetyData.getTrendPeriodAmount()));
        year.setText("$ " + currency.format(yearData.getTrendPeriodAmount()));

        setTrendPercentLabel(thirtyDayPercent, thirtyData);
        setTrendPercentLabel(sixtyDayPercent, sixtyData);
        setTrendPercentLabel(ninetyDayPercent, ninetyData);
        setTrendPercentLabel(yearPercent, yearData);

        thirtyDayLabel.setText(thirtyData.getActualTrendPeriod() + " Day:");
        sixtyDayLabel.setText(sixtyData.getActualTrendPeriod() + " Day:");
        ninetyDayLabel.setText(ninetyData.getActualTrendPeriod() + " Day:");
    }

    private void setTrendPercentLabel(JLabel label, InvestmentTrend data) {
        String formattedPercent = ApplicationLiterals.DOUBLE_FORMAT.format(data.getTrendPercent());
        label.setText(formattedPercent + ApplicationLiterals.PERCENT);

        if (data.getTrendPercent() > 0.0) {
            label.setForeground(ApplicationLiterals.APP_GREEN);
        } else {
            label.setForeground(Color.RED);
        }

        label.setFont(ApplicationLiterals.BOLD_FONT);
    }
}
