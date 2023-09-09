package FrontEnd;

import VendingMachine.App;
import VendingMachine.AppData;
import VendingMachine.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.io.IOException;

import static FrontEnd.PayByCardController.disableCaretForAsterisk;

public class LoginPageController {
// constructor -> @FXML fields -> initialize
    private String password = "";
    public TextField username;
    public Button loginButton;
    public TextField passwordField;
    public Button back;

    public void initialize() {
        disableCaretForAsterisk(passwordField);
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    public void loginOrCreate(ActionEvent actionEvent) throws Exception {
        String name = username.getText();
        try {
            AppData.setCustomer(UserData.loginOrCreate(name, password));
        } catch (Exception e) {
            AlertWindow.alert(e.getMessage());
        }
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    // hide password input with asterisk (*)
    // same as the one in PayByCardController
    public void asterisk(KeyEvent keyEvent) {
        // add char
        if (passwordField.getText().length() > password.length()) {
            String front = passwordField.getText().substring(0, passwordField.getText().length()-1);
            // get the last char
            String last = passwordField.getText().substring(passwordField.getText().length()-1);
            password += last;
            passwordField.setText(front + "*");
            passwordField.positionCaret(passwordField.getText().length());

            // delete char
        } else if (passwordField.getText().length() == 0) {
            password = "";
        } else {
            String front = password.substring(0, password.length()-1);
            password = front;
        }
    }
}
