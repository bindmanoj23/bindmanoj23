package orLyere;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import genaricMethod.WebUtil;

public class CheakOutCompletePageOR {
	private WebUtil wu;
	
	public CheakOutCompletePageOR( WebUtil wu) {
		
		this.wu=wu;
		PageFactory.initElements(wu.getDriver(), this);
				
	}

	@FindBy(className = "complete-header")
    private WebElement completeHeader;
	
	public WebElement getCompleteHeader() {
		return completeHeader;
	}
}
