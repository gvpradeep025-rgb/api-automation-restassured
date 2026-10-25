package com.pradeep.api.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Builds an HTML report at reports/api-test-report.html with every test, its status and failure reason. */
public class ExtentReportListener implements ITestListener {

    private static final ExtentReports EXTENT = new ExtentReports();
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    static {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/api-test-report.html");
        spark.config().setDocumentTitle("API Test Report");
        spark.config().setReportName("Restful Booker API Automation");
        EXTENT.attachReporter(spark);
        EXTENT.setSystemInfo("Tool", "REST Assured + TestNG");
        EXTENT.setSystemInfo("API", "restful-booker.herokuapp.com");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getTestClass().getRealClass().getSimpleName() + " :: " + result.getMethod().getMethodName();
        TEST.set(EXTENT.createTest(name, result.getMethod().getDescription()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TEST.get().pass("Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        TEST.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (TEST.get() != null) {
            TEST.get().skip("Skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        EXTENT.flush();
    }
}
