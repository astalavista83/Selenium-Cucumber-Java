package methods;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import env.BaseTest;
import env.DriverUtil;

public class NavigateMethods extends SelectElementByType implements BaseTest
{
	//SelectElementByType eleType= new SelectElementByType();
	private WebElement element=null;
	private String old_win = null;
	private String lastWinHandle;
	
	/** Method to open link
	 * @param url : String : URL for navigation
	 */
	public void navigateTo(String url) 
	{
		DriverUtil.getDefaultDriver().get(url);
	}
	
	/** Method to navigate back & forward
	 * @param direction : String : Navigate to forward or backward
	 */
	public void navigate(String direction)
	{
		if (direction.equals("back"))
			DriverUtil.getDefaultDriver().navigate().back();
		else
			DriverUtil.getDefaultDriver().navigate().forward();
	}
	
	/** Method to quite webdriver instance */
	public void closeDriver()
	{
		DriverUtil.getDefaultDriver().close();
	}
	
	/** Method to return key by OS wise
	 * @return Keys : Return control or command key as per OS
	 */
	public Keys getKey()
	{
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win"))
			return Keys.CONTROL;
		else if (os.contains("nux") || os.contains("nix"))
			return Keys.CONTROL;
		else if (os.contains("mac"))
			return Keys.COMMAND;
		else
			return null;
	}
	
	/** Method to zoom in/out page
	 * @param inOut : String : Zoom in or out
	 */
	public void zoomInOut(String inOut)
	{
		WebElement Sel= DriverUtil.getDefaultDriver().findElement(getelementbytype("tagName","html"));
		if(inOut.equals("ADD"))
			Sel.sendKeys(Keys.chord(getKey(), Keys.ADD));
		else if(inOut.equals("SUBTRACT"))
			Sel.sendKeys(Keys.chord(getKey(), Keys.SUBTRACT));
		else if(inOut.equals("reset"))
			Sel.sendKeys(Keys.chord(getKey(), Keys.NUMPAD0));
	}
	
	/** Method to zoom in/out web page until web element displays
	 * @param accessType : String : Locator type (id, name, class, xpath, css)
	 * @param inOut : String : Zoom in or out
	 * @param accessName : String : Locator value
	 */
	public void zoomInOutTillElementDisplay(String accessType,String inOut,String accessName)
	{
		Actions action = new Actions(DriverUtil.getDefaultDriver());
		element = DriverUtil.getDefaultWait().until(ExpectedConditions.presenceOfElementLocated(getelementbytype(accessType, accessName)));
		while(true)
		{
			if (element.isDisplayed())
				break;
			else
				action.keyDown(getKey()).sendKeys(inOut).keyUp(getKey()).perform();
		}
	}
	
	/** Method to resize browser
	 * @param width : int : Width for browser resize
	 * @param height : int : Height for browser resize
	 */
	public void resizeBrowser(int width, int height)
	{
		DriverUtil.getDefaultDriver().manage().window().setSize(new Dimension(width,height));
	}
	
	/** Method to maximize browser	 */
	public void maximizeBrowser()
	{
		DriverUtil.getDefaultDriver().manage().window().maximize();
	}
	
	/** Method to hover on element
	 * @param accessType : String : Locator type (id, name, class, xpath, css)
	 * @param accessName : String : Locator value
	 */
	public void hoverOverElement(String accessType, String accessName)
	{
		Actions action = new Actions(DriverUtil.getDefaultDriver());
		element = DriverUtil.getDefaultWait().until(ExpectedConditions.presenceOfElementLocated(getelementbytype(accessType, accessName)));
		action.moveToElement(element).perform();
	}
	
	/** Method to scroll page to particular element
	 * @param accessType : String : Locator type (id, name, class, xpath, css)
	 * @param accessName : String : Locator value
	 */
	public void scrollToElement(String accessType, String accessName)
	{
		element = DriverUtil.getDefaultWait().until(ExpectedConditions.presenceOfElementLocated(getelementbytype(accessType, accessName)));
		JavascriptExecutor executor = (JavascriptExecutor)DriverUtil.getDefaultDriver();
		executor.executeScript("arguments[0].scrollIntoView();", element);
	}
	
	/** Method to scroll page to top or end
	 * @param to : String : Scroll page to Top or End
	 * @throws Exception
	 */
	public void scrollPage(String to) throws Exception
	{
		JavascriptExecutor executor = (JavascriptExecutor)DriverUtil.getDefaultDriver();
		if (to.equals("end"))
			executor.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
		else if (to.equals("top"))
            executor.executeScript("window.scrollTo(Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight),0);");
		else
			throw new Exception("Exception : Invalid Direction (only scroll \"top\" or \"end\")");
	}
	
	/**Method to switch to new window */
    public void switchToNewWindow()
    {
    	old_win = DriverUtil.getDefaultDriver().getWindowHandle();
    	for(String winHandle : DriverUtil.getDefaultDriver().getWindowHandles())
    		lastWinHandle = winHandle;
    	DriverUtil.getDefaultDriver().switchTo().window(lastWinHandle);
    }
    
    /** Method to switch to old window */
    public void switchToOldWindow()
    {
    	DriverUtil.getDefaultDriver().switchTo().window(old_win);
    }
    
    /** Method to switch to window by title
     * @param windowTitle : String : Name of window title to switch
     * @throws Exception */
    public void switchToWindowByTitle(String windowTitle) throws Exception
    {
    	//System.out.println("++"+windowTitle+"++");
    	old_win = DriverUtil.getDefaultDriver().getWindowHandle();
    	boolean winFound = false;
    	for(String winHandle : DriverUtil.getDefaultDriver().getWindowHandles())
    	{
    		String str = DriverUtil.getDefaultDriver().switchTo().window(winHandle).getTitle();
    		//System.out.println("**"+str+"**");
    		if (str.equals(windowTitle))
    		{
    			winFound = true;
    			break;
    		}
    	}
    	if (!winFound)
    		throw new Exception("Window having title "+windowTitle+" not found");
    }

    /**Method to close new window*/
    public void closeNewWindow()
    {
    	DriverUtil.getDefaultDriver().close();
    }
    
    /** Method to switch frame using web element frame
     * @param accessType : String : Locator type (index, id, name, class, xpath, css)
	 * @param accessName : String : Locator value
     * */
    public void switchFrame(String accessType, String accessName)
    {
    	if(accessType.equalsIgnoreCase("index"))
    		DriverUtil.getDefaultDriver().switchTo().frame(accessName);
    	else
    	{
    		element = DriverUtil.getDefaultWait().until(ExpectedConditions.presenceOfElementLocated(getelementbytype(accessType, accessName)));
    		DriverUtil.getDefaultDriver().switchTo().frame(element);
    	}
    }
    
    /** method to switch to default content*/
    public void switchToDefaultContent()
    {
    	DriverUtil.getDefaultDriver().switchTo().defaultContent();
    }
}
