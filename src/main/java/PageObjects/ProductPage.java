package PageObjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import Framework.Base;
public class ProductPage extends Base {
	public WebDriver driver;
	public static final By itemquantity=By.xpath("//select[@id='quantity']");
	public static final By addtocart=By.xpath("//input[@id='add-to-cart-button']");
	public static final By popup=By.xpath("//button[contains(@class,'a-button-close a-declarative')]");
	public static final By confirm=By.xpath("//a[@id='nav-cart']");
	public static final By checkout=By.xpath("//a[@class='hucSprite s_checkout hlb-checkout-button']");
	public ProductPage(WebDriver driver) {
		this.driver=driver;
		}
	public void selectquantity(String quantity) throws InterruptedException
	{
		WebElement w=findAndWait(itemquantity,15);
		selectunit(w,quantity,"text");
		}
	public void addtocartclick() throws InterruptedException
	{
		WebElement w=findAndWait(addtocart,15);
		w.click();
		}
	public void popupclose() throws InterruptedException
	{
		Thread.sleep(3000);
		WebElement w=findAndWait(popup,15);
		w.click();
		
		}
	public String confirmitem() throws InterruptedException
	{
		WebElement w=findAndWait(confirm,15);
		return w.getAttribute("aria-label");
		}
	public void proceedcheckout() throws InterruptedException
	{
		WebElement w=findAndWait(checkout,15);
		w.click();
		}
}
