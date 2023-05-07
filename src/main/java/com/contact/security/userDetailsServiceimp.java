package com.contact.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contact.dao.UserRepository;
import com.contact.entities.User;

public class userDetailsServiceimp implements UserDetailsService {

	 @Autowired
	 UserRepository userRepo;
	 
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//authentication is done by using database
		 User user= userRepo.getEmail(username);//fetch user from database
		
		 if(user==null) {
			throw new UsernameNotFoundException("could not found user ");
			
		 }
		 
		 userDetailsimpl userDetailsimpl = new userDetailsimpl(user);
		 
		
		return userDetailsimpl;
		
	}
	

}
