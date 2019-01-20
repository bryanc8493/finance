package views.payments;

import domain.dto.FinancingSummary;
import literals.ApplicationLiterals;
import literals.Icons;
import services.FinancingService;
import views.common.MainMenu;
import views.common.components.PrimaryButton;
import views.common.components.RequestFocusListener;
import views.common.components.Title;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

public class FinancedPayment extends JFrame {

    private List<FinancingSummary> financedData;
    private JButton cancel;
    private JButton payment;
    private List<JRadioButton> paymentSelections;
    private ButtonGroup selectionGroup;

    public FinancedPayment() {
        paymentSelections = new ArrayList<>();
        selectionGroup = new ButtonGroup();
        cancel = new PrimaryButton("Cancel");
        payment = new PrimaryButton("Make Payment");
        payment.setEnabled(false);

        final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(cancel);
        buttons.add(payment);

        final JPanel content = new JPanel(new BorderLayout());
        content.add(getFinancedDataListPanel(), BorderLayout.CENTER);
        content.setBorder(BorderFactory.createCompoundBorder(
                ApplicationLiterals.PADDED_SPACE,
                BorderFactory.createTitledBorder("Active Financed Purchases"))
        );

        final JPanel main = new JPanel(new BorderLayout());
        main.add(new Title("Make New Payment"), BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        add(main);
        setTitle("New Payment");
        setIconImage(Icons.APP_ICON.getImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        JRootPane rp = SwingUtilities.getRootPane(cancel);
        rp.setDefaultButton(cancel);
        rp.setBorder(ApplicationLiterals.PADDED_SPACE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

        cancel.addActionListener(e -> cancel());
        payment.addActionListener(e -> makePayment());
    }

    private void cancel() {
        dispose();
        MainMenu.modeSelection(2);
    }

    private void makePayment() {
        Double amount = getUserAmountInput();

        if (amount == 0.0) {
            JOptionPane.showMessageDialog(this,
                "Amount must be greater than 0",
                    "Invalid Amount",
                    JOptionPane.ERROR_MESSAGE
            );
        } else if (amount == -1.0) {
            // do nothing, let dialog close
        } else {
            for (JRadioButton selection : paymentSelections) {
                if (selection.isSelected()) {
                boolean isInserted = FinancingService.addNewPayment(
                    selection.getActionCommand(),
                    amount
                );
                    if (isInserted) {
                        JOptionPane.showMessageDialog(this,
                            "Payment made successfully",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        dispose();
                        MainMenu.modeSelection(2);
                    }
                    break;
                }
            }
        }

    }

    private Double getUserAmountInput() {
        final JFormattedTextField input = new JFormattedTextField(ApplicationLiterals.getCurrencyFormat());
        input.setColumns(5);
        input.setValue(0.0);
        input.setCaretPosition(1);
        input.addAncestorListener(new RequestFocusListener());
        int okCancel = JOptionPane.showConfirmDialog(
            this,
                input,
                "Amount to pay:",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (okCancel == JOptionPane.OK_OPTION) {
            String parsed = input.getText()
                .replace(ApplicationLiterals.COMMA, ApplicationLiterals.EMPTY)
                .replace(ApplicationLiterals.DOLLAR, ApplicationLiterals.EMPTY)
                .trim();
            return Double.parseDouble(parsed);
        }

        return -1.0;
    }

    private JPanel getFinancedDataListPanel() {
        final JPanel viewList = new JPanel();
        viewList.setLayout(new BoxLayout(viewList, BoxLayout.Y_AXIS));

        financedData = FinancingService.getFinancingSummaryList();

        for (FinancingSummary item : financedData) {
            final JRadioButton rb = new JRadioButton();
            rb.setActionCommand(item.getUniqueId().toString());
            final JLabel title = new JLabel(item.getTitle());
            final JLabel amount = new JLabel(item.getRemaining().toString());
            paymentSelections.add(rb);
            selectionGroup.add(rb);

            final JPanel lineItem = new JPanel(new FlowLayout(FlowLayout.LEADING, 20,10));
            lineItem.add(rb);
            lineItem.add(title);
            lineItem.add(amount);
            lineItem.add(setDateLabel(item));

            viewList.add(lineItem);
            rb.addActionListener(e -> enablePayment());
        }

        return viewList;
    }

    private void enablePayment() {
        payment.setEnabled(true);
        pack();
    }

    private JLabel setDateLabel(FinancingSummary item) {
        JLabel date = new JLabel();

        if (item.getLastPaymentDate() == null) {
            date.setText("---");
        } else {
            date.setText(
                ApplicationLiterals.MONTH_DAY_YEAR.format(
                    item.getLastPaymentDate()
                )
            );
        }

        return date;
    }

}
