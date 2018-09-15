package PageObjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import Framework.Base;
public class SignInPage extends Base {
	public WebDriver driver;
	public static final By verify=By.xpath("//label[@class='a-form-label']");
	public SignInPage(WebDriver driver) {
		this.driver=driver;
		}
	public String textverify()
	{
		WebElement w=findAndWait(verify,15);
		return w.getText().trim();
		}
	
}
