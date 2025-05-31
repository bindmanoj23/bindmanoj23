package genaricMethod;
import org.openqa.selenium.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.common.io.Files;

public class WebUtil {
	
	private WebDriver driver;// driver 
	private ExtentTest et;
	
	public WebUtil(ExtentTest et) {// constructor
		this.et=et;
		
	}
	
	// generic method, utility method, wrapper method
	
	
	
	
      public WebDriver getDriver() {
    	  return driver;
      }
	
	/* This method will lanch the browser
	 * parameter browser name . ie - chrome
	 * return type- object of WebDriver
	 */
      
      
      
      public WebDriver launchBrowser(String browserName, int timeInSeconds) {
    	    switch (browserName.toLowerCase()) {
    	        case "chrome":
    	            driver = new ChromeDriver();
    	            break;
    	        case "firefox":
    	            driver = new FirefoxDriver();
    	            break;
    	        case "edge":
    	            driver = new EdgeDriver();
    	            break;
    	        default:
    	            et.log(Status.FAIL, "Invalid browser name: " + browserName);
    	            throw new IllegalArgumentException("Invalid browser name: " + browserName);
    	    }
    	    driver.manage().window().maximize();
    	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeInSeconds));
    	    et.log(Status.INFO, browserName + " browser launched successfully");
    	    return driver;
    	}


//	}
	/*this metheod will be get page title
	 * return type -String */
      public String getPageTitle() {
    	    try {
    	        String title = driver.getTitle();
    	        et.log(Status.INFO, "Page title retrieved successfully: " + title);
    	        System.out.println("Page title found successfully: " + title);
    	        return title;
    	    } catch (Exception e) {
    	        et.log(Status.FAIL, "Failed to retrieve page title: " + e.getMessage());
    	        System.out.println("Failed to retrieve page title: " + e.getMessage());
    	        e.printStackTrace();
    	        // Optional: capture screenshot on failure
    	        takeScreenShot("getPageTitle_Failure_" + System.currentTimeMillis());
    	        return null;
    	    }
    	}


	
	/*this metheod will be get page url
	 * return type -String */
      public String getPageURL() {
    	    try {
    	        String url = driver.getCurrentUrl();
    	        et.log(Status.INFO, "Page URL retrieved successfully: " + url);
    	        System.out.println("Page URL found successfully: " + url);
    	        return url;
    	    } catch (Exception e) {
    	        et.log(Status.FAIL, "Failed to retrieve page URL: " + e.getMessage());
    	        System.out.println("Failed to retrieve page URL: " + e.getMessage());
    	        e.printStackTrace();
    	        // Optionally take a screenshot on failure
    	        takeScreenShot("getPageURL_Failure_" + System.currentTimeMillis());
    	        return null;
    	    }
    	}


	/*this metheod will be open page url
	 * parameter- String */
      public void openURL(String url) {
    	    try {
    	        driver.get(url);
    	        et.log(Status.INFO, "URL opened successfully: " + url);
    	        System.out.println(url + ": opened successfully");
    	    } catch (Exception e) {
    	        et.log(Status.FAIL, "Failed to open URL: " + url + ". Error: " + e.getMessage());
    	        System.out.println("Failed to open URL: " + url + ". Error: " + e.getMessage());
    	        e.printStackTrace();
    	        // Call your screenshot method with a descriptive name
    	        takeScreenShot("openURL_Failure_" + System.currentTimeMillis());
    	    }
    	}


      
      
   
      
      public void takeScreenShot(String screenshotName) {
    	    TakesScreenshot ts = (TakesScreenshot) driver;
    	    File sourceFile = ts.getScreenshotAs(OutputType.FILE);
    	    String filePath = "ScreenShot\\" + screenshotName + ".png";
    	    File targetLocation = new File(filePath);
    	    try {
    	        Files.copy(sourceFile, targetLocation);
    	        et.log(Status.FAIL, "Screenshot taken: " + filePath);
    	    } catch (IOException e) {
    	        et.log(Status.FAIL, "Failed to save screenshot. Error: " + e.getMessage());
    	        System.out.println("Failed to save screenshot. Error: " + e.getMessage());
    	        e.printStackTrace();
    	    }
    	}

	/*this metheod will be search element
	 * parameter -String */
      public WebElement searchElement(String xpath, String element) {
    	    WebElement we = null;
    	    try {
    	        we = driver.findElement(By.xpath(xpath));
    	        et.log(Status.PASS, element + " found successfully");
    	        System.out.println(element + " found successfully");
    	    } catch (NoSuchElementException e) {
    	        et.log(Status.WARNING, element + " not found on first attempt, retrying after wait...");
    	        System.out.println(element + " not found on first attempt, retrying...");
    	        try {
    	            Thread.sleep(5000); // shorter wait for retry
    	            we = driver.findElement(By.xpath(xpath));
    	            et.log(Status.PASS, element + " found successfully after retry");
    	            System.out.println(element + " found successfully after retry");
    	        } catch (NoSuchElementException e2) {
    	            et.log(Status.FAIL, element + " not found after retry");
    	            takeScreenShot(element + "_notFound_" + System.currentTimeMillis());
    	            e2.printStackTrace();
    	            throw e2;  // rethrow so the test knows this is a failure
    	        } catch (InterruptedException ie) {
    	            ie.printStackTrace();
    	            // optionally log this too
    	        }
    	    } catch (InvalidSelectorException e) {
    	        et.log(Status.FAIL, element + " has invalid XPath syntax: " + e.getMessage());
    	        takeScreenShot(element + "_invalidXPath_" + System.currentTimeMillis());
    	        System.out.println(element + " xpath syntax is wrong, please check");
    	        e.printStackTrace();
    	        throw e;
    	    } catch (Exception e) {
    	        et.log(Status.FAIL, "Exception while searching for " + element + ": " + e.getMessage());
    	        takeScreenShot(element + "_exception_" + System.currentTimeMillis());
    	        e.printStackTrace();
    	        throw e;
    	    }
    	    return we;
    	}

		
