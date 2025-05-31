package orLyere;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import genaricMethod.WebUtil;

public class CheakoutPageOR {
	
	private WebUtil wu;
	public CheakoutPageOR(WebUtil wu) {
		
		this.wu=wu;
		PageFactory.initElements(wu.getDriver(), this);
		
	}
	 @FindBy(id = "first-name")
	    private WebElement firstNameInput;
	 
	 public WebElement getFirstName() {
		 return firstNameInput;
	 }

	    @FindBy(id = "last-name")
	    private WebElement lastNameInput;
	    public WebElement getLastName() {
			 return lastNameInput;
		 }

	    @FindBy(id = "postal-code")
	    private WebElement postalCodeInput;
	    public WebElement getPostalCode() {
			 return postalCodeInput;
		 }

	    @FindBy(id = "continue")
	    private WebElement continueBtn;
	    
	    public WebElement getContinueBT() {
			 return continueBtn;
		 }


}
