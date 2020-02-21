package com.qa.hubspot.tests;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qa.hubspot.base.BasePage;
import com.qa.hubspot.page.HomePage;
import com.qa.hubspot.page.LoginPage;
import com.qa.hubspot.util.AppConstants;
import com.qa.hubspot.util.Credentials;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Epic - 101 : create login page features")
@Feature("US - 501 : create test for login page on hubspot")
public class LoginPageTest {

	WebDriver driver;
	BasePage basePage;
	Properties prop;
	LoginPage loginPage;
	Credentials userCred;
	
	Logger log = Logger.getLogger(LoginPageTest.class);


	@BeforeTest(alwaysRun=true)
	@Parameters(value={"browser"})
	public void setUp(String browser) {
		String browserName = null;
		basePage = new BasePage();
		prop = basePage.init_properties();
		
		if(browser.equals(null)){
			 browserName = prop.getProperty("browser");
		}else{
			browserName = browser;
		}

		driver = basePage.init_driver(browserName);
		driver.get(prop.getProperty("url"));
		log.info("url is launched: " + prop.getProperty("url"));
		loginPage = new LoginPage(driver);
		userCred = new Credentials(prop.getProperty("username"), prop.getProperty("password"));

	}

	@Test(priority = 1, description = "verify login page title test ....!!!")
	@Description("verify Login Page Title Test....")
	@Severity(SeverityLevel.NORMAL)
	public void verifyLoginPageTitleTest() throws InterruptedException {
		log.info("starting---------------------->>>>>>>>>>verifyLoginPageTitleTest");
		String title = loginPage.getPageTitle();
		System.out.println("login page title is: " + title);
		log.info("login page title is: " + title);
		Assert.assertEquals(title, AppConstants.LOGIN_PAGE_TITLE);
		log.info("ending---------------------->>>>>>>>>>verifyLoginPageTitleTest");
		log.warn("some warning");
		log.error("some error");
		log.fatal("fatal error");
	}

	@Test(priority = 2, groups = "sanity")
	@Description("verify Sign up link Test....")
	@Severity(SeverityLevel.CRITICAL)
	public void verifySignUpLinkTest() {
		log.info("starting---------------------->>>>>>>>>>verifySignUpLinkTest");
		Assert.assertTrue(loginPage.checkSignUpLink());
		log.info("ending---------------------->>>>>>>>>>verifySignUpLinkTest");

	}

	@Test(priority = 3)
	@Description("verify Login Test....")
	@Severity(SeverityLevel.BLOCKER)
	public void loginTest() {
		log.info("starting---------------------->>>>>>>>>>loginTest");
		HomePage homePage = loginPage.doLogin(userCred);
		String accountName = homePage.getLoggedInUserName();
		Assert.assertEquals(accountName, prop.getProperty("accountname"));
		log.info("ending---------------------->>>>>>>>>>loginTest");

	}

	@DataProvider
	public Object[][] getLoginInvalidData() {
		Object data[][] = {
				            { "test1111@gmail.com", "test123" },
				            { "test2@gmail.com", "" },
				            { "", "test12345" },
				            {"test","test"},
				            {"",""}
				          };
		return data;
	}

	@Test(priority =4,dataProvider = "getLoginInvalidData", enabled =false)
	public void login_InvalidTestCases(String username, String pwd) {
		log.info("starting---------------------->>>>>>>>>>login_InvalidTestCases");
		userCred.setAppUserName(username);
		userCred.setAppUserPassword(pwd);
		loginPage.doLogin(userCred);
		Assert.assertTrue(loginPage.checkLoginErrorMesg());
		log.info("ending---------------------->>>>>>>>>>login_InvalidTestCases");
	}

	@AfterTest(alwaysRun=true)
	public void tearDown() {
		driver.quit();
	}

}