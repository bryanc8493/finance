package persistence;

import beans.User;
import literals.ApplicationLiterals;
import literals.enums.Databases;
import literals.enums.Tables;
import org.apache.log4j.Logger;
import utilities.exceptions.AppException;
import utilities.ReadConfig;
import utilities.security.Encoding;
import views.common.Loading;
import views.common.MainMenu;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

public class Connect extends ApplicationLiterals {

	private static char PERMISSION = ApplicationLiterals.VIEW_ONLY;
	private static User currentUser;
	private static Logger logger = Logger.getLogger(Connect.class);

	private static Connection con;

	public static void InitialConnect(String user) throws GeneralSecurityException, IOException {
		initializeUser(user);

		String url = ReadConfig.getConfigValue(DB_URL);
		String className = ReadConfig.getConfigValue(MY_SQL_CLASS);
		String username = Encoding.decrypt(ReadConfig.getConfigValue(DB_USER));
		String pass = Encoding.decrypt(ReadConfig.getConfigValue(DB_PASS));

		logger.info("Establishing initial database connection..." + username + "\t" + pass);
		Loading.update("Connecting to database", 9);

		try {
			Class.forName(className);
			con = DriverManager.getConnection(url, username, pass);
		} catch (Exception e) {
			throw new AppException(e);
		}

		updateUsersLastLogin();

		setUsersPermission();

		launchMainMenu();
	}

	public static Connection getConnection() {
		String url = ReadConfig.getConfigValue(DB_URL);
		String className = ReadConfig.getConfigValue(MY_SQL_CLASS);
		String username;
		String pass;

		Connection con;
		try {
			username = Encoding.decrypt(ReadConfig.getConfigValue(DB_USER));
			pass = Encoding.decrypt(ReadConfig.getConfigValue(DB_PASS));
			Class.forName(className);
			con = DriverManager.getConnection(url, username, pass);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error connecting to Database:"
					+ NEW_LINE + e.toString(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
			throw new AppException(e);
		}
		return con;
	}

	public static String getCurrentUser() {
		return currentUser.getUsername();
	}

	public static char getUsersPermission() {
		return PERMISSION;
	}

	private static void initializeUser(String user) {
		currentUser = new User();
		currentUser.setUsername(user);
	}

	private static void updateUsersLastLogin() {
		PreparedStatement ps;
		String SQL_TEXT = ("UPDATE " + Databases.ACCOUNTS + DOT + Tables.USERS
				+ " set LAST_LOGIN = now() WHERE USERNAME = '"
				+ currentUser.getUsername() + "'");
		try {
			ps = con.prepareStatement(SQL_TEXT);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Failed updating last login table - " + e.toString()
					+ Arrays.toString(e.getStackTrace()));
		}
	}

	private static void setUsersPermission() {
		String SQL_TEXT = "SELECT PERMISSION from " + Databases.ACCOUNTS + DOT
				+ Tables.USERS + " WHERE USERNAME = '"
				+ currentUser.getUsername() + "'";
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(SQL_TEXT);
			rs.next();
			PERMISSION = rs.getString(1).charAt(0);
			currentUser.setPermission(PERMISSION);
			logger.info("User " + currentUser.getUsername() + " set with permission " + PERMISSION);
			Loading.update("Setting user's permission", 18);
		} catch (Exception e) {
			logger.error("Failed getting user's permission, default to 0 - "
					+ e.toString() + Arrays.toString(e.getStackTrace()));
		}
	}

	private static void launchMainMenu() {
		if (con != null) {
			logger.info("Connected successfully, logged in as user: " + getCurrentUser());
			MainMenu.modeSelection(0);
		}
	}
}
