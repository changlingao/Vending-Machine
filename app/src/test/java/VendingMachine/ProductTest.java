package VendingMachine;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void printProduct() {
        try {
            Product water = new Product("1", "Drinks", "Mineral Water", 7, 0, 2, 50);
            String actual = water.toString();
            String expected = "1,Mineral Water,Drinks,7,0,2,50";
            assertEquals(expected, actual, "Incorrect print format for Product");
        } catch (Exception e){
            assertEquals("Product id already existed.",e.getMessage());
        }
    }
}
