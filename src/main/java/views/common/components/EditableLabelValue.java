package views.common.components;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class EditableLabelValue extends LabelValue {

    public EditableLabelValue(JLabel label, JTextField value, Integer gap) {
        super(label, value, gap);
        value.setEditable(true);
    }
}
