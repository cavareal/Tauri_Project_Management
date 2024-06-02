package fr.eseo.tauri.selenium;

import fr.eseo.tauri.util.CustomLogger;
import org.junit.jupiter.api.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Level;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumLoginTest {
        private static final String URL = System.getProperty("selenium.server.url", "http://localhost:5173/");
        private static final String TITLE = "Bienvenue sur Tauri !";
        private static WebDriver webdriver;
        private static WebDriverWait wait;

        @BeforeAll
        public static void beforeTest(){
                WebDriverManager.safaridriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox");
                options.addArguments("--headless");
                options.addArguments("--ignore-certificate-errors");
                options.setCapability("goog:loggingPrefs", new LoggingPreferences() {{
                        enable(LogType.DRIVER, Level.ALL);
                }});

                SeleniumLoginTest.webdriver = new ChromeDriver(options);
                wait = new WebDriverWait(SeleniumLoginTest.webdriver, Duration.ofSeconds(10));
                SeleniumLoginTest.webdriver.get(SeleniumLoginTest.URL+"login");
        }



        @Test
        @Order(1)
        void titleIsPresentTest(){
                WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("text-dark-blue")));
                Assertions.assertEquals(SeleniumLoginTest.TITLE,
                        titleElement.getText(), "Title");
        }

        @Test
        @Order(2)
        void loginPl() {
                webdriver.findElement(By.id("radix-1-form-item")).sendKeys("p.l@tauri.com");
                webdriver.findElement(By.id("radix-2-form-item")).sendKeys("pl");
                {
                        WebElement element = webdriver.findElement(By.id("radix-1-form-item"));
                        Actions builder = new Actions(webdriver);
                        builder.moveToElement(element).clickAndHold().perform();
                }
                {
                        WebElement element = webdriver.findElement(By.id("radix-1-form-item"));
                        Actions builder = new Actions(webdriver);
                        builder.moveToElement(element).perform();
                }
                {
                        WebElement element = webdriver.findElement(By.id("radix-1-form-item"));
                        Actions builder = new Actions(webdriver);
                        builder.moveToElement(element).release().perform();
                }
                webdriver.findElement(By.id("radix-1-form-item")).click();
                webdriver.findElement(By.cssSelector(".bg-white")).click();
                webdriver.findElement(By.cssSelector(".inline-flex")).click();

                WebElement welcomeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("welcome-text")));
                String welcomeText = "Bonjour Richard 👋";
                Assertions.assertEquals(welcomeText,
                        welcomeElement.getText(), "Title");
        }

        @AfterAll
        public static void afterTests(){
                SeleniumLoginTest.webdriver.close();
        }
}