//				// genaric method for sendKeys
	public void type(WebElement we, String value, String element) {
	    try {
	        we.clear();  // clear existing text before typing, optional but good practice
	        we.sendKeys(value);
	        et.log(Status.INFO, element + " entered '" + value + "' successfully");
	        System.out.println(element + " " + value + " entered in textbox successfully");
	    } catch (ElementNotInteractableException e) {
	        try {
	            JavascriptExecutor jse = (JavascriptExecutor) driver;
	            jse.executeScript("arguments[0].value='" + value + "';", we);
	            et.log(Status.INFO, element + " entered '" + value + "' successfully by JavaScriptExecutor");
	            System.out.println(element + " " + value + " entered in textbox successfully by JavaScript");
	        } catch (Exception jsEx) {
	            et.log(Status.FAIL, "JS typing failed on " + element + ". Error: " + jsEx.getMessage());
	            takeScreenShot(element + "_typeFail_" + System.currentTimeMillis());
	            System.out.println("JS typing failed on " + element);
	            jsEx.printStackTrace();
	            throw jsEx;  // or handle accordingly
	        }
	    } catch (Exception e) {
	        et.log(Status.FAIL, "Typing failed on " + element + ". Error: " + e.getMessage());
	        takeScreenShot(element + "_typeFail_" + System.currentTimeMillis());
	        System.out.println("Typing failed on " + element);
	        e.printStackTrace();
	        throw e;  // or handle accordingly
	    }
	}

	// genaric method for click

	public void click(WebElement we, String element) throws ElementClickInterceptedException {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.elementToBeClickable(we));
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", we);
	        we.click();
	        et.log(Status.INFO, element + " clicked successfully");
	        System.out.println(element + " clicked successfully");
	    } catch (ElementNotInteractableException e) {
	        try {
	            JavascriptExecutor jse = (JavascriptExecutor) driver;
	            jse.executeScript("arguments[0].click();", we);
	            et.log(Status.INFO, e.getMessage() + "\n" + element + " clicked successfully by JavaScript");
	            System.out.println(element + " clicked successfully by JavaScript");
	        } catch (Exception jsEx) {
	            et.log(Status.FAIL, "JS click failed on " + element + ". Error: " + jsEx.getMessage());
	            takeScreenShot(element + "_clickFail_" + System.currentTimeMillis());
	            System.out.println("JS click also failed on " + element);
	            jsEx.printStackTrace();
	            throw jsEx;
	        }
	    } catch (Exception e) {
	        et.log(Status.FAIL, "Click failed on " + element + ". Error: " + e.getMessage());
	        takeScreenShot(element + "_clickFail_" + System.currentTimeMillis());
	        System.out.println("Click failed on " + element);
	        e.printStackTrace();
	        throw e;
	    }
	}


		//click by javaScript
		public void jsClick(WebElement we, String element) {
		    try {
		        JavascriptExecutor jse = (JavascriptExecutor) driver;
		        jse.executeScript("arguments[0].click();", we);

		        et.log(Status.INFO, element + " clicked successfully by JavaScriptExecutor");
		        System.out.println(element + " clicked successfully by JavaScript");
		    } catch (Exception e) {
		        et.log(Status.FAIL, "Failed to click " + element + " by JavaScriptExecutor. Error: " + e.getMessage());
		        takeScreenShot(element + "_jsClickFail_" + System.currentTimeMillis());
		        System.out.println("Failed to click " + element + " by JavaScript");
		        e.printStackTrace();
		    }
		}

		
        // sendkeys by javaScript 
		public void jsType(WebElement we, String value, String element, ExtentTest extTest) {
		    try {
		        JavascriptExecutor jse = (JavascriptExecutor) driver;
		        jse.executeScript("arguments[0].value=arguments[1];", we, value);

		        extTest.log(Status.INFO, element + " typed successfully by JavaScript");
		        System.out.println(element + " typed successfully by JavaScript");
		    } catch (Exception e) {
		        extTest.log(Status.FAIL, "Failed to type " + element + " by JavaScript. Error: " + e.getMessage());
		        takeScreenShot(element + "_jsTypeFail_" + System.currentTimeMillis());
		        System.out.println("Failed to type " + element + " by JavaScript");
		        e.printStackTrace();
		    }
		}

		

		 /*this method scroll by javaScript 
		   parameter- webElement     */
		public void jsScrollToBottom() {
		    try {
		        JavascriptExecutor jse = (JavascriptExecutor) driver;
		        jse.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		        et.log(Status.PASS, "Scrolled to the bottom of the page successfully.");
		        System.out.println("Scrolled to the bottom successfully.");
		    } catch (Exception e) {
		        et.log(Status.FAIL, "Failed to scroll to the bottom. Error: " + e.getMessage());
		        takeScreenShot("ScrollToBottomFail_" + System.currentTimeMillis());
		        System.out.println("Failed to scroll to the bottom.");
		        e.printStackTrace();
		    }
		}

		

		 /*this method scroll by javaScript 
		   parameter- webElement     */
		public void jsScrollByAmount(int x, int y) {
		    try {
		        JavascriptExecutor jse = (JavascriptExecutor) driver;
		        jse.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
		        et.log(Status.PASS, "Scrolled by amount X: " + x + ", Y: " + y + " using JavaScript.");
		        System.out.println("Scrolled by amount successfully.");
		    } catch (Exception e) {
		        et.log(Status.FAIL, "Failed to scroll by amount X: " + x + ", Y: " + y + ". Error: " + e.getMessage());
		        takeScreenShot("ScrollByAmountFail_" + System.currentTimeMillis());
		        System.out.println("Failed to scroll by amount.");
		        e.printStackTrace();
		    }
		}


		
		 /*this method scroll by javaScript 
		   parameter- webElement     */
		public void jsScrollToElement(WebElement we, String elementName) {
		    try {
		        JavascriptExecutor jse = (JavascriptExecutor) driver;
		        jse.executeScript("arguments[0].scrollIntoView(true);", we);
		        et.log(Status.PASS, "Scrolled to element: " + elementName + " using JavaScript.");
		        System.out.println("Scrolled to " + elementName + " successfully.");
		    } catch (Exception e) {
		        et.log(Status.FAIL, "Failed to scroll to element: " + elementName + ". Error: " + e.getMessage());
		        takeScreenShot("ScrollFail_" + elementName + "_" + System.currentTimeMillis());
		        System.out.println("Failed to scroll to " + elementName);
		        e.printStackTrace();
		    }
		}

		
		
		
		
