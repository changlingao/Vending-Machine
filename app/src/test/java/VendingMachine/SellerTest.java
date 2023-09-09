package VendingMachine;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class SellerTest {

    final String dummyProductFile = "src/main/resources/dummy_products_inventory.txt";
    ArrayList<String> dummyProducts = new ArrayList<>();
    List<Product> OriginalProductList;

    /**
     * Initialise product list
     */
    @BeforeEach
    public void setUp() {
        // use dummy file
        ProductData.productFile = dummyProductFile;
        loadDummyProducts();
        ProductData.Write(dummyProducts, dummyProductFile);
        OriginalProductList = ProductData.readInventory();
    }

    public void loadDummyProducts() {
        dummyProducts.add("1,Mineral Water,Drinks,5,1,2,50");
        dummyProducts.add("6,Mars,Chocolates,7,0,3,0");
        dummyProducts.add("10,Smiths,Chips,7,0,2,50");
        dummyProducts.add("14,Mentos,Candies,7,0,2,0");
    }

    @Test
    public void constructValidSeller() {
        Seller s = new Seller("xqd", "SerERet43t", "", "", new String[]{"", "", "", "", ""});
        assertEquals(s.getUsername(), "xqd");
        assertEquals(s.getRole(), "seller");
    }

    @Test
    public void checkAvailableProductList() {
        List<Product> availableProductList = Seller.getAvailableProductList();

        for (Product availableProduct: availableProductList) {
            // check the quantity remain is larger than 0 for every product in the available product list
            assertTrue(availableProduct.getQuantityRemain() > 0, "Product" + availableProduct.getName() + "was sold out, but it is in the available product list");
            // remove the available product from the product list
            OriginalProductList.remove(availableProduct);
        }

        for (Product soldOutProduct: OriginalProductList) {
            // check the products remain in the product list are all sold out
            assertEquals(0, soldOutProduct.getQuantityRemain(), "Product" + soldOutProduct.getName() + "was available, but it is NOT in the available product list");
        }
    }

    @Test
    public void checkGetSummary() {
        Seller theReeves = new Seller("ReevesCompany", "iAmAsElLeR");
        List<HashMap<String, String>> summaryList = theReeves.getSummary();

        assertEquals(OriginalProductList.size(), summaryList.size(),
                "Incorrect number of products in the products summary");

        for (int i = 0; i < OriginalProductList.size(); i++) {
            Product p = OriginalProductList.get(i);
            String expectedId = p.getId();
            String expectedName = p.getName();
            int expectedQuantitySold = p.getQuantitySold();

            HashMap<String, String> summary = summaryList.get(i);
            String actualId = summary.get("id");
            String actualName = summary.get("name");
            int actualQuantitySold = Integer.parseInt(summary.get("quantitySold"));

            // assert each value in the summary
            assertEquals(expectedId, actualId, "Incorrect product id");
            assertEquals(expectedName, actualName, "Incorrect product name");
            assertEquals(expectedQuantitySold, actualQuantitySold, "Incorrect product quantity sold");
        }
    }

    @Test
    public void checkProductSummary() {
        List<String> summaryList = Seller.productSummary();
        assertEquals(OriginalProductList.size(), summaryList.size(),
                "Incorrect number of products in the products summary");
        for (int i = 0; i < OriginalProductList.size(); i++) {
            Product p = OriginalProductList.get(i);
            String expected = p.getId() + ". " + p.getName() + ";  " +
                    "Sold: " + p.getQuantitySold();
            String actual = summaryList.get(i);
            assertEquals(expected, actual);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"})
    public void ValidUpdateQuantityRemainForAProduct(String newQuantityRemain) {
        String previousId = "6";
        Seller.modifyProduct(previousId, "", "", "", newQuantityRemain, "", "");
        List<Product> prodInFile = ProductData.readInventory();
        List<Product> prodInAttr = ProductData.getProducts();
        for (Product p: prodInFile) {
            if (p.getId().equals(previousId)) {
                assertEquals(Integer.parseInt(newQuantityRemain), p.getQuantityRemain(), "Quantity remain was not modified correctly in file.");
            }
        }
        for (Product p: prodInAttr) {
            if (p.getId().equals(previousId)) {
                assertEquals(Integer.parseInt(newQuantityRemain), p.getQuantityRemain(), "Quantity remain was not modified correctly in attribute.");
            }
        }
    }

    @Test
    public void ValidUpdateMultipleDetailsForAProduct() {
        String previousId = "1";
        String newId = "2";
        String newName = "Sparkling Water";
        String newCategory = "Chocolates";
        String newQuantityRemain = "10";
        String newDollars = "3";
        String newCents = "20";

        Seller.modifyProduct(previousId, newId, newName, newCategory, newQuantityRemain, newDollars, newCents);

        List<Product> prodInFile = ProductData.readInventory();
        List<Product> prodInAttr = ProductData.getProducts();

        for (Product p: prodInFile) {
            if (p.getId().equals(previousId)) {
                assertEquals(newId, p.getId(), "Product id was not modified correctly in file.");
                assertEquals(newName, p.getName(), "Product name was not modified correctly in file.");
                assertEquals(newCategory, p.getCategory(), "Product category was not modified correctly in file.");
                assertEquals(Integer.parseInt(newQuantityRemain), p.getQuantityRemain(), "Quantity remain was not modified correctly in file.");
                assertEquals(Integer.parseInt(newDollars), p.getDollars(), "Dollars in product price was not modified correctly in file.");
                assertEquals(Integer.parseInt(newCents), p.getCents(), "Cents in product price was not modified correctly in file.");
            }
        }

        for (Product p: prodInAttr) {
            if (p.getId().equals(previousId)) {
                assertEquals(newId, p.getId(), "Product id was not modified correctly in file.");
                assertEquals(newName, p.getName(), "Product name was not modified correctly in file.");
                assertEquals(newCategory, p.getCategory(), "Product category was not modified correctly in file.");
                assertEquals(Integer.parseInt(newQuantityRemain), p.getQuantityRemain(), "Quantity remain was not modified correctly in file.");
                assertEquals(Integer.parseInt(newDollars), p.getDollars(), "Dollars in product price was not modified correctly in file.");
                assertEquals(Integer.parseInt(newCents), p.getCents(), "Cents in product price was not modified correctly in file.");
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "16"})
    public void InValidUpdateQuantityRemainForAProduct() {
        String previousId = "6";
        String newQuantityRemain = "16";
        try {
            Seller.modifyProduct(previousId, "", "", "", newQuantityRemain, "", "");
        } catch (IllegalArgumentException  e) {
            assertTrue(e.getMessage().contains("Product quantity remain"), "Wrong error message was thrown");
            return;
        } catch (Exception e) {
            fail("Wrong type of exception was thrown");
        }
        fail("Did not throw IllegalArgumentException");
    }

    @ParameterizedTest
    @MethodSource("newValidProducts")
    public void ValidAddANewProduct(String previousId, String newId, String newName, String newCategory, String newQuantityRemain, String newDollars, String newCents) {
        Seller.modifyProduct(previousId, newId, newName, newCategory, newQuantityRemain, newDollars, newCents);

        // Use default value if not given
        if (newQuantityRemain.equals("")) {
            newQuantityRemain = String.valueOf(Product.defaultQuantityRemain);
        }
        List<Product> prodInFile = ProductData.readInventory();
        List<Product> prodInAttr = ProductData.getProducts();
        for (Product p: prodInFile) {
            if (p.getId().equals(newId)) {
                assertEquals(newName, p.getName(), "Name of the new product was not added correctly in file.");
                assertEquals(newCategory, p.getCategory(), "Category of the new product was not added correctly in file.");
                assertEquals(Integer.parseInt(newQuantityRemain), p.getQuantityRemain(), "Quantity remain of the new product was not added correctly in file.");
                assertEquals(Integer.parseInt(newDollars), p.getDollars(), "Dollars of the new product was not added correctly in file.");
                assertEquals(Integer.parseInt(newCents), p.getCents(), "Cents of the new product was not added correctly in file.");
            }
        }
        for (Product p: prodInAttr) {
            if (p.getId().equals(newId)) {
                assertEquals(newName, p.getName(), "Name of the new product was not added correctly in attribute.");
                assertEquals(newCategory, p.getCategory(), "Category of the new product was not added correctly in attribute.");
                assertEquals(Integer.parseInt(newQuantityRemain), p.getQuantityRemain(), "Quantity remain of the new product was not added correctly in attribute.");
                assertEquals(Integer.parseInt(newDollars), p.getDollars(), "Dollars of the new product was not added correctly in attribute.");
                assertEquals(Integer.parseInt(newCents), p.getCents(), "Cents of the new product was not added correctly in attribute.");
            }
        }
    }

    private static Stream<Arguments> newValidProducts() {
        return Stream.of(
                Arguments.of("", "2", "Bundaberg ginger beer", "Drinks", "", "3", "0"),
                Arguments.of("", "3", "Schweppes bitter lemon", "Drinks", "12", "2", "99"),
                Arguments.of("", "7", "Raisin Chocolate", "Chocolates", "5", "2", "55")
        );
    }

    @ParameterizedTest
    @MethodSource("newInvalidProducts")
    public void InvalidAddANewProduct(String previousId, String newId, String newName, String newCategory, String newQuantityRemain, String newDollars, String newCents) {
        try {
            Seller.modifyProduct(previousId, newId, newName, newCategory, newQuantityRemain, newDollars, newCents);
        } catch (IllegalArgumentException e) {
            return;
        } catch (Exception e) {
            fail("Wrong type of exception was thrown");
        }
        fail("Did not throw IllegalArgumentException");
    }

    private static Stream<Arguments> newInvalidProducts() {
        return Stream.of(
                Arguments.of("", "", "Bundaberg ginger beer", "Drinks", "", "3", "0"),      // no id
                Arguments.of("", "3", "Schweppes bitter lemon", "Sodas", "12", "2", "99"),  // invalid category
                Arguments.of("", "7", "Raisin Chocolate", "Chocolates", "20", "2", "55"),    // more than max quantity
                Arguments.of("", "7", "Raisin Chocolate", "Chocolates", "5", "", "55"),    // no dollars
                Arguments.of("", "7", "Raisin Chocolate", "Chocolates", "5", "2", ""),    // no cents
                Arguments.of("", "7", "Raisin Chocolate", "Chocolates", "5", "2", "100"),    // invalid cents
                Arguments.of("", "7", "Raisin Chocolate", "Chocolates", "5", "2", "-1"),    // invalid cents
                Arguments.of("", "7", "Raisin Chocolate", "Chocolates", "5", "-1", "0")    // invalid dollars
        );
    }
}
