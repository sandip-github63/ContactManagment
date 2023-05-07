package com.contact.User;

public class updateUser {
	
	private String old_password;
	private String new_password;
	private String new_image;
	
	
	public String getOld_password() {
		return old_password;
	}
	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}
	public String getNew_password() {
		return new_password;
	}
	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	public String getNew_image() {
		return new_image;
	}
	public void setNew_image(String new_image) {
		this.new_image = new_image;
	}
	public updateUser(String old_password, String new_password, String new_image) {
		super();
		this.old_password = old_password;
		this.new_password = new_password;
		this.new_image = new_image;
	}
	
	public updateUser() {
		
		
	}
	@Override
	public String toString() {
		return "updateUser [old_password=" + old_password + ", new_password=" + new_password + ", new_image="
				+ new_image + "]";
	}
	
	
}
