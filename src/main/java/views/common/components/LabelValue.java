package views.common.components;

import javax.swing.*;
import java.awt.*;

public class LabelValue extends JPanel {

    private JLabel label;
    private JTextField value;

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }


    public JTextField getValue() {
        return value;
    }

    public void setValue(JTextField value) {
        this.value = value;
    }

    public LabelValue(JLabel labelInput, JTextField valueInput) {
        valueInput.setColumns(20);
        valueInput.setEditable(false);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        setLabel(labelInput);
        setValue(valueInput);
        this.add(getLabel());
        this.add(getValue());
    }

    public LabelValue(JLabel labelInput, JTextField valueInput, Integer gap) {
        valueInput.setColumns(20);
        valueInput.setCaretPosition(0);
        valueInput.setEditable(false);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));

        setLabel(labelInput);
        setValue(valueInput);
        this.add(getLabel());
        this.add(Box.createRigidArea(new Dimension(gap,0)));
        this.add(getValue());
    }

    public LabelValue(String labelText, String valueText) {
        final JLabel label = new JLeftLabel(labelText);
        final JTextField value = new JTextField(valueText);
        value.setColumns(20);
        value.setCaretPosition(0);
        value.setEditable(false);

        setLabel(label);
        setValue(value);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(getLabel());
        this.add(getValue());
    }

    public LabelValue(String labelText, String valueText, Integer gap) {
        final JLabel label = new JLeftLabel(labelText);
        final JTextField value = new JTextField(valueText);
        value.setColumns(20);
        value.setCaretPosition(0);
        value.setEditable(false);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(label);
        this.add(Box.createRigidArea(new Dimension(gap, 0)));
        this.add(value);
    }
}
