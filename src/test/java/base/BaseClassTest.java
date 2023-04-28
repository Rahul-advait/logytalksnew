package base;

import PageClasses.LoginPage;
import PageClasses.NavigationBar;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

public class BaseClassTest {
    private WebDriver driver;
    private NavigationBar navigationBar;
    private LoginPage login;


    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        String baseURL = "https://logytalks.com/";
        driver.get(baseURL);
        navigationBar = new NavigationBar(driver);
        login = navigationBar.clickLogin();
    }
}
