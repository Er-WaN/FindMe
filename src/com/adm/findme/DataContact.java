package com.adm.findme;

public class DataContact {

	private String name;
	private String phoneNumber;
	
	public DataContact(){
		name = null;
		phoneNumber = null;
	}
	
	public DataContact(String name, String phoneNumber) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
