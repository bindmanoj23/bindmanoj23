package orLyere;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import genaricMethod.WebUtil;

public class ProductPageOR {

    private WebUtil wu;

    public ProductPageOR(WebUtil wu) {
        this.wu = wu;
        PageFactory.initElements(wu.getDriver(), this);
    }

    @FindBy(xpath = "//select[@class='product_sort_container']")
    private WebElement filterDropDown;

    public WebElement getFilterDropDown() {
        return filterDropDown;
    }

    @FindBy(xpath = "//div[@class='inventory_item']")
    private List<WebElement> products;

    public List<WebElement> getProducts() {
        return products;
    }

    @FindBy(xpath = "//a[@class='shopping_cart_link']")
    private WebElement cartBT;

    public WebElement getCartBT() {
        return cartBT;
    }

    // Locator for price inside a product
    @FindBy(className = "inventory_item_price")
    private WebElement productPrice;

    public WebElement getProductPriceElement(WebElement product) {
        return product.findElement(By.className("inventory_item_price"));
    }

    // Locator for add-to-cart button inside a product
    @FindBy(tagName = "button")
    private WebElement addToCartButton;

    public WebElement getAddToCartButton(WebElement product) {
        return product.findElement(By.tagName("button"));
    }
}
