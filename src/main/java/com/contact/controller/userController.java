package com.contact.controller;

import java.io.File;
import com.razorpay.*;
import java.io.IOException;
import com.razorpay.RazorpayException;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.contact.User.updateUser;
import com.contact.dao.ContactRepository;
import com.contact.dao.UserRepository;
import com.contact.entities.Contact;
import com.contact.entities.User;
import com.contact.filewriter.FileWrite;
import com.contact.messager.Message;


@Controller
@RequestMapping("/users")
public class userController {
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Autowired
	 UserRepository userRepo;
	
	@Autowired
	ContactRepository contactRepo;
	
	
	@Autowired
	FileWrite fw;
	
	@ModelAttribute
	public void globalmethodForAll(Model model,Principal principal) {
		
		String email=principal.getName();//return eamil address
		//System.out.println("email="+email);
		
		User user= userRepo.getEmail(email);
		model.addAttribute("user", user);
		
		
	}
	
	

	
	
	@GetMapping("/index")
	public String dashboard() {
		//when user got login successfully then spring security send 
		//username to this page and by the help of principal we can get that value
	    	return "normal/user_dashboard";
	}
	
	//when user add new contact
	@GetMapping("/add-contact")
	public String addContact(Model model) {
		 model.addAttribute("contact",new Contact());
			
		return "normal/add-contact";
	}
	
	
	//handle or process AddContact form 
	
	
	
	@PostMapping("/processContact")
	public String processAddContact(@Valid @ModelAttribute Contact contact,BindingResult result, Model model,Principal principal,@RequestParam("profile")  MultipartFile multipart,HttpSession session) throws IOException {
		//we are going to get user name from principal 
		model.addAttribute("contact",contact);
		
		
    //		
		
	   if(result.hasErrors()) {
		   return "normal/add-contact";
	   }
		//file upload into server from client 
	   
	   contact.setImage(multipart.getOriginalFilename());
	   
	   if(!contact.getImage().equals("")) {
		   
		   //if user upload file then it will run 
		   
		  boolean re= fw.saveFile(multipart);
		  if(re==true)
			  System.out.println("file uploaded successfully");
		  else
		  System.out.println("file not uploaded ");
		  
		  
	   }
	    
	   if(multipart.isEmpty()){
		   //file not send by user
		   //set default image		   
		   contact.setImage("default.png");		   
	   }
	    
		System.out.println(contact);
		
		String email=principal.getName();
		
		User user=userRepo.getEmail(email);
		contact.setUser(user);
		
		user.getContact().add(contact);
	
		System.out.println(contact);
		
		
		
        //contact object save into database
		try {
		 User u = userRepo.save(user);
		 if(u==null) {
			 new Exception("something went wrong , user did not saved");
			 
		 }
		}
		catch(DataIntegrityViolationException e) {
			session.setAttribute("response",new Message("email is already taken by someone","alert-danger"));
			return "normal/add-contact";
			
		}
		catch(Exception exc) {
			
			session.setAttribute("response",new Message("something went wrong","alert-danger"));
			exc.printStackTrace();
			return "normal/add-contact";
		}
		
		session.setAttribute("response",new Message("contact added successfully","alert-success"));
		return "normal/add-contact";
		
	}
	
	
	//handler for  viewContact 
	
	@GetMapping("/show-contact/{pageno}")
	 public String showViewContact(@PathVariable("pageno") int currentPage, Model model,Principal principal){
		//return email id of current user
		String email=principal.getName();
		
		 //get user by email 
		 User user = userRepo.getEmail(email);
		 
		 //Pageable parent and pageRequest child class of Pageable interface
		    int objectPerPage=4;
		   
		 
		  Pageable pageable = PageRequest.of(currentPage,objectPerPage);   
		   
		  Page<Contact> page=contactRepo.getContactByUserId(user.getId(),pageable);
		  model.addAttribute("title","viewPage");
		  
		   model.addAttribute("contact",page); //page ,currentPage,totalpage of pagination
		   model.addAttribute("currentpage",currentPage);//currentpageno 
		   model.addAttribute("totalpages",page.getTotalPages());//send total page 
		  
		
		 return "normal/viewContact";	
		 
		
	}	


	
	//hanlder for user can view their profile in detail
	
	
	@GetMapping("/view-profile/{id}")
	public  String showMyProfile(@PathVariable("id") int id,Model model,Principal principal) {		
		
		    Optional<Contact> con = contactRepo.findById(id);
		    Contact contact=con.get();
		    //fetch current user userid 
		    
		   String email= principal.getName();//return current user username ie email address
		    
		   //fetch user by email 
		   User user=userRepo.getEmail(email);
		   
		   //here we write logic that user can only see his contact list not other contact list 
		  
		   
		   //user id equals contact's table userid then it means contact is his .
		   if(user.getId()==contact.getUser().getId()) {
			   //user can see contact because it is his contact list 
			    model.addAttribute("title","show profile page");
			    model.addAttribute("contact",contact);
			    return "normal/show-profile";
			   
		   }else {
			   return "normal/show-profile";
			   
		   }
		   
	}
	
	
	//handler for delete contact
	
