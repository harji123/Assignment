package Framework;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IResultMap;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import Resources.HandleEvents;
@Listeners(Resources.Listeners.class)
public abstract class Base{
	protected static final Logger log = LogManager.getLogger(Base.class.getName());
	public static ExtentReports extent=null;
	public static ExtentHtmlReporter htmlReporter=null;
	public static ExtentTest test=null;
	//--------------Login Related Parameters---------------
		public static String root;
	    public static WebDriver driver;
	    public static String browser;
	   
	 //--------------Prop File Related Parameters-----------
    public static FileInputStream parentPropFileInp = null;
    public static Properties parentPropFile = null;
    //--------------Prop File Related Parameters-----------
   // @Parameters({"platform","browser","nodeurl"})
	@BeforeSuite
	public static void setup() throws Exception {
		parentPropFile = openPropertiesFile(System.getProperty("user.dir")+"\\src\\main\\java\\Resources\\Data.properties");
                   if(parentPropFile == null){
			log.info("Parent properties file not found. Application testing aborted.");
			assert false;
		}
		
		//Read value for root
        Base.root = parentPropFile.getProperty("ApplicationURL");
		browser= parentPropFile.getProperty("browser");
		
		//Initiate Remote WebDriver or Local WebDriver and navigate to root URL
		if(parentPropFile.getProperty("RemoteOrLocal").contentEquals("Remote")){
	        driver = getRemoteWebDriver(parentPropFile.getProperty("platform"), browser, parentPropFile.getProperty("nodeurl"));
	        
	        getDriver(root);
	        
			driver = new Augmenter().augment(driver);
		} else{
           driver=getLocalWebDriver(browser);
	        driver=handle();
	        getDriver(root);
		}
    	
       }

	/*@AfterMethod
	public static void afterMethod() {
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("exception in after method");
		}
	}*/
	
	@AfterSuite
	public static void tearDown() {
		//Closing properties file
		try {
			closePropFile(parentPropFileInp);
			quitDriver(driver);
			} catch (Exception e) {
			e.printStackTrace();
			log.error("error in AfterSuite");
		}
		
		}
	
	public static WebDriver getRemoteWebDriver(String platform, String browser, String nodeURL) throws MalformedURLException {
	//String nodeURL = "http://192.168.68.129:5555/wd/hub";
	WebDriver driver = null;
	DesiredCapabilities caps = new DesiredCapabilities();
	
	// Platforms
	if (platform.equalsIgnoreCase("Windows")) {
		caps.setPlatform(Platform.ANY);
	}
	if (platform.equalsIgnoreCase("MAC")) {
		caps.setPlatform(Platform.MAC);
	}
	// Browsers
	if (browser.equalsIgnoreCase("chrome")) {
		caps = DesiredCapabilities.chrome();
	}
	if (browser.equalsIgnoreCase("firefox")) {
		caps = DesiredCapabilities.firefox();
	}
	// Version
	//caps.setVersion(version);
	driver = new RemoteWebDriver(new URL(nodeURL), caps);
	 //Maximize the browser's window
	 driver.manage().window().maximize();
	return driver;
}
	
