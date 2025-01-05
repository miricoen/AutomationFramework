package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import utils.ConfigManager;

import java.util.concurrent.TimeUnit;

/**
 * DriverManager is a utility class responsible for managing the creation and lifecycle
 * of WebDriver instances for different browsers. It provides methods for initializing
 * a WebDriver instance based on the desired browser and for quitting the WebDriver when done.
 *
 * <p>
 * The class uses WebDriverManager to handle driver binary setup automatically and allows for
 * configuring the browser through external properties (e.g., headless mode, timeout, browser type).
 * </p
 * @author Miriam Felman
 *
 */
public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    /**
     * Retrieves the WebDriver instance for the desired browser. If a WebDriver instance
     * has not been created yet for the current thread, it will be initialized using the
     * browser type specified in the configuration.
     *
     * @return The WebDriver instance for the specified browser.
     */
    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            String browser = ConfigManager.getProperty("browser").toUpperCase();
            BrowserType browserType = BrowserType.valueOf(browser);

            WebDriver driver = createDriver(browserType);

            driver.manage().timeouts().implicitlyWait(
                    Integer.parseInt(ConfigManager.getProperty("timeout")), TimeUnit.SECONDS);

            driverThread.set(driver);
        }
        return driverThread.get();
    }

    /**
     * Creates a WebDriver instance based on the specified browser type.
     * This method handles the creation of the appropriate WebDriver for the browser.
     *
     * @param browserType The type of browser for which the WebDriver should be created.
     * @return The WebDriver instance for the specified browser.
     */
    private static WebDriver createDriver(BrowserType browserType) {
        switch (browserType) {
            case CHROME:
                return createChromeDriver();
            case FIREFOX:
                return createFirefoxDriver();
            case EDGE:
                return createEdgeDriver();
            case SAFARI:
                return createSafariDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }
    }

    /**
     * Creates and returns a WebDriver instance for Chrome browser.
     * This method ensures the correct version of the ChromeDriver is used and sets
     * any required Chrome options such as running in headless mode.
     *
     * @return The WebDriver instance for Chrome browser.
     */
    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        if (ConfigManager.getProperty("headless").equals("true")) {
            chromeOptions.addArguments("--headless");
        }

        return new ChromeDriver(chromeOptions);
    }

    /**
     * Creates and returns a WebDriver instance for Firefox browser.
     * This method sets the necessary system property for GeckoDriver and configures
     * any required options, such as headless mode.
     *
     * @return The WebDriver instance for Firefox browser.
     */
    private static WebDriver createFirefoxDriver() {
        System.setProperty("webdriver.gecko.driver", ConfigManager.getProperty("driverPath"));
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (ConfigManager.getProperty("headless").equals("true")) {
            firefoxOptions.addArguments("--headless");
        }
        return new FirefoxDriver(firefoxOptions);
    }

    /**
     * Creates and returns a WebDriver instance for Edge browser.
     * This method sets the necessary system property for EdgeDriver.
     *
     * @return The WebDriver instance for Edge browser.
     */
    private static WebDriver createEdgeDriver() {
        System.setProperty("webdriver.edge.driver", ConfigManager.getProperty("driverPath"));
        EdgeOptions edgeOptions = new EdgeOptions();
        return new EdgeDriver(edgeOptions);
    }

    /**
     * Creates and returns a WebDriver instance for Safari browser.
     * This method creates a SafariDriver instance without any additional configuration.
     *
     * @return The WebDriver instance for Safari browser.
     */
    private static WebDriver createSafariDriver() {
        return new SafariDriver();
    }

    /**
     * Quits the WebDriver instance for the current thread and removes it from memory.
     * This method ensures the WebDriver session is properly closed and cleaned up.
     */
    public static void quitDriver() {
        try {
            if (driverThread.get() != null) {
                driverThread.get().quit();
                driverThread.remove();
            }
        } catch (Exception e) {
            System.err.println("Error while quitting WebDriver: " + e.getMessage());
        }
    }
}