// ===========================Get Attributr value=======================================
		
		public String getAttributeValue(WebElement we, String attributeName, String elementName) {
		    String attrValue = "";
		    try {
		        attrValue = we.getDomAttribute(attributeName);
		        et.log(Status.PASS, elementName + " attribute '" + attributeName + "' found: " + attrValue);
		        System.out.println(elementName + " attribute '" + attributeName + "' found successfully");
		    } catch (Exception e) {
		        et.log(Status.FAIL, "Failed to get attribute '" + attributeName + "' from " + elementName + ". Error: " + e.getMessage());
		        takeScreenShot("getAttributeFail_" + elementName + "_" + System.currentTimeMillis());
		        System.out.println("Failed to get attribute '" + attributeName + "' from " + elementName);
		        e.printStackTrace();
		    }
		    return attrValue;
		}
	
		
		
		
// =====================================select class method select innertext========================
		
		public void selectTextFromListBox(WebElement we, String selectText, String elementName) {
		    try {
		        Select select = new Select(we);
		        select.selectByVisibleText(selectText);
		        et.log(Status.PASS, elementName + " selected successfully with text: " + selectText);
		        System.out.println(elementName + " " + selectText + " selected successfully");
		    } catch (Exception e) {
		        et.log(Status.FAIL, "Failed to select " + elementName + " with text: " + selectText + ". Error: " + e.getMessage());
		        takeScreenShot("SelectByVisibleTextFail_" + elementName + "_" + System.currentTimeMillis());
		        System.out.println("Failed to select " + elementName + " with text: " + selectText);
		        e.printStackTrace();
		    }
		}

		
		
		//=========================== select by vissible text===================================
		
		public void selectTextFromListBoxByIndex(WebElement we, int index, String elementName) {
		    try {
		        Select select = new Select(we);
		        select.selectByIndex(index);
		        et.log(Status.PASS, elementName + " selected successfully by index: " + index);
		        System.out.println(elementName + " selected successfully by index: " + index);
		    } catch (Exception e) {
		        et.log(Status.FAIL, "Failed to select " + elementName + " by index: " + index + ". Error: " + e.getMessage());
		        takeScreenShot("SelectByIndexFail_" + elementName + "_" + System.currentTimeMillis());
		        System.out.println("Failed to select " + elementName + " by index: " + index);
		        e.printStackTrace();
		    }
		}
	
		//=========================== select by value ===================================
		
			public void selectTextByValue(WebElement we, String value) {
			    try {
			        Select select = new Select(we);
			        select.selectByValue(value);
			        et.log(Status.PASS, "Selected value '" + value + "' successfully.");
			        System.out.println(value + " selected successfully");
			    } catch (Exception e) {
			        et.log(Status.FAIL, "Failed to select value '" + value + "'. Error: " + e.getMessage());
			        takeScreenShot("SelectByValueFail_" + value + "_" + System.currentTimeMillis());
			        System.out.println("Failed to select value: " + value);
			        e.printStackTrace();
			    }
			}
		
			

			
//================================Action class right click=======================
			public void rightClick(WebElement we, String elementName) {
			    try {
			        Actions act = new Actions(driver);
			        act.contextClick(we).build().perform();
			        et.log(Status.PASS, "Right click performed successfully on element: " + elementName);
			        System.out.println(elementName + " right click performed successfully");
			    } catch (Exception e) {
			        et.log(Status.FAIL, "Failed to perform right click on element: " + elementName + ". Error: " + e.getMessage());
			        takeScreenShot("RightClickFail_" + elementName + "_" + System.currentTimeMillis());
			        System.out.println("Failed to perform right click on element: " + elementName);
			        e.printStackTrace();
			    }
			}					

		
//================================= dubble click=================================
		
		public void doubleClick(WebElement we, String elementName) {
		    try {
		        Actions act = new Actions(driver);
		        act.doubleClick(we).build().perform();
		        et.log(Status.PASS, "Double clicked successfully on element: " + elementName);
		        System.out.println(elementName + " clicked successfully");
		    } catch (Exception e) {
		        et.log(Status.FAIL, "Failed to double click on element: " + elementName + ". Error: " + e.getMessage());
		        takeScreenShot("DoubleClickFail_" + elementName + "_" + System.currentTimeMillis());
		        System.out.println("Failed to double click on element: " + elementName);
		        e.printStackTrace();
		    }
		}

			
