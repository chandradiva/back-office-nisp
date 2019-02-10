package com.optima.nisp.utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.activation.*;
import javax.annotation.Resource;

@Component("mailProcessing")
public class MailProcessing {
	private static final Logger logger = Logger.getLogger(MailProcessing.class);
	
//	private static Properties properties;
	
	@Resource(name="confProp")
	private Properties properties;
	
	public void sendEmail(String from, String to, String subject, String body, List<String> attachments, long id) throws AddressException, MessagingException, MalformedURLException, UnsupportedEncodingException{
		String host = (String) properties.get("smtp_server");
		String port = (String) properties.get("smtp_port");
		final String username = (String) properties.get("username");
		final String password = (String) properties.get("password");
		//to = "marthin.nainggolan@devteam.net";
		// Get system properties
		Properties sysProp = new Properties();
		
		// Setup mail server
		sysProp.setProperty("mail.smtp.host", host);
		sysProp.setProperty("mail.smtp.port", port);
		Authenticator auth = new Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getInstance(sysProp, auth);
		
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);
		message.addHeader("Disposition-Notification-To", from);
	    message.addHeader("Return-Receipt-To", from);
	    message.addHeader("NID", "N--"+id+"--N");
		/*Header*/
		message.setFrom(new InternetAddress(from));
		String[] tos = to.split(";");
		for(int i=0; i<tos.length; i++ ){
			if( tos[i] != null && tos[i].length() > 0 )
				message.addRecipient(Message.RecipientType.TO,new InternetAddress(tos[i]));
		}
		
        message.setSubject(subject);
        
        // Create a multipart message
        Multipart multipart = new MimeMultipart();

        /*Body*/
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(body, "text/html" );
//        messageBodyPart.setText(body);
        
        // Set text message part
        multipart.addBodyPart(messageBodyPart);

        if(attachments.size()>0){
        	String strContextPath = (String) properties.get("api_protocol") + "://"
					+ (String) properties.get("api_server");
			String strPort = (String) properties.get("api_port");
			if(!strPort.equals("80")){
				strContextPath += ":"+strPort;
			}
			strContextPath += (String) properties.get("api_context_path");
        	for(String attachment : attachments){
        		messageBodyPart = new MimeBodyPart();
		        String filename;
		        DataSource source;
		        if( attachment.startsWith("FROM_API|") ){
		        	filename = attachment.substring(9);
		        	String encodedFilename = URLEncoder.encode(filename, "UTF-8");
			        URL url = new URL(strContextPath+"api/downloadanyfile?filename="+encodedFilename);
			        source = new URLDataSource(url);			        
		        }else{
		        	filename = attachment;
		        	source = new FileDataSource(filename);			        
		        }
		        messageBodyPart.setDataHandler(new DataHandler(source));
		        File f = new File(filename);
		        
		        messageBodyPart.setFileName(f.getName());
		        multipart.addBodyPart(messageBodyPart);
        	}
        }
        
        // Send the complete message parts
        message.setContent(multipart);

        // Send message
        Transport.send(message);
        System.out.println("Sent message successfully....");
	}
	
//	public void setProperties(Properties confProp){
//		MailProcessing.properties = confProp;
//	}
}
