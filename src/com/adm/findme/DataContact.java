package com.adm.findme;

public class DataContact {

	private int id;
	private String name;
	private String phoneNumber;
	private boolean favorite;
	private boolean block;
	
	public DataContact(){
		name = null;
		phoneNumber = null;
		favorite = false;
		block = false;
	}
	
	public DataContact(String name, String phoneNumber, boolean favorite, boolean block) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.favorite = favorite;
		this.block = block;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
	
	public boolean getFavorite() {
		return favorite;
	}
	
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	
	public boolean getBlock() {
		return block;
	}
	
	public void setBlock(boolean block) {
		this.block = block;
	}
	
}
