import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class Extent_manager {
    public static ExtentReports Extent_reporter() {
        ExtentReports extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter("C:\\Users\\prajchaudhary\\IdeaProjects\\HU_API_Main_Assignment\\logs\\extents.html");
        extentReports.attachReporter(reporter);
        reporter.config().setTheme(Theme.DARK);
        reporter.config().setDocumentTitle("API MAIN ASSIGNMENT");
        reporter.config().setReportName("MAIN ASSIGNMENT EXTENT REPORT");
        return extentReports;
    }
}
