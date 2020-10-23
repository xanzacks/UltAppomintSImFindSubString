package com.company;
//P13.9
import java.util.Scanner;

public class AssignmentP13_9 {
    static int pos = 0;
    static int function(String target, String find){//find pos of substring in a string
        if(find.length() <= target.length() && target.substring(0, find.length()).equals(find)){
            return pos;
        }
        else if(find.length() > target.length()){
            return -1;
        }
        pos++;
        return function(target.substring(1), find);//recursion
    }
    public static void main(String[] args){
        Scanner Userin = new Scanner(System.in);
        System.out.println("Please input the word that you would like to search in");//input the string to search in
        String target = Userin.nextLine();
        System.out.println("Please input the word that you would like to search for");//the substring
        String find = Userin.nextLine();
        int pos = function(target, find);
        if(pos == -1){//if found nothing
            System.out.println("Found nothing");
        }
        else{//return the position
            System.out.println("Found at pos " + pos);
        }
    }
}