//========================== move to element======================================
			
			public void moveToElement(WebElement we, String elementName) {
			    try {
			        Actions act = new Actions(driver);
			        act.moveToElement(we).build().perform();
			        et.log(Status.PASS, "Hovered successfully on element: " + elementName);
			        System.out.println(elementName + " hover successfuly");
			    } catch (Exception e) {
			        et.log(Status.FAIL, "Failed to hover on element: " + elementName + ". Error: " + e.getMessage());
			        takeScreenShot("HoverFail_" + elementName + "_" + System.currentTimeMillis());
			        System.out.println("Failed to hover on element: " + elementName);
			        e.printStackTrace();
			    }
			}

				// ========================scroll By amount==========================
				
				public void scrollByAmount(int x, int y) {
				    try {
				        Actions act = new Actions(driver);
				        act.scrollByAmount(x, y).build().perform();
				        et.log(Status.PASS, "Scrolled successfully by amount x: " + x + ", y: " + y);
				        System.out.println("Scrolled successfully by amount x: " + x + ", y: " + y);
				    } catch (Exception e) {
				        et.log(Status.FAIL, "Failed to scroll by amount x: " + x + ", y: " + y + ". Error: " + e.getMessage());
				        takeScreenShot("ScrollByAmountFail_" + System.currentTimeMillis());
				        System.out.println("Failed to scroll by amount x: " + x + ", y: " + y);
				        e.printStackTrace();
				    }
				}

				
				// ======================scroll By Action class======================
				
				public void scrollToElement(WebElement we, String elementName) {
				    try {
				        Actions act = new Actions(driver);
				        act.scrollToElement(we).build().perform();
				        et.log(Status.PASS, "Scrolled successfully to element: " + elementName);
				        System.out.println("Scrolled successfully to element: " + elementName);
				    } catch (Exception e) {
				        et.log(Status.FAIL, "Failed to scroll to element: " + elementName + ". Error: " + e.getMessage());
				        takeScreenShot("ScrollFail_" + elementName + "_" + System.currentTimeMillis());
				        System.out.println("Failed to scroll to element: " + elementName);
				        e.printStackTrace();
				    }
				}

		
		/* this method will return inner text of element
		 * parameter- WebElement object
		 * return type- String
		 * Author -manoj kumar
		 * Modified date-
		 * modifide by-*/
				public String getInnerText(WebElement we, String elementName) {
				    String text = null;
				    try {
				        text = we.getText();
				        et.log(Status.PASS, elementName + " inner text found successfully: " + text);
				        System.out.println(elementName + " inner text: " + text);
				    } catch (Exception e) {
				        et.log(Status.FAIL, "Failed to get inner text of element: " + elementName + ". Error: " + e.getMessage());
				        takeScreenShot(elementName + "_getInnerTextFail_" + System.currentTimeMillis());
				        System.out.println("Failed to get inner text of element: " + elementName);
				        e.printStackTrace();
				    }
				    return text;
				}
	
     // =====================Get attribute value====================
       
       public String getAttribute(WebElement we, String attributeName, String elementName) {
    	    String attrValue = null;
    	    try {
    	        attrValue = we.getDomAttribute(attributeName);
    	        et.log(Status.PASS, elementName + " attribute '" + attributeName + "' found successfully with value: " + attrValue);
    	        System.out.println(elementName + " attribute '" + attributeName + "' found successfully");
    	    } catch (Exception e) {
    	        et.log(Status.FAIL, "Failed to get attribute '" + attributeName + "' for element: " + elementName + ". Error: " + e.getMessage());
    	        takeScreenShot(elementName + "_getAttributeFail_" + System.currentTimeMillis());
    	        System.out.println("Failed to get attribute '" + attributeName + "' for element: " + elementName);
    	        e.printStackTrace();
    	    }
    	    return attrValue;
    	}

    
    
    //======================= get all ElementText==========================
       
    public List<String> getAllElementsText(String xpath) {
        List<String> elementTextList = new ArrayList<>();
        try {
            List<WebElement> elements = driver.findElements(By.xpath(xpath));

            if (elements.isEmpty()) {
                et.log(Status.WARNING, "No elements found for XPath: " + xpath);
                takeScreenShot("NoElementsFound_" + System.currentTimeMillis()); // screenshot method call
            } else {
                for (WebElement element : elements) {
                    elementTextList.add(element.getText().trim());
                }
                et.log(Status.PASS, "Found " + elements.size() + " elements for XPath: " + xpath);
            }
        } catch (Exception e) {
            et.log(Status.FAIL, "Exception while getting elements text for XPath: " + xpath + ". Error: " + e.getMessage());
            takeScreenShot("Exception_" + System.currentTimeMillis());
            e.printStackTrace();
        }

        return elementTextList;
    }

 // click all element
    public void clickAllElements(String xpath) {
    	List<WebElement>list=driver.findElements(By.xpath(xpath));
    	for(WebElement we:list ) {
    		we.click();
    	}
    }

    /*This metheod will will be window handle
     
     * */
    
    //========================WindoHandle By UrL======================================
    
    public void switchTowindowByUrl(String expectedURL) {
	    Set<String> handleValues = driver.getWindowHandles();
	    boolean isSwitched = false;

	    for (String handleValue : handleValues) {
	        driver.switchTo().window(handleValue);
	        String currentWindowURL = driver.getCurrentUrl();

	        if (currentWindowURL.equalsIgnoreCase(expectedURL)) {
	            et.log(Status.PASS, "Switched to window with URL: " + expectedURL);
	            System.out.println("Focus switched to new window");
	            isSwitched = true;
	            break;
	        }
	    }

	    if (!isSwitched) {
	        et.log(Status.FAIL, "Failed to switch to window with URL: " + expectedURL);
	        System.out.println("Failed to find window with the expected URL");
	        takeScreenShot(expectedURL); // Call your existing screenshot method
	    }
	}

    //========================WindoHandle By Title======================================
    
    public void switchToWindowByTitle(String expectedTitle) {
        Set<String> handles = driver.getWindowHandles();
        boolean isSwitched = false;

        for (String handle : handles) {
            driver.switchTo().window(handle);
            String currentTitle = driver.getTitle();

            if (currentTitle.equalsIgnoreCase(expectedTitle)) {
                et.log(Status.PASS, "Switched to window with Title: " + expectedTitle);
                System.out.println("Focus switched to window with Title");
                isSwitched = true;
                break;
            }
        }

        if (!isSwitched) {
            et.log(Status.FAIL, "Failed to switch to window with Title: " + expectedTitle);
            System.out.println("Window with expected Title not found");
            takeScreenShot(expectedTitle);  // Screenshot on failure
        }
    }

    
    //====================windoHandleBY Index============================
    
    public void switchToWindowByIndex(int index) {
        Set<String> handles = driver.getWindowHandles();

        if (index < 0 || index >= handles.size()) {
            et.log(Status.FAIL, "Invalid window index: " + index);
            System.out.println("Invalid window index");
            takeScreenShot("InvalidIndex_" + index);
            return;
        }

        String[] handlesArray = handles.toArray(new String[0]);
        driver.switchTo().window(handlesArray[index]);
        et.log(Status.PASS, "Switched to window at index: " + index);
        System.out.println("Focus switched to window at index: " + index);
    }

    //=========================closeAllWindoBackMainWindow==================
    
    public void closeAllChildWindowsAndSwitchToParent() {
	    String parentHandle = driver.getWindowHandle();
	    Set<String> handles = driver.getWindowHandles();

	    for (String handle : handles) {
	        if (!handle.equals(parentHandle)) {
	            driver.switchTo().window(handle);
	            driver.close();
	            et.log(Status.INFO, "Closed child window: " + handle);
	            System.out.println("Closed child window: " + handle);
	        }
	    }

	    driver.switchTo().window(parentHandle);
	    et.log(Status.INFO, "Switched back to parent window.");
	    System.out.println("Switched back to parent window");
	}

	
    
    //========================handleIframe======================================
    
  //=========================== Switch iframe by index=======================================
    
    public void switchToFrameByIndex(int index) {
        try {
            driver.switchTo().frame(index);
            et.log(Status.INFO, "Switched to frame with index: " + index);
            System.out.println("Switched to frame with index: " + index);
        } catch (NoSuchFrameException e) {
            et.log(Status.FAIL, "No frame found at index: " + index + ". Error: " + e.getMessage());
            takeScreenShot("frameSwitchFail_" + index + "_" + System.currentTimeMillis());
            System.out.println("No frame found at index: " + index);
            e.printStackTrace();
        } catch (Exception e) {
            et.log(Status.FAIL, "Error while switching to frame by index: " + index + ". Error: " + e.getMessage());
            takeScreenShot("frameSwitchFail_" + index + "_" + System.currentTimeMillis());
            System.out.println("General error while switching to frame at index: " + index);
            e.printStackTrace();
        }
    }

