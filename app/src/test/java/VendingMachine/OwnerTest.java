package VendingMachine;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Array;
import java.util.stream.Stream;

public class OwnerTest {
    @ParameterizedTest
    @CsvSource({
            "deGrom,4sfb",
            ",aef43",
            "re,"
    })
    public void constructOwner1(String username, String password) {
        Owner owner = new Owner(username, password);
    }

    @ParameterizedTest
    @MethodSource("FullOwner")
    public void constructOwner2(String username, String password, String cardholderName, String creditCardNumber, String[] lastFiveProductID) {
        Owner owner = new Owner(username, password, cardholderName, creditCardNumber, lastFiveProductID);
    }

    private static Stream<Arguments> FullOwner() {
        return Stream.of(
                Arguments.of("deGrom", "4sfb", "name", "4321", new String[] {"Pepsi", "Smith", "", "", ""})
        );
    }


}
