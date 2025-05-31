package PageWaiseClasses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import genaricMethod.WebUtil;
import orLyere.ProductPageOR;

public class ProductPage extends ProductPageOR {

    private WebUtil wu;

    public ProductPage(WebUtil wu) {
        super(wu);
        this.wu = wu;
            }

    public void applyFilter() {
        wu.selectTextFromListBox(getFilterDropDown(), "Price (low to high)", "filterDropDown");
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public List<Double> productPrice() {
        List<Double> priceList = new ArrayList<>();
        for (WebElement product : getProducts()) {
            String priceText = getProductPriceElement(product).getText();
            priceList.add(Double.parseDouble(priceText.replace("$", "").trim()));
        }
        return priceList;
    }

    public void addProductToCartByPrice(double price) {
        for (WebElement product : getProducts()) {
            String priceText = getProductPriceElement(product).getText();
            double productPrice = Double.parseDouble(priceText.replace("$", "").trim());
            if (productPrice == price) {
                WebElement addBtn = getAddToCartButton(product);
                wu.click(addBtn, "Add to Cart button for price: " + price);
                break;
            }
        }
    }

    public void cartBt() {
        wu.click(getCartBT(), "Cart Button");
    }
}