//=================================== switchTo iframe =========================================
    public void switchToFrame(WebElement weFrame) {
        try {
            driver.switchTo().frame(weFrame);
            et.log(Status.INFO, "Switched to frame successfully using WebElement.");
            System.out.println("Switched to frame using WebElement.");
        } catch (NoSuchFrameException e) {
            et.log(Status.FAIL, "No such frame found using the provided WebElement. Error: " + e.getMessage());
            takeScreenShot("frameSwitchFail_Element_" + System.currentTimeMillis());
            System.out.println("No such frame found using the provided WebElement.");
            e.printStackTrace();
        } catch (Exception e) {
            et.log(Status.FAIL, "Error while switching to frame using WebElement. Error: " + e.getMessage());
            takeScreenShot("frameSwitchFail_Element_" + System.currentTimeMillis());
            System.out.println("Error while switching to frame using WebElement.");
            e.printStackTrace();
        }
    }
   //=================================SWITCH FRANME BY ID===========================================
    public void switchToFrameByNameOrId(String nameOrId) {
        try {
            driver.switchTo().frame(nameOrId);
            et.log(Status.INFO, "Switched to frame: " + nameOrId);
            System.out.println("Switched to frame by name/ID: " + nameOrId);
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to switch to frame: " + nameOrId + ". Error: " + e.getMessage());
            takeScreenShot("frameNameId_" + nameOrId + "_fail_" + System.currentTimeMillis());
            e.printStackTrace();
        }
    }

    
   //================================Switch defalt/main frame==================================
    
    public void switchToDefaultContent() {
        try {
            driver.switchTo().defaultContent();
            et.log(Status.INFO, "Switched back to default content");
            System.out.println("Switched to default content");
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to switch to default content. Error: " + e.getMessage());
            takeScreenShot("defaultContent_fail_" + System.currentTimeMillis());
            e.printStackTrace();
        }
    }


// page maximize
    public void maximize() {
        try {
            driver.manage().window().maximize();
            et.log(Status.INFO, "Browser window maximized successfully");
            System.out.println("Browser window maximized successfully");
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to maximize browser window. Error: " + e.getMessage());
            takeScreenShot("maximize_fail_" + System.currentTimeMillis());
            System.out.println("Failed to maximize browser window");
         
            e.printStackTrace();
        }
    }

		// set size
    

    public void setSize(int width, int height) {
        try {
           Dimension dim = new Dimension(width, height);  // Make sure this is selenium Dimension
            driver.manage().window().setSize(dim);
     
            et.log(Status.INFO, "Browser window size set to: Width = " + width + ", Height = " + height);
            System.out.println("Browser window resized successfully to Width: " + width + ", Height: " + height);
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to set browser window size. Error: " + e.getMessage());
            takeScreenShot("setSize_fail_" + System.currentTimeMillis());
            System.out.println("Failed to resize browser window");
            e.printStackTrace();
        }
    }



     //   close browser
    public void closeBrowser() {
        try {
            if (driver != null) {
                driver.quit();  // Ends the entire session
                et.log(Status.INFO, "Browser session closed successfully.");
                System.out.println("Browser session closed successfully.");
            }
        } catch (WebDriverException e) {
            et.log(Status.FAIL, "WebDriverException while quitting driver: " + e.getMessage());
            takeScreenShot("closeBrowser_WebDriverException_" + System.currentTimeMillis());
            System.out.println("WebDriverException while quitting driver: " + e.getMessage());
        } catch (Exception e) {
            et.log(Status.FAIL, "Unexpected exception while quitting driver: " + e.getMessage());
            takeScreenShot("closeBrowser_UnexpectedException_" + System.currentTimeMillis());
            System.out.println("Unexpected exception while quitting driver: " + e.getMessage());
        }
    }

		
		
		
		


		
	     //   quit browser
    public void quitBrowser() {
        try {
            if (driver != null) {
                driver.quit();
                et.log(Status.INFO, "Browser session quit successfully.");
                System.out.println("Browser session quit successfully.");
            }
        } catch (WebDriverException e) {
            et.log(Status.FAIL, "WebDriverException while quitting browser: " + e.getMessage());
            takeScreenShot("quitBrowser_WebDriverException_" + System.currentTimeMillis());
            System.out.println("WebDriverException while quitting browser: " + e.getMessage());
        } catch (Exception e) {
            et.log(Status.FAIL, "Unexpected exception while quitting browser: " + e.getMessage());
            takeScreenShot("quitBrowser_UnexpectedException_" + System.currentTimeMillis());
            System.out.println("Unexpected exception while quitting browser: " + e.getMessage());
        }
    }


