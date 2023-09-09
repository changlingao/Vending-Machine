package VendingMachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    int dollars1;
    int cents1;
    int dollars2;
    int cents2;
    int dollars3;
    int cents3;
    int[] expectedTotal12;
    int[] expectedTotal13;
    int[] expectedTotal23;

    @BeforeEach
    public void setUp() {
        this.dollars1 = 2;
        this.cents1 = 45;
        this.dollars2 = 3;
        this.cents2 = 30;
        this.dollars3 = 3;
        this.cents3 = 95;
        this.expectedTotal12 = new int[]{5, 75};
        this.expectedTotal13 = new int[]{6, 40};
        this.expectedTotal23 = new int[]{7, 25};
    }

    @Test
    public void addTwoPricesWithoutCarry() {
        int[] actualTotal = Calculator.add(this.dollars1, this.cents1, this.dollars2, this.cents2);
        assertEquals(this.expectedTotal12[0], actualTotal[0], "Incorrect total dollars");
        assertEquals(this.expectedTotal12[1], actualTotal[1], "Incorrect total cents");
    }

    @Test
    public void addTwoPricesWithCarry1() {
        int[] actualTotal = Calculator.add(this.dollars1, this.cents1, this.dollars3, this.cents3);
        assertEquals(expectedTotal13[0], actualTotal[0], "Incorrect total dollars");
        assertEquals(expectedTotal13[1], actualTotal[1], "Incorrect total cents");
    }

    @Test
    public void addTwoPricesWithCarry2() {
        int[] actualTotal = Calculator.add(this.dollars2, this.cents2, this.dollars3, this.cents3);
        assertEquals(expectedTotal23[0], actualTotal[0], "Incorrect total dollars");
        assertEquals(expectedTotal23[1], actualTotal[1], "Incorrect total cents");
    }

    @Test
    public void mulPriceWithoutCarry() {
        int[] actualTotal = Calculator.mul(this.dollars2, this.cents2, 3);
        int[] expectedTotal = new int[]{9, 90};
        assertEquals(expectedTotal[0], actualTotal[0], "Incorrect total dollars");
        assertEquals(expectedTotal[1], actualTotal[1], "Incorrect total cents");
    }

    @Test
    public void mulPriceWithCarry1() {
        int[] actualTotal = Calculator.mul(this.dollars1, this.cents1, 10);
        int[] expectedTotal = new int[]{24, 50};
        assertEquals(expectedTotal[0], actualTotal[0], "Incorrect total dollars");
        assertEquals(expectedTotal[1], actualTotal[1], "Incorrect total cents");
    }

    @Test
    public void calculateTotal1() {
        DataBase.productFile = "src/main/resources/product_test.txt";
        Map<String, Integer> shoppingCart = new HashMap<String, Integer>();
        shoppingCart.put("1", 2);
        shoppingCart.put("2", 1);
        shoppingCart.put("3", 1);
        shoppingCart.put("10", 3);
        shoppingCart.put("16", 1);
        int[] actualTotal = Calculator.calculateTotal(shoppingCart);
        int[] expectedTotal = new int[]{20, 60};
        assertEquals(expectedTotal[0], actualTotal[0], "Incorrect total dollars");
        assertEquals(expectedTotal[1], actualTotal[1], "Incorrect total cents");
    }

    @Test
    public void roundUpPriceWithoutCarry1() {
        int[] actualTotal = Calculator.roundUpPrice(this.dollars1, this.cents1);
        assertEquals(this.dollars1, actualTotal[0], "Incorrect dollars after rounding up");
        assertEquals(this.cents1, actualTotal[1], "Incorrect cents after rounding up");

    }

    @Test
    public void roundUpPriceWithoutCarry2() {
        int[] actualTotal = Calculator.roundUpPrice(this.dollars2, this.cents2);
        assertEquals(this.dollars2, actualTotal[0], "Incorrect dollars after rounding up");
        assertEquals(this.cents2, actualTotal[1], "Incorrect cents after rounding up");
    }

    @Test
    public void roundUpPriceWithoutCarry3() {
        int[] actualTotal = Calculator.roundUpPrice(3, 12);
        assertEquals(3, actualTotal[0], "Incorrect dollars after rounding up");
        assertEquals(15, actualTotal[1], "Incorrect cents after rounding up");
    }

    @Test
    public void roundUpPriceWithCarry1() {
        int[] actualTotal = Calculator.roundUpPrice(3, 96);
        assertEquals(4, actualTotal[0], "Incorrect dollars after rounding up");
        assertEquals(0, actualTotal[1], "Incorrect cents after rounding up");
    }

}
