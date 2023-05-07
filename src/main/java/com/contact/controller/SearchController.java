package com.contact.controller;

import java.security.Principal;
import java.util.List;

//import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contact.dao.ContactRepository;
import com.contact.dao.UserRepository;
import com.contact.entities.Contact;
import com.contact.entities.User;

@RestController
public class SearchController {	
	@Autowired
	ContactRepository contactRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchContact(@PathVariable("query") String q,Principal principal){
		
		//return json object 
		
		//we have to search only current login user
		
	    String email=principal.getName();
	    
	    System.out.println("principal="+principal.getName());
	  
	     User user= userRepo.getEmail(email);
	     System.out.println(user);
	     int userid=user.getId();
	     
	     List<Contact> contact= contactRepo.findByNameContainingAndUser(q,user);
	     System.out.println(contact);
	     System.out.println(q);
	     
		 return ResponseEntity.ok(contact);
		 
		 
		 
	}
}
