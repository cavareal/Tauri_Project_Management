package fr.eseo.tauri.selenium;

import fr.eseo.tauri.util.CustomLogger;
import org.junit.jupiter.api.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebElement;
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
public class SeleniumTauriTest {
        private static final String URL = System.getProperty("selenium.server.url", "http://172.24.1.22/");
        private static final String LOGIN = "p.l@tauri.com";
        private static final String PASSWORD = "pl";
        private static final String TITLE = "Bienvenue sur Tauri !";
        private static WebDriver webdriver;
        private static String settings = System.getProperty("selenium.options", "false");


        @BeforeAll
        public static void beforeTest(){
                WebDriverManager.safaridriver().setup();
                ChromeOptions options = new ChromeOptions();
//                if(settings.equals("true")){
                options.addArguments("--no-sandbox");
                options.addArguments("--headless");
                options.addArguments("--ignore-certificate-errors");
//                }
                SeleniumTauriTest.webdriver = new ChromeDriver(options);
                SeleniumTauriTest.webdriver.get(SeleniumTauriTest.URL+"login");

        }

        @Test
        @Order(1)
        void login(){
                WebDriverWait wait = new WebDriverWait(SeleniumTauriTest.webdriver, Duration.ofSeconds(10));
                WebElement titleElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("text-dark-blue")));

                Assertions.assertEquals(SeleniumTauriTest.TITLE,
                        titleElement.getText(), "Title");
        }


        @AfterAll
        public static void afterTests(){
                SeleniumTauriTest.webdriver.close();
        }
}
