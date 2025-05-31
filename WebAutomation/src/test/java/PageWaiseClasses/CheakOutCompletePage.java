package PageWaiseClasses;

import genaricMethod.WebUtil;
import orLyere.CheakOutCompletePageOR;

public class CheakOutCompletePage  extends CheakOutCompletePageOR{
	
	private WebUtil wu;
	public CheakOutCompletePage(WebUtil wu) {
		super(wu);
		this.wu=wu;
		
		
	}

	public String completePage() {
		String msg=wu.getInnerText(getCompleteHeader(), "Coplete Header");
		wu.takeScreenShot("purchase_success.png");
		return msg;
		
	}
}
