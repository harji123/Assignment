package TestCases;
import org.testng.Assert;
import org.testng.annotations.Test;
import Framework.Base;
import PageObjects.HomePage;
import PageObjects.ProductPage;
import PageObjects.SignInPage;
public class verifyloginscreen extends Base {
	HomePage l = new HomePage(driver);
	ProductPage p = new ProductPage(driver);
	SignInPage s=new SignInPage(driver);
    @Test(priority = 1, description = "Verify User is on the Home Page")
	public void verifyTitle() throws Exception {
		l.shopbydepartment();
		l.selectitem("Kindle");
		l.selectsubitem("Kindle Paperwhite");
		p.selectquantity("2");
		p.addtocartclick();
		p.popupclose();
		Assert.assertEquals(p.confirmitem(), "2 items in cart");
		p.proceedcheckout();
		Assert.assertEquals(s.textverify(), "E-mail address or mobile phone number");
}
}
