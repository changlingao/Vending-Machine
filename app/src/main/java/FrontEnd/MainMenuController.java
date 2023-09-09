package FrontEnd;

import VendingMachine.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.io.IOException;

public class MainMenuController {

    public ListView<String> drinks;
    public ListView<String> chocolates;
    public ListView<String> chips;
    public ListView<String> candies;
    public TextField code;
    public TextField quantity;
    public Button role;
    public Button login;
    public Label history;
    public Label timer;
    public Button logout;

    @FXML
    public void initialize() {
        // button visible depends on role
        Customer customer = AppData.getCustomer();
        if (customer.getUsername().equals("anonymous")) {
            role.setVisible(false);
            logout.setVisible(false);
        } else {
            String text = "Role " + customer.getRole();
            role.setVisible(true);
            role.setText(text);
            login.setVisible(false);
            logout.setVisible(true);
        }

        // menu display
        drinks.setItems(FXCollections.observableArrayList(ProductData.getProductsByCategory("Drinks")));
        chocolates.setItems(FXCollections.observableArrayList(ProductData.getProductsByCategory("Chocolates")));
        chips.setItems(FXCollections.observableArrayList(ProductData.getProductsByCategory("Chips")));
        candies.setItems(FXCollections.observableArrayList(ProductData.getProductsByCategory("Candies")));

        // shopping history
        String[] recentFive = UserData.getRecentFive(customer.getUsername());
        String temp = "history: [";
        for (String s: recentFive) {
            temp += s + ", ";
        }
        temp += "]";
        history.setText(temp);

        // timer
        TimeTracker.attach(timer);
    }

    public void loginOrCreate(ActionEvent actionEvent) throws IOException {
        // reset shopping cart and timer
        AppData.reset();
        new App().changeScene("LoginPage.fxml", "Login or Create Account");
    }

    public void checkOut(ActionEvent actionEvent) throws IOException {
        if (AppData.getShoppingCart().isEmpty()) {
            AlertWindow.alert("Nothing in shopping cart");
            new App().changeScene("MainMenu.fxml", "Vending Machine");
        } else {
            new App().changeScene("CheckOutPage.fxml", "Check Out");
        }
    }

    public void cancel(ActionEvent actionEvent) {
        TransactionData.addCancelledTransactionToFile("user cancelled");
        AlertWindow.alert("cancelled");
        // log out and reset shopping cart
        AppData.reset();
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    public void addToShoppingCart(ActionEvent actionEvent) {
        try {
            AppData.addToCart(code.getText(), quantity.getText());
            AlertWindow.alert("Successfully added to shopping cart");
        } catch (NumberFormatException e) {
            AlertWindow.alert("Invalid quantity");
        } catch (Exception e) {
            AlertWindow.alert(e.getMessage());
        }
        code.setText("");
        quantity.setText("");
    }

    // switch to page based on role
    public void roleFunction(ActionEvent actionEvent) throws IOException {
        switch (AppData.getCustomer().getRole()) {
            // case "customer" : nothing

            case "seller" -> new App().changeScene("SellerPage.fxml", "Welcome Seller");

            case "cashier" -> new App().changeScene("CashierPage.fxml", "Welcome Cashier");

            case "owner" -> new App().changeScene("OwnerPage.fxml", "Welcome Owner");
        }
    }

    // start timer when user type in the product code
    public void startTimer(KeyEvent keyEvent) {
        TimeTracker.startTimer();
    }

    // function is the same as cancel
    public void logout(ActionEvent actionEvent) {
        TransactionData.addCancelledTransactionToFile("user cancelled");
        AlertWindow.alert("Logged out and transaction is cancelled");
        // log out and reset shopping cart
        AppData.reset();
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }
}
