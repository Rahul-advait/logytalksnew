package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import java.util.concurrent.TimeUnit;

public class WebDriverFactory {
    private static final WebDriverFactory instance = new WebDriverFactory();
    private static ThreadLocal<WebDriver> threadedDriver = new ThreadLocal<WebDriver>();

    private WebDriverFactory() {
    }

    public static WebDriverFactory getInstance() {
        return instance;
    }

    public WebDriver getDriver(String browser) {
        WebDriver driver = null;
        setDriver(browser);
        if (threadedDriver.get() == null) {
            try {
                if (browser.equalsIgnoreCase("firefox")) {
                    FirefoxOptions ffOptions = setFFOptions();
                    driver = new FirefoxDriver(ffOptions);
                    threadedDriver.set(driver);
                }
                if (browser.equalsIgnoreCase("chrome")) {
                    ChromeOptions chromeOptions = setChromeOptions();
                    driver = new ChromeDriver(chromeOptions);
                    threadedDriver.set(driver);
                }
                if (browser.equalsIgnoreCase("iexplorer")) {
                    InternetExplorerOptions ieOptions = setIEOptions();
                    driver = new InternetExplorerDriver(ieOptions);
                    threadedDriver.set(driver);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            threadedDriver.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            threadedDriver.get().manage().window().maximize();
        }
        return threadedDriver.get();
    }


    public void quitDriver() {
        threadedDriver.get().quit();
        threadedDriver.set(null);
    }

    private void setDriver(String browser) {
        String driverPath, driverValue = "", driverKey = "";
        String directory = System.getProperty("user.dir") + "//drivers//";
        String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
        System.out.println("OS name from system property: " + os);

        if (browser.equalsIgnoreCase("chrome")) {
            driverKey = "webdriver.chrome.driver";
            driverValue = "chromedriver";
        } else if (browser.equalsIgnoreCase("firefox")) {
            driverKey = "webdriver.gecko.driver";
            driverValue = "geckodriver";
        } else if (browser.equalsIgnoreCase("ie")) {
            driverKey = "webdriver.edge.driver";
            driverValue = "msedgedriver";
        } else {
            System.out.println("Browser not supported");
        }

        try {
            driverPath = directory + driverValue + (os.equals("win") ? ".exe" : "");
            System.setProperty(driverKey, driverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ChromeOptions setChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        return options;
    }

    private FirefoxOptions setFFOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("nativeEvents", false);
        return options;
    }

    private InternetExplorerOptions setIEOptions() {
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
        options.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
        options.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
        options.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        options.setCapability(InternetExplorerDriver.
                INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        return options;
    }
}
