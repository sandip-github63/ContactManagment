package com.contact.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.contact.entities.Contact;
import com.contact.entities.User;

@Component
public interface ContactRepository  extends JpaRepository<Contact,Integer>{

	
	//fetch all contact by userid with pagination 
	
	@Query(" from Contact as c where c.user.id=:userId")
	public Page<Contact> getContactByUserId(@Param("userId") int userId,Pageable pageable);

	
	//pageable class 
	//page interface that have all infromation about pagination
	//total number of page 
	

	
	
	
	public List<Contact> findByNameContainingAndUser(String name,User user);
	
	


}
