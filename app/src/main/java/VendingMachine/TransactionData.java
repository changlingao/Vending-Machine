package VendingMachine;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionData extends DataBase{
    /**
     * datetime,item_sold,money_paid,returned_change,payment_method.
     * @param payment_method card or cash
     */
    public static void addSuccessfulTransactionToFile(HashMap<String, Integer> itemSold, double moneyPaid, double returnedChange, String payment_method) {
        String datetime = LocalDateTime.now().toString();
        String transaction = datetime + ",";

        String items_sold = "{";
        for (String code: itemSold.keySet()) {
            items_sold += ProductData.getName(code) + ":" + itemSold.get(code) + ";";
        }
        items_sold += "}";
        transaction += items_sold + ",";

        transaction += moneyPaid + "," + returnedChange + "," + payment_method;

        ArrayList<String> successful = Read(transactionSuccessfulFile);
        successful.add(transaction);
        Write(successful, transactionSuccessfulFile);
    }

    /**
     * datetime,user_name,reason
     * @param reason (e.g. "timeout", "user cancelled", "change not available", ...)
     */
    public static void addCancelledTransactionToFile(String reason) {
        // nothing then will not write to file
//        if (AppData.getShoppingCart().size() == 0) {
//            throw new Exception("Nothing in shopping cart");
//        }

        String datetime = LocalDateTime.now().toString();
        String user_name = AppData.getCustomer().getUsername();
        String transaction = datetime + "," + user_name + "," + reason;

        ArrayList<String> cancelled = Read(transactionCancelledFile);
        cancelled.add(transaction);
        Write(cancelled, transactionCancelledFile);
    }

    /**
     * for UI display, make it readable
     * may not test
     */
    public static ArrayList<String> getSuccessfulTransactions() {
        String prompt = "date time, item sold, money paid, returned change, payment method";
        ArrayList<String> successful = Read(transactionSuccessfulFile);
        successful.add(0, prompt);
        return successful;
    }

    /**
     * for UI display, make it readable
     * may not test
     */
    public static ArrayList<String> getCancelledTransactions() {
        String prompt = "datetime, user_name, reason";
        ArrayList<String> cancelled = Read(transactionCancelledFile);
        cancelled.add(0, prompt);
        return cancelled;
    }

}
