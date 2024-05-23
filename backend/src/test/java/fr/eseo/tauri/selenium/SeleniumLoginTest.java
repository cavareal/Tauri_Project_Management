package fr.eseo.tauri.selenium;

import org.junit.jupiter.api.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumLoginTest {
        private static final String URL = "http://localhost:5173/";
        private static final String LOGIN = "pl@tauri.com";
        private static final String PASSWORD = "pl";
        private static final String TITLE = "Bienvenue sur Tauri !";
        private static WebDriver webdriver;
        private static WebDriverWait wait;

        @BeforeAll
        public static void beforeTest(){
                WebDriverManager.safaridriver().setup();
                SafariOptions options = new SafariOptions();
                SeleniumLoginTest.webdriver = new SafariDriver(options);
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


        @AfterAll
        public static void afterTests(){
                SeleniumLoginTest.webdriver.close();
        }
}
