package PageObjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import Framework.Base;
public class HomePage extends Base {
	public WebDriver driver;
	public static final By shopbydepartment=By.xpath("//a[@id='nav-link-shopall']");
	public static final By cart=By.xpath("//a[@id='nav-cart']");
	public static final By delete=By.xpath("//input[@value='Delete']");
	public HomePage(WebDriver driver) {
		this.driver=driver;
		}
	public void shopbydepartment()
	{
		WebElement w=findAndWait(shopbydepartment,15);
		hoverOverThroughActions(w);
		}
	public void selectitem(String item)
	{
		WebElement w=findAndWait(By.xpath("//span[@role='navigation']//span[text()='"+item+"']"),15);
		hoverOverThroughActions(w);
	}
	public void selectsubitem(String item)
	{
		WebElement w=findAndWait(By.xpath("//span[contains(text(),'"+item+"')]"),15);
		w.click();
	}
	
}
