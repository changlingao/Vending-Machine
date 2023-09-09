package VendingMachine;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    // TODO: Initialise quantity of all notes type to 5 in cash.txt after we run the tests.
    //  Since in CashReturn we will change cash.txt file
    public static LinkedHashMap<String,Integer> machine_notes_before = Payment.ReadMachineNotes();
    // This is to store quantity of each notes in cash.txt. The reason why I store this is that in the test,
    // Every time I call CashReturn, it will change the cash.txt since Payment.WriteMachineNotes is in Payment.CashReturn
    // So after all test, I will store this in cash.txt

    @AfterAll
    public static void SetToBefore(){
        Payment.WriteMachineNotes(machine_notes_before);
    }
    public static LinkedHashMap<String,Integer> CreateMachineNotes
        (int hundred$, int fifty$, int twenty$, int ten$,int five$, int two$, int one$, int fiftyC, int twentyC, int tenC, int fiveC){
        LinkedHashMap<String, Integer> machine_notes = new LinkedHashMap<>();
        machine_notes.put("100$",hundred$);
        machine_notes.put("50$",fifty$);
        machine_notes.put("20$",twenty$);
        machine_notes.put("10$",ten$);
        machine_notes.put("5$",five$);
        machine_notes.put("2$",two$);
        machine_notes.put("1$",one$);
        machine_notes.put("50c",fiftyC);
        machine_notes.put("20c",twentyC);
        machine_notes.put("10c",tenC);
        machine_notes.put("5c",fiveC);
        return machine_notes;
    }
    // Unit test:
    @Test
    // Check whether CashReturn can get correct changes that should give to customer.
    // input values are dollar and cent
    public void CashReturnTest()  {
        LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(1,2,4,3,0,5,5,5,2,2,5);
        int dollar = 56;
        int cent =80;
        LinkedHashMap<String, Integer> return_expected = CreateMachineNotes(0,1,0,0,0,3,0,1,1,1,0);
        LinkedHashMap<String, Integer> InsertNotes = CreateMachineNotes(1,0,0,0,0,0,0,0,0,0,0);
        LinkedHashMap<String, Integer> return_actual = Payment.CashReturn(machine_notes,dollar,cent,InsertNotes);
        assertEquals(return_expected,return_actual);
    }
    // Test overloading function of CashReturn
    // input value is double
    @Test
    public void CashReturnTest1() {
        try {
            LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(0, 2, 4, 3, 0, 5, 5, 5, 2, 2, 5);
            double change_return = 56.8;
            LinkedHashMap<String, Integer> return_expected = CreateMachineNotes(0, 1, 0, 0, 0, 3, 0, 1, 1, 1, 0);
            LinkedHashMap<String, Integer> InsertNotes = CreateMachineNotes(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            LinkedHashMap<String, Integer> return_actual = Payment.CashReturn(machine_notes, change_return, InsertNotes);
            assertEquals(return_expected, return_actual);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // Try to test whether we can catch correct exception(notes in machine is not enough to return) from Cash Return
    // This can be regard as one integration test(CanReturn and CashReturn)
    @Test
    public void CashReturnTest2(){
        try {
            double change_return = 34.45;
            LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(3,5,6,0,2,0,2,5,5,5,5);
            LinkedHashMap<String,Integer> insert_notes = CreateMachineNotes(0,1,0,0,0,0,0,0,0,0,0);
            Payment.CashReturn(machine_notes,change_return,insert_notes);
        }catch (Exception e){
            assertEquals("There is no available change",e.getMessage());
        }
    }

    // Try to test whether we can catch correct exception(Insert money is not enough)
    @Test
    public void CashReturnTest3(){
        try{
            double change_return = -10;
            // Since change_return = notes_insert - bills
            LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(5,6,1,5,2,2,4,4,24,5,6);
            LinkedHashMap<String,Integer> insert_notes = CreateMachineNotes(0,0,0,0,0,3,0,0,0,0,0);
            Payment.CashReturn(machine_notes,change_return,insert_notes);
        }catch(Exception e){
            assertEquals("Inserted money is not enough",e.getMessage());
        }
    }


    @Test
    // This test is to check whether Payment.CashReturn function can change the Hashmap machine_notes.
    // input values are dollar and cent
    public void AutoChangeMachineNotesTest() {
        LinkedHashMap<String, Integer> rest_actual = CreateMachineNotes(1,2,4,3,0,5,5,5,2,2,5);
        LinkedHashMap<String,Integer> InsertNotes = CreateMachineNotes(1,0,0,0,0,0,0,0,0,0,0);
        int dollar = 56;
        int cent = 80;
        Payment.CashReturn(rest_actual,dollar,cent,InsertNotes);
        LinkedHashMap<String,Integer> rest_expected = CreateMachineNotes(2,1,4,3,0,2,5,4,1,1,5);
        assertEquals(rest_expected,rest_actual);
    }

    @Test
    // Test overloading function of CashReturn
    // input value is double
    public void AutoChangeMachineNotesTest1() {
        try {
            LinkedHashMap<String, Integer> rest_actual = CreateMachineNotes(1, 2, 4, 3, 0, 5, 5, 5, 2, 2, 5);
            LinkedHashMap<String, Integer> InsertNotes = CreateMachineNotes(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            double change_return = 56.8;
            Payment.CashReturn(rest_actual, change_return, InsertNotes);
            LinkedHashMap<String, Integer> rest_expected = CreateMachineNotes(2, 1, 4, 3, 0, 2, 5, 4, 1, 1, 5);
            assertEquals(rest_expected, rest_actual);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    // Test vending machine can check whether it can give a change.
    public void CanReturnTest(){
        LinkedHashMap<String, Integer> machineNotes = CreateMachineNotes(0,2,4,3,0,5,5,5,2,2,5);
        // input values are dollar and cent
        int dollar = 45;
        int cent = 75;
        assertTrue(Payment.CanReturn(machineNotes,dollar,cent));

        // input value is double
        double money_return = 45.75;
        assertTrue(Payment.CanReturn(machineNotes,money_return));
    }

    @Test
    // Test condition that if amount of money in vending machine is enough,
    // but cannot return since vending machine doesn't have enough particular notes.
    public void CanReturnTest1(){
        LinkedHashMap<String, Integer> machineNotes = CreateMachineNotes(0,2,1,1,0,5,0,5,2,2,5);
        // input values are dollar and cent
        int dollar = 45;
        int cent = 75;
        assertFalse(Payment.CanReturn(machineNotes,dollar,cent));

        // input value is double
        double change_return = 45.75;
        assertFalse(Payment.CanReturn(machineNotes,change_return));
    }

    @Test
    // Test condition that if amount of money in vending machine is enough,
    // but cannot return since vending machine doesn't have enough particular cents.
    public void CanReturnTest2(){
        LinkedHashMap<String, Integer> machineNotes = CreateMachineNotes(0,2,1,1,0,5,5,5,1,1,0);
        // input values are dollar and cent
        int dollar = 45;
        int cent = 45;
        assertFalse(Payment.CanReturn(machineNotes,dollar,cent));

        // input value is double
        double change_return = 45.45;
        assertFalse(Payment.CanReturn(machineNotes,change_return));
    }

    @Test
    // Test if amount of money in machine is not enough.
    public void CanReturnTest3(){
        LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(0,0,0,1,2,1,1,0,0,0,3);
        // Input values are dollar and cent;
        int dollar = 56;
        int cent = 35;
        assertFalse(Payment.CanReturn(machine_notes,dollar,cent));

        // Input value is double;
        double change_return = 56.35;
        assertFalse(Payment.CanReturn(machine_notes,change_return));
    }

    @Test
    // This is to test whether transformToDouble can return correct double value but not like:x.33333333
    public void TransformToDoubleTest(){
        LinkedHashMap<String,Integer> insert_notes = CreateMachineNotes(0,0,1,2,2,1,1,3,2,1,1);
        double expected = 55.05;
        double actual = Payment.TransformNotesToDouble(insert_notes);
        assertEquals(expected,actual);
    }

    @Test
    // Test some edge case
    public void TransformToDoubleTest1(){
        LinkedHashMap<String,Integer> insert_notes = CreateMachineNotes(5,20,5,3,87,2,6,3,4,1,57);
        double expected = 2080.25;
        double actual = Payment.TransformNotesToDouble(insert_notes);
        assertEquals(expected,actual);
    }

    @Test
    public void TransformToDoubleTest2() {
        LinkedHashMap<String, Integer> insert_notes = CreateMachineNotes(5, 20, 99999, 3, 99999, 2, 6, 3, 4, 1, 99999);
        double expected = 2506517.35;
        double actual = Payment.TransformNotesToDouble(insert_notes);
        assertEquals(expected, actual);
    }

    @Test
    // To test reading cash.txt, we must know the quantity of each notes in cash.txt.
    // So we combine writing and reading together.
    // Then we can set what we want in cash.txt and check reading and writing.
    public void WriteAndReadMachineNotesTest(){
        LinkedHashMap<String, Integer> expected_machine_notes = CreateMachineNotes(14,20,12,5,2,5,0,34,55,2,4);
        Payment.WriteMachineNotes(expected_machine_notes);
        LinkedHashMap<String,Integer> actual_machine_notes = Payment.ReadMachineNotes();
        assertEquals(expected_machine_notes,actual_machine_notes);
    }

    @Test
    // Try some edge test.
    public void WriteAndReadMachineNotes1Test(){
        LinkedHashMap<String, Integer> expected_machine_notes = CreateMachineNotes(999999,99999,9999,999,99,9,0,9999,999,99,9);
        Payment.WriteMachineNotes(expected_machine_notes);
        LinkedHashMap<String,Integer> actual_machine_notes = Payment.ReadMachineNotes();
        assertEquals(expected_machine_notes,actual_machine_notes);
    }

    @Test
    public void GetSummaryNotesTest(){
        LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(5,23,32,4,2,6,2,4,6,7,13);
        ArrayList<String> e = new ArrayList<>();
        e.add("100$: 5");
        e.add("50$: 23");
        e.add("20$: 32");
        e.add("10$: 4");
        e.add("5$: 2");
        e.add("2$: 6");
        e.add("1$: 2");
        e.add("50c: 4");
        e.add("20c: 6");
        e.add("10c: 7");
        e.add("5c: 13");
        ArrayList<String> a = Payment.GetSummaryNotes(machine_notes);
        ArrayList<String> cashier_a = Cashier.getSummaryNotes(machine_notes);
        assertEquals(a,e);
        assertEquals(cashier_a,e);
    }

    @Test
    public void GetSummaryNotesTest1(){
        LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(999999,99999,9999,999,99,9,0,9999,999,99,9);
        ArrayList<String> e = new ArrayList<>();
        e.add("100$: 999999");
        e.add("50$: 99999");
        e.add("20$: 9999");
        e.add("10$: 999");
        e.add("5$: 99");
        e.add("2$: 9");
        e.add("1$: 0");
        e.add("50c: 9999");
        e.add("20c: 999");
        e.add("10c: 99");
        e.add("5c: 9");
        ArrayList<String> a = Payment.GetSummaryNotes(machine_notes);
        ArrayList<String> cashier_a = Cashier.getSummaryNotes(machine_notes);
        assertEquals(a,e);
        assertEquals(cashier_a,e);

    }

    @Test
    // To test create notes in Payment, since we also have CreateMachineNotes in PaymentTest.java
    // We directly test these two together
    public void CreateNotesTest(){
        LinkedHashMap<String,Integer> expected1 = CreateMachineNotes(13,14,15,16,17,18,19,20,19,18,17);
        LinkedHashMap<String,Integer> expected2  = Payment.CreateNotes(13,14,15,16,17,18,19,20,19,18,17);
        LinkedHashMap<String,Integer> actual = new LinkedHashMap<>();
        actual.put("100$",13);
        actual.put("50$",14);
        actual.put("20$",15);
        actual.put("10$",16);
        actual.put("5$",17);
        actual.put("2$",18);
        actual.put("1$",19);
        actual.put("50c",20);
        actual.put("20c",19);
        actual.put("10c",18);
        actual.put("5c",17);
        assertEquals(expected1,actual);
        assertEquals(expected2,actual);
    }

    @Test
    // Test Verify Card in Payment
    public void VerifyCardTest(){
        // Try correct input value(name,number)::
        assertTrue(Payment.verifyCard("Christine","35717"));
        assertTrue(Payment.verifyCard("Vincent","59141"));
        assertTrue(Payment.verifyCard("Blake","14138"));
        assertTrue(Payment.verifyCard("Audrey","45925"));

        // Try incorrect input value(name,number):
        assertFalse(Payment.verifyCard("Zelin","24521"));
        // Try some edge case:
        assertFalse(Payment.verifyCard("",""));
        assertFalse(Payment.verifyCard("12344sffa","ascave"));
        // Try some special symbol
        assertFalse(Payment.verifyCard("!@$^&$Aaa","1452&*)"));
    }

    @Test
    // Test whether cashier can setup the machine notes;
    // Normal input(correct format)
    public void SetMachineNotesTest(){
        try {
            LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(2, 3, 4, 5, 3, 7, 2, 7, 3, 0, 2);
            Cashier.SetMachineNotes(machine_notes, "50$","10");
            Cashier.SetMachineNotes(machine_notes,"10c","20");
            Cashier.SetMachineNotes(machine_notes,"100$","0");
            Cashier.SetMachineNotes(machine_notes,"5$","10");
            LinkedHashMap<String,Integer> expected = CreateMachineNotes(0,10,4,5,10,7,2,7,3,20,2);
            assertEquals(machine_notes,expected);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    // Try some edge case that include incorrect input: "", "abc".
    public void SetMachineNotesTest1(){
        try {
            LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(2, 3, 4, 5, 3, 7, 2, 7, 3, 0, 2);
            Cashier.SetMachineNotes(machine_notes, "50$","");
        }catch (Exception e){
            assertEquals("Invalid quantity",e.getMessage());
        }
    }

    @Test
    // Edge case: notes_type is ""
    public void SetMachineNotesTest2(){
        try {
            LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(2, 3, 4, 5, 3, 7, 2, 7, 3, 0, 2);
            Cashier.SetMachineNotes(machine_notes, "","");
        }catch (Exception e){
            assertEquals("Please choose a value",e.getMessage());
        }
    }

    @Test
    // Test if input quantity is less than 0
    public void SetMachineNotesTest3(){
        try {
            LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(2, 3, 4, 5, 3, 7, 2, 7, 3, 0, 2);
            Cashier.SetMachineNotes(machine_notes, "20$","-10");
        }catch (Exception e){
            assertEquals("Invalid quantity",e.getMessage());
        }
    }

    @Test
    // Test if we input quantity with symbols
    public void SetMachineNotesTest4(){
        try {
            LinkedHashMap<String, Integer> machine_notes = CreateMachineNotes(2, 3, 4, 5, 3, 7, 2, 7, 3, 0, 2);
            Cashier.SetMachineNotes(machine_notes, "20$","#20^");
        }catch (Exception e){
            assertEquals("Invalid quantity",e.getMessage());
        }
    }
}
