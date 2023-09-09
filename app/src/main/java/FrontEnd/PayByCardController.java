package FrontEnd;

import VendingMachine.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.time.LocalDateTime;

public class PayByCardController {
    public TextField name;
    public TextField numberFiled;
    public Button submitButton;
    private String cardNumber = "";
    public Label timer;

    @FXML
    public void initialize() {
        // automatically fill in for logged in customer
        Customer customer = AppData.getCustomer();
        if (!customer.getUsername().equals("anonymous")) {
            String cardholderName = customer.getCardholderName();
            String creditCardNumber = customer.getCreditCardNumber();
            if (cardholderName.length() != 0) {
                name.setText(cardholderName);
            }
            if (creditCardNumber.length() != 0) {
                cardNumber = creditCardNumber;
                numberFiled.setText("*****");
            }
        }

        // timer
        TimeTracker.attach(timer);

        // disable select and position caret
        disableCaretForAsterisk(numberFiled);
    }

    public static void disableCaretForAsterisk(TextField textField) {
        textField.setOnMouseDragged(e -> {
            textField.deselect();
            textField.positionCaret(textField.getLength());
        });
        textField.setOnMouseClicked(e -> {
            textField.positionCaret(textField.getLength());
        });
        textField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.UP) {
                textField.positionCaret(textField.getLength());
            }
        });
    }

    public void submit(ActionEvent actionEvent) {
        boolean status = Payment.verifyCard(name.getText(), cardNumber);
        if (status) {
            // Payment Successful
//            AlertWindow.alert("Payment Successful"); cannot use showAndWait
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            TimeTracker.attachAlert(a);
            a.setContentText("Payment Successful");
            a.showAndWait();

            // save this credit card detail for logged-in user
            Customer customer = AppData.getCustomer();
            if (!customer.getUsername().equals("anonymous")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                TimeTracker.attachAlert(alert);
                alert.setHeaderText("Save this card ?");
                alert.setContentText("So it will be auto filled in for next transaction");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        customer.addCardInfo(name.getText(), cardNumber);
                    }
                });
            }

            // time out
            if (!TimeTracker.isTimeOut()) {
                AppData.completeSuccessfulTransaction(Calculator.totalDouble(AppData.getShoppingCart()), 0.0, "card");
                new App().changeScene("MainMenu.fxml", "Vending Machine");
            }

        } else {
            AlertWindow.alert("Invalid Card");
            new App().changeScene("PayByCard.fxml", "Pay By Card");
        }
    }

    // hide card number input with asterisk (*)
    public void asterisk(KeyEvent keyEvent) {
        // add char
        if (numberFiled.getText().length() > cardNumber.length()) {
            String front = numberFiled.getText().substring(0, numberFiled.getText().length()-1);
            // get the last char
            String last = numberFiled.getText().substring(numberFiled.getText().length()-1);
            cardNumber += last;
            numberFiled.setText(front + "*");
            numberFiled.positionCaret(numberFiled.getText().length());

        // delete char
        } else if (numberFiled.getText().length() == 0) {
            cardNumber = "";
        } else {
            String front = cardNumber.substring(0, cardNumber.length()-1);
            cardNumber = front;
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        new App().changeScene("CheckOutPage.fxml", "Check Out");
    }

    public void cancel(ActionEvent actionEvent) throws IOException {
        try {
            TransactionData.addCancelledTransactionToFile("user cancelled");
            AlertWindow.alert("cancelled");
        } catch (Exception e) {
            AlertWindow.alert(e.getMessage());
        }
        // log out and reset shopping cart
        AppData.reset();
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }
}