	@GetMapping("/delete-contact/{id}")
	public String deleteContactByID(@PathVariable("id") int id,Principal principal,HttpSession session) {
		//delete logic 
		
		   Optional<Contact> con= contactRepo.findById(id);
		   //fetch contact by id 
		   Contact contact=con.get();
		   
		   //before deleting first check whether this contact is his or not 
		  String email= principal.getName();
		  
		    User user= userRepo.getEmail(email);
		    
		    if(user.getId()==contact.getUser().getId()) {
		    	//valid user means this contact is his contact 
		    	//delete perform
		    	
		    	//we cannot delete contact directly because contact is depend on 
		    	//user because of cascade so first we have to delte user from user then delete contact 
		    	// so the idea is that we can assign userid of contact null in this way compiler see userid is null then it will not going to check cascade 
		    	
		    	
		          // contactRepo.delete(contact); not delete
		    	
		    	  //set userid of contact as null 
		    	
		    	    contact.setUser(null);
		    	    
		    	    try {
		    	   
		    	     contactRepo.delete(contact);
		    	     session.setAttribute("response",new Message("contact deleted successfully!!!","alert-success"));
		    	     return "redirect:/users/show-contact/0";		    	     
		    	     
		    	    }catch(IllegalArgumentException e) {
		    	    	session.setAttribute("response",new Message("something went wrong","alert-danger"));
		    	    	return "redirect:/users/show-contact/0";
		  		   		
		    	    	
		    	    }
		           
		    	
		    }else {
		    	//not valid user this contact is not his
		    	
		    	session.setAttribute("response",new Message("this is not your contact","alert-danger"));
	    	    
		    	return "redirect:/users/show-contact/0";
				
		    	
		    }
		   
		 
		
	}
	
	
	
	
	//handler to show update contact page
	
	@PostMapping("/update-contact/{id}")
	public String updateContact(@PathVariable("id") int id,Model model,HttpSession session) {
		
		//get user whose id have to update 
	   
	   //fetch Contact by id 
	   
	    Optional<Contact> con = contactRepo.findById(id);
	    Contact contact=con.get();    
	 
	   //invalid user this is not his contact	    
	    	
	    model.addAttribute("contact", contact); 
	    return "normal/update-contact";
	    
	   
	  
	}
   
	
	//handler to Update contact page
	
	@PostMapping("/processUpdate/{cid}")
	public String processUpdateContact(@PathVariable("cid") int cid,@ModelAttribute Contact contact,@RequestParam("profile") MultipartFile multipart,HttpSession session) throws IOException {
		//get old file name 
		
		Optional<Contact> con = contactRepo.findById(cid);
		Contact oldcontact=con.get();
		String oldimage=oldcontact.getImage();
		
		if(!multipart.isEmpty()){
			//it will true when your select new image 
			
			//delete old image from system
			
			    File deletefile=new ClassPathResource("/static/image/").getFile();
			  
			    File f=new File(deletefile,oldimage);
			    f.delete();
			    
			
			//update new image 
			
			fw.saveFile(multipart);//save image into system 
			
			contact.setImage(multipart.getOriginalFilename());//set image into contact 
			
		}else {
			//file empty then set old file into database
			
			contact.setImage(oldimage);
			
		}
		
		 try {
			 //user id is null right now 
			 
			 //set user old user 
		 contact.setUser(oldcontact.getUser());	 
		 contactRepo.save(contact);
		 
	     session.setAttribute("response",new Message("user updated scuccessfully!!","alert-success"));
		 }catch(Exception e) {
			 e.printStackTrace();
			 session.setAttribute("response",new Message("some went wrong , user not updated","alert-danger"));
			
		 }
		
		return "normal/update-contact";
	}
	
	
	//handler for  user profile module
	
	
	
	@GetMapping("/your-profile")
	public String yourProfile(Model model,Principal principal) {
		 
	    User user=userRepo.getEmail(principal.getName());
	      
	   //String image= user.getImageUrl();
	   
	     
		model.addAttribute("title","my profile page");
		model.addAttribute("user", user);
		
		return "normal/user-profile";	
		
		
	}
	
	
	
	//handler for setting menu 
	
