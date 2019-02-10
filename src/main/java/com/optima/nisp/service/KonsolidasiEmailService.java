package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CategoryCons;
import com.optima.nisp.constanta.EmailContentType;
import com.optima.nisp.constanta.ParameterCons;
import com.optima.nisp.constanta.SendEmailStatus;
import com.optima.nisp.dao.EmailAttachmentDao;
import com.optima.nisp.dao.EmailContentDao;
import com.optima.nisp.dao.KonsolidasiEmailDao;
import com.optima.nisp.dao.KonsolidasiEmailLogDao;
import com.optima.nisp.dao.SystemParameterDao;
import com.optima.nisp.model.EmailAttachment;
import com.optima.nisp.model.EmailContent;
import com.optima.nisp.model.KonsolidasiEmail;
import com.optima.nisp.model.KonsolidasiEmailLog;
import com.optima.nisp.model.SystemParameter;
import com.optima.nisp.response.KonsolidasiEmailResponse;
import com.optima.nisp.utility.CommonUtils;
import com.optima.nisp.utility.FileProcessing;
import com.optima.nisp.utility.MailProcessing;

@Component
public class KonsolidasiEmailService {
	
	private static final Logger logger = Logger.getLogger(KonsolidasiEmailService.class);

	@Autowired
	private KonsolidasiEmailDao konsolidasiDao;
	
	@Autowired
	private KonsolidasiEmailLogDao konsolidasiLogDao;
	
	@Autowired
	private EmailContentDao emailContentDao;
	
	@Autowired
	private EmailAttachmentDao emailAttachmentDao;
	
	@Autowired
	private SystemParameterDao systemParameterDao;
	
	@Autowired
	private ApplicationParameterService appParamService;
	
	@Resource(name="confProp")
	private Properties properties;
	
	@Autowired
	private MailProcessing mailProcessing;
	
	public List<KonsolidasiEmailResponse> getEmails(String cifKey, String periode, int status, int page, int totalPerPage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<KonsolidasiEmailResponse> listSE = konsolidasiDao.getEmails(cifKey, periode, status, page, totalPerPage);
		
		boolean wls = false;
		
		SystemParameter sysParam = systemParameterDao.getByKey(ParameterCons.WHITELIST_STATUS);
		if(sysParam!=null){
			if(Integer.parseInt(sysParam.getValue())==1){	//WhiteListStatus = ON
				wls = true;
			}
		}
		
		for(KonsolidasiEmailResponse se : listSE){			
			if(se.getFilename()!=null)
				se.setFilename(FileProcessing.getFileName(se.getFilename()));
			
			if(wls==true && konsolidasiDao.getWhitelistCifKey(se.getCifKey())==null){	//Tidak Ada di whitelist
				se.setEnabledStatus(false);
			}			
			if(se.getStatus()==SendEmailStatus.PROCESSING || se.getStatus()==SendEmailStatus.QUEUE){
				se.setEnabledStatus(false);
			}			
		}
		
		return listSE;
	}
	
	public int getTotalRow(String cifKey, String periode, int status) {
		return konsolidasiDao.getTotalRow(cifKey, periode, status);
	}
	
	public int getWhitelistTotalRow(String cifKey, String periode, int status) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		boolean wls = false;
		
		SystemParameter sysParam = systemParameterDao.getByKey(ParameterCons.WHITELIST_STATUS);
		if(sysParam!=null){
			if(Integer.parseInt(sysParam.getValue())==1){	//WhiteListStatus = ON
				wls = true;
			}
		}
		
