package com.contact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

//import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RequestMapping;

import com.contact.dao.UserRepository;
import com.contact.entities.User;
import com.contact.messager.Message;

@org.springframework.stereotype.Controller

public class Controller {
	
	 @Autowired
	 private BCryptPasswordEncoder bcryptpassword;
	
	//creating object of UserRepostiory 
	  @Autowired
	  UserRepository userRepo;
	
	  @GetMapping("/")
	  public String home(Model model) {
		  model.addAttribute("title","home page");
		  
		  
		  return "home";
	  }
	  
	  @GetMapping("/about")
	  public String About(Model model) {
		  model.addAttribute("title","about page");
		  return "about";
	  }
	  
	  
	  @GetMapping("/signup")
	  public String singnup(Model model) {
		 // model.addAttribute("title","singnup page");
		  model.addAttribute("user",new User());
		  return "signup";
	  }
	  
	  //Register 
	  
	  
	  @PostMapping("/register")
	  public String register(@Valid @ModelAttribute("user") User user,BindingResult  result,HttpSession session) {
		
		 
		 
		 
		  try {
			  if(user.isCheck()==false) {
				  //user not  accepted terms and condition 
				  System.out.println("user did not accepted terms and conditon");
				  throw  new Exception("please accept terms and condition");
				  
			  }
			  
			  if(result.hasErrors()) {
				  return "signup";
			  }
			  
			
			  
			  //this is run when user accepted terms and condtion
			  
			  
			 user.setEnabled(true);
			 user.setRole("ROLE_USER");
			 user.setImageUrl("default.png");
			 user.setPassword(bcryptpassword.encode(user.getPassword()));
			 
			 
			 //stored user into database
			
			 User newuser=userRepo.getEmail(user.getEmail());
			 //System.out.println(newuser);
			 if(newuser==null) {
				 
				 userRepo.save(user);
				 
				 session.setAttribute("response",new Message("successfully registered","alert-primary"));
				  return "signup";		
				 
			 }else {
				 session.setAttribute("response",new Message("email id is already taken","alert-danger"));
				return "signup"; 
			 }			  
		  }	  
		   catch(Exception e) {	  
			  String message=e.getMessage();			   
			  session.setAttribute("response",new Message(message,"alert-danger"));
			  return "signup";
		  }
		 
	    }
	  
	
	  
	  //custom login page
	  
	  @GetMapping("/login")
	  public String customLoginPage(Model model) {
		  model.addAttribute("title","login Page");
		  
		  return "login";
		  
	  }
	  
	  //when user got login successfully
	  
	  
	  @PostMapping("/loginProcess")
	  public String loginProcess() {
		  return "normal/user_dashboard";
	  }
	  

	  
}