// static wait
    public void staticWait(int timeInSecond) {
        try {
            Thread.sleep(timeInSecond * 1000);
            et.log(Status.INFO, "Static wait applied for " + timeInSecond + " seconds.");
            System.out.println("Static wait applied for " + timeInSecond + " seconds.");
        } catch (InterruptedException e) {
            et.log(Status.FAIL, "Static wait interrupted. Error: " + e.getMessage());
            takeScreenShot("staticWait_Interrupted_" + System.currentTimeMillis());
            System.out.println("Static wait interrupted. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

  // implicit wait
    public void implicityWait() {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
            et.log(Status.INFO, "Implicit wait of 60 seconds applied successfully.");
            System.out.println("Implicit wait of 60 seconds applied successfully.");
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to apply implicit wait. Error: " + e.getMessage());
            takeScreenShot("implicitWait_Fail_" + System.currentTimeMillis());
            System.out.println("Failed to apply implicit wait. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

      // Explicit Wait
    public void waitForVisibility(WebElement we, int timeouts) throws TimeoutException {
        try {
            WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeouts));
            wt.until(ExpectedConditions.visibilityOf(we));
            et.log(Status.INFO, "Element became visible within " + timeouts + " seconds.");
            System.out.println("Element became visible.");
        } catch (Exception e) {
            et.log(Status.FAIL, "Error during waitForVisibility: " + e.getMessage());
            takeScreenShot("waitForVisibility_Error_" + System.currentTimeMillis());
            System.out.println("Unexpected error in waitForVisibility.");
            e.printStackTrace();
        }
    }

   // Explicit Wait
    public void waitForEnabling(WebElement we, int timeouts) throws TimeoutException {
        try {
            WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeouts));
            wt.until(ExpectedConditions.elementToBeClickable(we));
            et.log(Status.INFO, "Element is enabled and clickable within " + timeouts + " seconds.");
            System.out.println("Element is enabled and clickable.");
        } catch (Exception e) {
            et.log(Status.FAIL, "Error during waitForEnabling: " + e.getMessage());
            takeScreenShot("waitForEnabling_Error_" + System.currentTimeMillis());
            System.out.println("Unexpected error in waitForEnabling.");
            e.printStackTrace();
        }
    }

      
   // Explicit Wait
    public void waitForText(WebElement we, int timeouts) throws TimeoutException {
        try {
            WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeouts));
            wt.until(ExpectedConditions.textToBePresentInElement(we, "ReLead"));
            et.log(Status.INFO, "Text 'ReLead' is present in the element within " + timeouts + " seconds.");
            System.out.println("Text 'ReLead' found successfully.");
        } catch (Exception e) {
            et.log(Status.FAIL, "Error occurred in waitForText: " + e.getMessage());
            takeScreenShot("waitForText_Error_" + System.currentTimeMillis());
            System.out.println("Unexpected error in waitForText.");
            e.printStackTrace();
        }
    }

      // Explicit Wait
    public void waitForInvisibility(WebElement we, int timeouts) throws TimeoutException {
        try {
            WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(timeouts));
            wt.until(ExpectedConditions.invisibilityOf(we));
            et.log(Status.INFO, "Element became invisible within " + timeouts + " seconds.");
            System.out.println("Element is now invisible.");
        } catch (Exception e) {
            et.log(Status.FAIL, "Error occurred while waiting for invisibility: " + e.getMessage());
            takeScreenShot("waitForInvisibility_Error_" + System.currentTimeMillis());
            System.out.println("Unexpected error while waiting for invisibility.");
            e.printStackTrace();
        }
    }

     
     // page timeouts
    public void changePageLoadTimeout(int timeouts) {
        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeouts));
            et.log(Status.INFO, "Page load timeout set to " + timeouts + " seconds successfully.");
            System.out.println("Page load timeout set to " + timeouts + " seconds.");
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to set page load timeout. Error: " + e.getMessage());
            takeScreenShot("PageLoadTimeout_Fail_" + System.currentTimeMillis());
            System.out.println("Error occurred while setting page load timeout.");
            e.printStackTrace();
        }
    }public boolean getElementDisplayStatus(WebElement we, String elementName) {
        boolean status = false;
        try {
            org.openqa.selenium.Dimension dim = we.getSize();
            if (dim.getHeight() > 0 && dim.getWidth() > 0) {
                status = true;
                et.log(Status.PASS, elementName + " is displayed with dimensions: " + dim);
                System.out.println(elementName + " is visible with dimensions: " + dim);
            } else {
                et.log(Status.FAIL, elementName + " is not displayed properly (zero size).");
                takeScreenShot(elementName + "_DisplayStatusFail_" + System.currentTimeMillis());
            }
        } catch (Exception e) {
            et.log(Status.FAIL, "Exception while checking display status of " + elementName + ": " + e.getMessage());
            takeScreenShot(elementName + "_DisplayStatusException_" + System.currentTimeMillis());
            System.out.println("Exception while checking display status for " + elementName);
            e.printStackTrace();
        }
        return status;
    }


      
      // element display
    public boolean getElementDisplayStatus1(WebElement we, String elementName) {
        boolean status = false;
        try {
            org.openqa.selenium.Dimension dim = we.getSize();
            if (dim.getHeight() > 0 && dim.getWidth() > 0) {
                status = true;
                et.log(Status.PASS, elementName + " is displayed with size: " + dim);
                System.out.println(elementName + " is visible with dimensions: " + dim);
            } else {
                et.log(Status.FAIL, elementName + " is not displayed (zero size).");
                takeScreenShot(elementName + "_DisplayFail_" + System.currentTimeMillis());
            }
        } catch (Exception e) {
            et.log(Status.FAIL, "Error checking display status of " + elementName + ": " + e.getMessage());
            takeScreenShot(elementName + "_Exception_" + System.currentTimeMillis());
            System.out.println("Exception while checking display status for " + elementName);
            e.printStackTrace();
        }
        return status;
    }

      
      
    

