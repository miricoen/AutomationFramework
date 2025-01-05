package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

/**
 * This class extends the WebDriver to provide a custom WebDriver implementation
 * that allows for additional flexibility and customization in handling WebDriver instances.
 * It is designed to allow specific behaviors, such as adding options or modifying settings
 * for different browsers.
 *
 * @author Miriam Felman
 *
 */
public class CustomWebDriver {

    private WebDriver driver;

    /**
     * Constructs a new CustomWebDriver instance with the desired browser type.
     * It allows for further configuration of WebDriver settings such as headless mode.
     *
     * @param browser The browser type to use for the WebDriver instance (e.g., "chrome").
     * @throws IllegalArgumentException If an unsupported browser is provided.
     */
    public CustomWebDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                driver = createChromeDriver();
                break;
            // Add other browsers here as needed
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    /**
     * Creates a ChromeDriver with optional headless configuration.
     *
     * @return A WebDriver instance for Chrome.
     */
    private WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        // Enable headless mode if specified
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // Optional: setting implicit wait
        return driver;
    }

    /**
     * Returns the current WebDriver instance.
     *
     * @return The WebDriver instance.
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Quits the current WebDriver session and closes all associated windows.
     */
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
