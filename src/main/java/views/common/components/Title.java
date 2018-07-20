package views.common.components;

import literals.ApplicationLiterals;

import javax.swing.*;
import java.awt.*;

public class Title extends JLabel {

	private static final long serialVersionUID = 1L;

	public Title(String s) {
		super(s, JLabel.CENTER);
		setFont(new Font("sans serif", Font.BOLD, 18));
		setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		setForeground(ApplicationLiterals.APP_COLOR);
	}
}
