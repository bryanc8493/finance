package views.finance;

import beans.Transaction;
import literals.Icons;
import views.common.components.PrimaryButton;

import javax.swing.*;
import java.awt.*;

public class TransactionRecord {

    private JTextField tranID = new JTextField();
    private JTextField title = new JTextField();
    private JTextField type = new JTextField();
    private JTextField category = new JTextField();
    private JTextField date = new JTextField();
    private JTextField amount = new JTextField();
    private JTextField description = new JTextField();
    private JTextField credit = new JTextField();
    private JTextField creditPaid = new JTextField();
    private JTextField cardUsed = new JTextField();

    public TransactionRecord(Transaction selectedTran) {

        final JFrame frame = new JFrame("Selected Transaction");

        JLabel tranIDLabel = new JLabel("Transactions ID:                   ");
        JLabel titleLabel = new JLabel("Title:");
        JLabel typeLabel = new JLabel("Type:");
        JLabel categoryLabel = new JLabel("Category:");
        JLabel dateLabel = new JLabel("Date:");
        JLabel amountLabel = new JLabel("Amount:");
        JLabel descriptionLabel = new JLabel("Description:");
        JLabel creditLabel = new JLabel("Credit");
        JLabel creditPaidLabel = new JLabel("Credit Paid:");
        JLabel cardUsedLabel = new JLabel("Credit Card:");

        JButton close = new PrimaryButton("Close");

        JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonHolder.add(close);
        buttonHolder.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        setValues(selectedTran);

        JPanel content = new JPanel(new GridLayout(10, 2, 5, 5));
        content.add(tranIDLabel);
        content.add(tranID);
        content.add(titleLabel);
        content.add(title);
        content.add(typeLabel);
        content.add(type);
        content.add(categoryLabel);
        content.add(category);
        content.add(dateLabel);
        content.add(date);
        content.add(amountLabel);
        content.add(amount);
        content.add(descriptionLabel);
        content.add(description);
        content.add(creditLabel);
        content.add(credit);
        content.add(creditPaidLabel);
        content.add(creditPaid);
        content.add(cardUsedLabel);
        content.add(cardUsed);

        JPanel full = new JPanel(new BorderLayout());
        full.add(content, BorderLayout.CENTER);
        full.add(buttonHolder, BorderLayout.SOUTH);

        frame.add(full);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JRootPane rp = SwingUtilities.getRootPane(close);
        rp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rp.setDefaultButton(close);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        close.addActionListener((e) -> frame.dispose());
    }

    private void setValues(Transaction selectedTran) {

        tranID.setText(selectedTran.getTransactionID());
        title.setText(selectedTran.getTitle());
        type.setText(selectedTran.getType());
        category.setText(selectedTran.getCategory());
        date.setText(selectedTran.getDate());
        amount.setText(selectedTran.getAmount());
        description.setText(selectedTran.getDescription());
        credit.setText(setCreditText(selectedTran.getCredit()));
        creditPaid.setText(setCreditText(selectedTran.getCreditPaid()));
        cardUsed.setText(selectedTran.getCreditCard());

        tranID.setEditable(false);
        title.setEditable(false);
        type.setEditable(false);
        category.setEditable(false);
        date.setEditable(false);
        amount.setEditable(false);
        description.setEditable(false);
        credit.setEditable(false);
        creditPaid.setEditable(false);
        cardUsed.setEditable(false);
    }

    private String setCreditText(char credit) {
        if (credit == '0')
            return "False";
        return "True";
    }
}
