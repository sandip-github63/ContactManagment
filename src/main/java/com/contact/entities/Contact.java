package com.contact.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="contact")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	@Column(length = 20)	
	@NotBlank(message = "name must not be blank")
	@Size(min =3 ,max = 15,message = "name must between 3-15 character")
	private String name;
	
	@Column(length = 20)
	@Size(max=15,message = "name too long")
	private String nickName;
	
	private String work;
	
	@Column(unique = true,length=40)
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "invalid email")
	private String email;
	
	@Column(name = "image" ,columnDefinition = "varchar(50) default 'sandeep.png' ")
	private String image;
	@Size(min=10,max=12 ,message = "phone number must between 10 to 12 digit")
	@Column(length =12)
	private String phone;
	@Column(length=2000)
	private String discription;
	
	
	@ManyToOne
	@JoinColumn(name="uid")
	@JsonIgnore //ignore json this user
	private User user;
	
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public Contact(String name, String nickName, String work, String email, String image, String phone,
			String discription) {
		super();
		this.name = name;
		this.nickName = nickName;
		this.work = work;
		this.email = email;
		this.image = image;
		this.phone = phone;
		this.discription = discription;
	}
	public Contact() {
		
		
	}
	
	@Override
	public String toString() {
		return "Contact [cid=" + cid + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", email="
				+ email + ", image=" + image + ", phone=" + phone + ", discription=" + discription + ", user=" + user
				+ "]";
	}
	
	
	
}
