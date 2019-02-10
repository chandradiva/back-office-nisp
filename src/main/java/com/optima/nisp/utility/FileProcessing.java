package com.optima.nisp.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.optima.nisp.constanta.CommonCons;

@Component
public class FileProcessing {
	
	public static void saveEmailContentAttachment(MultipartFile mpf, String folderPath) throws IOException{
		File folder = new File(folderPath);
		folder.mkdirs();	//Buat folder jika folder belum ada
		
		File convFile = new File(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(mpf.getBytes());
	    fos.close(); 
	}
	
	public static String getFileName(String fullpath){
		return (new File(fullpath).getName());
	}
}
