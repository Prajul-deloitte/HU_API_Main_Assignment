import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener implements ITestListener{
    log4j_file log = new log4j_file();
    public static ExtentReports extent;
    public static ExtentTest test;


    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(""+result.getName());

    }
    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass("the test case is passed ");
        log.logger.info("The " + result.getName()+" is successful");


    }

    @Override
    public void onTestFailure(ITestResult result) {

        test.fail("The test is failed ");
        log.logger.info("The " + result.getName()+" is failed");

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.logger.info("The Test case is skipped "+result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext result) {
        extent = Extent_manager.Extent_reporter();
        log.logger.info("The Test "+result.getName()+" started");

    }

    @Override
    public void onFinish(ITestContext result) {
        extent.flush();
        log.logger.info("The Testing is Completed");

    }
}
