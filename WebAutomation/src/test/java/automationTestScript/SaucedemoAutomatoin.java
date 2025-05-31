package automationTestScript;

import java.lang.reflect.Method;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import PageWaiseClasses.CartPage;
import PageWaiseClasses.CheakOutCompletePage;
import PageWaiseClasses.CheakOutOverviewPage;
import PageWaiseClasses.CheakoutPage;
import PageWaiseClasses.LoginPage;
import PageWaiseClasses.ProductPage;
import genaricMethod.WebUtil;

public class SaucedemoAutomatoin {
	
	
	private WebUtil wu;
	private ExtentReports report;
	private ExtentTest et;
	
	@BeforeSuite
	public void beforeSuite() {
		report=new ExtentReports();
		ExtentSparkReporter sparkReport=new ExtentSparkReporter("Reports//report.html");
		report.attachReporter(sparkReport);
		
	}
   @AfterSuite
   public void afterSuit() {
	   report.flush();
  
   }

  @BeforeMethod
  public void setup(Method m) {
	  String tcName=m.getName();
	  et=report.createTest(tcName);
	  wu=new WebUtil(et);
	  wu.launchBrowser("chrome", 60);
  }

  @AfterMethod
  public void tareDown() {
	  wu.closeBrowser();
  }

  @Test(priority = 1)
  public void tc_001PurchaseOrderSuccessfuly() {
	  
	  wu.openURL("https://www.saucedemo.com/");
	  
	  LoginPage lp= new LoginPage(wu);
	  lp.validLogin();
	  
	  ProductPage pp= new ProductPage(wu);
	  pp.applyFilter();
	  List<Double> prices = pp.productPrice();
	  int n = prices.size();
      double highestPrice1 = prices.get(n - 1);
      double highestPrice2 = prices.get(n - 2);

      pp.addProductToCartByPrice(highestPrice1);
      pp.addProductToCartByPrice(highestPrice2);

      pp.cartBt();
      
      CartPage cp= new CartPage(wu);
     // Assert.assertEquals(cp.getNumberOfItems(), 2, "Cart must contain exactly 2 items");
       
      double sumPrices = cp.getTotalPrice();

      cp.clickCheckout();

      CheakoutPage ceakOutP=  new CheakoutPage(wu);
      ceakOutP.fillupCheakotDetauls();
      
      CheakOutOverviewPage cop= new CheakOutOverviewPage(wu);
     double itemTotal= cop.getItemTotal();

     // Assert.assertEquals(itemTotal, sumPrices, 0.01, "Item total must match sum of prices");

      cop.clickFinish();
     
      CheakOutCompletePage ccp= new CheakOutCompletePage(wu);
     String msg= ccp.completePage();
      
     // Assert.assertEquals(msg.trim(), "THANK YOU FOR YOUR ORDER");

     
  }


  @Test(priority = 2)
  public void tc002LoginFaild() {
	  
 wu.openURL("https://www.saucedemo.com/");
	  
	  LoginPage lp= new LoginPage(wu);
	  lp.InvalidLogin();
  }










}
