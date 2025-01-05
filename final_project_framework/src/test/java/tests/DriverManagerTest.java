package tests;

import drivers.DriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.WebDriver;


public class DriverManagerTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // קבלת WebDriver מהמחלקה DriverManager
        driver = DriverManager.getDriver();
    }

    @Test
    public void testDriverFunctionality() {
        // פתיחת דף אינטרנט
        String url = "https://example.com";
        driver.get(url);

        // בדיקה והדפסת כותרת הדף
        String pageTitle = driver.getTitle();
        System.out.println("Page Title: " + pageTitle);

        // ודא שהדפדפן לא ריק
        assert pageTitle != null && !pageTitle.isEmpty() : "Page title is empty!";
    }

    @AfterEach
    public void tearDown() {
        // סגירת הדפדפן
        DriverManager.quitDriver();
    }
}
