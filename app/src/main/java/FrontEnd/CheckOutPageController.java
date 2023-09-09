package FrontEnd;

import VendingMachine.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.HashMap;

public class CheckOutPageController {
    public Button cashButton;
    public Button cardButton;
    public Label total;
    public Label shoppingCartLabel;
    public Label timer;

    @FXML
    public void initialize() {
        HashMap<String, Integer> shoppingCart = AppData.getShoppingCart();
        int[] totalPrice = Calculator.calculateTotal(shoppingCart);
        total.setText("Total Price: " + totalPrice[0] + "." + totalPrice[1]);
        String cart = "Shopping Cart \n";
        for (String productCode: shoppingCart.keySet()) {
            cart += ProductData.getName(productCode) + " X " + shoppingCart.get(productCode) + " \n";
        }
        shoppingCartLabel.setText(cart);

        // timer
        TimeTracker.attach(timer);
    }

    public void payByCash(ActionEvent actionEvent) throws IOException {
        new App().changeScene("PayByCash.fxml", "Pay By Cash");
    }

    public void payByCard(ActionEvent actionEvent) throws IOException {
        new App().changeScene("PayByCard.fxml", "Pay By Card");
    }

    public void back(ActionEvent actionEvent) throws IOException {
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    public void cancel(ActionEvent actionEvent) {
        TransactionData.addCancelledTransactionToFile("user cancelled");
        AlertWindow.alert("cancelled");
        // log out and reset shopping cart
        AppData.reset();
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }
}
