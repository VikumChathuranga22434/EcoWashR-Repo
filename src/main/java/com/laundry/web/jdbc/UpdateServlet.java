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
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CustomerDBUtill customerDbUtill;

	// define the datasource/connection pool for resource injection
	@Resource(name = "jdbc/web_laundry_service")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		// create our customer Db util ... and pass in the conn pool/ dataSource
		try {
			customerDbUtill = new CustomerDBUtill(dataSource);
		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String theCommand = request.getParameter("command");
			if (theCommand != null && theCommand.equals("LOAD")) {
				loadCustomer(request, response);
			}else if(theCommand != null && theCommand.equals("UPDATE")){
				updateCustomer(request, response);
			}else if(theCommand != null && theCommand.equals("DELETE")){
				deleteCustomer(request, response);
			}
			else {
				// Handle other commands as needed.
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			// Log the exception details.
		}
	}

	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//getting the customerId
		int thecustomerID = Integer.parseInt(request.getParameter("customerID"));
		
		//delete user from the database
		customerDbUtill.deleteStudent(thecustomerID);
		
		// redirect to the user.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
		
	}

	private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//getting the email and the name
		int customerID = Integer.parseInt(request.getParameter("customerID"));
		String updateUsername = request.getParameter("updatedName");
		String updatedEmail = request.getParameter("updatedEmail");
		
		//forwarding to the customer utill class to update details of the customer 
		customer tempCustomer = new customer(customerID, updateUsername, updatedEmail);
		
		//forward to the tempCustomer
		customerDbUtill.updateCustomer(tempCustomer);
		
		//getting the updated customer details form the database
		customer theCustomer = customerDbUtill.getCustomer(customerID);
		
		//set Attribute for the request
		request.setAttribute("CUS_ID", theCustomer.getCustomerId());
		request.setAttribute("CUS_NAME", theCustomer.getName()); 
		request.setAttribute("CUS_EMAIL", theCustomer.getEmail()); 
		
		//redirect to the user.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("user.jsp");
		dispatcher.forward(request, response);
		
	}

	private void loadCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// getting the user email from the jsp page
		String email = request.getParameter("updatedEmail");

		// get the customer details from the db
		int customerId = customerDbUtill.getCustomerDet(email);

		//getting the customerdetails
		customer theCustomer = customerDbUtill.getCustomer(customerId);
		
		// setting the attribute
		request.setAttribute("CUS_ID", customerId);
		request.setAttribute("CUS_NAME", theCustomer.getName());
		request.setAttribute("CUS_EMAIL", theCustomer.getEmail());

		// forwarding the request for the userprofile.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("userprofile.jsp");
		dispatcher.forward(request, response);

	}
	

	

}
