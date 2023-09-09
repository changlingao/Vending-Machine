package VendingMachine;

import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;

public class DataBase {
    public static String userFile = "src/main/resources/user.txt"; // default user file
    public static String productFile = "src/main/resources/products_inventory.txt";
    public static String transactionSuccessfulFile = "src/main/resources/transactions_successful.txt";
    public static String transactionCancelledFile = "src/main/resources/transactions_cancelled.txt";

    public static ArrayList<String> Read(String file){
        ArrayList<String> fileList = new ArrayList<>();
        try {
            File myObj = new File(file);
            //System.out.println(myObj);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.replace("\n", "").replace("\r", "");
                fileList.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("file read failed");
            System.out.println(e);
            return null;
        }
        return fileList;
    }

    // different database override this based on requirements
    public static void Write(ArrayList<String> info, String file){
        try {
            FileWriter myWriter = new FileWriter(file);

            for(int i = 0; i < info.size(); i ++) {
                myWriter.write(info.get(i));
                myWriter.write('\n');
            }
            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
