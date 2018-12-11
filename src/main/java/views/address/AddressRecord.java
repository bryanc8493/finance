package views.address;

import domain.beans.Address;
import literals.Icons;
import views.common.components.PrimaryButton;

import javax.swing.*;
import java.awt.*;

public class AddressRecord {

    private JTextField lastName = new JTextField();
    private JTextField firstName = new JTextField();
    private JTextField address = new JTextField();
    private JTextField city = new JTextField();
    private JTextField state = new JTextField();
    private JTextField zip = new JTextField();

    public AddressRecord(Address record) {
        final JFrame frame = new JFrame("Address Record");

        JLabel lastNameLabel = new JLabel("Last Name:                      ");
        JLabel firstNameLabel = new JLabel("First Name(s):");
        JLabel addressLabel = new JLabel("Street Address:");
        JLabel cityLabel = new JLabel("City:");
        JLabel stateLabel = new JLabel("State:");
        JLabel zipLabel = new JLabel("Zip Code:");

        JButton close = new PrimaryButton("Close");

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.add(close);
        buttonWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        setValues(record);

        JPanel content = new JPanel(new GridLayout(6, 2, 5, 5));
        content.add(lastNameLabel);
        content.add(lastName);
        content.add(firstNameLabel);
        content.add(firstName);
        content.add(addressLabel);
        content.add(address);
        content.add(cityLabel);
        content.add(city);
        content.add(stateLabel);
        content.add(state);
        content.add(zipLabel);
        content.add(zip);

        JPanel full = new JPanel(new BorderLayout());
        full.add(content, BorderLayout.CENTER);
        full.add(buttonWrapper, BorderLayout.SOUTH);

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

    private void setValues(Address record) {
        lastName.setText(record.getLastName());
        firstName.setText(record.getFirstName());
        address.setText(record.getAddress());
        city.setText(record.getCity());
        state.setText(record.getState());
        zip.setText(record.getZipcode());

        lastName.setEditable(false);
        firstName.setEditable(false);
        address.setEditable(false);
        city.setEditable(false);
        state.setEditable(false);
        zip.setEditable(false);
    }
}
