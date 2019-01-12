package views.common.components;

import javax.swing.JLabel;
import java.awt.Component;

public class JLeftLabel extends JLabel {

    public JLeftLabel(final String s) {
        super(s);
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}