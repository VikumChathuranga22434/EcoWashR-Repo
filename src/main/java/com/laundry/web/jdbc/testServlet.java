package com.laundry.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class testServlet
 */
@WebServlet("/testServlet")
public class testServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Define the dataSource/ connection pool for the resource injection
	@Resource(name="jdbc/web_laundry_service")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//step 1: Set up the print writer
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		//step 2: Get a connection to the database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
			myConn = dataSource.getConnection();
			
			//step 3: Create SQL Statement
			String sql = "select * from customer";
			myStmt = myConn.createStatement();
			
			//step 4: Execute SQl Statement
			myRs = myStmt.executeQuery(sql);
			
			//step 5: Process the resultset
			while(myRs.next()) {
				String email = myRs.getString("email");
				out.println(email);
			}
			
			
			
		}catch(Exception exc) {
			exc.printStackTrace();
		}
				
	}

}
