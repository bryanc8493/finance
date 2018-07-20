package views.common.components;

import javax.swing.*;
import java.awt.*;

public class PrimaryButton extends JButton {

    private static final long serialVersionUID = 1L;
    private Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

    public PrimaryButton() {
        super();
        this.setCursor(handCursor);
    }

    public PrimaryButton(String label) {
        super(label);
        this.setCursor(handCursor);
    }

    public PrimaryButton(String label, ImageIcon icon) {
        super(label);
        this.setCursor(handCursor);
        this.setIcon(icon);
    }
}
