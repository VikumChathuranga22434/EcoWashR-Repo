package com.laundry.web.jdbc;

public class customer {
	
	private int customerId;
	private String name;
	private String email;
	private String password;
	private String checkPassword;
	private int userType;
	
	public customer(int customerId, String name, String email, String password, int userType) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.userType = userType;
	}

	public customer(int customerId, String name, String email) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.email = email;
	}



	public customer(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public customer(int customerId, String name, String email, int userType) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.email = email;
		this.userType = userType;
	}

	public customer(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getCheckPassword() {
		return checkPassword;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setCheckPassword(String checkPassword) {
		this.checkPassword = checkPassword;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public int getUserType() {
		return userType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "customer [name=" + name + ", email=" + email + ", password=" + password + ", userType=" + userType + "]";
	}

}