	public static WebDriver getLocalWebDriver(String browser){
		driver = null;
		if(browser.equals("chrome"))
		{
			 System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver= new ChromeDriver();
				//execute in chrome driver
			
		}
		else if (browser.equals("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
			 driver= new FirefoxDriver();
			//firefox code
		}
		else if (browser.equals("edge"))
		{
         System.setProperty("webdriver.edge.driver", "drivers\\MicrosoftWebDriver.exe");
         EdgeOptions options = new EdgeOptions();
        		 driver= new EdgeDriver(options);

		}
		 driver.manage().window().maximize();
		return driver;
	}
	
	public static Properties openPropertiesFile(String pathPropFile) {
		//Properties file variables
		parentPropFileInp = null;
	    Properties propFile = new Properties();
	    //Accesses properties file
	    log.info("Opening properties file - " + pathPropFile);
    	try {
    		parentPropFileInp = new FileInputStream(pathPropFile);
    		propFile.load(parentPropFileInp);
    		//System.out.println("Properties file - " + pathPropFile + " Loaded.");
    		log.info("Properties file - " + pathPropFile + " Loaded.");
		} catch (FileNotFoundException e) {
			//Set Null if not found
			propFile = null;
			log.info("Properties file not found - " + pathPropFile);
			e.printStackTrace();
			//log.info(e.getMessage());
		} catch (IOException e) {
			//Set Null if IO Exception
			propFile = null;
			log.info("IO Exception while accessing properties file - "  + pathPropFile);
			e.printStackTrace();
			log.error(e.getMessage());
		}
    	
    	//Return properties file object
    	return propFile;
	}
	
	public static void getDriver(String url) {
		driver.get(url);
	}
	
	public static void closeDriver(WebDriver driver){
		driver.close();
	}

	public static void quitDriver(WebDriver driver){
		driver.quit();
	}
	public void waitForSeconds(int noOfMilliSeconds){
		try {
			Thread.sleep(noOfMilliSeconds);
		} catch (InterruptedException e) {
			log.error("error in wait");
			e.printStackTrace();
		}
	}

	public static WebElement findAndWait(final By by, int waitInSec){
		try{
			WebElement webElement = (new WebDriverWait(driver, waitInSec))
	  			  .until(new ExpectedCondition<WebElement>(){
	  	           public WebElement apply(WebDriver d) {
	  					try {
	  						if (d.findElement(by).isDisplayed()){
	  							return d.findElement(by);
	  						}else{
	  							return null;
	  						}
	  					}catch(Exception e){
	  						//log.error("error in finding webelement");
	  						WebElement webElement = null;
							return webElement;
	  					}
	  				}
	  				}
	  			  );
			return webElement;
		} catch(Exception e) {
			//log.info(e.getMessage());
			WebElement webElement = null;
			return webElement;
		}
	}
	public static List<WebElement> findAndWaitWebElements(final By by, int waitInSec) {
		List<WebElement> webElement = null;
		try{
			webElement = (new WebDriverWait(driver, waitInSec)).until(new ExpectedCondition<List<WebElement>>(){
	  				public List<WebElement> apply(WebDriver d) {
	  					List<WebElement> elementList = null;
	  					try{
						elementList = d.findElements(by);
	  						if (elementList.size()>0){
	  							return d.findElements(by);
	  						}else{
	  							return null;
	  						}
	  					}catch(Exception e){
	  						log.error("no element found");
							return null;
	  					}
	  				}
	  				}
	  			  );
			return webElement;
		} catch(Exception e) {
			log.error("failed findAndWaitWebElements");
			return null;
		}
	}
	
	public static String captureScrnShot(String Path, String fileName){
		try{
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(Path + fileName +".png"));
		}catch(IOException IE){
			log.info("Failed To Capture Image: "+IE.getMessage());
		}
		return Path + fileName + ".png";
	}
		

public static void closePropFile(FileInputStream propFileInp){
	try{
		//Close Properties File
		propFileInp.close();
		log.info("Properties File - '" + propFileInp + "' Closed successfully");
	}catch(IOException e){
		log.error("IO Exception while closing properties file - '" + propFileInp + "'");
	}
}

public static void clickElementThroughJavaScript(WebElement element){
	//To Click on elements which may not be visible on screen
	((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
}
public void scrollThroughJavaScript(){
	//To Click on elements which may not be visible on screen
	((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -4500);");
}
public void clickElementThroughActions(WebDriver driver, WebElement element){
	//To Click on elements which may not be visible or are from 'LI' tag
	Actions action = new Actions(driver);
	action.moveToElement(element).click().perform();
}

public void hoverOverThroughActions(WebElement element){
	Actions action = new Actions(driver);
	action.moveToElement(element).perform();
	
}
public void DragDropThroughActions(WebDriver driver, WebElement Dragelement, WebElement Dropelement){
	//To Drag and Drop
	Actions action = new Actions(driver);
	action.dragAndDrop(Dragelement, Dropelement).build().perform();
}

public static void sendReportByGMail(String from, String pass, String to, String subject, String body) {
    Properties props = System.getProperties();
    String host = "smtp.gmail.com";
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.user", from);
    props.put("mail.smtp.password", pass);
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");

    Session session = Session.getDefaultInstance(props);
    MimeMessage message = new MimeMessage(session);

    try {
    	//Set from address
        message.setFrom(new InternetAddress(from));
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
       //Set subject
        message.setSubject(subject);
        message.setText(body);
      
        BodyPart objMessageBodyPart = new MimeBodyPart();
        
        objMessageBodyPart.setText("Please Find The Attached Report File!");
        
        Multipart multipart = new MimeMultipart();

        multipart.addBodyPart(objMessageBodyPart);

        objMessageBodyPart = new MimeBodyPart();

        //Set path to the report file
        String filename = "C:\\test\\ExtentReportsTestNG.html";//System.getProperty("c:\\test\\ExtentReportsTestNG.html");//("user.dir")+"\\Default test.pdf"; 
        //Create data source to attach the file in mail
       
        DataSource source = new FileDataSource(filename);
        
        objMessageBodyPart.setDataHandler(new DataHandler(source));

        objMessageBodyPart.setFileName(filename);

        multipart.addBodyPart(objMessageBodyPart);

        message.setContent(multipart);
        Transport transport = session.getTransport("smtp");
        transport.connect(host, from, pass);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
    catch (AddressException ae) {
    	log.info("(AddressException in MAIL");
        ae.printStackTrace();
    }
    catch (MessagingException me) {
    	log.info("MessagingException in MAIL");
        me.printStackTrace();
    }
}
public static WebDriver handle() {
	try{
		EventFiringWebDriver eDriver = new EventFiringWebDriver(driver);
	HandleEvents he = new HandleEvents();
	eDriver.register(he);
	return eDriver;
	}
	catch(Exception e) {
		log.error("error in handle webdriver");
		return null;
	}
	}
public static void getExtentInstance(String path,boolean a) {
	   try {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(path);
		   //htmlReporter.setAppendExisting(a);
		    htmlReporter.config().setDocumentTitle("ExtentReports - Created by TestNG Listener");
		    htmlReporter.config().setReportName("ExtentReports - Created by TestNG Listener");
		    htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		    htmlReporter.config().setTheme(Theme.STANDARD);
		    
		    extent = new ExtentReports();
		    extent.attachReporter(htmlReporter);
		    extent.setReportUsesManualConfiguration(true);
	} catch (Exception e) {
		log.error("error in getExtentInstance");
		e.printStackTrace();
	}

	}
public void buildTestNodes(IResultMap tests, Status status) {
 if (tests.size() > 0) {
        for (ITestResult result : tests.getAllResults()) {
            test = extent.createTest(result.getMethod().getMethodName());
            
            for (String group : result.getMethod().getGroups())
                test.assignCategory(group);

            if (result.getThrowable() != null) {
                test.log(status, result.getThrowable());
            }
            else {
                test.log(status, "Test " + status.toString().toLowerCase() + "ed");
            }
            
            test.getModel().setStartTime(getTime(result.getStartMillis()));
            test.getModel().setEndTime(getTime(result.getEndMillis()));
        }
    }
}
@AfterMethod
public void tearDown(ITestResult testResult) throws Exception {
	waitForSeconds(1000);
	if (testResult.getStatus() == ITestResult.SUCCESS) {
		log.info(testResult.getName()+ "PASS");
		//setCellData("PASS");
		
		//fileSaveClose("C:\\Meschino_Workspace\\Meschino_Wellness\\src\\main\\java\\SEB_GROUP\\Meschino_Wellness\\Resources\\","BPDATA.xlsx");
	}else if(testResult.getStatus() == ITestResult.FAILURE) {
		//setCellData("FAIL");
		log.info(testResult.getName()+ " FAIL");
	}else if(testResult.getStatus() == ITestResult.SKIP){
		//setCellData("SKIPPED");
		log.info(testResult.getName()+ " SKIPPED");}
		//fileSaveClose("C:\\Meschino_Workspace\\Meschino_Wellness\\src\\main\\java\\SEB_GROUP\\Meschino_Wellness\\Resources\\",parentPropFile.getProperty("Excel"));
	
}
private Date getTime(long millis) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return calendar.getTime();      
}

public static void selectunit(WebElement a,String value,String kk) throws InterruptedException{
	Select sel = new Select(a);
	if(kk.equals("value"))
	sel.selectByValue(value);
	if(kk.equals("text"))
    sel.selectByVisibleText(value);

}

}


