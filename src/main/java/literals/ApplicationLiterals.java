package literals;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class ApplicationLiterals {

	public static final String NEW_LINE = "\n";
	public static final String DOT_CSV = ".csv";
	public static final String DOT_JAR = ".jar";
	public static final String UNDERSCORE = "_";
	public static final String COMMA = ",";
	public static final String SLASH = "/";
	public static final char DOT = '.';
	public static final String EMPTY = "";
	public static final String SEMI_COLON = ";";
	public static final String DOLLAR = "$";
	public static final String DASH = "-";
	public static final String SPACE = " ";
	public static final String PERCENT = "%";
	public static final String WHITESPACE = "\\s";
	public static final String NULL_PLACEHOLDER = "- - -";

	public static final String APP_TITLE = "Finance Utility";
	public static final String APP_ARTIFACT = "finance";
	public static final String VERSION = "3.1.0";

	public static final char VIEW_ONLY = '0';
	public static final char FULL_ACCESS = '1';

	public static final String USER_DIR = "user.dir";
	public static final String USER_NAME = "user.name";


	public static final Font APP_FONT = new Font("Sans Serif", Font.PLAIN, 16);
	public static final Font BOLD_FONT = new Font("Sans Serif", Font.BOLD, 12);
	public static final Font ACTIVE_TAB_FONT = new Font("Sans Serif", Font.BOLD, 14);
	public static final Font TAB_FONT = new Font("Sans Serif", Font.PLAIN, 14);

	public static final Color APP_COLOR = new Color(7, 142, 104);
	public static final Color LINK_NOT_CLICKED = new Color(6, 69, 173);
	public static final Color LINK_CLICKED = new Color(102, 51, 102);
	public static final Color GREY_TAB = Color.gray;
	public static final Color APP_GREEN = new Color(7, 109, 31);

	public static final Border TAB_BORDER = BorderFactory.createEmptyBorder(0,0,10,0);

	public static final Border PADDED_SPACE = BorderFactory.createEmptyBorder(10, 25, 15, 25);

	public static final String EXPENSE = "Expense";
	public static final String INCOME = "Income";
	public static final String SAVINGS = "Savings";
	public static final String SAVINGS_TRANSFER = "Savings Transfer";
	public static final String HOUSE_SAVINGS = "House Savings";

	public static final String LOCK = "lock";
	public static final String LOCKED = "LOCKED";
	public static final String UNLOCK = "unlock";
	public static final String UNLOCKED = "UNLOCKED";

	public static final Double BONUS_MINIMUM = 0.0;
	public static final Double BONUS_MAXIMUM = 200.0;
	public static final Double COMP_RATIO_MIN = 80.0;
	public static final Double COMP_RATIO_MAX = 120.0;

	public static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#.00");

	/*
	 * Config literals
	 */
	public static final String DB_URL = "ConnectionURL";
	public static final String MY_SQL_CLASS = "MySQLClassName";
	public static final String DB_USER = "DBUsername";
	public static final String DB_PASS = "DBPassword";
	public static final String DB_PORT = "DBPort";

	/*
	 * Dates
	 */
	public static final SimpleDateFormat YEAR_MONTH_DAY = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat YEAR_MONTH = new SimpleDateFormat("yyyy-MM");
	public static final SimpleDateFormat YEAR = new SimpleDateFormat("yyyy");
	public static final SimpleDateFormat MONTH = new SimpleDateFormat("M");
	public static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("EEEE, MMM d  h:mm:ss a");
	public static final SimpleDateFormat YEAR_MONTH_DAY_CONDENSED = new SimpleDateFormat("yyyyMMdd");
	public static final SimpleDateFormat MONTH_DAY_YEAR = new SimpleDateFormat("MMM d, yyyy");
	public static final DateTimeFormatter DATABASE_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/*
	 * States
	 */
	public final static String[] STATE_CODES = { "AL", "AK", "AZ", "AR", "CA",
			"CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
			"KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE",
			"NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA",
			"RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI",
			"WY" };

	public static NumberFormat getNumberFormat() {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		return nf;
	}

	public static NumberFormatter getCurrencyFormat() {
		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setMinimum(0.0);
		formatter.setMaximum(10000000.0);
		formatter.setAllowsInvalid(false);
		return formatter;
	}
}
