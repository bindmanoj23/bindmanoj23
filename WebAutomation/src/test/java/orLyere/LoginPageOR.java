package orLyere;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import genaricMethod.WebUtil;

public class LoginPageOR {
	
	private WebUtil wu;
	public LoginPageOR(WebUtil wu) {
		this.wu=wu;
		PageFactory.initElements(wu.getDriver(), this);
		
	}

	@FindBy(xpath="//input[@id='user-name']")
	private WebElement userName;
	
	public WebElement getUserName() {
		return userName;
	}
	
	@FindBy(xpath="//input[@id='password']")
	private WebElement password;
	
	public WebElement getPassword() {
		return password;
	}
	
	@FindBy(xpath="//input[@id='login-button']")
	private WebElement loginBT;
	
	public WebElement getLoginBT() {
		return loginBT;
	}








}
