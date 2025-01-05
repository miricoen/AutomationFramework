package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigManager is a utility class that loads and manages configuration properties
 * from a properties file. It allows retrieving configuration values based on a
 * provided key.
 * <p>
 * The configuration file is expected to be located at "src/main/resources/config.properties".
 * </p>
 * <p>
 * This class ensures that the properties are loaded once at the beginning of the program
 * and provides a static method to fetch property values throughout the application.
 * </p>
 *
 * @author Miriam Felman
 */
public class ConfigManager {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the value of a property from the configuration file.
     *
     * @param key The key of the property to be retrieved.
     * @return The value associated with the given key, or null if the key does not exist.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
