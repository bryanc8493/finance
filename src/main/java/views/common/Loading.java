package views.common;

import literals.ApplicationLiterals;
import literals.Icons;
import persistence.Connect;
import utilities.exceptions.AppException;
import views.common.components.Title;

import javax.swing.*;
import java.awt.*;

public class Loading {

	private static JFrame frame;
	private static JProgressBar progressBar;
	private static JLabel title;

	public Loading(String user) {
		frame = new JFrame(ApplicationLiterals.APP_TITLE);
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setValue(0);

		title = new Title("Loading and initializing application....");

		JPanel content = new JPanel(new BorderLayout(0, 5));
		content.add(title, BorderLayout.NORTH);
		content.add(progressBar, BorderLayout.SOUTH);

		frame.add(content);
		frame.setIconImage(Icons.APP_ICON.getImage());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		JRootPane rp = SwingUtilities.getRootPane(frame);
		rp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		try {
			Connect.InitialConnect(user);
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public static void terminate() {
		frame.dispose();
	}

	public static void update(String text, int value) {
		progressBar.setString(String.valueOf(value) + ApplicationLiterals.PERCENT);
		progressBar.setValue(value);
		title.setText(text);
	}
}
