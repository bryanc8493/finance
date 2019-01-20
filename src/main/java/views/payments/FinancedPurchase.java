package views.payments;

import domain.dto.FinancingPurchase;
import literals.ApplicationLiterals;
import literals.Icons;
import org.jdatepicker.impl.JDatePickerImpl;
import services.FinancingService;
import utilities.DateUtility;
import views.common.MainMenu;
import views.common.components.JLeftLabel;
import views.common.components.PrimaryButton;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class FinancedPurchase extends JFrame {

    private JLabel dateLabel;
    private JLabel totalLabel;
    private JLabel storeLabel;
    private JLabel descLabel;

    private JDatePickerImpl datePicker;
    private JFormattedTextField total;
    private JTextField store;
    private JTextField desc;

    private JButton close;
    private JButton add;

    public FinancedPurchase() {
        initializeComponents();

        final JPanel labelPanel = new JPanel(new GridLayout(7,1));
        labelPanel.add(descLabel);
        labelPanel.add(Box.createRigidArea(new Dimension(0,7)));
        labelPanel.add(dateLabel);
        labelPanel.add(Box.createRigidArea(new Dimension(0,7)));
        labelPanel.add(totalLabel);
        labelPanel.add(Box.createRigidArea(new Dimension(0,7)));
        labelPanel.add(storeLabel);

        final JPanel inputPanel = new JPanel(new GridLayout(4,1));
        inputPanel.add(desc);
        inputPanel.add(datePicker);
        inputPanel.add(total);
        inputPanel.add(store);

        final JPanel content = new JPanel(new FlowLayout(FlowLayout.CENTER));
        content.add(labelPanel);
        content.add(inputPanel);

        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,0));
        buttons.add(close);
        buttons.add(add);

        final JPanel main = new JPanel(new BorderLayout());
        main.add(new Title("New Financed Purchase"), BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        add(main);
        setTitle("New Purchase");
        setIconImage(Icons.APP_ICON.getImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        JRootPane rp = SwingUtilities.getRootPane(close);
        rp.setDefaultButton(close);
        rp.setBorder(ApplicationLiterals.PADDED_SPACE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

        close.addActionListener(e -> cancel());
        add.addActionListener(e -> addNewPurchase());
    }

    private void cancel() {
        dispose();
        MainMenu.modeSelection(2);
    }

    private void addNewPurchase() {
        FinancingPurchase purchase = new FinancingPurchase();
        purchase.setDescription(desc.getText().trim());
        purchase.setStore(store.getText().trim());

        String amount = total.getText()
            .replace(ApplicationLiterals.COMMA, ApplicationLiterals.EMPTY)
            .replace(
                ApplicationLiterals.DOLLAR,
                ApplicationLiterals.EMPTY
            ).trim();

        purchase.setAmount(Double.parseDouble(amount));
        purchase.setDate((Date) datePicker.getModel().getValue());

        if (FinancingService.addNewPurchase(purchase)) {
            dispose();
            MainMenu.modeSelection(2);
        }
    }

    private void initializeComponents() {
        dateLabel = new JLeftLabel("Date:");
        totalLabel = new JLeftLabel("Amount:");
        storeLabel = new JLeftLabel("Store:");
        descLabel = new JLeftLabel("Description:");

        datePicker = DateUtility.getDatePicker();
        total = new JFormattedTextField(ApplicationLiterals.getCurrencyFormat());
        total.setColumns(5);
        total.setValue(0.0);
        total.setCaretPosition(1);
        store = new JTextField();
        desc = new JTextField();

        close = new PrimaryButton("Cancel");
        add = new PrimaryButton("Add");
    }
}
