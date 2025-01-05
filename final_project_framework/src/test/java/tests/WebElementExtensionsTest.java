package tests;

import drivers.DriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebElementExtensions;

public class WebElementExtensionsTest {
    private static WebDriver driver;
    private static WebElementExtensions elementUtils;

    @BeforeAll
    public static void setup() {
        driver = DriverManager.getDriver();
        driver.get("https://example.com");
        elementUtils = new WebElementExtensions(driver);
    }

    @Test
    public void testFormSubmission() {
        By[] inputLocators = {
                By.id("username"),
                By.id("password")
        };
        String[] inputValues = {"testUser", "testPass"};

        By submitButtonLocator = By.id("submitButton");

        elementUtils.fillFormAndSubmit(inputLocators, inputValues, submitButtonLocator);
        elementUtils.waitForPageLoad();

        Assertions.assertTrue(driver.getTitle().contains("Welcome"));
    }

    @AfterAll
    public static void teardown() {
        DriverManager.quitDriver();
    }
}
