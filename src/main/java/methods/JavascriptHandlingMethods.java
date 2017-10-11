package methods;

import env.BaseTest;
import env.DriverUtil;
import org.openqa.selenium.WebDriver;

public class JavascriptHandlingMethods implements BaseTest {
	/**Method to handle alert
	 * @param decision : String : Accept or dismiss alert
	 */
	public void handleAlert(String decision)
	{
		if(decision.equals("accept"))
			getDriver().switchTo().alert().accept();
		else
			getDriver().switchTo().alert().dismiss();
	}

	public WebDriver getDriver() {
		return DriverUtil.getDefaultDriver();
	}
}