		if(wls==true){
			return konsolidasiDao.getWhitelistTotalRow(cifKey, periode, status);
		} else {
			return konsolidasiDao.getSendableTotalRow(cifKey, periode, status);
		}		
	}
	
	public void sendAll(List<KonsolidasiEmail> emails) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {				
		String mailFrom = (String) properties.get("mail_from");
		String preSubject = "Consolidastion Report Periode ";
		String separator = "\n\n";
		
		for(KonsolidasiEmail email : emails){
			List<String> attachments = new ArrayList<String>();
			Integer emailType = EmailContentType.LAPORAN_KONSOLIDASI;
			Integer emailCategory = email.getCategory();
			if (emailCategory == CategoryCons.LAPORAN_KONSOLIDASI) {
				emailType = EmailContentType.LAPORAN_KONSOLIDASI;
			}
			
//			List<String> mailTos  = sendEmailDao.getEmail(email.getAccountNumber(), emailType);
			List<String> mailTos  = new ArrayList<String>();
			for (String mailTo : mailTos) {
				String periode = CommonUtils.getLongPeriode(email.getPeriode());
				String subject = preSubject + periode;
				
				EmailContent content = emailContentDao.getEmailContent(emailType);
				String emailCallbackUrl = appParamService.getByKey("ERNC").getValue();
				String body = "<html><head></head><body><p>"+content.getHeader() + separator +"</p><p>"+ content.getBody() + separator+"</p><p>";
				if( email.getIsPasswordDefault() != 1 )
					body+=content.getPembukaPassword();
				else
					body+=content.getPembukaNoPassword();
				body+= separator + "</p><p>"+ content.getPenutup() + separator+"</p><p>"
						+ content.getFooter()+"</p>\n";
				body += "<img src=\""+emailCallbackUrl+email.getId()+".png?email="+mailTo+"&subject="+subject+"\" >"
						 + "<p style=\"display:none\">N--"+email.getId()+"--N</p></body></html>";
				
				
				String password = konsolidasiDao.getStatementPassword(email.getCifKey());
				
				body = body.replace("<account-number>", email.getAccountNumber());
				body = body.replace("<period>", (periode!=null) ? periode : "");
				body = body.replace("<password>", (password!=null) ? password : "");
				body = body.replace("<today>", CommonUtils.getStrDate(new Date(), "dd MMMM yyyy", Locale.forLanguageTag("id-ID")));
				body = body.replace("<customer-name>", email.getNamaRekening());
				body = body.replace("<product-name>", email.getProductName());
							
				boolean emptyMailTo = false;
				if(mailTo==null || mailTo.isEmpty()){
					emptyMailTo = true;
				}
				
				if(email.getFilename()!=null && !email.getFilename().isEmpty())
					attachments.add("FROM_API|"+email.getFilename());
				
				if (emailType.equals(EmailContentType.LAPORAN_KONSOLIDASI)) {
					EmailAttachment attachment = emailAttachmentDao.getByCategory(emailCategory);
					
					if(attachment!=null && attachment.getAttachment()!=null && !attachment.getAttachment().isEmpty()){
						attachments.add(attachment.getAttachment());
					}
				}
				
				try {
					if(!emptyMailTo){
						mailProcessing.sendEmail(mailFrom, mailTo, subject, body, attachments, email.getId());
						
						KonsolidasiEmail newEmail = new KonsolidasiEmail();
						newEmail.setId(email.getId());
						newEmail.setStatus(SendEmailStatus.SENT);
						newEmail.setTglKirim(new Date());
						konsolidasiDao.update(newEmail);
						
						KonsolidasiEmailLog log = new KonsolidasiEmailLog();
						log.setMailTo(mailTo);
						log.setStatus(SendEmailStatus.SENT);
						log.setSubject(subject);
						//log.setTime(new Date());
						log.setNamaRekening(email.getNamaRekening());
						log.setCifKey(email.getCifKey());
						log.setAccountNumber(email.getAccountNumber());
						konsolidasiLogDao.create(log);
					} else {
						KonsolidasiEmail newEmail = new KonsolidasiEmail();
						newEmail.setId(email.getId());
						newEmail.setStatus(SendEmailStatus.FAILED);
						newEmail.setTglKirim(new Date());
						konsolidasiDao.update(newEmail);
						
						KonsolidasiEmailLog log = new KonsolidasiEmailLog();
						log.setMailTo(mailTo);
						log.setStatus(SendEmailStatus.FAILED);
						log.setSubject(subject);
						//log.setTime(new Date());
						log.setNamaRekening(email.getNamaRekening());
						log.setCifKey(email.getCifKey());
						log.setAccountNumber(email.getAccountNumber());
						konsolidasiLogDao.create(log);
					}
					
				} catch(Exception e){
					KonsolidasiEmail newEmail = new KonsolidasiEmail();
					newEmail.setId(email.getId());
					newEmail.setStatus(SendEmailStatus.FAILED);
					newEmail.setTglKirim(new Date());
					konsolidasiDao.update(newEmail);
					
					KonsolidasiEmailLog log = new KonsolidasiEmailLog();
					log.setMailTo(mailTo);
					log.setStatus(SendEmailStatus.FAILED);
					log.setSubject(subject);
					//log.setTime(new Date());
					log.setNamaRekening(email.getNamaRekening());
					log.setCifKey(email.getCifKey());
					log.setAccountNumber(email.getAccountNumber());
					konsolidasiLogDao.create(log);
				
					logger.error("Error", e);
				}
			}
			
		}
	}
	
	public List<KonsolidasiEmail> getEmails(String cifKey, String accountNumber, String periode, int status) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		boolean wls = false;
		
		SystemParameter sysParam = systemParameterDao.getByKey(ParameterCons.WHITELIST_STATUS);
		if(sysParam!=null){
			if(Integer.parseInt(sysParam.getValue())==1){	//WhiteListStatus = ON
				wls = true;
			}
		}
		
		if(wls==true){
			return konsolidasiDao.getWhitelistEmails(cifKey, periode, status);
		} else {
			return konsolidasiDao.getEmails(cifKey, periode, status);
		}
	}
	
	public KonsolidasiEmail getById(Long id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return konsolidasiDao.getById(id);
	}
	
	public List<KonsolidasiEmailLog> getLogList(String subject, String mailTo, String tglKirimFrom, String tglKirimTo, int status, int start, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return konsolidasiLogDao.getList(subject, mailTo, tglKirimFrom, tglKirimTo, status, start, length);
	}
	
	public Integer getTotalLogRow(String subject, String mailTo, String tglKirimFrom, String tglKirimTo, int status){
		return konsolidasiLogDao.getTotalRow(subject, mailTo, tglKirimFrom, tglKirimTo, status);
	}
	
	public void addToQueue(Long[] ids){
		konsolidasiDao.addToQueue(ids);
	}
	
	public void addToProcessing(Long[] ids){
		konsolidasiDao.addToProcessing(ids);
	}
	
	public void sendNextQueue(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					List<KonsolidasiEmail> emails = konsolidasiDao.getNextQueueEmail();
					do{
						sendAll(emails);
						emails = konsolidasiDao.getNextQueueEmail();
					}while(emails.size() > 0 );
				}catch(Exception e){
					logger.error("Error", e);					
				}
				
			}
		}).start();		
	}
	
}
