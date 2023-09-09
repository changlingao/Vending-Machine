package FrontEnd;

import VendingMachine.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.LinkedHashMap;

public class PayByCashController {

    public Label total;
    public Label inserted;
    public Label timer;

    public void initialize() {
        int[] totalPrice = Calculator.calculateTotal(AppData.getShoppingCart());
        total.setText("Total Price: " + totalPrice[0] + "." + totalPrice[1]);
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));

        // timer
        TimeTracker.attach(timer);
    }

    public void n100(ActionEvent actionEvent) {
        Payment.insertMoney("100$");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void n50(ActionEvent actionEvent) {
        Payment.insertMoney("50$");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void n20(ActionEvent actionEvent) {
        Payment.insertMoney("20$");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void n10(ActionEvent actionEvent) {
        Payment.insertMoney("10$");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void n5(ActionEvent actionEvent) {
        Payment.insertMoney("5$");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void dc2(ActionEvent actionEvent) {
        Payment.insertMoney("2$");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void dc1(ActionEvent actionEvent) {
        Payment.insertMoney("1$");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void c50(ActionEvent actionEvent) {
        Payment.insertMoney("50c");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void c20(ActionEvent actionEvent) {
        Payment.insertMoney("20c");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void c10(ActionEvent actionEvent) {
        Payment.insertMoney("10c");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void c5(ActionEvent actionEvent) {
        Payment.insertMoney("5c");
        inserted.setText("Inserted Money: " + Payment.TransformNotesToDouble(Payment.getInsertedMoney()));
    }

    public void back(ActionEvent actionEvent) throws IOException {
        new App().changeScene("CheckOutPage.fxml", "Check Out");
    }

    public void cancel(ActionEvent actionEvent) {
        TransactionData.addCancelledTransactionToFile("user cancelled");
        AlertWindow.alert("cancelled");
        // log out and reset shopping cart
        AppData.reset();
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    public void submit(ActionEvent actionEvent) throws IOException {
        try {
            LinkedHashMap<String, Integer> returnedChanges = Payment.CashReturn(Payment.getMachineNotes(), Payment.getChangeReturn(), Payment.getInsertedMoney());

            // Payment Successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Payment Successful");
            // receive products
            String productsBought = "Products Bought: \n";
            for (String productCode: AppData.getShoppingCart().keySet()) {
                productsBought += ProductData.getName(productCode) + " X " + AppData.getShoppingCart().get(productCode) + " \n";
            }

            // receive correct change
            String changes = "";
            if (returnedChanges.isEmpty()) {
                changes = "No returned changes";
            } else {
                changes = "\n Returned Changes: \n";
                for (String value: returnedChanges.keySet()) {
                    if(returnedChanges.get(value)>0) {
                        changes += value + " X " + returnedChanges.get(value) + " \n";
                    }
                }
            }
            alert.setContentText(productsBought + changes);
            alert.show();

            // record this transaction
            AppData.completeSuccessfulTransaction(Payment.TransformNotesToDouble(Payment.getInsertedMoney()), Payment.getChangeReturn(), "cash");
            new App().changeScene("MainMenu.fxml", "Vending Machine");

        } catch (Exception e) {
            if (e.getMessage().equals("Inserted money is not enough")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(e.getMessage());
                alert.setContentText("click OK and enter the remaining mount or click cancel to cancel the transaction");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        cancel(null);
                    }
                });

            } else if (e.getMessage().equals("There is no available change")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(e.getMessage());
                alert.setContentText("click OK and insert different notes/coins to complete the payment or click cancel to cancel the transaction");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        changeNotAvailable();
                    } else if (response == ButtonType.OK) {
                        new App().changeScene("PayByCash.fxml", "Pay By Cash");
                    }
                });
            }
        }

    }

    // similar to cancel just diff in reason
    public void changeNotAvailable() {
        TransactionData.addCancelledTransactionToFile("change not available");
        AlertWindow.alert("transaction is cancelled");
        // log out and reset shopping cart
        AppData.reset();
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }
}