//===================================validation / validate inner text===============================
   
     
    public void validateInnerText(WebElement we, String expectedText, String elementName) {
        try {
            String actualText = we.getText();
            if (actualText.equalsIgnoreCase(expectedText)) {
                et.log(Status.PASS, "Validation passed for " + elementName + 
                       ". Actual: '" + actualText + "', Expected: '" + expectedText + "'");
                System.out.println("PASS: " + elementName + " - Actual: '" + actualText + "', Expected: '" + expectedText + "'");
            } else {
                et.log(Status.FAIL, "Validation failed for " + elementName + 
                       ". Actual: '" + actualText + "', Expected: '" + expectedText + "'");
                takeScreenShot(elementName + "_TextMismatch_" + System.currentTimeMillis());
                System.out.println("FAIL: " + elementName + " - Actual: '" + actualText + "', Expected: '" + expectedText + "'");
            }
        } catch (Exception e) {
            et.log(Status.FAIL, "Exception while validating text for " + elementName + ": " + e.getMessage());
            takeScreenShot(elementName + "_Exception_" + System.currentTimeMillis());
            System.out.println("Exception while validating inner text for " + elementName);
            e.printStackTrace();
        }
    }

//=================================Validate Atribute value=======================================
    
    public void validateAttribute(WebElement we, String expectedAttribute, String attributeName, String elementName) {
        try {
            String actualAttribute = we.getDomAttribute(attributeName);
            
            if (actualAttribute != null && actualAttribute.equalsIgnoreCase(expectedAttribute)) {
                et.log(Status.PASS, "Validation passed for attribute '" + attributeName + "' of " + elementName + 
                       ". Actual: '" + actualAttribute + "', Expected: '" + expectedAttribute + "'");
                System.out.println("PASS: " + elementName + " - Attribute '" + attributeName + "' Actual: '" + actualAttribute + "', Expected: '" + expectedAttribute + "'");
            } else {
                et.log(Status.FAIL, "Validation failed for attribute '" + attributeName + "' of " + elementName + 
                       ". Actual: '" + actualAttribute + "', Expected: '" + expectedAttribute + "'");
                takeScreenShot(elementName + "_AttrValidationFail_" + System.currentTimeMillis());
                System.out.println("FAIL: " + elementName + " - Attribute '" + attributeName + "' Actual: '" + actualAttribute + "', Expected: '" + expectedAttribute + "'");
            }
        } catch (Exception e) {
            et.log(Status.FAIL, "Exception while validating attribute '" + attributeName + "' for " + elementName + ": " + e.getMessage());
            takeScreenShot(elementName + "_AttrException_" + System.currentTimeMillis());
            System.out.println("Exception while validating attribute '" + attributeName + "' for " + elementName);
            e.printStackTrace();
        }
    }

  	  
        
         
         

    public void validateElementIsVisible(WebElement we, String elementName) {
        try {
            boolean actualStatus = we.isDisplayed();
            if (actualStatus) {
                et.log(Status.PASS, elementName + " is visible on the page.");
                System.out.println("PASS: " + elementName + " is visible (actual: " + actualStatus + ")");
            } else {
                et.log(Status.FAIL, elementName + " is NOT visible on the page.");
                takeScreenShot(elementName + "_VisibilityFail_" + System.currentTimeMillis());
                System.out.println("FAIL: " + elementName + " is NOT visible (actual: " + actualStatus + ")");
            }
        } catch (Exception e) {
            et.log(Status.FAIL, "Exception while verifying visibility of " + elementName + ": " + e.getMessage());
            takeScreenShot(elementName + "_VisibilityException_" + System.currentTimeMillis());
            System.out.println("Exception while verifying visibility of " + elementName);
            e.printStackTrace();
        }
    }

         public void  validateElementIsInVisible(WebElement we) {
             boolean actualStetus= we.isDisplayed();
             if(actualStetus==true) {
       		  System.out.println("passed. actual-"+actualStetus+" && expected-"+"false");
       		  
       	  }else {
       		  System.out.println("faild. actual-"+actualStetus+" && expected-"+"false");

       	  }
       	  
         }
