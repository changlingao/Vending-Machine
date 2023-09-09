package VendingMachine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {
    @Test
    public void constructAnonUser() {
        String[] lastFiveProductID = new String[] {"1","2","3","4","5"};
        Customer c = new Customer(lastFiveProductID);
        assertEquals(c.getUsername(), "anonymous");
        assertEquals(c.getLastFiveProductID(), lastFiveProductID);
        assertEquals(c.getCardholderName(), "");
        assertEquals(c.getCreditCardNumber(), "");
    }

    @Test
    public void constructActualUser() throws Exception {
        String username = "user";
        String password = "1234";
        String role = "customer";
        String cardholderName = "Charles";
        String creditCardNumber = "40691";
        String[] lastFiveProductID = new String[] {"1","2","3","4","5"};
        Customer c = null;
        try {
            c = new Customer(username, password, cardholderName, creditCardNumber, lastFiveProductID);
        } catch (Exception e) {
            assertTrue(false, "Raise Exception when constructing valid customer");
        }
        assertEquals(c.getUsername(), username);
        assertEquals(c.getLastFiveProductID(), lastFiveProductID);
        assertEquals(c.getCardholderName(), cardholderName);
        assertEquals(c.getCreditCardNumber(), creditCardNumber);
    }

    @Test
    public void constructInvalidUser() throws IllegalArgumentException {
        String username = "anonymous";
        String password = "1234";
        String role = "customer";
        String cardholderName = "Charles";
        String creditCardNumber = "40691";
        String[] lastFiveProductID = new String[] {"1","2","3","4","5"};
        Customer c = null;
        try {
            c = new Customer(username, password, cardholderName, creditCardNumber, lastFiveProductID);
        } catch (IllegalArgumentException e) {
            return;
        } catch (Exception e) {
            assertTrue(false, "Raise incorrect exception when constructing invalid customer");
        }
        assertTrue(false, "Did not raise exception when constructing invalid customer");
    }

    @Test
    public void addCardInfoForUser() {
        String username = "user1";
        String password = "password1";
        String role = "customer";
        String cardholderName = "Charles";
        String creditCardNumber = "40691";
        String[] lastFiveProductID = new String[] {"1","2","3","4","5"};
        Customer c = new Customer(username, password, "", "", lastFiveProductID);
        c.addCardInfo(cardholderName, creditCardNumber);
        assertEquals(cardholderName, c.getCardholderName(), "Incorrect cardholder name");
        assertEquals(creditCardNumber, c.getCreditCardNumber(), "Incorrect credit card number");

    }

}
