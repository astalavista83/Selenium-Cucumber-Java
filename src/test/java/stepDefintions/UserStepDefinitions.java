package stepDefintions;
import cucumber.api.java.After;
import env.BaseTest;
import env.DriverUtil;

public class UserStepDefinitions implements BaseTest {
	@After
	public void after() {
		DriverUtil.closeDriver();
	}
}
