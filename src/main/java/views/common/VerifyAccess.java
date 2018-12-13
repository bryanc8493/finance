package views.common;

import literals.ApplicationLiterals;
import literals.Icons;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import persistence.Connect;
import persistence.accounts.AccountData;
import program.PersonalFinance;
import utilities.security.Permission;
import views.accounts.NewUser;
import views.accounts.UserManagement;
import views.common.components.HintPassField;
import views.common.components.HintTextField;
import utilities.PasswordGenerator;
import utilities.exceptions.AppException;
import utilities.security.Encoding;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VerifyAccess {

    private int attempts = 0;
    private Logger logger = Logger.getLogger(VerifyAccess.class);

    private JFrame frame;

    public VerifyAccess() {
        logger.debug("Displaying GUI Prompting verification");
        frame = new JFrame("Version " + ApplicationLiterals.VERSION);
        JPanel p = new JPanel();
        JLabel title = new JLabel(ApplicationLiterals.APP_TITLE, SwingConstants.CENTER);
        title.setFont(new Font("sans serif", Font.BOLD, 22));
        title.setForeground(Color.darkGray);
        final HintTextField userField = new HintTextField("Username", true);
        final HintPassField passField = new HintPassField("Password", true);


        final JButton submit = new JButton(Icons.LOGIN_ICON);
        submit.setBorder(null);
        submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        final JButton create = new JButton("<html><u>Create Account</u></html>");
        makeButtonLink(create);
        final JButton forgot = new JButton("<html><u>Forgot Password?</u></html>");
        makeButtonLink(forgot);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        passwordPanel.add(passField);
        passField.setColumns(13);

        final JButton showButton = new JButton(Icons.SHOW_PASS_ICON);
        final JButton hideButton = new JButton(Icons.HIDE_PASS_ICON);
        hideButton.setBorder(null);
        hideButton.setVisible(false);
        showButton.setBorder(null);
        passwordPanel.add(showButton);
        passwordPanel.add(hideButton);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottom.add(create);
        bottom.add(forgot);

        JPanel input = new JPanel(new BorderLayout(0, 15));
        input.add(userField, BorderLayout.NORTH);
        input.add(passwordPanel, BorderLayout.CENTER);
        input.add(bottom, BorderLayout.SOUTH);

        p.setLayout(new BorderLayout(0, 15));
        p.add(title, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        p.add(submit, BorderLayout.SOUTH);

        p.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        frame.add(p);
        frame.setIconImage(Icons.APP_ICON.getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        JRootPane rp = SwingUtilities.getRootPane(submit);
        rp.setDefaultButton(submit);

        submit.addActionListener(e -> {
            String username = userField.getText().trim().toUpperCase();
            if (username.equals(ApplicationLiterals.EMPTY)) {
                logger.debug("User name field was empty");
                JOptionPane.showMessageDialog(frame,
                        "The username field cannot be empty!",
                        "No Username", JOptionPane.WARNING_MESSAGE);
                userField.requestFocusInWindow();
                return;
            }

            if (!AccountData.doesUsernameExist(username)) {
                JOptionPane.showMessageDialog(null, "Username " + username
                                + " does not exist." + ApplicationLiterals.NEW_LINE
                                + "Try again or create new account!",
                        "Invalid User", JOptionPane.ERROR_MESSAGE);
                frame.pack();
            } else {
                verifyNotBanned(username);

                if (validPassword(username, new String(passField.getPassword()))) {
                    logger.debug("Login successful"
                            + ApplicationLiterals.NEW_LINE
                            + "Launching Application from GUI");
                    // if forgot password feature was used, they need to reset password
                    if (AccountData.wasUserPasswordReset(username)) {
                        if(AccountData.isResetTimerValid(username)) {
                            frame.dispose();
                            UserManagement.changePassword(true, username);
                        }else{
                            JOptionPane.showMessageDialog(frame,
                                    "Your temporary password has expired\n" +
                                            "Please click Forgot Password to generate a new one",
                                    "Expired", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        frame.dispose();
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    new Loading(username);
                                } catch (Exception ex) {
                                    throw new AppException(ex);
                                }
                            }
                        });

                        thread.start();
                    }
                } else {
                    attempts++;
                    if (attempts == 3) {
                        frame.dispose();
                        banUser(username);
                    }
                    logger.debug("Incorrect password");
                    int result = JOptionPane.showOptionDialog(null,
                        "Incorrect Password!", "Incorrect",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE, null, new String[] {
                                "Ok", "Forgot Password?" },
                        JOptionPane.NO_OPTION);
                    if (result == JOptionPane.NO_OPTION) {
                        resetUserPassword();
                    }
                    passField.requestFocusInWindow();
                    frame.pack();
                }
            }
        });

        showButton.addActionListener(e -> {
            showButton.setVisible(false);
            hideButton.setVisible(true);
            passField.setEchoChar((char)0);
        });

        hideButton.addActionListener(e -> {
            hideButton.setVisible(false);
            showButton.setVisible(true);
            passField.setEchoChar('•');
        });

        forgot.addActionListener(e -> {
            forgot.setForeground(ApplicationLiterals.LINK_CLICKED);
            resetUserPassword();
        });

        create.addActionListener(e -> {
            create.setForeground(ApplicationLiterals.LINK_CLICKED);
            NewUser.createUser();
        });
    }

    private void resetUserPassword() {
        String user = JOptionPane.showInputDialog(frame,
                "Please input your username", "Reset",
                JOptionPane.INFORMATION_MESSAGE);
        verifyNotBanned(user);
        if(AccountData.doesUsernameExist(user) && Permission.isUserAllowedToChangePass()) {
            String tempPassword = PasswordGenerator.generatePassword();
            AccountData.resetPassword(user, tempPassword);

            StringSelection selection = new StringSelection(tempPassword);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            JOptionPane.showMessageDialog(
                            frame,
                            "<html>Your Password has been temporarily reset to: "
                                    + "<i>" + tempPassword
                                    + "</i><br><br>Your new password has been copied to your clipboard<br>" +
                                    "You have 10 minutes to login and set a new password.</html>",
                            "Password Reset", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void banUser(String user) {
        AccountData.lockUser(user);

        PersonalFinance.appLogger.logFooter();

        JOptionPane.showMessageDialog(null,
                "Due to too many password attempts, you have"
                        + "\nbeen locked out of the application", "Banned",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    private void verifyNotBanned(String user) {
        String status = "";
        try {
            Connection con = Connect.getConnection();

            String SQL_TEXT = ("SELECT STATUS FROM " + Databases.ACCOUNTS
                    + ApplicationLiterals.DOT + Tables.USERS
                    + " where USERNAME = '" + user + "'");

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);

            rs.next();
            try {
                status = rs.getString(1);
            } catch (SQLException s) {
                logger.error(s.toString());
            }

            con.close();
        } catch (Exception e) {
            throw new AppException(e);
        }

        if (status.equalsIgnoreCase(ApplicationLiterals.LOCKED)) {
            logger.warn(user + " user account is locked out");
            PersonalFinance.appLogger.logFooter();
            JOptionPane.showMessageDialog(null,
                    "Your user account has been locked out of the application",
                    "Banned", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private boolean validPassword(String user, String pass) {

        boolean valid = false;
        try {
            Connection con = Connect.getConnection();

            String SQL_TEXT = ("select AES_DECRYPT(ENCRYPTED_PASS, '"
                    + Encoding.decrypt(ApplicationLiterals.getEncryptionKey())
                    + "') from " + Databases.ACCOUNTS + ApplicationLiterals.DOT
                    + Tables.USERS + " where USERNAME ='" + user + "'");

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(SQL_TEXT);

            rs.next();

            String result = rs.getString(1);
            if (pass.equals(result)) {
                valid = true;
            }

            con.close();
        } catch (Exception e) {
            throw new AppException(e);
        }
        return valid;
    }

    private void makeButtonLink(JButton b) {

        b.setBorder(null);
        b.setForeground(ApplicationLiterals.LINK_NOT_CLICKED);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
    }
}
