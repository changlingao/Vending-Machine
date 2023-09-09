package VendingMachine;

import FrontEnd.AlertWindow;
import org.checkerframework.checker.units.qual.A;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Payment {
    private static LinkedHashMap<String, Integer> machineNotes = Payment.ReadMachineNotes();
    private static LinkedHashMap<String, Integer> insertedMoney = CreateNotes(0,0,0,0,0,0,0,0,0,0,0);
    // Initialise insertedMoney with default quantity 0.
    public static LinkedHashMap<String,Integer> getMachineNotes(){
        return machineNotes;
    }

    public static LinkedHashMap<String, Integer> getInsertedMoney() {
        //System.out.println("Inserted money:"+insertedMoney);
        return insertedMoney;
    }
    public static double getChangeReturn(){
        double money_return = TransformNotesToDouble(Payment.getInsertedMoney()) - Calculator.totalDouble(AppData.getShoppingCart());
        double round_money = (double) Math.round(money_return*100)/100;
        //System.out.println("money_return in getChangeReturn"+round_money);
        return round_money;
    }

    /**
     * one note each time
     * @param type
     */
    public static void insertMoney(String type) {
        int quantity = insertedMoney.getOrDefault(type, 0) + 1;
        insertedMoney.put(type, quantity);
    }

    public static void ResetInsertZero(){
        for(String notes_type: insertedMoney.keySet()){
            insertedMoney.put(notes_type,0);
        }
    }

    public static boolean verifyCard(String name, String number) {
        try {
            Path filename = Path.of(App.class.getClassLoader().getResource("credit_cards.json").toURI());
            String data = Files.readString(filename);
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(data);
            for (Object o : array) {
                JSONObject object = (JSONObject) o;
                String nameJSON = (String) object.get("name");
                String numberJSON = (String) object.get("number");
                if (nameJSON.equals(name) && numberJSON.equals(number)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("fail to read credit_cards.json");
            AlertWindow.alert("Invalid Card");
        }
        return false;
    }

    // This function will return Hashmap where key is notes/coins
    // and value is quantity for a particular notes
    // Be careful that before you use this function,
    // make sure the parameters is the money that should be returned
    // eg: goods bill is 78.5, user's cash is 100 dollar,
    // then parameter should be 100-78.5=21.5
    // CashReturn(MachineNotes, 21, 50)
    public static LinkedHashMap<String, Integer> CashReturn(LinkedHashMap<String, Integer> MachineNotes, Integer dollar, Integer cent,LinkedHashMap<String,Integer> InsertNotes) {
        /*System.out.println("machine"+MachineNotes);
        System.out.println("dollar:"+dollar+"cent:"+cent);
        System.out.println("insert in CashReturn"+InsertNotes);
         */

        LinkedHashMap<String, Integer> cash_return = new LinkedHashMap<>();
        // MachineNotes:{100$:5,50$:5, 20$:5, 10$:5,
        //               2$:5, 5$:5, 1$:5, 50c:5,
        //               20c:5, 10c:5, 5c:5}
        // dollar:21 what machine should return
        // cent:50 what machine should return
        int[] dollar_type = {100,50,20,10,5,2,1};
        int[] cent_type = {50,20,10,5};
        LinkedHashMap<Integer,Integer> dollars = new LinkedHashMap<>();//dollar's type in machine
        LinkedHashMap<Integer,Integer> cents = new LinkedHashMap<>();//cent's type in machine
        LinkedHashMap<Integer,Integer> dollars_return = new LinkedHashMap<>();
        LinkedHashMap<Integer,Integer> cents_return = new LinkedHashMap<>();
        // transfer String,Integer to Integer, Integer and divide by notes and coins
        int i = 0;
        for(String key: MachineNotes.keySet()){
            int value = MachineNotes.get(key);
            if(i < 7){
                dollars.put(dollar_type[i],value);
                dollars_return.put(dollar_type[i],0);
            }
            else if(i >= 7 && i < 11){
                cents.put(cent_type[i-7],value);
                cents_return.put(cent_type[i-7],0);
            }
            i++;
        }
        /*
        System.out.println("machine dollars"+dollars);
        System.out.println("machine cents"+cents);
        System.out.println("user return dollars"+dollars_return);
        System.out.println("user return cents"+cents_return);
         */

        // For different change type we should return
        // By update dollars_return and cents_return.
        ValuePrefer(dollars,dollar,dollars_return);
        ValuePrefer(cents,cent,cents_return);
        // Combine dollar and cent together in only one hashmap
        int j = 0;
        for(String note_type: MachineNotes.keySet()){
            if(j < 7){
                cash_return.put(note_type,dollars_return.get(dollar_type[j]));
                MachineNotes.put(note_type,dollars.get(dollar_type[j])+InsertNotes.get(note_type));
                InsertNotes.put(note_type,0);
                //update quantity of changes in machine
            }
            else if(j < 11){
                cash_return.put(note_type,cents_return.get(cent_type[j-7]));
                MachineNotes.put(note_type,cents.get(cent_type[j-7])+InsertNotes.get(note_type));
                InsertNotes.put(note_type,0);
            }
            j++;
        }
        //System.out.println("machine notes in CashReturn"+MachineNotes);
        WriteMachineNotes(MachineNotes);
        return cash_return;
    }

    // The function below is one overloading function which can get different parameters
    // This is to deal with input value is double number.
    // eg: goods bill is 78.5, user's cash is 100 dollar
    // CashReturn(MachineNotes, 21.5)
    public static LinkedHashMap<String, Integer> CashReturn(LinkedHashMap<String, Integer> MachineNotes, double money,LinkedHashMap<String,Integer> InsertNotes) throws Exception{
        // money < 0 means money that customer inserts is smaller than goods' bill.
        if(money<0){
            throw new Exception("Inserted money is not enough");
        } else if (! CanReturn(MachineNotes,money)) {
            ResetInsertZero();
            throw new Exception("There is no available change");
        }

        int dollar = (int) money;
        int cent = (int)Math.round((money % 1)*100);
        return CashReturn(MachineNotes,dollar,cent,InsertNotes);
    }

    private static void ValuePrefer(LinkedHashMap<Integer, Integer> machine_notes, int return_need, LinkedHashMap<Integer, Integer> notes_return){
        for(int note_type : machine_notes.keySet()){
            if(return_need <= 0){
                // We have finished money return.
                break;
            }
            else if (note_type > return_need) {
                // For current note type, we didn't use it, no update for which quantity of note was 0.
                continue;
            } else if (note_type * machine_notes.get(note_type) < return_need) {
                // For current note type, we can use it, but not enough.
                // For example:20$:2, but we need 3 20$.then we will use 2 20$
                int quantity = machine_notes.get(note_type);
                machine_notes.put(note_type,0);
                notes_return.put(note_type,quantity);
                return_need = return_need - note_type * quantity;
            } else {
                // For current note type, we have enough quantity of it to return.
                int[] a = RestReturn(note_type, return_need);
                machine_notes.put(note_type, machine_notes.get(note_type) - a[0]);
                notes_return.put(note_type, notes_return.get(note_type) + a[0]);
                return_need = a[1];
            }
        }
    }

    private static int[] RestReturn(int note_type,int rest_return){
        int quantity = rest_return/note_type;
        int rest = rest_return%note_type;
        return new int[]{quantity,rest};
    }


    // One function judge whether money return can be done.
    // whether machine notes is enough.
    public static boolean CanReturn(LinkedHashMap<String, Integer> MachineNotes, int dollar, int cent){

        int[] dollar_type = {100,50,20,10,5,2,1};
        int[] cent_type = {50,20,10,5};
        int d = dollar;
        int c = cent;
        LinkedHashMap<Integer,Integer> dollars = new LinkedHashMap<>();
        LinkedHashMap<Integer,Integer> cents = new LinkedHashMap<>();
        // transfer String,Integer to Integer, Integer and divide by notes and coins
        int k = 0;
        for(String key: MachineNotes.keySet()){
            int value = MachineNotes.get(key);
            if(k < 7){
                dollars.put(dollar_type[k],value);
            }
            else if(k < 11){
                cents.put(cent_type[k-7],value);
            }
            k++;
        }
        if(RestMoneyUpdated(dollars,d) > 0 || RestMoneyUpdated(cents,c) > 0){
            return false;
        }
        return true;
    }

    //Overloading function for CanReturn.
    public static boolean CanReturn(LinkedHashMap<String, Integer> MachineNotes, double money){
        int dollar = (int) money;
        int cent = (int)Math.round((money % 1)*100);
        //System.out.println("notes in machine"+MachineNotes);
        //System.out.println("dollar in CanReturn"+dollar+"cent in CanReturn" + cent);
        return CanReturn(MachineNotes,dollar,cent);
    }



    private static int RestMoneyUpdated(LinkedHashMap<Integer, Integer> machine_notes, int return_need){
        for(int note_type : machine_notes.keySet()){
            if(return_need <= 0){
                // We have finished money return.
                break;
            }
            else if (note_type > return_need) {
                // For current note type, we didn't use it, no update for which quantity of note was 0.
                continue;
            } else if (note_type * machine_notes.get(note_type) < return_need) {
                // For current note type, we can use it, but not enough.
                // For example:20$:2, but we need 3 20$.then we will use 2 20$
                int quantity = machine_notes.get(note_type);
                return_need = return_need - note_type * quantity;
            } else {
                // For current note type, we have enough quantity of it to return.
                int[] a = RestReturn(note_type, return_need);
                return_need = a[1];
            }
        }
        return return_need;
    }

    public static LinkedHashMap<String,Integer> ReadMachineNotes(){
        LinkedHashMap<String,Integer> machine_notes = new LinkedHashMap<>();
        try{
            File f = new File("src/main/resources/cash.txt");
            Scanner scan = new Scanner(f);
            String[] notes_type = new String[11];
            String[] quantity = new String[11];
            int line_num = 0;
            while(scan.hasNextLine()){
                if(line_num==0){
                    notes_type = scan.nextLine().replace("\n","").split(",");
                }
                if(line_num==1){
                    quantity = scan.nextLine().replace("\n","").split(",");
                }
                line_num++;
            }
            for(int i=0;i<11;i++){
                machine_notes.put(notes_type[i],Integer.parseInt(quantity[i]));
            }
            scan.close();

        }catch (FileNotFoundException e) {
            System.out.println("cash.txt cannot be found");
            e.printStackTrace();
        }
        //System.out.println("machine notes read:"+machine_notes);
        return machine_notes;
    }

    public static void WriteMachineNotes(LinkedHashMap<String,Integer> current_notes){
            // FileWriter myWriter = new FileWriter("app/src/main/resources/cash.txt");
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/cash.txt");
            String line0 = "";
            String line1 = "";
            for(String key: current_notes.keySet()) {
                line0 = line0 + key + ",";
                line1 = line1 + current_notes.get(key)+",";
            }
            myWriter.write(line0+"\n");
            myWriter.write(line1);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Writing file failed");
            e.printStackTrace();
        }

    }
    // This is to get one summary of notes, you can input one LinkedHashmap including notes, it will give you string.
    public static ArrayList<String> GetSummaryNotes(LinkedHashMap<String, Integer> machine_notes){
        ArrayList<String> summary = new ArrayList<>();
        for(String key: machine_notes.keySet()){
            summary.add(key+": "+machine_notes.get(key));
        }
        return summary;
    }

    public static double TransformNotesToDouble(LinkedHashMap<String,Integer> notes){
        int[] dollar_type = {100,50,20,10,5,2,1};
        int[] cent_type = {50,20,10,5};
        double total = 0;
        int i = 0;
        for(String key: notes.keySet()){
            if(i<7){
                total += notes.get(key)*dollar_type[i];
            }
            else if(i<11){
                total += 0.01*notes.get(key)*cent_type[i-7];
            }
            i++;
        }
        return (double)Math.round(total * 100)/100;
    }

    public static LinkedHashMap<String,Integer> CreateNotes(int hundred$,int fifty$,
                                                            int twenty$,int ten$, int five$,
                                                            int two$, int one$, int fiftyc,
                                                            int twentyc,int tenc, int fivec){
        LinkedHashMap<String,Integer> notes = new LinkedHashMap<>();
        notes.put("100$",hundred$);
        notes.put("50$",fifty$);
        notes.put("20$",twenty$);
        notes.put("10$",ten$);
        notes.put("5$",five$);
        notes.put("2$",two$);
        notes.put("1$",one$);
        notes.put("50c",fiftyc);
        notes.put("20c",twentyc);
        notes.put("10c",tenc);
        notes.put("5c",fivec);
        return notes;

    }



}
