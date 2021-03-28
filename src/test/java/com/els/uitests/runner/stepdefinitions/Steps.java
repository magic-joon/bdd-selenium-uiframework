package com.els.uitests.runner.stepdefinitions;

import com.cucumber.listener.Reporter;
import com.els.uitests.runner.model.WebElementModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Steps {
    private WebDriver driver;
    private Map<String, WebElementModel> webElementRepo;

    @Before
    public void setUp() {
        InputStream objectRepoResource = Steps.class.getClassLoader().getResourceAsStream("testdata/objectrepo.json");
        try (Reader reader = new InputStreamReader(objectRepoResource)) {
            Gson gson = new Gson();
            Type dataMapType = new TypeToken<Map<String, WebElementModel>>() {}.getType();
            webElementRepo = gson.fromJson(reader, dataMapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Given("^I am using the \"([^\"]*)\" browser$")
    public void iAmUsingTheBrowser(String browser) throws Throwable {
        browser = System.getProperty("browser", browser);
        System.out.println("browser is: " + browser);
        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } else if(browser.equalsIgnoreCase("firefox")){
            System.setProperty("webdriver.gecko.driver", "src/test/resources/drivers/geckodriver");
            driver = new FirefoxDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } else {
            Assert.fail("Invalid browser.");
        }
    }

    @And("^I open the URL \"([^\"]*)\"$")
    public void iOpenTheURL(String baseURL) throws Throwable {
        WebElementModel elementModel = webElementRepo.get(baseURL);
        if (elementModel == null) {
            System.out.println("URL listed in Feature file: " + baseURL +" not found in object-repo file. Please check!");
        } else {
            System.out.println("URL is: " + elementModel.getValue());
            driver.get(elementModel.getValue());
        }
    }

    @And("^I click on \"([^\"]*)\"$")
    public void iClickOn(String link) throws Throwable {
        WebElementModel elementModel = webElementRepo.get(link);
        if (elementModel == null) {
            System.out.println(" WebElement listed in Feature file" + link + " not found in object-repo file. Please check");
        } else {
            System.out.println("Click on: " + link);
            waitForVisibility(elementModel.getCssSelector());
            WebElement webLink = driver.findElement(By.cssSelector(elementModel.getCssSelector()));
            webLink.click();
        }
    }

    @Then("^I verify \"([^\"]*)\" is there in sign-in section$")
    public void iVerifyIsThereInSignInSection(String button) throws Throwable {
        WebElementModel elementModel = webElementRepo.get(button);
        if (elementModel == null) {
            System.out.println("WebElement listed in Feature file: " + button + " not found in object-repo file. Please check");
        } else {
            System.out.println("Check Sign-in: " + button);
            waitForVisibility(elementModel.getCssSelector());
            WebElement webLink = driver.findElement(By.cssSelector(elementModel.getCssSelector()));
            Assert.assertTrue("Sign-in not displayed!", webLink.isDisplayed());
            Thread.sleep(2000);
        }
    }

    @After
    public void cleanUp() throws InterruptedException {
        driver.close();
        Reporter.loadXMLConfig("src/test/resources/config/extent-config.xml");
    }

    private void waitForVisibility(String cssSelector) {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
    }

}
