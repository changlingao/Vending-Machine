package VendingMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>This role is able to fill/modify the item details.</p>
 * <p>To fill and modify the items, this role is able to select and modify
 * item details such as item name, item code, item category, item quantity
 * and item price.</p>
 * <p>Appropriate error message must be shown if the quantity added will be
 * over than the limit (15 of each item/product) or there is conflicting
 * item code/name/category.</p>
 * <p>This role will also able to obtain two reports (either csv or text file) upon logged in:</p>
 * <ul>
 *     <li>A list of the current available items that include the item details.</li>
 *     <li>A summary that includes items codes, item names and the total number of quantity sold for each item (e.g. "1001; Mineral Water; 12", "1002; Mars; 1", etc.).</li>
 * </ul>
 */
public class Seller extends Customer{

    /**
     * Constructor for Seller
     * @param username username
     * @param password password
     * @param cardholderName cardholder name
     * @param creditCardNumber credit card number
     * @param lastFiveProductID the id of the last five products the user bought
     */
    public Seller(String username, String password, String cardholderName, String creditCardNumber, String[] lastFiveProductID) {
        super(username, password, cardholderName, creditCardNumber, lastFiveProductID);
        this.role = "seller";    // assign correct role
    }

    public Seller(String username, String password){
        super(username, password);
        this.role = "seller";
    }

    /**
     *
     * @return A list of <code>Product</code> which <code>quantityRemain</code> is larger than 0
     */
    public static List<Product> getAvailableProductList() {
        List<Product> availableProductList = new ArrayList<>();
        // Iterate over the list and append the available product to a new list
        for (Product product : ProductData.getProducts()) {
            // add available product into the list
            if (product.getQuantityRemain() > 0) {
                availableProductList.add(product);
            }
        }
        // Return the new list
        return availableProductList;
    }

    /**
     *
     * @return A list of <code>HashMap</code> whose keys are product id, name, and quantity sold.
     */
    public List<HashMap<String, String>> getSummary() {
        List<HashMap<String, String>> saleReportList = new ArrayList<>();
        // Iterate over the list and add id, name, quantitySold to a Map and append it to the list
        for (Product product : ProductData.getProducts()) {
            HashMap<String, String> saleReport = new HashMap<>();
            saleReport.put("id", product.getId());
            saleReport.put("name", product.getName());
            saleReport.put("quantitySold", Integer.toString(product.getQuantitySold()));
            saleReportList.add(saleReport);
        }
        // Return the sale report (list of hashmap)
        return saleReportList;
    }

    /**
     * List of String instead of Product for UI display
     * no need to test maybe
     * @return a list of String
     */
    public static List<String> getAvailableProducts() {
        List<String> products = new ArrayList<>();
        for (Product p: getAvailableProductList()) {
            String cell = p.getId() + ". " + p.getName() + ";  " +
                    "Price: " + p.getDollars()+"."+p.getCents() + ";  " +
                    "Available: " + p.getQuantityRemain();
            products.add(cell);
        }
        return products;
    }

    /**
     * compared with List<HashMap<String, String>> getSummary
     * maybe be good for testing ??
     * But List<String> can actually be used to display UI
     * @return a list of String
     */
    public static List<String> productSummary() {
        List<String> summary = new ArrayList<>();
        for (Product p : ProductData.getProducts()) {
            String cell = p.getId() + ". " + p.getName() + ";  " +
                    "Sold: " + p.getQuantitySold();
            summary.add(cell);
        }
        return summary;
    }

    public static boolean checkMoney(int dollar, int cents){
        return dollar >= 0 && dollar <= 100 && cents >= 0 && cents <= 100;
    }

    /**
     * Modify product details. If an argument is an empty string, it means no changes is required.
     * @param previousId product id of the product to be modified
     * @param newId new product id
     * @param newName new name
     * @param newCategory new category
     * @param newQuantityRemain new quantity remain
     * @param newDollars new dollars of its price
     * @param newCents new cents of its price
     */
    public static void modifyProduct(String previousId, String newId, String newName, String newCategory, String newQuantityRemain, String newDollars, String newCents) throws IllegalArgumentException {
        // cannot change to existing name
        if (ProductData.nameExists(newName)) {
            throw new IllegalArgumentException("product name already exists");
        }

        // previousId and newId cannot both be empty
        if (previousId.equals("") && newId.equals("")) {
            throw new IllegalArgumentException("Previous and new product id \ncannot both be empty.");

        // previousId is filled: update product details
        } else if (!previousId.equals("")) {
            if (ProductData.checkProductExists(previousId) == -1) {
                throw new IllegalArgumentException("Previous product id not found.");
            }
            // get product list
            List<Product> productList = ProductData.getProducts();
            // find the product to modify from the product list
            for (int i = 0; i < productList.size(); i++) {
                // assume previousId is always valid
                Product p = productList.get(i);
                if (previousId.equals(p.getId())) {    // found the product to be modified in the product list
                    // update modifiedProduct first to see if any exception was raised
                    p.setId(newId);
                    p.setCategory(newCategory);
                    p.setName(newName);
                    // update quantity remain if it is not empty
                    if (!newQuantityRemain.equals("")) {
                        p.setQuantityRemain(Integer.parseInt(newQuantityRemain));
                    }
                    // update price if it is not empty
                    if (!newDollars.equals("") || !newCents.equals("")) {
                        try{
                            int dollar = Integer.parseInt(newDollars);
                            int cents = Integer.parseInt(newCents);
                            if (checkMoney(dollar, cents)){
                                p.setDollars(Integer.parseInt(newDollars));
                                p.setCents(Integer.parseInt(newCents));
                            }else{throw new IllegalArgumentException("money and cents have to be\n integer in 0 to 100");}
                        }catch (Exception e){throw new IllegalArgumentException("check money format");}
                    }
                    // update products in ProductData and products_inventory.txt file
                    productList.set(i, p);
                    ProductData.setProducts(productList);
                }
            }
        // previousId is empty but newId is filled: add new product
        } else {
            // get product list
            List<Product> productList = ProductData.getProducts();
            // error handling
            if (newCategory.equals("")) {
                throw new IllegalArgumentException("Please fill in product category when adding a new product.");
            } else if (newName.equals("")) {
                throw new IllegalArgumentException("Please fill in product name when adding a new product.");
            } else if (newQuantityRemain.equals("")) {
                newQuantityRemain = String.valueOf(Product.defaultQuantityRemain);
            } else if (newDollars.equals("")) {
                throw new IllegalArgumentException("Please fill in dollars when adding a new product.");
            } else if (newCents.equals("")) {
                throw new IllegalArgumentException("Please fill in cents when adding a new product.");
            }
            // create new product
            Product newProduct = new Product(newId, newCategory, newName,
                    Integer.parseInt(newQuantityRemain), 0,
                    Integer.parseInt(newDollars), Integer.parseInt(newCents));
            productList.add(newProduct);
            // update products in ProductData and products_inventory.txt file
            ProductData.setProducts(productList);
        }
    }
}
