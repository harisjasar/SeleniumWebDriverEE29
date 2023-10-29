package org.example;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SauceDemo {

    private WebDriver driver;
    @BeforeEach
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.saucedemo.com/");// used to navigate to url
    }

    @AfterEach
    public void teardown(){
        try {
            Thread.sleep(Duration.ofSeconds(5));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        driver.quit();
    }
    @Test
    public void SampleTest(){

        String title = driver.getTitle();
        System.out.println(title);
        String currentUrl = driver.getCurrentUrl();
        System.out.println(currentUrl);
        WebElement userNameField = driver.findElement(By.id("user-name"));
        userNameField.sendKeys("standard_user");
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("secret_sauce");
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        WebElement appLogo = driver.findElement(By.className("app_logo"));
        Assertions.assertTrue(appLogo.isDisplayed());
        if(appLogo.isDisplayed()){
            System.out.println("We are logged in");
        }
    }

    @Test
    public void LoginInvalidCredentials(){
        CheckCredentialsFlow("standard_user", "secret_sauce123", "Epic sadface: Username and password do not match any user in this service");
    }

    @Test
    public void LoginWithExpiredCredentials(){
        CheckCredentialsFlow("locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out.");
    }

    private void CheckCredentialsFlow(String username, String password, String expectedErrorMessage){
        login(username, password);
        WebElement errorMessageByCssSelector = driver.findElement(By.cssSelector("h3[data-test='error']"));
        WebElement errorMessageByXpath = driver.findElement(By.xpath("//h3[@data-test='error']"));
        Assertions.assertTrue(errorMessageByXpath.isDisplayed());
        String errorMessageText = errorMessageByCssSelector.getText();
        Assertions.assertEquals(expectedErrorMessage, errorMessageText);
    }

    private void login(String username, String password){
        WebElement userNameField = driver.findElement(By.id("user-name"));
        userNameField.sendKeys(username);
        //locate password field and enter password
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(password);
        //locate login button and click on it
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
    }

    @Test
    public void CheckoutFlowE2E(){
        login("standard_user", "secret_sauce");

        //locate add to cart button
        WebElement addToCart = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addToCart.click();

        //locate shopping cart
        WebElement shoppingCart = driver.findElement(By.className("shopping_cart_link"));
        shoppingCart.click();

        //locate cart item and assert it is displayed
        WebElement cartItem = driver.findElement(By.className("cart_item"));
        Assertions.assertTrue(cartItem.isDisplayed());

        //locate checkout button
        WebElement checkoutBtn = driver.findElement(By.id("checkout"));
        checkoutBtn.click();

        //locate first name and populate it
        WebElement firstName = driver.findElement(By.id("first-name"));
        firstName.sendKeys("John");

        //locate last name and populate it
        WebElement lastName = driver.findElement(By.id("last-name"));
        lastName.sendKeys("Doe");

        //locate postal code and populate it
        WebElement postalCode = driver.findElement(By.id("postal-code"));
        postalCode.sendKeys("123456");

        //locate continue button and click it
        WebElement continueBtn = driver.findElement(By.id("continue"));
        continueBtn.click();

        //locate shipping info and assert text and presence
        WebElement shippingInfo = driver.findElement(By.xpath("(//div[@class='summary_value_label'])[2]"));
        Assertions.assertTrue(shippingInfo.isDisplayed());

        String shippingInfoText = shippingInfo.getText();
        String expectedShippingInfoText = "Free Pony Express Delivery!";
        Assertions.assertEquals(expectedShippingInfoText, shippingInfoText);

        //locate finish button
        WebElement finishBtn = driver.findElement(By.id("finish"));
        finishBtn.click();

        //locate complete header
        WebElement completeHeader = driver.findElement(By.className("complete-header"));
        Assertions.assertTrue(completeHeader.isDisplayed());

        Assertions.assertEquals("Thank you for your order!", completeHeader.getText());

        //locate back home button
        WebElement backHomeBtn = driver.findElement(By.id("back-to-products"));
        backHomeBtn.click();

        //locate hambuarger menu
        WebElement hambMenu = driver.findElement(By.id("react-burger-menu-btn"));
        hambMenu.click();

        //locate logout button
        //WebElement logoutBtn = driver.findElement(By.id("logout_sidebar_link"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
        logoutBtn.click();
    }
}
