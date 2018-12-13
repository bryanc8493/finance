package utilities;

import utilities.exceptions.AppException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class ReadConfig {

	public static String getConfigValue(String property) {
		String value;
		Properties prop = new Properties();
		InputStream input;

		try {
			input = ReadConfig.class.getResourceAsStream("/config/config.properties");
			prop.load(input);
			value = prop.getProperty(property);
		} catch (Exception e) {
			throw new AppException(e);
		}
		return value;
	}

	public static Map<String, String> getAllProperties() {
		Map<String, String> properties = new HashMap<>();

		Properties props = new Properties();
		InputStream input;

		try {
			input = ReadConfig.class.getResourceAsStream("/config/config.properties");
			props.load(input);
		} catch (Exception e) {
			throw new AppException(e);
		}

		for (Entry<Object, Object> entry : props.entrySet()) {
			properties.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return properties;
	}
}
