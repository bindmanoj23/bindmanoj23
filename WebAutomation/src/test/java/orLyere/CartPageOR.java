package orLyere;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import genaricMethod.WebUtil;

public class CartPageOR {

    private WebUtil wu;

    public CartPageOR(WebUtil wu) {
        this.wu = wu;
        PageFactory.initElements(wu.getDriver(), this);
    }

    @FindBy(xpath = "//div[@class='cart_item']")
    private List<WebElement> cartItems;

    public List<WebElement> getCartItems() {
        return cartItems;
    }

    @FindBy(id = "checkout")
    private WebElement checkoutBtn;

    public WebElement getCheckoutBtn() {
        return checkoutBtn;
    }

    @FindBy(className = "inventory_item_price")
    private WebElement itemprice;
   
    public WebElement getPriceElement() {
        return itemprice;
    }
}
