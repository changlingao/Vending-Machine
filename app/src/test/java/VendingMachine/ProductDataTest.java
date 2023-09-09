package VendingMachine;
import org.junit.After;
import org.junit.jupiter.api.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDataTest {

    @BeforeAll
    public static void UpdateProductFileAddress(){
        DataBase.productFile = "src/main/resources/product_test.txt";
    }

    @AfterAll
    public static void ReturnProductFileBack(){
        DataBase.productFile = "src/main/resources/products_inventory.txt";
    }

    @Test
    public void checkProductExistsTest(){
        int expected = -1;
        int actual = ProductData.checkProductExists("4");
        assertNotEquals(expected,actual);
    }

    @Test
    // Input id doesn't not exist
    public void checkProductExistsTest1(){
        int expected = -1;
        int actual = ProductData.checkProductExists("20");
        assertEquals(expected,actual);
    }

    @Test
    public void getPriceTest(){
        int dollar_actual = ProductData.getPrice("8")[0];
        int cent_actual = ProductData.getPrice("8")[1];
        assertEquals(3,dollar_actual);
        assertEquals(20,cent_actual);
    }

    @Test
    // Check if product id doesn't exist
    public void getPriceTest1(){
        int dollar_actual = ProductData.getPrice("99")[0];
        int cent_actual = ProductData.getPrice("99")[1];
        assertEquals(-1,dollar_actual);
        assertEquals(-1,cent_actual);
    }

    @Test
    // Test whether we can get correct quantity remain for one particular product.
    public void getQuantityRemainTest(){
        int quantity_actual = ProductData.getQuantityRemain("14");
        assertEquals(5,quantity_actual);
    }

    @Test
    // Test if product id doesn't exist.
    public void getQuantityRemainTest1(){
        int quantity_actual = ProductData.getQuantityRemain("56");
        assertEquals(-1,quantity_actual);
    }

    @Test
    public void getQuantitySoldTest(){
        int actual = ProductData.getQuantitySOLD("14");
        assertEquals(2,actual);
    }

    @Test
    // Test id not exist
    public void getQuantitySoldTest1(){
        int actual = ProductData.getQuantitySOLD("57");
        assertEquals(-1,actual);
    }

    @Test
    public void getCategoryTest(){
        String actual = ProductData.getCategory("12");
        assertEquals("Chips",actual);
    }

    @Test
    public void getCategoryTest1(){
        String actual = ProductData.getCategory("56");
        assertEquals(null,actual);
    }

    @Test
    public void getNameTest(){
        String actual = ProductData.getName("11");
        assertEquals("Pringles",actual);
    }

    @Test
    // doesn't exist
    public void getNameTest1(){
        String actual = ProductData.getName("55");
        assertEquals(null,actual);
    }

    @Test
    // Read and get products by string
    // To test read file, we can combine read and get together
    // so that we can know whether what we got is correct
    public void getProductsByCategoryTest(){
        ProductData.readInventory();
        List<String> actual = ProductData.getProductsByCategory("Chocolates");
        List<String> expected = new ArrayList<>();
        expected.add("6. Mars\nPrice: 3.0\nAvailable: 5");
        expected.add("7. M&M\nPrice: 3.0\nAvailable: 7");
        expected.add("8. Bounty\nPrice: 3.20\nAvailable: 5");
        expected.add("9. Snickers\nPrice: 2.50\nAvailable: 5");
        assertEquals(actual,expected);
    }

    @Test
    public void getAllCategories(){
        ProductData.readInventory();
        List<String> actual = ProductData.getAllCategories();
        List<String> expected = new ArrayList<>();
        expected.add("Drinks");
        expected.add("Chocolates");
        expected.add("Chips");
        expected.add("Candies");
        assertEquals(actual,expected);
    }

    @Test
    // This is one big integration test, including construct products,
    // write product list in file
    // read file
    // catch error
    public void writeInventoryTest(){
        try {
            List<Product> original = ProductData.readInventory();
            Product East = new Product("101", "Drinks", "Eastern Leaf", 11, 5, 3, 20);
            Product Vodka = new Product("102", "Drinks", "Vodka", 30, 15, 9, 90);
            Product BlackTea = new Product("103", "Drinks", "Black Tes", 21, 10, 3, 50);
            Product snicker = new Product("105", "Chocolates", "Snicker", 16, 10, 2, 20);
            List<Product> products = new ArrayList<>();
            products.add(East);
            products.add(Vodka);
            products.add(BlackTea);
            products.add(snicker);
            ProductData.writeInventory(products);
            List<Product> actual = ProductData.readInventory();
            assertEquals(products, actual);
        }catch(Exception e){
            assertEquals("Product quantity remain must be \nbetween 0 and 15.",e.getMessage());
            // Here we catch this error since quantity we remain is larger than 15
        }
    }

    @Test
    // Normal test
    public void writeInventoryTest1(){
        try {
            List<Product> original = ProductData.readInventory();
            Product East = new Product("101", "Drinks", "Eastern Leaf", 11, 5, 3, 20);
            Product Vodka = new Product("102", "Drinks", "Vodka", 9, 15, 9, 90);
            Product BlackTea = new Product("103", "Drinks", "Black Tes", 4, 10, 3, 50);
            Product snicker = new Product("105", "Chocolates", "Snicker", 6, 10, 2, 20);
            List<Product> products = new ArrayList<>();
            products.add(East);
            products.add(Vodka);
            products.add(BlackTea);
            products.add(snicker);
            ProductData.writeInventory(products);
            List<Product> actual = ProductData.readInventory();
            for(int i = 0; i < products.size(); i++){
                assertEquals(products.get(i).getId(),actual.get(i).getId());
                assertEquals(products.get(i).getCategory(),actual.get(i).getCategory());
                assertEquals(products.get(i).getName(),actual.get(i).getName());
                assertEquals(products.get(i).getQuantityRemain(),actual.get(i).getQuantityRemain());
                assertEquals(products.get(i).getQuantitySold(),actual.get(i).getQuantitySold());
                assertEquals(products.get(i).getDollars(),actual.get(i).getDollars());
                assertEquals(products.get(i).getCents(),actual.get(i).getCents());
            }
            ProductData.writeInventory(original);
        }catch(Exception e){
            System.out.println(e.getMessage());
            // Here we catch this error since quantity we remain is larger than 15
        }
    }


}
