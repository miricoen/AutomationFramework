package drivers;

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

public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver() {
        // אם יש WebDriver קיים, מחזירים אותו
        if (driverThread.get() == null) {
            String browser = ConfigManager.getProperty("browser").toLowerCase();
            WebDriver driver = createDriver(browser);

            // הגדרת זמן המתנה חצי שנייה להשלמת כל פעולה
            driver.manage().timeouts().implicitlyWait(
                    Integer.parseInt(ConfigManager.getProperty("timeout")), TimeUnit.SECONDS);

            // שומרים את ה- WebDriver במשתנה ThreadLocal כך שכל Thread יקבל מופע נפרד
            driverThread.set(driver);
        }
        return driverThread.get();
    }

    // יצירת WebDriver עבור הדפדפן המתאים
    private static WebDriver createDriver(String browser) {
        WebDriver driver;
        switch (browser) {
            case "chrome":
                driver = createChromeDriver();
                break;
            case "firefox":
                driver = createFirefoxDriver();
                break;
            case "edge":
                driver = createEdgeDriver();
                break;
            case "safari":
                driver = createSafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        return driver;
    }


    // יצירת ChromeDriver
    private static WebDriver createChromeDriver() {
        System.setProperty("webdriver.chrome.driver", ConfigManager.getProperty("driverPath"));
        ChromeOptions chromeOptions = new ChromeOptions();
        if (ConfigManager.getProperty("headless").equals("true")) {
            chromeOptions.addArguments("--headless");
        }
        return new ChromeDriver(chromeOptions);
    }

    // יצירת FirefoxDriver
    private static WebDriver createFirefoxDriver() {
        System.setProperty("webdriver.gecko.driver", ConfigManager.getProperty("driverPath"));
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (ConfigManager.getProperty("headless").equals("true")) {
            firefoxOptions.addArguments("--headless");
        }
        return new FirefoxDriver(firefoxOptions);
    }

    // יצירת EdgeDriver
    private static WebDriver createEdgeDriver() {
        System.setProperty("webdriver.edge.driver", ConfigManager.getProperty("driverPath"));
        EdgeOptions edgeOptions = new EdgeOptions();
        return new EdgeDriver(edgeOptions);
    }

    // יצירת SafariDriver
    private static WebDriver createSafariDriver() {
        return new SafariDriver();
    }


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
