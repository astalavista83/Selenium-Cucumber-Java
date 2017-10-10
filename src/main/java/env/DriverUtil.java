package env;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.*;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.ErrorHandler;


public class DriverUtil {
    public final static long DEFAULT_WAIT = 20;
	public final static int DEFAULT_WINDOW_WIDTH = 1920;
	public final static int DEFAULT_WINDOW_HEIGHT = 1080;
	private static WebDriver driver;

	public static WebDriver getDefaultDriver() {
		if (driver != null) {
			return driver;
		}
		System.out.println("DriverUtil.setUpDriver init");
		System.setProperty("webdriver.chrome.driver", "./chromedriver");
		System.setProperty("webdriver.gecko.driver", "./geckodriver");
		DesiredCapabilities capabilities = DesiredCapabilities.firefox(); //DesiredCapabilities.phantomjs();
		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability("takesScreenshot", true);
		driver = chooseDriver(capabilities);
		driver.manage().timeouts().setScriptTimeout(DEFAULT_WAIT,
				TimeUnit.SECONDS);
		driver.manage().window().setSize(chooseWindowDimension());
		return driver;
	}

    private static final String WINDOW_SIZE = "WindowSize";

	/**
	 * By default, window size is 1920x1080
	 *
	 * Override it by passing -DwindowSize={width}x{height} e.g. -DwindowSize=1024x768
	 *
	 * @return the dimension of the WebDriver window size.
	 */
	private static Dimension chooseWindowDimension() {
    	final String windowSizeStr = System.getProperty(WINDOW_SIZE);

    	if (windowSizeStr != null) {
    		String[] sizes = windowSizeStr.split("x");
			if (sizes.length == 2) {
				try {
					int w = Integer.parseUnsignedInt(sizes[0]);
					int h = Integer.parseUnsignedInt(sizes[1]);
					return new Dimension(w, h);
				} catch (NumberFormatException ex) {
					// malformed window size
					System.out.println("Malformed windowSize param: " + windowSizeStr);
				}
			} else {
				System.out.println("Malformed windowSize param: " + windowSizeStr);
			}
		}

		return new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
	}

    /**
     * By default to web driver will be PhantomJS
     *
     * Override it by passing -DWebDriver=Chrome to the command line arguments
     * @param capabilities - desired capabilities
     * @return - FirefoxDriver by default
     */
    private static WebDriver chooseDriver(DesiredCapabilities capabilities) {
		String preferredDriver = System.getProperty("WebDriver", "Firefox");
		boolean headless = System.getProperty("Headless", "true").equals("true");
		
		switch (preferredDriver) {
			case "Chrome":
				/*
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless", "--disable-gpu", "--disable-extensions", "--remote-debugging-port=9222");
				//options.addArguments("--disable-gpu", "--disable-extensions");
				//options.setBinary("/usr/bin/chromium-browser");
				options.setBinary("/usr/bin/google-chrome");
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				ChromeDriver driver = new ChromeDriver(capabilities);
				*/
				
				
				// /usr/bin/google-chrome
				final ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setBinary("/usr/bin/chromium-browser");
				if (headless) {
					chromeOptions.addArguments("--headless");
				}
				capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				ChromeDriver driver = new ChromeDriver(capabilities);
				ErrorHandler handler = new ErrorHandler();
				handler.setIncludeServerErrors(false);
				driver.setErrorHandler(handler);
				return driver;
			case "PhantomJS":
				return new PhantomJSDriver(capabilities);
			default:
				//return new PhantomJSDriver(capabilities);
				FirefoxOptions options = new FirefoxOptions();
				//capabilities.s
				if (headless) {
					options.addArguments("-headless", "-safe-mode");
				}
				capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
				return new FirefoxDriver(capabilities);
		}
    }

	public static WebElement waitAndGetElementByCssSelector(WebDriver driver, String selector,
                                                            int seconds) {
        By selection = By.cssSelector(selector);
        return (new WebDriverWait(driver, seconds)).until( // ensure element is visible!
                ExpectedConditions.visibilityOfElementLocated(selection));
    }

	public static void closeDriver() {
    	System.out.println("DriverUtil.closeDriver");
		if (driver != null) {
			try {
				System.out.println("DriverUtil.closeDriver closing...");
				driver.close();
				System.out.println("DriverUtil.closeDriver closed");
				// driver.quit(); // fails in current geckodriver! TODO: Fixme
			} catch (NoSuchMethodError nsme) {
				// in case quit fails
				System.out.println("DriverUtil.closeDriver: NoSuchMethodError");
				nsme.printStackTrace();
			} catch (NoSuchSessionException nsse) { // in case close fails
				System.out.println("DriverUtil.closeDriver: NoSuchSessionException");
				nsse.printStackTrace();
			} catch (SessionNotCreatedException snce) {
				System.out.println("DriverUtil.closeDriver: SessionNotCreatedException");
				snce.printStackTrace();
			} // in case close fails

			driver = null;
		} else {
			System.out.println("DriverUtil.closeDriver Driver already closed...");
		}

	}


}
