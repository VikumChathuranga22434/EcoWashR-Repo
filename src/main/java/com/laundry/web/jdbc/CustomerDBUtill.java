package com.laundry.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class CustomerDBUtill {
	
	private DataSource dataSource;
	
	public CustomerDBUtill (DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public boolean validateUser(customer theCustomer) throws Exception {
	
			//get customer details
			boolean customerSts = false;
				
			//Get connection to the database
			Connection myConn = null;
			PreparedStatement myStmt = null;
			ResultSet myRs = null;
			
			try {
				
				//get a connection
				myConn = dataSource.getConnection();
				
				//create sql connection
				String sql = "select * from web_laundry_service.customer where email=?";
				
				//preparing the statement for the query
				myStmt = myConn.prepareStatement(sql);
				
				//Setting value for the statement
				myStmt.setString(1, theCustomer.getEmail());
				
				//execute the sql query
				myRs = myStmt.executeQuery();
				
				//process result set
				while (myRs.next()) {
					int customerId = myRs.getInt("id");
					String userName = myRs.getString("username");
					String Email = myRs.getString("email");
					String UserPass = myRs.getString("Password");
					int userType = myRs.getInt("userType");
						
					customerSts = theCustomer.getPassword().equals(UserPass);
						
				}
				
				
			}catch (Exception exc) {
				exc.printStackTrace();
			}finally {
				//close JDBC object
				close(myConn, myStmt, myRs);
			}
			
			return customerSts;
			
	}

	private void close(Connection myConn, PreparedStatement myStmt, ResultSet myRs) {
		
		try {
					
				if(myRs != null) {
					myRs.close();
				}if (myStmt != null) {	
					myStmt.close();	
				}if (myConn != null) {	
					myConn.close(); // doesn't really close it ... just put back in to connection pool	
				}	
				
			}catch (Exception exc) {	
				exc.printStackTrace();	
			}
		
	}

	public int getCustomerType(String email, String password) {

		//declaring the connection variable for the connection
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
				
		//return value
		int userStatus = 0;
		 
		//getting the parameter from the servlet
		String userEmail = email;
		String userPass = password;
				
		try {
					
			//get the db connection
			myConn = dataSource.getConnection();
			
			//create sql query
			String sql = "select * from web_laundry_service.customer where email=? and password=?";
			
			myStmt = myConn.prepareStatement(sql);
			
			//preparing the statement
			myStmt.setString(1, userEmail);
			myStmt.setString(2, userPass);
			
			//execute the query
			myRs = myStmt.executeQuery();
					
			while(myRs.next()) {
				int customerId = myRs.getInt("id");
				String userName = myRs.getString("username");
				String Email = myRs.getString("email");
				String UserPass = myRs.getString("Password");
				int userType = myRs.getInt("userType");
				
				userStatus = userType; 
				
			}
					
		}catch (Exception exc){
			exc.printStackTrace();
		}
				
		
		return userStatus;
	}

	public void addCustomer(customer tempCustomer) throws Exception {
		
		//declaring the connection pool variables and etc
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			
			//get database connection
			myConn = dataSource.getConnection();
			
			//Create the sql query for the insert operation
			String sql = "INSERT INTO web_laundry_service.customer (username, password, email) VALUES (?, ?, ?);";
			
			//Preparing the statement
			myStmt = myConn.prepareStatement(sql);
			
			//Set value for the query
			myStmt.setString(1, tempCustomer.getName());
			myStmt.setString(2, tempCustomer.getPassword());
			myStmt.setString(3, tempCustomer.getEmail());
			
			//execute the statement
			myStmt.execute();
									
			
		}catch(Exception exc) {
			
			exc.printStackTrace();
			
		}finally {
			
			//close the JDBC
			close(myConn, myStmt, null);
			
		}
				
	}

	public List<customer> getDetails(String email) throws Exception {
		
//		customer theCustomer = null;

		//Declaring the connection variables and others
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
//		String Email = null;
		
		//declarinthe array list to get the customer details
		List<customer> tempCustomer = new ArrayList<>();
		
		try {
			
			//get email for the parameter
//			Email = email;
			
			//get the data base connection
			myConn = dataSource.getConnection();
			
			//declaring the statement variable
			String sql = "select * from web_laundry_service.customer where email=?";
			
			//preparing the statement
			myStmt = myConn.prepareStatement(sql);
			
			// set the value for the statement
			myStmt.setString(1, email);
			
			//execute the statement
			myRs = myStmt.executeQuery();
			
			while(myRs.next()) {
				
				//getting the customer details
				int customerID = myRs.getInt("id");
				String customerName = myRs.getString("name");
				String email1 = myRs.getString("email");
				int userType = myRs.getInt("userType");
				
				//create a new customer obj
				customer theCustomer = new customer(customerID, customerName, email1, userType);
				
				//add the elements to the list
				tempCustomer.add(theCustomer);
				
			}
			
		}catch(Exception exc) {
			exc.printStackTrace();
		}
		finally {
			//close the JDBC
			close(myConn, myStmt, myRs);
		}
		
		return tempCustomer;

	}

	public customer getCustomer(int cusID) throws Exception {
		
		//create a customer obj variable
		customer theCustomer = null;
		
		//create the variables for the connection pool
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		//get the email
		int customerID;
		
		//create a try catch block
		try { 
			
			//Convert the id to a string
			customerID = cusID;
			
			//create the database connection
			myConn = dataSource.getConnection();
			
			//write the query
			String sql = "select * from web_laundry_service.customer where id=?";
			
			//prepare the statement
			myStmt = myConn.prepareStatement(sql);
			
			//set value for the statement
			myStmt.setInt(1, customerID);
			
			//execute the query
			myRs = myStmt.executeQuery();
			
			//if return data from the myRs
			if(myRs.next()) {
				
				int CustomerID = customerID;
				String customerName = myRs.getString("username");
				String cusEmail = myRs.getString("email");
				
				//Following data add to a customer obj
				theCustomer = new customer(CustomerID, customerName, cusEmail);
				
			}else {
				throw new Exception("Couldn't find user according tho the id : " + customerID);
			}
			
			return theCustomer;
			
		}finally {
			//close the JDBC connection
			close(myConn, myStmt, myRs);
		}
		
	}
	
	public int getCustomerDet(String email) throws Exception {
		
		//get the email from the update servlet 
		String Email = email;
//		int id1 = 0;
		int id1 = 0;
		
		//get the database connection variables
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		//create the customer Arraylist variable
		List<customer> theCustomer = new ArrayList<>();
		
		try {
			
			//create the connection
			myConn = dataSource.getConnection();
			
			//write and prepare the statement
			String sql = "select * from web_laundry_service.customer where email=?";
			
			//preparing the statement
			myStmt = myConn.prepareStatement(sql);
			
			//set the parameters to the query
			myStmt.setString(1, Email);
			
			//execute the query
			myRs = myStmt.executeQuery();
			
			//get the result set and assign it in to a customer object
			if (myRs.next()) {
				id1 = myRs.getInt("id");
				
			}
			
			return id1;
			
		}finally {
			//close the JDBC
			close(myConn, myStmt, myRs);
		}
		
	}
	
	public int getCustomerID(String email, String password) throws Exception {
			
			//get the email
			String Email =  email;
			String Pass = password;
			
			//create the variables for the connection pool
			Connection myConn = null;
			PreparedStatement myStmt = null;
			ResultSet myRs = null;
			
			//create a try catch block
			try { 
				
				//create the dataabse connection
				myConn = dataSource.getConnection();
				
				//write the query
				String sql = "select * from customer where email=? and password=?";
				
				//prepare the statement
				myStmt = myConn.prepareStatement(sql);
				
				//set value for the statement
				myStmt.setString(1, Email);
				myStmt.setString(2, Pass);
				
				//execute the query
				myRs = myStmt.executeQuery();
				
				//if return data from the myRs
				if(myRs.next()) {
					
					int customerID = myRs.getInt("id");
	
					return customerID;
					
				}else {
					throw new Exception("Couldn't find user according tho the email : " + Email);
				}
				
			}finally {
				//close the JDBC connection
				close(myConn, myStmt, myRs);
			}
			
		}
	
	public void updateCustomer(customer tempCustomer) throws Exception {
	
		//get the connection variables
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		//do operation in the try catch block
		try {
			
			//create the connection
			myConn = dataSource.getConnection();
			
			//update query
			String sql = "update web_laundry_service.customer set username=?, email=? where id=?";
			
			//prepare the statement
			myStmt = myConn.prepareStatement(sql);
			
			//setting the attributes
			myStmt.setString(1, tempCustomer.getName());
			myStmt.setString(2, tempCustomer.getEmail());
			myStmt.setInt(3, tempCustomer.getCustomerId());
			
			//execute the query
			myStmt.execute();
			
		}finally {
			//close the JDBC 
			close(myConn, myStmt, null);
		}
		
	}
	
	public void deleteStudent(int theCustomerId) throws Exception {
		
		//get the connection variables
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		//do works in here
		try {
			
			//converting the theCustomerId to the Integer
			int customerId = theCustomerId;
			
			//get the connection 
			myConn = dataSource.getConnection();
			
			//create the sql query
			String sql = "DELETE FROM web_laundry_service.customer WHERE id=?";
			
			//prepare the statement
			myStmt = myConn.prepareStatement(sql);
			
			//set the paramenter to the query
			myStmt.setInt(1, customerId);
			
			//execute the sql query
			myStmt.execute();
			
		}finally {
			//close JDBC
			close(myConn, myStmt, null);
		}
		
	}
}