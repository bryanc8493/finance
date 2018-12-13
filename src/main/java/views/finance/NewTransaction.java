package views.finance;

import domain.beans.Transaction;
import domain.beans.UserSettings;
import literals.ApplicationLiterals;
import literals.Icons;
import org.apache.log4j.Logger;
import org.jdatepicker.impl.JDatePickerImpl;
import persistence.finance.Transactions;
import program.PersonalFinance;
import utilities.CommonConfigValues;
import utilities.DateUtility;
import utilities.settings.SettingsService;
import views.common.MainMenu;
import views.common.components.HintTextField;
import views.common.components.PrimaryButton;
import views.common.components.PromptComboBoxRenderer;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class NewTransaction {

    private static Logger logger = Logger.getLogger(NewTransaction.class);
    private static String[] expenseCategories;
    private static String[] incomeCategories;
    private static Set<JRadioButton> creditCardRadios;
    private static JComboBox<String> selectCategory = new JComboBox<>();

    private final static JTextField descField = new HintTextField("Description", false, 12);
    private final static JTextField storeField = new HintTextField("Store", false, 12);
    private final static JTextField titleField = new HintTextField("Transaction Title", false, 12);
    private final static JFormattedTextField amountField = new JFormattedTextField(
            ApplicationLiterals.getCurrencyFormat());

    public static void InsertFrame() {
        logger.debug("Displaying GUI to insert new transaction");
        final UserSettings settings = SettingsService.getCurrentUserSettings();

        Set<String> expenseCategoriesSet = settings.getExpenseCategories();
        Set<String> incomeCategoriesSet = settings.getIncomeCategories();
        expenseCategories = expenseCategoriesSet.toArray(new String[expenseCategoriesSet.size()]);
        incomeCategories = incomeCategoriesSet.toArray(new String[incomeCategoriesSet.size()]);
        final String[] TYPE_CATEGORIES = { "Expense", "Income" };

        final JFrame frame = new JFrame(ApplicationLiterals.APP_TITLE);

        final JComboBox<String> typeCb = new JComboBox<>(TYPE_CATEGORIES);
        typeCb.setFont(ApplicationLiterals.APP_FONT);
        typeCb.setSelectedIndex(-1);
        typeCb.setRenderer(new PromptComboBoxRenderer("Select Type"));
        typeCb.setPrototypeDisplayValue(" temp prototype value ");

        final JDatePickerImpl datePicker = DateUtility.getDatePicker();

        amountField.setColumns(10);
        amountField.setValue(0.0);
        amountField.setFont(ApplicationLiterals.APP_FONT);

        creditCardRadios = new LinkedHashSet<>();
        final Set<String> creditCards = CommonConfigValues.getCreditCards();

        final JCheckBox credit = new JCheckBox("  Credit");

        final JButton insert = new PrimaryButton("    Insert    ");
        final JButton back = new PrimaryButton("    < Back    ");
        final JButton close = new PrimaryButton("    Close    ");

        final JLabel missingField = new JLabel(
                "Please fill in all fields with a *");
        missingField.setForeground(Color.RED);
        missingField.setVisible(false);

        JPanel details = new JPanel(new BorderLayout(0,10));
        details.add(titleField, BorderLayout.NORTH);
        details.add(descField, BorderLayout.CENTER);
        details.add(storeField, BorderLayout.SOUTH);
        details.setBorder(BorderFactory.createCompoundBorder(
                ApplicationLiterals.PADDED_SPACE,
                BorderFactory.createTitledBorder("General Details:")));

        JPanel typeAndCategory = new JPanel(new BorderLayout(0, 10));
        selectCategory.setPrototypeDisplayValue(" temp prototype value ");
        typeAndCategory.add(typeCb, BorderLayout.NORTH);
        typeAndCategory.add(selectCategory, BorderLayout.SOUTH);
        typeAndCategory.setBorder(BorderFactory.createCompoundBorder(
                ApplicationLiterals.PADDED_SPACE,
                BorderFactory.createTitledBorder("Type and Category:")));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        top.add(details);
        top.add(typeAndCategory);

        JPanel creditCardSelection = new JPanel(new GridLayout(determineRows(creditCards.size()),2,5,5));
        ButtonGroup group = new ButtonGroup();
        for(String card : creditCards) {
            JRadioButton radio = new JRadioButton(card);
            group.add(radio);
            creditCardRadios.add(radio);
            creditCardSelection.add(radio);
        }
        creditCardSelection.setVisible(false);

        JPanel amountDetails = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amountDetails.add(datePicker);
        amountDetails.add(amountField);
        amountDetails.add(credit);

        JPanel amountDetailsWrapper = new JPanel(new BorderLayout(0,10));
        amountDetailsWrapper.add(amountDetails, BorderLayout.NORTH);
        amountDetailsWrapper.add(creditCardSelection, BorderLayout.SOUTH);
        amountDetailsWrapper.setBorder(BorderFactory.createCompoundBorder(
                ApplicationLiterals.PADDED_SPACE,
                BorderFactory.createTitledBorder("Amount and Date")));

        JPanel content = new JPanel(new BorderLayout(0, 5));
        content.add(top, BorderLayout.NORTH);
        content.add(amountDetailsWrapper, BorderLayout.SOUTH);

        JPanel missing = new JPanel();
        missing.setLayout(new FlowLayout(FlowLayout.CENTER));
        missing.add(missingField);
        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.add(content);
        middle.add(missing);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttons.add(back);
        buttons.add(close);
        buttons.add(insert);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        JLabel frameTitle = new Title("Insert Transaction");
        main.add(frameTitle, BorderLayout.NORTH);
        main.add(middle, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        frame.add(main);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        JRootPane rp = SwingUtilities.getRootPane(insert);
        rp.setDefaultButton(insert);
        rp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        close.addActionListener(e -> {
            frame.dispose();
            logger.info("Closed by user");
            PersonalFinance.appLogger.logFooter();
            System.exit(0);
        });

        back.addActionListener(e -> {
            frame.dispose();
            MainMenu.modeSelection(0);
        });

        selectCategory.addActionListener(e -> {
            try {
                if (selectCategory.getSelectedItem().toString().equalsIgnoreCase("Credit Card")) {
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Would you like to pay existing outstanding credit charges?", "Confirm",
                            JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        new CreditPayments();
                        resetDefaults();
                    }
                }
            } catch (Exception ex) { }
        });

        typeCb.addActionListener(e -> {
            if (typeCb.getSelectedItem().toString()
                    .equalsIgnoreCase(ApplicationLiterals.EXPENSE)) {
                addCategories(true);
                storeField.setVisible(true);
                credit.setVisible(true);
            } else if (typeCb.getSelectedItem().toString()
                    .equalsIgnoreCase(ApplicationLiterals.INCOME)) {
                addCategories(false);
                storeField.setVisible(false);
                credit.setSelected(false);
                credit.setVisible(false);
            }
        });

        credit.addActionListener(e -> {
            creditCardSelection.setVisible(credit.isSelected());
            frame.pack();
        });

        insert.addActionListener(e -> {
            // First will check if any required fields are not filled in
            String input_title = titleField.getText().trim();
            String input_amount = amountField
                    .getText()
                    .replace(ApplicationLiterals.DOLLAR,
                            ApplicationLiterals.EMPTY).trim();
            input_amount = input_amount.replace(ApplicationLiterals.COMMA,
                    ApplicationLiterals.EMPTY);
            Date selectedDate = (Date) datePicker.getModel().getValue();
            String input_trans_date = ApplicationLiterals.YEAR_MONTH_DAY
                    .format(selectedDate);

            // Second will check if the date is in the correct format
            if (input_trans_date.length() != 10) {
                logger.warn("Date not in correct format, must be 10 total characters: 'YYYY-MM-DD'");
            } else if (input_title.equals(ApplicationLiterals.EMPTY)
                    || input_amount.equals("0.00")
                    || input_trans_date.contains(ApplicationLiterals.UNDERSCORE)
                    || typeCb.getSelectedItem().toString().equals(ApplicationLiterals.EMPTY)) {
                logger.warn("Missing some required fields");
                missingField.setText("Please fill in all required fields");
                missingField.setVisible(true);
                frame.pack();
            } else if (credit.isSelected() && getSelectedCreditCard() == null) {
                logger.warn("Credit Card not selected");
                missingField.setText("Please select credit card");
                missingField.setVisible(true);
                frame.pack();
            }

            else {
                try {
                    typeCb.getSelectedItem().toString();
                    selectCategory.getSelectedItem().toString();
                } catch (NullPointerException ex) {
                    logger.warn("Income or expense must be selected as well as a category!");
                    missingField.setVisible(true);
                    frame.pack();
                    return;
                }

                Transaction tran = new Transaction();
                tran.setTitle(input_title);
                tran.setCategory(selectCategory.getSelectedItem()
                        .toString());

                if (typeCb.getSelectedItem().toString()
                        .equalsIgnoreCase(ApplicationLiterals.EXPENSE)) {
                    tran.setType(ApplicationLiterals.EXPENSE);
                    tran.setCombinedAmount(ApplicationLiterals.DASH + input_amount);
                } else if (typeCb.getSelectedItem().toString()
                        .equalsIgnoreCase(ApplicationLiterals.INCOME)) {
                    tran.setType(ApplicationLiterals.INCOME);
                    tran.setCombinedAmount(input_amount);
                }

                tran.setDate(input_trans_date);
                tran.setAmount(input_amount);
                tran.setDescription(descField.getText());
                tran.setStore(storeField.getText());
                char creditFlag = credit.isSelected() ? '1' : '0';
                tran.setCredit(creditFlag);
                if (creditFlag == '1') {
                    tran.setCreditPaid('0');
                    tran.setCreditCard(getSelectedCreditCard());
                }

                Transactions.addTransaction(tran);

                resetDefaults();
            }
        });
    }

    private static int determineRows(int totalCreditCards) {
        int remainder = totalCreditCards % 2;
        return (totalCreditCards/2) + remainder;
    }

    private static String getSelectedCreditCard() {
        for (JRadioButton radio : creditCardRadios) {
            if(radio.isSelected())
                return radio.getText();
        }
        return null;
    }

    private static void resetDefaults() {
        titleField.setText(ApplicationLiterals.EMPTY);
        selectCategory.setSelectedIndex(-1);
        amountField.setText("0.00");
        amountField.setCaretPosition(1);
        descField.setText(ApplicationLiterals.EMPTY);
        storeField.setText(ApplicationLiterals.EMPTY);
        titleField.requestFocusInWindow();
    }

    private static void addCategories(boolean isExpenses) {

        selectCategory.removeAllItems();
        String[] categories = isExpenses ? expenseCategories : incomeCategories;
        for (String c : categories) {
            selectCategory.addItem(c);
        }
        selectCategory.setFont(ApplicationLiterals.APP_FONT);
        selectCategory.setRenderer(new PromptComboBoxRenderer("Select Category"));
        selectCategory.setSelectedIndex(-1);
    }
}
