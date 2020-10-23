package com.company;
import java.io.*;
//This is P9.5
import com.sun.source.tree.AssignmentTree;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.Test;
import junit.framework.TestCase;

public class Main {
    abstract static class Appointment{//the parent class
        LocalDate Date;
        String Description;

        Appointment(){
            Date = null;
            Description = null;
        }

        Appointment(String Inputdate, String description){//convert strings into date and description
            try{
                Date = LocalDate.parse(Inputdate);
            }
            catch(Exception e){
                System.out.println("unable to initialize the time, Using the current time as default");
                Date = LocalDate.now();//if error take the current time
            }
            Description = description;
        }

        boolean equal(Appointment obj){//see if two Appointments are equal
            boolean see = Description.equals(obj.Description);
            if(Date.equals(obj.Date) && Description.equals(obj.Description)){
                return true;
            }
            else{
                return false;
            }
        }

        void setLocalDate(String Inputdate){
            try{
                Date = LocalDate.parse(Inputdate);
            }
            catch(Exception e){
                System.out.println("unable to initialize the time, Using the current time as default");
                Date = LocalDate.now();
            }
        }

        void setDescription(String description){//set description
            Description = description;
        }

        String getDay(){//return days
            if(Date.getDayOfMonth() >= 10){
                String value = Integer.toString(Date.getDayOfMonth());
                return value;
            }
            else{
                String value = "0" + Integer.toString(Date.getDayOfMonth());
                return value;
            }
        }

        int getYear(){//return years
            return Date.getYear();
        }

        int getMounth(){//return mounths
            return Date.getMonthValue();
        }

        String ShowDescription(){//return the description
            return Description;
        }

        abstract void Show(String inputdate);//show the output message

        abstract char ShowType();//return types

        public abstract boolean occursOn(String input);//the abstract class that would be implemented later
    }

    static class Onetime extends Appointment{
        private char type = 'O';
        Onetime(String Inputdate, String description){//constructor
            super(Inputdate, description);
        }

        Onetime(){//default constructor
            super();
        }


        public boolean occursOn(String input) throws DateTimeException{
            LocalDate Date = LocalDate.parse(input);
            if(Date.equals(super.Date)) {//if the date equal
                return true;
            }
            else{
                return false;
            }
        }

        void Show(String inputdate){
            System.out.println("Onetime appoinment " + inputdate + " with such description: " + super.ShowDescription());
        }

        char ShowType(){ return type; }
    }

    static class Daily extends Appointment{
        private char type = 'D';
        Daily(String Inputdate, String description){
            super(Inputdate, description);
        }

        Daily(){//default constructor
            super();
        }

        public boolean occursOn(String input) throws DateTimeException {
            LocalDate Date = LocalDate.parse(input);
            if(Date.compareTo(super.Date) >= 0){// if the date is larger than the compared date
                return true;
            }
            else{
                return false;
            }
        }

        void Show(String inputdate){
            System.out.println("Daily appoinment " + inputdate + " with such description: " + super.ShowDescription());
        }

        char ShowType(){ return type; }
    }

    static class Monthly extends Appointment{
        private char type = 'M';
        Monthly(String Inputdate, String description){
            super(Inputdate, description);
        }

        Monthly(){//default constructor
            super();
        }

        public boolean occursOn(String input)throws DateTimeException{
            LocalDate Date = LocalDate.parse(input);
            if(Date.compareTo(super.Date) >= 0 && Date.getDayOfMonth() == Integer.parseInt(super.getDay())){//if date is larger and days are the same
                return true;
            }
            else{
                return false;
            }
        }

        void Show(String inputdate){
            System.out.println("Monthly appoinment " + inputdate + " with such description: " + super.ShowDescription());
        }

        char ShowType(){ return type; }
    }

