package com.contact.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.contact.entities.User;

@Component
public interface UserRepository extends JpaRepository<User,Integer> {

	
	@Query("select u from User u where u.email=:a")
	public User getEmail(@Param("a") String value);
	
	
	
}
