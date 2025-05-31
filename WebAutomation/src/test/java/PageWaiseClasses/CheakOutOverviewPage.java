package PageWaiseClasses;

import genaricMethod.WebUtil;
import orLyere.CheakOutOverviewPageOR;

public class CheakOutOverviewPage  extends CheakOutOverviewPageOR{

	private WebUtil wu;
	
	public CheakOutOverviewPage(WebUtil wu) {
		super(wu);
		this.wu=wu;
		
	}

	public double getItemTotal() {
	  String text=wu.getInnerText(getitemTotalLable(), "PriceLable");
       return Double.parseDouble(text.replace("Item total: $", "").trim());
    }

	public void clickFinish() {
        wu.click(getFinishBT(), "FinishBt");
    }
}