    public static Appointment ChooseTypes(char input){//function return the according object and ask the user to redo if error
        int i = 0;
        while (i == 0){
            if (input == 'O'){
                Onetime obj = new Onetime();
                return obj;
            }
            else if(input == 'D'){
                Daily obj = new Daily();
                return obj;
            }
            else if(input == 'M'){
                Monthly obj = new Monthly();
                return obj;
            }
            else{
                System.out.println("Not an option, please try again");//ask the user to do it again
                System.out.print("Enter the type (O-Onetime, D-Daily, or M-Monthly): ");
                Scanner Userin = new Scanner(System.in);
                String inputstr = Userin.nextLine();
                input = inputstr.charAt(0);
            }
        }
        return null;//never reach
    }

    static String AddString(String[] Mylist){// adding string together
        String ReturnValue = "";
        for(int i = 1; i < Mylist.length - 1; i++){
            if(i == 1){
                ReturnValue =Mylist[i];
            }
            else{
                ReturnValue = ReturnValue + " " + Mylist[i];
            }
        }
        return ReturnValue;
    }

    public static void save(String file, Appointment appointment) throws IOException{//save to file
        File Fileobj = new File(file);
        FileWriter fwriter = new FileWriter(file, Fileobj.exists());
        PrintWriter fout = new PrintWriter(fwriter);
        fout.println(appointment.ShowType() + " " + appointment.ShowDescription() + " " + appointment.getYear() + "-" + appointment.getMounth() + "-" + appointment.getDay());
        fout.close();
    }

    public static ArrayList<Appointment> load(String fileName) throws FileNotFoundException{//load from file
        ArrayList<Appointment> MyAppointment = new ArrayList<Appointment>();
        File Fileobj = new File(fileName);
        while(!Fileobj.exists()){//check if exists
            System.out.println("Such file does not exist, Please re-enter filename or press Q to exit.");
            Scanner Userin = new Scanner(System.in);
            fileName = Userin.nextLine();
            if(fileName.equals("Q")){
                System.exit(5);
            }
            Fileobj = new File(fileName);
        }
        Scanner fin = new Scanner(Fileobj);
        while(fin.hasNextLine()){
            String content = fin.nextLine();
            String strarry[] = content.split(" ");
            String description = null;
            if(strarry[0].equals("O")){//when Onetime obj
                description = AddString(strarry);
                Onetime obj = new Onetime(strarry[strarry.length - 1], AddString(strarry));
                MyAppointment.add(obj);
            }
            else if(strarry[0].equals("D")){//when Daily obj
                Daily obj = new Daily(strarry[strarry.length - 1], AddString(strarry));
                MyAppointment.add(obj);
            }
            else if(strarry[0].equals("M")){//when Monty obj
                Monthly obj = new Monthly(strarry[strarry.length - 1], AddString(strarry));
                MyAppointment.add(obj);
            }
        }
        System.out.println("Load successfully");
        fin.close();
        return MyAppointment;
    }

    static void OccursOn(ArrayList<Appointment> MyAppointment, String inputdate){// function occurs on
        Boolean NothingToday = false;
        for(Appointment i : MyAppointment){//go through the whole array and compare everything
            if(i.occursOn(inputdate)){
                i.Show(inputdate);
                NothingToday = true;
            }
        }
        if(!NothingToday){//if it is false until the complete iteration, give the user nothing happened information
            System.out.println("nothing special at date " + inputdate);
        }
    }

    public static class Testclass extends TestCase {//Testclass
        void TestoccursOn(){//checking occursOn
            ArrayList<Appointment> MyAppointment = new ArrayList<Appointment>();
            Daily Dobj = new Daily("2020-10-15", "Daily test");
            Onetime Oobj = new Onetime("2020-10-15", "Onetime test");
            Monthly  Mobj = new Monthly("2020-10-15", "Monthly test");
            MyAppointment.add(Dobj);
            MyAppointment.add(Oobj);
            MyAppointment.add(Mobj);
            OccursOn(MyAppointment, "2020-10-15");
            OccursOn(MyAppointment, "2020-11-15");
            OccursOn(MyAppointment, "2020-12-05");
            OccursOn(MyAppointment, "2020-09-15");
        }

        void Testsavingfile() throws IOException {//checking save function
            Daily Dobj = new Daily("2020-10-15", "Daily test");
            Onetime Oobj = new Onetime("2020-10-15", "Onetime test");
            Monthly  Mobj = new Monthly("2020-10-15", "Monthly test");
            String filename = "appointments.txt";
            save(filename, Dobj);
            save(filename, Oobj);
            save(filename, Mobj);
            System.out.println("File added and saved");
        }

