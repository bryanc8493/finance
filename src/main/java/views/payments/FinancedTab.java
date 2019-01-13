package views.payments;

import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import views.common.components.ApplicationControl;
import views.common.components.MultiLabelButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;

public class FinancedTab extends JPanel {

    private static Logger logger = Logger.getLogger(FinancedTab.class);

    public FinancedTab() {
        logger.debug("Initializing and populating Financed Tab");

        final JButton view = new MultiLabelButton(" View Details ", MultiLabelButton.BOTTOM, Icons.VIEW_ICON);
        final JButton add = new MultiLabelButton("New Purchase", MultiLabelButton.BOTTOM, Icons.ADD_ICON);
        final JButton pay = new MultiLabelButton("Make Payment", MultiLabelButton.BOTTOM, Icons.BANK_ICON);

        JLabel title = new Title("Financed Purchases");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(view);
        buttons.add(add);
        buttons.add(pay);

        JPanel content = new JPanel(new BorderLayout());
        content.add(buttons, BorderLayout.NORTH);
//        content.add(getPartialAddressData(), BorderLayout.SOUTH);

        content.setBorder(ApplicationLiterals.PADDED_SPACE);

        this.setLayout(new BorderLayout(10, 10));
        this.add(title, BorderLayout.NORTH);
        this.add(content, BorderLayout.CENTER);
        this.add(ApplicationControl.closeAndLogout((JFrame) SwingUtilities.getRoot(this)), BorderLayout.SOUTH);

        view.addActionListener(e ->  viewDetails());

        pay.addActionListener(e -> makePayment());

        add.addActionListener(e -> newPurchase());
    }

    private void viewDetails() {
        System.out.println("view clicked");
    }

    private void makePayment() {
        System.out.println("payment clicked");
    }

    private void newPurchase() {
        System.out.println("new purchase clicked");
    }
}
