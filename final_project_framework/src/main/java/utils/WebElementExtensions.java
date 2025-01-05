package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * This class provides utility methods for interacting with web elements using Selenium WebDriver.
 * It includes methods for actions like clicking, filling forms, waiting for elements, and scrolling.
 * The purpose of this class is to simplify common WebDriver operations and add custom functionality.
 *
 * @author Miriam Felman
 */
public class WebElementExtensions {

    private WebDriver driver;
    private WebDriverWait wait;

    /**
     * Constructs a new WebElementExtensions object.
     * This constructor initializes the WebElementExtensions with a WebDriver instance
     * and sets up a WebDriverWait with a default timeout of 10 seconds.
     *
     * @param driver The WebDriver instance to be used for web interactions.
     *               This driver will be used for all operations performed by this WebElementExtensions instance.
     */
    public WebElementExtensions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Timeout set to 10 seconds by default
    }

    /**
     * Clicks on a specified web element and waits for the page to load completely.
     * This method performs a click action on the given element and then waits for the page
     * to finish loading before proceeding.
     *
     * @param element The WebElement to be clicked. This should be a valid, interactable element on the page.
     * @throws org.openqa.selenium.StaleElementReferenceException   if the element is no longer attached to the DOM
     * @throws org.openqa.selenium.ElementClickInterceptedException if the click is intercepted by another element
     * @throws org.openqa.selenium.TimeoutException                 if the page does not load within the specified timeout period
     */
    public void clickAndWaitForPageLoad(WebElement element) {
        element.click();
        waitForPageLoad();
    }

    /**
     * Waits for the page to fully load.
     * This method uses a WebDriverWait to pause execution until the page's readyState is "complete".
     * It utilizes JavaScript execution to check the document's readyState.
     * <p>
     * The default timeout for this wait is set to 10 seconds.
     *
     * @throws org.openqa.selenium.TimeoutException if the page does not load within the specified timeout period
     */
    public void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Fills out a form with provided input values and submits it.
     * This method fills in multiple input fields identified by their locators,
     * then clicks a submit button to send the form.
     *
     * @param inputLocators       An array of By locators identifying the input fields to be filled.
     * @param inputValues         An array of String values to be entered into the corresponding input fields.
     * @param submitButtonLocator A By locator identifying the submit button of the form.
     * @throws IllegalArgumentException If the number of input locators doesn't match the number of input values.
     */
    public void fillFormAndSubmit(By[] inputLocators, String[] inputValues, By submitButtonLocator) {
        if (inputLocators.length != inputValues.length) {
            throw new IllegalArgumentException("Number of locators and values must match.");
        }

        for (int i = 0; i < inputLocators.length; i++) {
            WebElement inputField = driver.findElement(inputLocators[i]);
            inputField.clear();
            inputField.sendKeys(inputValues[i]);
        }

        WebElement submitButton = driver.findElement(submitButtonLocator);
        clickAndWaitForPageLoad(submitButton);
    }

    /**
     * Waits for an element to become visible on the page.
     * This method uses WebDriverWait to wait until the specified element is visible.
     *
     * @param locator The By locator identifying the element to wait for.
     * @throws org.openqa.selenium.TimeoutException if the element is not visible within the timeout.
     */
    public void waitForElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Sends text to a web element while ensuring the element is clickable.
     * This method waits for the element to become clickable before sending the text.
     *
     * @param element The WebElement to send keys to.
     * @param keys    The text to be sent to the element.
     */
    public void sendKeysWithWait(WebElement element, String keys) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.clear();
        element.sendKeys(keys);
    }

    /**
     * Scrolls the page to bring the specified element into view.
     * This method uses JavaScript execution to scroll the page to the given element.
     *
     * @param element The WebElement to scroll to.
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Checks if an element is present on the page.
     * This method returns true if the element is present, even if it is not visible.
     *
     * @param locator The By locator identifying the element to check for.
     * @return true if the element is present, false otherwise.
     */
    public boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /**
     * Checks if a specific text is present in the page's source code.
     *
     * @param text The text to search for in the page source.
     * @return true if the text is present, false otherwise.
     */
    public boolean isTextPresent(String text) {
        return driver.getPageSource().contains(text);
    }
}
