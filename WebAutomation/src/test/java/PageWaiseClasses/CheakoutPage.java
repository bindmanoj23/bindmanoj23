package PageWaiseClasses;

import genaricMethod.WebUtil;
import orLyere.CheakoutPageOR;

public class CheakoutPage extends CheakoutPageOR {
	
	private WebUtil wu;
	
	public CheakoutPage(WebUtil wu) {
		super(wu);
		this.wu=wu;
		
		
	}
	
	public void fillupCheakotDetauls() {
		
		wu.type(getFirstName(), "Manoj", "FirstName");
		wu.type(getLastName(), "Bind", "LastName");
		wu.type(getPostalCode(), "221406", "PostalCode");
		wu.click(getContinueBT(), "continueBT");
	}

}
