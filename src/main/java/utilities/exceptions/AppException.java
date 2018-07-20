package utilities.exceptions;

import literals.ApplicationLiterals;
import org.apache.log4j.Logger;
import program.PersonalFinance;

import javax.swing.*;
import java.util.Arrays;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = -5372718122579774824L;
	private static final Logger logger = Logger.getLogger(AppException.class);

	public AppException(Exception e) {
		super(e);
		String msgAndStacktrace = e.toString()
				+ ApplicationLiterals.NEW_LINE
				+ Arrays.toString(e.getStackTrace()).replace(
						ApplicationLiterals.COMMA, "\r\n");
		logger.fatal("Application Exception: " + msgAndStacktrace);

		furtherActionsIfNotNull(e);
	}

	private void furtherActionsIfNotNull(Exception e) {
		if(PersonalFinance.appLogger != null) {
			PersonalFinance.appLogger.logFooter();

			JOptionPane.showMessageDialog(null, e.toString()
							+ "\n\nCheck logs for more info", "Application Exception",
					JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}
	}
}
