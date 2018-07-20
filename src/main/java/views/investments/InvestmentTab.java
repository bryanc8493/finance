package views.investments;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import persistence.Connect;
import persistence.finance.InvestmentData;
import views.common.Loading;
import views.common.components.ApplicationControl;
import views.common.components.MultiLabelButton;
import views.common.components.RequestFocusListener;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class InvestmentTab extends JPanel {

    private static final long serialVersionUID = -3456368332519377049L;
    private static Logger logger = Logger.getLogger(InvestmentTab.class);

    public final static JButton fidelity = new MultiLabelButton("Update 401K", MultiLabelButton.BOTTOM, Icons.FIDELITY_ICON);
    public final static JButton janus = new MultiLabelButton("Update Janus", MultiLabelButton.BOTTOM, Icons.JANUS_ICON);
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
        investContent.setBorder(BorderFactory.createCompoundBorder(
                ApplicationLiterals.PADDED_SPACE,
                BorderFactory.createTitledBorder("Investment Actions:")));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(investContent, BorderLayout.NORTH);

        this.setLayout(new BorderLayout());
        this.add(new Title("Investments"), BorderLayout.NORTH);
        this.add(wrapper, BorderLayout.CENTER);
        this.add(ApplicationControl.closeAndLogout(con,
                    (JFrame) SwingUtilities.getRoot(this)),
                    BorderLayout.SOUTH);

        fidelity.addActionListener(e -> {
            JFormattedTextField tf = new JFormattedTextField(
                    ApplicationLiterals.getCurrencyFormat());
            tf.setColumns(10);
            tf.setValue(0.0);
            tf.setFont(ApplicationLiterals.APP_FONT);
            tf.addAncestorListener(new RequestFocusListener());
            int input = JOptionPane.showConfirmDialog(null, tf,
                    "Updated Fidelity Balance",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            if (input == JOptionPane.OK_OPTION) {
                String balance = tf
                        .getText()
                        .replace(ApplicationLiterals.DOLLAR,
                                ApplicationLiterals.EMPTY)
                        .replace(ApplicationLiterals.COMMA,
                                ApplicationLiterals.EMPTY);

                InvestmentData.updateInvestmentAccount(con,
                        ApplicationLiterals.FIDELITY, balance);
                JOptionPane.showMessageDialog(null,
                        "Updated Fidelity Table", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        janus.addActionListener(e -> {
            JFormattedTextField tf = new JFormattedTextField(
                    ApplicationLiterals.getCurrencyFormat());
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

                InvestmentData.updateInvestmentAccount(con,
                        ApplicationLiterals.JANUS, balance);
                JOptionPane.showMessageDialog(null, "Updated Janus Table",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        fidelityV.addActionListener(e -> InvestmentData.getLatestFidelityBalance(con));

        janusV.addActionListener(e -> InvestmentData.getLatestJanusBalance(con));
    }
}
