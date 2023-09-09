package VendingMachine;

import java.util.LinkedHashMap;
import java.util.Map;

public class Calculator {
    /**
     * Calculate the sum of the two prices
     * @param dollars1  dollars of the first price
     * @param cents1    cents of the first price
     * @param dollars2  dollars of the second price
     * @param cents2    cents of the second price
     * @return integer array of length 2: first element is the total dollars and second element is the total cents
     */
    public static int[] add(int dollars1, int cents1, int dollars2, int cents2) {
        int cents = cents1 + cents2;
        int dollars = 0;
        if (cents >= 100) {
            dollars += cents / 100;
        }
        cents = cents % 100;
        dollars += (dollars1 + dollars2);
        return new int[]{dollars, cents};
    }

    /**
     * Calculate the multiplication of the price and an integer
     * @param dollars1  dollars of the price
     * @param cents1    cents of the price
     * @param quantity  total quantities
     * @return integer array of length 2: first element is the total dollars and second element is the total cents
     */
    public static int[] mul(int dollars1, int cents1, int quantity) {
        int cents = cents1 * quantity;
        int dollars = dollars1 * quantity;
        if (cents >= 100) {
            dollars += cents / 100;
        }
        cents = cents % 100;
        return new int[]{dollars, cents};
    }

    /**
     * Calculate total price from the shopping cart
     * @param shoppingCart (product_code, quantity_wanted)
     * @return integer array of length 2: first element is the total dollars and second element is the total cents
     */
    public static int[] calculateTotal(Map<String, Integer> shoppingCart) {
        int[] total = new int[] {0, 0};
        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            String id = entry.getKey();
            int quantity = entry.getValue();
            int[] curr = ProductData.getPrice(id);  // current product price
            curr = mul(curr[0], curr[1], quantity);
            total = add(total[0], total[1], curr[0], curr[1]); // add current to total
        }
        return total;
    }

    /**
     * Round up the price to the nearest denomination for cash payment
     * @param dollars  dollars of the price
     * @param cents    cents of the price
     * @return integer array of length 2: first element is dollars and second element is cents after rounding up
     */
    public static int[] roundUpPrice(int dollars, int cents) {
        if (cents % 5 == 0) {
            return new int[]{dollars, cents};
        }
        cents += (5 - cents % 5);
        if (cents >= 100) {
            dollars += cents / 100;
        }
        cents = cents % 100;
        return new int[]{dollars, cents};
    }

    public static double totalDouble(Map<String, Integer> shoppingCart) {
        int[] totalPrice = calculateTotal(shoppingCart);
        double total = totalPrice[0] + totalPrice[1] / 100.0;
        return total;
    }

}
