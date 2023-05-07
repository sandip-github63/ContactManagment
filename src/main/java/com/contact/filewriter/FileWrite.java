package com.contact.filewriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileWrite {
	
	
	public boolean saveFile(MultipartFile multipart)throws IOException {
		boolean result=false;
		
		//read data from file 
		
		
		 
		   FileInputStream  fis=(FileInputStream)multipart.getInputStream();
		  
		   byte data[]=new byte[fis.available()];
		   
		   fis.read(data);
		   
		   
		   //write file into resources/static/image/filename.extension
		   
		String path=
				   new ClassPathResource("/static/image/").getFile().getAbsolutePath();
		   
		
		 FileOutputStream fos=new FileOutputStream(path+File.separator+multipart.getOriginalFilename());
		 fos.write(data);
		 result=true;
		   
		return result;
		
	}

}