//====================================Validate element is Enabled =======================================
         
     public void validateElementIsEnabled(WebElement we) {
    	boolean actualStetus= we.isEnabled();
    	if(actualStetus==true) {
    		System.out.println("passed. actual"+actualStetus+"&& expected- true");
    	}else {
    		System.out.println("faild. actual"+actualStetus+"&& expected- true");

    	}
    
     }
    public void validateElementIsDisabled(WebElement we) {
    	boolean actualStetus=we.isEnabled();
    	if(actualStetus==false) {
    		System.out.println("passed.- actual"+actualStetus+" && axoected- false");
    	}else {
    		System.out.println("pasaildsed.- actual"+actualStetus+" && axoected- false");

    	}
    }
    
    // =======================================Validate page Title=========================================
    
    public void validatePageTitle(String expectedTitle) {
        try {
            String actualTitle = driver.getTitle();
            if (actualTitle.equalsIgnoreCase(expectedTitle)) {
                et.log(Status.PASS, "Page title validation passed. Actual: '" + actualTitle + "' | Expected: '" + expectedTitle + "'");
                System.out.println("PASS: actual '" + actualTitle + "' && expected '" + expectedTitle + "'");
            } else {
                et.log(Status.FAIL, "Page title validation failed. Actual: '" + actualTitle + "' | Expected: '" + expectedTitle + "'");
                takeScreenShot("PageTitleFail_" + System.currentTimeMillis());
                System.out.println("FAIL: actual '" + actualTitle + "' && expected '" + expectedTitle + "'");
            }
        } catch (Exception e) {
            et.log(Status.FAIL, "Exception during page title validation: " + e.getMessage());
            takeScreenShot("PageTitleException_" + System.currentTimeMillis());
            System.out.println("Exception during page title validation");
            e.printStackTrace();
        }
    }
//==================================Validate DropDown SlectedText================================
    public void validateDropDownSelectedText(WebElement we, String expectedSelectedText) {
        try {
            Select select = new Select(we);
            String actualSelectedText = select.getFirstSelectedOption().getText();

            if (actualSelectedText.equalsIgnoreCase(expectedSelectedText)) {
                et.log(Status.PASS, "Dropdown selected text validation passed. Actual: '" + actualSelectedText + "' | Expected: '" + expectedSelectedText + "'");
                System.out.println("PASS: actualSelectedText '" + actualSelectedText + "' && expectedSelectedText '" + expectedSelectedText + "'");
            } else {
                et.log(Status.FAIL, "Dropdown selected text validation failed. Actual: '" + actualSelectedText + "' | Expected: '" + expectedSelectedText + "'");
                takeScreenShot("DropdownSelectedTextFail_" + System.currentTimeMillis());
                System.out.println("FAIL: actualSelectedText '" + actualSelectedText + "' && expectedSelectedText '" + expectedSelectedText + "'");
            }
        } catch (Exception e) {
            et.log(Status.FAIL, "Exception during dropdown selected text validation: " + e.getMessage());
            takeScreenShot("DropdownSelectedTextException_" + System.currentTimeMillis());
            System.out.println("Exception during dropdown selected text validation");
            e.printStackTrace();
        }
    }

  //  =====================Handle javScript Alert=================================
    		
    public void alertAccept() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            et.log(Status.INFO, "Alert text: " + alertText);
            System.out.println("Alert text: " + alertText);
            alert.accept();
            et.log(Status.PASS, "Alert accepted successfully");
        } catch (NoAlertPresentException e) {
            et.log(Status.FAIL, "No alert present to accept. Error: " + e.getMessage());
            takeScreenShot("AlertAcceptFail_" + System.currentTimeMillis());
            System.out.println("No alert present to accept");
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to accept alert. Error: " + e.getMessage());
            takeScreenShot("AlertAcceptFail_" + System.currentTimeMillis());
            System.out.println("Failed to accept alert");
            e.printStackTrace();
        }
    }
//==================================Alert Dismis==========================================
    
    public void alertDismiss() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            et.log(Status.INFO, "Alert text: " + alertText);
            System.out.println("Alert text: " + alertText);
            alert.dismiss();
            et.log(Status.PASS, "Alert dismissed successfully");
        } catch (NoAlertPresentException e) {
            et.log(Status.FAIL, "No alert present to dismiss. Error: " + e.getMessage());
            takeScreenShot("AlertDismissFail_" + System.currentTimeMillis());
            System.out.println("No alert present to dismiss");
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to dismiss alert. Error: " + e.getMessage());
            takeScreenShot("AlertDismissFail_" + System.currentTimeMillis());
            System.out.println("Failed to dismiss alert");
            e.printStackTrace();
        }
    }

    //=============================Alert SendKey===============================================
    
    public void alertSendKey(String sendText) {
        try {
            Alert alert = driver.switchTo().alert();
            alert.sendKeys(sendText);
            et.log(Status.PASS, "Text sent to alert successfully: " + sendText);
            System.out.println("Text sent to alert: " + sendText);
        } catch (NoAlertPresentException e) {
            et.log(Status.FAIL, "No alert present to send keys. Error: " + e.getMessage());
            takeScreenShot("AlertSendKeyFail_" + System.currentTimeMillis());
            System.out.println("No alert present to send keys");
        } catch (Exception e) {
            et.log(Status.FAIL, "Failed to send keys to alert. Error: " + e.getMessage());
            takeScreenShot("AlertSendKeyFail_" + System.currentTimeMillis());
            System.out.println("Failed to send keys to alert");
            e.printStackTrace();
        }
    }
//==============================Broken Link =============================================
    public void brokenLink() {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("Total links found: " + links.size());

        for (WebElement link : links) {
            String url = link.getDomAttribute("href");

            if (url == null || url.isEmpty()) {
                System.out.println("URL is either null or empty for link: " + link.getText());
                continue;
            }

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("HEAD");
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode >= 400) {
                    System.out.println(url + " is a broken link. Response code: " + responseCode);
                    et.log(Status.FAIL, url + " is a broken link. Response code: " + responseCode);
                    takeScreenShot("BrokenLink_" + System.currentTimeMillis());
                } else {
                    System.out.println(url + " is a valid link. Response code: " + responseCode);
                    et.log(Status.PASS, url + " is a valid link.");
                }

            } catch (Exception e) {
                System.out.println("Exception while checking URL: " + url);
                et.log(Status.FAIL, "Exception while checking URL: " + url + " Error: " + e.getMessage());
                takeScreenShot("BrokenLinkException_" + System.currentTimeMillis());
            }
        }
    }
    
    
    }









