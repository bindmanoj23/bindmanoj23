package PageWaiseClasses;

import genaricMethod.WebUtil;
import orLyere.LoginPageOR;

public class LoginPage extends LoginPageOR{
	
	private WebUtil wu;
	public LoginPage(WebUtil wu) {
		super(wu);
		this.wu=wu;
		
	}

	public void validLogin() {
		wu.type(getUserName(), "standard_user", "userName");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wu.type(getPassword(), "secret_sauce", "password");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wu.jsClick(getLoginBT(), "LoginBT");
	}

	public void InvalidLogin() {
		wu.type(getUserName(), "standard", "userName");
		
		
		wu.type(getPassword(), "secret", "password");
		wu.click(getLoginBT(), "LoginButton");
		wu.takeScreenShot("loginFaild.png");
	}


}
