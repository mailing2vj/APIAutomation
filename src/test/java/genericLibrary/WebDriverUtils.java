package genericLibrary;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.xml.XmlTest;

public class WebDriverUtils {

	/**
	 * getDriver : Launches driver and returns the instance of the driver
	 * @return Instance of the driver
	 * @throws Exception 
	 */
	public static WebDriver getDriver() throws Exception {

		WebDriver driver = null;

		try {

			XmlTest test = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
			String driverType = test.getParameter("driverType");
			String driverPath = System.getProperty("user.dir") + "\\Common\\Drivers\\";

			switch (driverType.toUpperCase()) {

			case "CHROME" : {

				ChromeOptions opt = new ChromeOptions(); // Set the capabilities for set the user agent
				opt.addArguments("--start-maximized");

				System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
				driver = new ChromeDriver(opt);

				break;

			} //End case : Chrome			

			default : {

				synchronized (WebDriverUtils.class) {

					System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver.exe");
					FirefoxOptions opt = new FirefoxOptions();
					driver = new FirefoxDriver(opt);

				}

				break;
			} //End default : firefox

			} //End switch

			if (driver.equals(null))
				throw new SkipException("Driver not initialized.");

			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);			

		}
		catch (Exception e) {
			throw new SkipException("Exception encountered in getDriver Method : " + e.getMessage().toString(), e);
		}
		return driver;

	} //End getDriver
}
