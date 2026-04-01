package com.atm;

import java.util.Scanner;
/*
 database- table
 name
 email
 account_number
 phone_number
 balance 
 pin
 branch
 */
public class AtmMain {

	public static void main(String[] args) throws ClassNotFoundException {
		
		Class.forName("org.postgresql.Driver");
		Scanner scanner=new Scanner(System.in);
		//Upcasting
		IAtm atm=new AtmImplementation();
		while(true) {
			
		System.out.println("WELCOME TO ATM PROJECT");
		System.out.println("1.CREATE ACCOUNT \n2.DEPOSIT \n3.WITHDRAW \n4.CHECK BALANCE \n5.UPDATION \n6.VIEW ACCOUNT DETAILS \n7.DELETE ACCOUNT \n8.EXIT");
		System.out.println("CHOOSE AN OPTION");
		int option=scanner.nextInt();
		
		switch (option) {
		case 1:
			atm.createAccount();
			break;
        case 2:
			atm.depositAmount();
			break;
        case 3:
			atm.withdrawAmount();
			break;
         case 4:
			atm.checkBalance();
			break;
         case 5:
 			atm.updation();
 			break;
 		 case 6:
 			atm.viewAccountDetails();	
 				break;
 		 case 7:
 			atm.deleteAccount();	
 			   break;
 	     case 8:
 				System.out.println("THANK YOU...");
 				System.exit(0);
 			break;
		default:
			break;
		}
		}
	}
}
