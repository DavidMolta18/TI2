package main;

import java.util.Scanner;

public class Main{

    Scanner sc = new Scanner(System.in);
    public static void main(String[]args){
        Main m = new Main();
        m.menu();
    }


    public void menu(){
        int option = 1;
        while (option !=0){

            System.out.println("Please select an option: " + "\n");
            option = sc.nextInt();
            sc.nextLine();


            switch(option){
                case 1:
                    break;
                case 2:
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }





        }




    }
}
