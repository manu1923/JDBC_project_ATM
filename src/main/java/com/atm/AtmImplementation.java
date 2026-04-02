package com.atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AtmImplementation implements IAtm{

	//first 2 steps are common to all
	//common method to create connection which can be reused for all CRUD operations 
	
	public Connection createConnection()  {
		Connection connection = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Bank","postgres","mannuu123");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	//insert operation-jdbc steps
	//user input
	Scanner scanner=new Scanner(System.in);
	public void createAccount() {
		Connection connection=createConnection();
		String query="insert into account values(?,?,?,?,?,?,?)";
		try {
			PreparedStatement  preparedStatement=connection.prepareStatement(query);
			//set data to delimeters -- by taking from user
			System.out.println("ENTER ACCOUNTNUMBER : ");
			preparedStatement.setString(1,scanner.next() );
			System.out.println("ENTER NAME : ");
			preparedStatement.setString(2,scanner.next() );
			System.out.println("ENTER EMAIL : ");
			preparedStatement.setString(3,scanner.next() );
			System.out.println("ENTER PHONENUMBER  : ");
			preparedStatement.setInt(4,scanner.nextInt() );
			System.out.println("ENTER BRANCH : ");
			preparedStatement.setString(5,scanner.next() );
//			System.out.println("ENTER BALANCE : ");  not ask user
			                                       //set balance zero 
			preparedStatement.setInt(6,0 );
			System.out.println("ENTER PIN : ");
			preparedStatement.setInt(7,scanner.nextInt() );
			
			int row=preparedStatement.executeUpdate(); //returns int value : number of rows that has been created
			if(row!=0) {
				System.out.println("ACCOUNT CREATED SUCCESSFULLY");
			}
			connection.close();
			
		}  catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void depositAmount() {
		Connection connection=createConnection();
		String query="update account set balance=balance+? where account_number=? and pin=?";
		
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			System.out.println("ENTER AMOUNT TO BE DEPOSITED : ");
			preparedStatement.setInt(1,scanner.nextInt() );
			System.out.println("ENTER ACCOUNTNUMBER : ");
			preparedStatement.setString(2,scanner.next() );
			System.out.println("ENTER PIN : ");
			preparedStatement.setInt(3,scanner.nextInt() );
			
			int rows=preparedStatement.executeUpdate();
			if(rows!=0) {
				System.out.println("DEPOSITION DONE");
			}
			else {
				System.out.println("INVALID ACCOUNT NUM OR PIN");
			}
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * balance>0 ---- insufficient balance
	 * amount
	 * accno and pin ---invalid accno or pin
	 * */
	
	public void withdrawAmount() {
		System.out.println("ENTER ACCOUNTNUMBER : ");
		String accountNumber=scanner.next();
		System.out.println("ENTER PIN : ");
		int pin=scanner.nextInt();
		System.out.println("ENTER AMOUNT TO BE WITHDRAW : ");
		int amount=scanner.nextInt();
		
		Connection connection=createConnection();
		
		String fetchBalanceQuery="select balance from account where account_number=? and pin=?";
		try {
			PreparedStatement fetchBalancePreparedStatement=connection.prepareStatement(fetchBalanceQuery);
			fetchBalancePreparedStatement.setString(1, accountNumber);
			fetchBalancePreparedStatement.setInt(2, pin);
			ResultSet resultSet=fetchBalancePreparedStatement.executeQuery();
			if(resultSet.next()) {
				
				int currentBalance=resultSet.getInt("balance");
				if(currentBalance>amount) {
					//do withdraw operation
				String withdrawQuery="update account set balance=balance-? where account_number=? and pin=?";
				PreparedStatement withdrawPreparedStatement=connection.prepareStatement(withdrawQuery);
				withdrawPreparedStatement.setInt(1, amount);
				withdrawPreparedStatement.setString(2, accountNumber);
				withdrawPreparedStatement.setInt(3, pin);
				
				withdrawPreparedStatement.executeUpdate();
				System.out.println("WITHDRAW SUCCESS,  REMAINING BALANCE IS : "+(currentBalance-amount));
				}else {
					System.out.println("INSUFFICIENT BALANCE");
				}
			}else {
				System.out.println("INVALID ACCOUNT NUMBER OR PIN");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkBalance() {
	
		Connection connection=createConnection();
		String balanceQuery="select balance from account where account_number=? and pin=?";
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(balanceQuery);
			System.out.println("ENTER ACCOUNTNUMBER : ");
			preparedStatement.setString(1,scanner.next() );
			System.out.println("ENTER PIN : ");
			preparedStatement.setInt(2,scanner.nextInt() );
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				System.out.println("YOUR CURRENT BALANCE IS : "+resultSet.getInt("balance"));
			}else {
				System.out.println("INVALID ACCOUNT NUMBER OR PIN");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updation() {
		System.out.println("1.NAME\n2.EMAIL\n3.PHONENUMBER\n4.BRANCH\n5.PIN");
		System.out.println("CHOOSE AN OPTION");
		int option=scanner.nextInt();
		
		Connection connection=createConnection();
		System.out.println("ENTER ACCOUNTNUMBER : ");
		String accountNumber=scanner.next();
		
		try {
			switch (option) {
			case 1://update name
				String updateName="update account set name=? where account_number=?";
				System.out.println("ENTER NEW NAME : ");
				String newName=scanner.next();
				PreparedStatement namePreparedStatement=connection.prepareStatement(updateName);
				namePreparedStatement.setString(1, newName);
				namePreparedStatement.setString(2, accountNumber);
				int row1=namePreparedStatement.executeUpdate();
				if(row1!=0) {
					System.out.println("NAME UPDATED");
				}else {
					System.out.println("INVALID ACCOUNT NUMBER");
				}
				break;
			case 2://update email
				String updateEmail="update account set email=? where account_number=?";
				System.out.println("ENTER NEW EMAIL : ");
				String newEmail=scanner.next();
				PreparedStatement emailPreparedStatement=connection.prepareStatement(updateEmail);
				emailPreparedStatement.setString(1, newEmail);
				emailPreparedStatement.setString(2, accountNumber);
				int row2=emailPreparedStatement.executeUpdate();
				if(row2!=0) {
					System.out.println("EMAIL UPDATED");
				}else {
					System.out.println("INVALID ACCOUNT NUMBER");
				}
				break;
			case 3://update phonenumber
				String updatePhone="update account set phone_number=? where account_number=?";
				System.out.println("ENTER NEW PHONENUMBER : ");
				int newPhone=scanner.nextInt();
				PreparedStatement phoneNumberPreparedStatement=connection.prepareStatement(updatePhone);
				phoneNumberPreparedStatement.setInt(1, newPhone);
				phoneNumberPreparedStatement.setString(2, accountNumber);
				int row3=phoneNumberPreparedStatement.executeUpdate();
				if(row3!=0) {
					System.out.println("PHONENUMBER UPDATED");
				}else {
					System.out.println("INVALID ACCOUNT NUMBER");
				}
				break;
			case 4: //update branch
				String updateBranch="update account set branch=? where account_number=?";
				System.out.println("ENTER NEW BRANCH : ");
				String newBranch=scanner.next();
				PreparedStatement branchPreparedStatement=connection.prepareStatement(updateBranch);
				branchPreparedStatement.setString(1, newBranch);
				branchPreparedStatement.setString(2, accountNumber);
				int row4=branchPreparedStatement.executeUpdate();
				if(row4!=0) {
					System.out.println("BRANCH UPDATED");
				}else {
					System.out.println("INVALID ACCOUNT NUMBER");
				}
				break;
			case 5://update	pin
				String updatePin="update account set pin=? where account_number=?";
				System.out.println("ENTER NEW PIN : ");
				int newPin=scanner.nextInt();
				PreparedStatement pinPreparedStatement=connection.prepareStatement(updatePin);
				pinPreparedStatement.setInt(1, newPin);
				pinPreparedStatement.setString(2, accountNumber);
				int row5=pinPreparedStatement.executeUpdate();
				if(row5!=0) {
					System.out.println("PIN UPDATED");
				}else {
					System.out.println("INVALID ACCOUNT NUMBER");
				}
				break;
			default:
					/* add acustom exception InvalidChoiceException*/
					try{
						throw new InvalidChoiceException("enter currect choice");
					}catch(InvalidChoiceException e){
						e.printStackTrace();
					}
				break;
			}
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void viewAccountDetails() {
		
		Connection connection=createConnection();
		String viewQuery="select * from account where account_number=?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(viewQuery);
			System.out.println("ENTER ACCOUNTNUMBER : ");
			preparedStatement.setString(1,scanner.next() );
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				System.out.println("-------- ACCOUNT DETAILS --------");
				System.out.println("ACCOUNT NUMBER : "+resultSet.getString("account_number"));
				System.out.println("NAME : "+resultSet.getString("name"));
				System.out.println("PHONENUMBER : "+resultSet.getString("phone_number"));
				System.out.println("EMAIL  : "+resultSet.getString("email"));
				System.out.println("BRANCH  : "+resultSet.getString("branch"));
			}
			else {
				System.out.println("INVALID ACCOUNT NUMBER");
			}
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void deleteAccount() {
		Connection connection=createConnection();
		String deleteQuery="delete from account where account_number=?";
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(deleteQuery);
			System.out.println("ENTER ACCOUNTNUMBER : ");
			preparedStatement.setString(1,scanner.next() );
			int row=preparedStatement.executeUpdate();
			if(row!=0) {
				System.out.println("ACCOUNT DELETED SUCCESSFULLY");
			}
			else {
				System.out.println("INVALID ACCOUNT NUMBER");
			}
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

