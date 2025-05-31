package PageWaiseClasses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import genaricMethod.WebUtil;
import orLyere.CartPageOR;

public class CartPage extends CartPageOR {

    private WebUtil wu;

    public CartPage(WebUtil wu) {
        super(wu);
        this.wu = wu;
    }

    public int getNumberOfItems() {
        return getCartItems().size();
    }

    public List<Double> getItemPrices() {
        List<Double> prices = new ArrayList<>();
        for (WebElement item : getCartItems()) {
            String priceText = getPriceElement().getText();
            prices.add(Double.parseDouble(priceText.replace("$", "").trim()));
        }
        return prices;
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (Double price : getItemPrices()) {
            total += price;
        }
        return total;
    }

    public void clickCheckout() {
        wu.click(getCheckoutBtn(), "Checkout Button");
    }
}