	@GetMapping("/setting-menu")
	public String settingMenu() {
		
		return "normal/setting-menu";
	}
	
	
	
	
	
	//handler  for setting
	
	@GetMapping("/setting")
	public String handlerSetting(Model model,Principal principal) {
		
		String email=principal.getName();
	//	System.out.println(email);
		User user = userRepo.getEmail(email);
		String image=user.getImageUrl();
		
		model.addAttribute("image",image);
		model.addAttribute("css","/css/second.css");//css link setting form dynamically
		return "normal/setting-form";
		
	}
	
	
	//handler for profile change 
	
	@GetMapping("/profile_change")
	public String changeProfilePicture(Model model,Principal principal) {
		
		  String email=principal.getName();
		//	System.out.println(email);
			User user = userRepo.getEmail(email);
			String image=user.getImageUrl();
			model.addAttribute("image",image);
			model.addAttribute("css","/css/second.css");//css link setting form dynamically
			
		
		return "normal/profile-picture";
	}
	
	
	
	
	
	//update password of user 
	
	
	@PostMapping("/update-password")
	public String updatePasswordOfUser(@ModelAttribute updateUser uUser,Principal principal,HttpSession session) {
		
		String email=principal.getName();
		User user= userRepo.getEmail(email);
		
		//math old password with user enter old password
		
		if(bCryptPasswordEncoder.matches(uUser.getOld_password(),user.getPassword())) {
			//System.out.println("password matched");
			if(!uUser.getNew_password().equals("")) {
				
				//update password 
				//before update we have to encrypt password 
				user.setPassword(bCryptPasswordEncoder.encode(uUser.getNew_password()));
				
				//after save user into database
				userRepo.save(user);
				session.setAttribute("response",new Message("password updated successfully","alert-success"));
				
			}else {
				session.setAttribute("response",new Message("password should not be blank","alert-danger"));
				
			}
			
			
		}else {
			//System.out.println("password not matched");
			
			session.setAttribute("response",new Message("your old password is wrong","alert-danger"));
			
		}
	
		 
		//
		
      // System.out.println(uUser);
		
		//return view
	
		 return "redirect:/users/setting";
		
	}
	
	
	
	
	//handler for change userProfile picture 
	
	@PostMapping("/changeUserProfile")
	public String changeProfile(@RequestParam("image-change") MultipartFile multipart,Principal principal,HttpSession session) throws IOException {
		
		
		System.out.println(multipart.getOriginalFilename());
		
		String email=principal.getName();
		
		User user = userRepo.getEmail(email);
		
		//image save into server file system
		boolean result=false;
		try {
			 result=fw.saveFile(multipart);
		}catch(ClassCastException e) {
			 session.setAttribute("response",new Message("first select image then upload","alert-danger"));
			   
			
		}
		
		if(result==true){
			
			//delete old image from server file system
		    File deletefile=new ClassPathResource("/static/image/").getFile();
			  
		    File f=new File(deletefile,user.getImageUrl());
		    f.delete();
		    
		    
		   user.setImageUrl(multipart.getOriginalFilename());
		 //save image name into user's image field
		   
		   userRepo.save(user);
		   
		   session.setAttribute("response",new Message("profile updated successfully","alert-success"));
		   
		}else {
			 session.setAttribute("response",new Message("please select image first then save","alert-danger"));
			
		}
		return "redirect:/users/profile_change";
	}
	
	
	//user check out form handler
	
	@GetMapping("/checkout-form")
	public String checkOutForm(Model model) {
		model.addAttribute("title","payment integration");
		
		return "normal/checkout-form";
	}
	
	
	
	//handler for create order
	
	
	@PostMapping("/create_order")	
	@ResponseBody
	public ResponseEntity<?> create_Order(@RequestBody Map<String,Object> data) throws RazorpayException  {	
		
           System.out.println(data);
     	   int amount= Integer.parseInt(data.get("amount").toString());
     	   
     	   
     	   
     	    RazorpayClient razorpayClient = new RazorpayClient("rzp_test_U1SGGFHqPvoBY1","F9TU1una8lIHE7gtd6ONplGA");
     	   
     	 
     		  JSONObject orderRequest = new JSONObject();
     		  orderRequest.put("amount",amount*100); // amount in the smallest currency unit
     		  orderRequest.put("currency", "INR");
     		  orderRequest.put("receipt", "txn_234567");
     		  
     		  
     		  //creating orders
     		  Order razorResponse =razorpayClient.orders.create(orderRequest);
     		  System.out.println(razorResponse);
     		  //save order id into database 
     		  
		   return  ResponseEntity.ok(razorResponse);
	}
	
	
	
}