        void Testloadingfile() throws IOException {//checking load function
            ArrayList<Appointment> SaveAppointment = new ArrayList<Appointment>();
            Daily Dobj = new Daily("2020-10-15", "Daily test");
            Onetime Oobj = new Onetime("2020-10-15", "Onetime test");
            Monthly  Mobj = new Monthly("2020-10-15", "Monthly test");
            SaveAppointment.add(Dobj);
            SaveAppointment.add(Oobj);
            SaveAppointment.add(Mobj);
            String filename = "SavedApps.txt";
            save(filename, Dobj);
            save(filename, Oobj);
            save(filename, Mobj);
            ArrayList<Appointment> LoadAppointment = load("appointments.txt");
            assertEquals("Test loading number correct", SaveAppointment.size(), LoadAppointment.size());//if equal
            for(int i = 0; i < LoadAppointment.size(); i++){//compare everthing in the saved and loaded array
                if(LoadAppointment.get(i).equal(SaveAppointment.get(i))){
                    System.out.println("The " + i + " pos matches");
                }
                else{
                    System.out.println("The " + i + " pos does not match");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Testclass mytest = new Testclass();
        mytest.TestoccursOn();
        mytest.Testsavingfile();
        mytest.Testloadingfile();
        ArrayList<Appointment> MyAppointment = new ArrayList<Appointment>();//arraylist
        boolean keeprunning = true;
        do{
            System.out.print("Select an option: S for saving appointments to file, L for loading assignment, C for checking, Q to quit: ");
            Scanner Userin = new Scanner(System.in);
            String input = Userin.nextLine();
            if(input.charAt(0) == 'S'){
                ArrayList<Appointment> TempAppointment = new ArrayList<Appointment>();
                System.out.print("Please enter the file name: ");
                String filename = Userin.nextLine();
                do{
                    System.out.print("Enter the type (O-Onetime, D-Daily, or M-Monthly) or Q to quit: ");
                    input = Userin.nextLine();
                    if(input.charAt(0) == 'Q') {
                        break;
                    }
                    Appointment obj = ChooseTypes(input.charAt(0));
                    System.out.print("Enter the date (yyyy-mm-dd): ");
                    input = Userin.nextLine();
                    obj.setLocalDate(input);
                    System.out.print("Enter the description: ");
                    input = Userin.nextLine();
                    obj.setDescription(input);
                    TempAppointment.add(obj);
                }while(!input.equals("Q"));
                if(TempAppointment.size() > 0){
                    for(Appointment i : TempAppointment){//go through the whole array and compare everything
                        save(filename, i);
                    }
                }
            }
            else if(input.charAt(0) == 'L'){
                System.out.print("Please enter the file name: ");
                String filename = Userin.nextLine();
                MyAppointment = load(filename);
            }
            else if(input.charAt(0) == 'C'){//If C, check
                if(MyAppointment.size() == 0){//When there is nothing
                    System.out.println("There is no appointment at all");
                }
                else{//else input everything
                    System.out.print("Enter the year: ");
                    input = Userin.nextLine();
                    System.out.print("Enter the month: ");
                    //input = input + "-" + Userin.nextLine();
                    String temp = Userin.nextLine();
                    if(Integer.parseInt(temp) > 9){
                        input = input + "-" + temp;
                    }
                    else{//when number is less then 10
                        input = input + "-0" + Integer.parseInt(temp);
                    }
                    System.out.print("Enter the day: ");
                    temp = Userin.nextLine();
                    if(Integer.parseInt(temp) > 9){
                        input = input + "-" + temp;
                    }
                    else{//when number is less then 10
                        input = input + "-0" + Integer.parseInt(temp);
                    }
                    OccursOn(MyAppointment, input);
                }
            }
            else if(input.charAt(0) == 'Q'){//quit
                System.exit(7);
            }
            else{
                System.out.println("Not an option, please try again");
            }
        }while(keeprunning);
    }
}
