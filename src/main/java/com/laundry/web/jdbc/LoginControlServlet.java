package com.laundry.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class LoginControlServlet
 */
@WebServlet("/LoginControlServlet")
public class LoginControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CustomerDBUtill customerDbUtill;
	
	//define the datasource/connection pool for resource injection
	@Resource(name="jdbc/web_laundry_service")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();
		
		//create our customer Db util ... and pass in the conn pool/ dataSource
		try {
			customerDbUtill = new CustomerDBUtill(dataSource);
		}catch (Exception exc) {
			throw new ServletException(exc);
		}
	
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		try {
			
			//read the command parameter
			String theCommand = request.getParameter("command");
			
			//route the appropriate method
			switch(theCommand) {
			
			case "LOGIN" : 
				//request for fill the login form
				validateUser(request, response);
				break;
				
			case "REGISTER" :
				addCustomer(request, response);
				break;
				
			}
			
			
		}catch (Exception exc) {
			throw new ServletException(exc);
		}
		
		try {
			String theCommand = request.getParameter("command");
			if (theCommand != null && theCommand.equals("LOAD")) {
				loadCustomer(request, response);
			} else {
				// Handle other commands as needed.
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			// Log the exception details.
		}
		
		
	}


	private void loadCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// getting the user email from the jsp page
		String email = request.getParameter("updatedEmail");

		// get the customer details from the db
		int customerId = customerDbUtill.getCustomerDet(email);
		
		// setting the attribute
		request.setAttribute("CUS_ID", customerId);

		// forwarding the request for the userprofile.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("userprofile.jsp");
		dispatcher.forward(request, response);

	}
	

	private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//Assigning the All value of the form to a customer obj
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("new-password");
		
		//create a new customer object
		customer tempCustomer = new customer(username, email, password);
		
		//pass the customer object to the dbUtill class
		customerDbUtill.addCustomer(tempCustomer);
		
		//send the user to the user profile
		viewUserDashBourd(request, response);
		
	}

	private void viewUserDashBourd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//get the customer email
		String email = request.getParameter("email");
		
		//get the customer details from the DB
		List<customer> Customer = customerDbUtill.getDetails(email);
		
		//add the details for the attribute
		request.setAttribute("CUS_DETAILS", Customer);
		
		//send the details for the user.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/user.jsp");
		dispatcher.forward(request, response);
		
	}

	private void validateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//Assigning the customer login details
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		//Assigning the value to the customer obj
		customer theCustomer = new customer(email, password);
		
		//get customer verification
		boolean customerSts = customerDbUtill.validateUser(theCustomer);
		
		//if the boolean value is true
		if (customerSts == true) {
			//forward the data to redirectUser
			redirectUser(request, response);
		}
		
		else {
			//ask to re enter user valid credentials
			askToEnterDetailsAgain(request, response);
		}
		
	}

	private void askToEnterDetailsAgain(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//Set the massage
		String invalid = "Invalid Username or Passsword";
		
		//set attribute
		request.setAttribute("INVALID", invalid);
		
		//forward the request details to the jsp page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
		dispatcher.forward(request, response);
		
	}

	private void redirectUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//setting the parameters
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		//getting the user details from the customerDbUtill
		int customerSts = customerDbUtill.getCustomerType(email, password);
		
		//get the student id
		int customerID = customerDbUtill.getCustomerID(email, password);
		
		//set email as the attribute
		request.setAttribute("CUS_EMAIL", email);
		request.setAttribute("CUS_ID", customerID);
		
		//redirect to the pages
		if (customerSts == 0) {
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/user.jsp");
			dispatcher.forward(request, response);
			
		}else if (customerSts == 1) {
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/admin.jsp");
			dispatcher.forward(request, response);
			
		}else {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
			dispatcher.forward(request, response);
		}
		
	}

}