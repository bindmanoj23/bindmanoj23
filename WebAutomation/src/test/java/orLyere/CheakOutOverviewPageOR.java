package orLyere;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import genaricMethod.WebUtil;

public class CheakOutOverviewPageOR {
	
	private WebUtil wu;
	public CheakOutOverviewPageOR( WebUtil wu) {
		this.wu=wu;
		PageFactory.initElements(wu.getDriver(), this);
		
	}

	@FindBy(className = "summary_subtotal_label")
	protected WebElement itemTotalLabel;
	
	public WebElement getitemTotalLable() {
		return itemTotalLabel;
	}

    @FindBy(id = "finish")
    private WebElement finishBtn;
    public WebElement getFinishBT() {
		return finishBtn;
	}

}
