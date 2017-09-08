package methods;

import env.BaseTest;
import env.DriverUtil;

public class JavascriptHandlingMethods implements BaseTest {
	/**Method to handle alert
	 * @param decision : String : Accept or dismiss alert
	 */
	public void handleAlert(String decision)
	{
		if(decision.equals("accept"))
			DriverUtil.getDefaultDriver().switchTo().alert().accept();
		else
			DriverUtil.getDefaultDriver().switchTo().alert().dismiss();
	}
}
