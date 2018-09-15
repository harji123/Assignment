package Resources;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;
import com.aventstack.extentreports.Status;

import Framework.Base;
public class Listeners extends Base implements IInvokedMethodListener, ITestListener, ISuiteListener,IReporter,IExecutionListener {

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		
		getExtentInstance("Screenshots&reports" + File.separator + "ExtentReportsTestNG.html", true);
		for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            
            for (ISuiteResult r : result.values()) {
            	System.out.print(r);
                ITestContext context = r.getTestContext();
         
                buildTestNodes(context.getFailedTests(), Status.FAIL);
                buildTestNodes(context.getSkippedTests(), Status.SKIP);
                buildTestNodes(context.getPassedTests(), Status.PASS);
                
               
            }
        }
        
        for (String s : Reporter.getOutput()) {
            extent.setTestRunnerOutput(s);
           
        }
        
        extent.flush();
    }

	
	public void onStart(ISuite suite) {
		
		
		// TODO Auto-generated method stub
		
	}

	public void onFinish(ISuite suite) {
		// TODO Auto-generated method stub
		
	}

	public void onTestStart(ITestResult result) {
		log.info(("Start Of TEST->"+result.getName()));
		
	}

	public void onTestSuccess(ITestResult result) {
		log.info(("Test Passed--"+result.getName()));
		
	}

	public void onTestFailure(ITestResult result) {
		log.info(("Test Failed--"+result.getName()));
		String path=captureScrnShot("Screenshots&reports\\",result.getInstanceName());
		log.info("screenshot captured "+path);
		
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(ITestContext context) {
		log.info(("Start Of Execution(TEST)->"+context.getName()));
		
	}

	public void onFinish(ITestContext context) {
		log.info(("Finish Of Execution(TEST)->"+context.getName()));
		
	}

	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		log.info(("Finish Of Execution(TEST)kkkkkkkkk->"+method.getTestMethod()));
	}

	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
	}


	public void onExecutionStart() {
		// TODO Auto-generated method stub
		
	}


	public void onExecutionFinish() {
		// TODO Auto-generated method stub
		
	}



}