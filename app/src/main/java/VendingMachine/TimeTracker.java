package VendingMachine;

import FrontEnd.AlertWindow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class TimeTracker {
    public static final int TIME = 120;
    public static int timeCount = TIME;
    public static ArrayList<Label> observers = new ArrayList<>();
    public static Timeline timeline;
    public static ArrayList<Alert> alertObservers = new ArrayList<>();

    public static void startTimer() {
        if (timeCount == TIME) {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), t -> {
                timeCount -= 1;
                notifyTimers();
                // change later
                if (timeCount == 0) {
                    timeout();
                }
            }));
            timeline.setCycleCount(TIME + 1);
            timeline.play();
        }
    }

    public static void timeout() {
        closeAlerts();
        TransactionData.addCancelledTransactionToFile("time out");
        // time out also log out
        AppData.reset();
        AlertWindow.alert("time out");
        new App().changeScene("MainMenu.fxml", "Vending Machine");
    }

    public static void attach(Label label) {
        observers.add(label);
    }

    public static void notifyTimers() {
        for (Label label: observers) {
            label.setText("Timer: " + timeCount + " s");
        }
    }

    /**
     * when user manually cancel the transaction
     */
    public static void stop() {
        timeCount = TIME;
        observers = new ArrayList<>();
        if (timeline != null) {
            timeline.stop();
        }
    }

    public static void attachAlert(Alert alert) {
        alertObservers.add(alert);
    }

    public static void closeAlerts() {
        for (Alert alert: alertObservers) {
            alert.close();
        }
    }

    public static boolean isTimeOut() {
        return timeCount == TIME;
    }
}
