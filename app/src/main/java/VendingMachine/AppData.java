package VendingMachine;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * data for executing this app
 */
public class AppData {
    private static Customer customer;
    // (product_code, quantity_wanted)
    private static HashMap<String, Integer> shoppingCart = new LinkedHashMap<>();

    public static Customer getCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer) {
        AppData.customer = customer;
    }

    public static HashMap<String, Integer> getShoppingCart() {
        return shoppingCart;
    }

    public static void addToCart(String productCode, String q) throws Exception {
        int quantity = Integer.parseInt(q);
        if(quantity <= 0) throw new IllegalArgumentException("quantity has to be greater than 0");
        if (ProductData.checkProductExists(productCode) != -1) {
            // also check the quantity in shopping cart
            int wanted_quantity = quantity + shoppingCart.getOrDefault(productCode, 0);
            if (ProductData.getQuantityRemain(productCode) >= wanted_quantity) {
                shoppingCart.put(productCode, wanted_quantity);
            } else {
                throw new IllegalArgumentException("Not enough in stock");
            }
        } else {
            throw new IllegalArgumentException("Invalid product code");
        }
    }

    /**
     * log out and reset shopping cart
     */
    public static void reset() {
        // default anonymous customer
        customer = UserData.getAnonymousCustomer();
        shoppingCart = new HashMap<>();
        TimeTracker.stop();
        Payment.ResetInsertZero();

        // clean all popped out windows
        // otherwise, user click some button, act abnormally

    }

    public static void completeSuccessfulTransaction(double moneyPaid, double returnedChange, String payment_method) {
        // update product stock
        ProductData.updateStocks(shoppingCart);

        // update customer bought history
        // directly write to file, no need transit through Customer's lastFiveProductID
        UserData.updateRecentFive(customer.getUsername(), shoppingCart);

        // add successful transaction to file
        TransactionData.addSuccessfulTransactionToFile(shoppingCart, moneyPaid, returnedChange, payment_method);

        // log out automatically
        reset();
    }

}
