package utilities;

import literals.ApplicationLiterals;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import utilities.exceptions.AppException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppLogger {

	private Logger logger;

	public AppLogger() {
		logger = Logger.getLogger(this.getClass());
		logHeader();
	}

	private void logHeader() {
		logger.info("===========================================");
		logger.info("STARTING APPLICATION");
		logger.info("===========================================");
	}

	public void logFooter() {
		logger.info("===========================================");
		logger.info("APPLICATION CLOSED");
		logger.info("===========================================\n\n\n");
	}
}
