package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.constanta.ReviewStatus;
import com.optima.nisp.constanta.CategoryCons;
import com.optima.nisp.constanta.EmailContentType;
import com.optima.nisp.constanta.NotifStatus;
import com.optima.nisp.dao.CCEmailAttachmentDao;
import com.optima.nisp.dao.CCEmailAttachmentTempDao;
import com.optima.nisp.dao.EmailAttachmentDao;
import com.optima.nisp.dao.EmailAttachmentTempDao;
import com.optima.nisp.dao.EmailContentDao;
import com.optima.nisp.dao.EmailContentTempDao;
import com.optima.nisp.model.CCEmailAttachment;
import com.optima.nisp.model.CCEmailAttachmentTemp;
import com.optima.nisp.model.EmailAttachment;
import com.optima.nisp.model.EmailAttachmentTemp;
import com.optima.nisp.model.EmailContent;
import com.optima.nisp.model.EmailContentTemp;
import com.optima.nisp.model.api.EmailAttachmentApi;
import com.optima.nisp.model.api.EmailAttachmentCCApi;
import com.optima.nisp.model.api.EmailContentCASAApi;
import com.optima.nisp.model.api.EmailContentCCApi;
import com.optima.nisp.utility.FileProcessing;

@Component
@Transactional
public class EmailContentService {

	@Autowired
	private EmailContentDao emailContentDao;

	@Autowired
	private EmailContentTempDao emailContentTempDao;
	
	@Autowired
	private EmailAttachmentDao emailAttachmentDao;
	
	@Autowired
	private EmailAttachmentTempDao emailAttachmentTempDao;
	
	@Autowired
	private CCEmailAttachmentDao ccEmailAttachmentDao;
	
	@Autowired
	private CCEmailAttachmentTempDao ccEmailAttachmentTempDao;
	
	public EmailContentCCApi getEmailContentCC() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		EmailContent ec = emailContentDao.getEmailContent(EmailContentType.CC);
		EmailContentCCApi ecApi = new EmailContentCCApi();
		List<EmailAttachmentCCApi> attchs = new ArrayList<EmailAttachmentCCApi>();
		
		ecApi.setAttachments(attchs);
		
		List<CCEmailAttachment> attachments = ccEmailAttachmentDao.getAllByCode();
		
		if(ec!=null){
			ecApi.setBody(ec.getBody());
			ecApi.setFooter(ec.getFooter());
			ecApi.setHeader(ec.getHeader());
			ecApi.setId(ec.getId());
			ecApi.setPembuka1(ec.getPembukaPassword());
			ecApi.setPembuka2(ec.getPembukaNoPassword());
			ecApi.setPenutup(ec.getPenutup());
		}
		
		for(CCEmailAttachment atch : attachments){
			if(atch.getAttachment()!=null){
				EmailAttachmentCCApi attch = new EmailAttachmentCCApi();
				attch.setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				attch.setAttchCode(atch.getAttchCode());
				attchs.add(attch);
			}
		}
		
		return ecApi;
	}
	
	public EmailContentCASAApi getEmailContentCASA() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		EmailContent ec = emailContentDao.getEmailContent(EmailContentType.CASA);
		EmailContentCASAApi ecApi = new EmailContentCASAApi();
		EmailAttachmentApi eaApi1 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi2 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi3 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi4 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi5 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi6 = new EmailAttachmentApi();
		
		ecApi.setAttachment1(eaApi1);
		ecApi.setAttachment2(eaApi2);
		ecApi.setAttachment3(eaApi3);
		ecApi.setAttachment4(eaApi4);
		ecApi.setAttachment5(eaApi5);
		ecApi.setAttachment6(eaApi6);
		
		List<EmailAttachment> attachments = emailAttachmentDao.getAllCASA();
		
		if(ec!=null){
			ecApi.setBody(ec.getBody());
			ecApi.setFooter(ec.getFooter());
			ecApi.setHeader(ec.getHeader());
			ecApi.setId(ec.getId());
			ecApi.setPembuka1(ec.getPembukaPassword());
			ecApi.setPembuka2(ec.getPembukaNoPassword());
			ecApi.setPenutup(ec.getPenutup());
		}
		
		for(EmailAttachment atch : attachments){
			if(atch.getAttachment()!=null){
				if(CategoryCons.MULTI_CURRENCY == atch.getCategory()){
					ecApi.getAttachment1().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}
					
				else if(CategoryCons.GIRO_NON_KRK == atch.getCategory()){
					ecApi.getAttachment2().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}					
					
				else if(CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN == atch.getCategory()){
					ecApi.getAttachment3().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}					
					
				else if(CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN == atch.getCategory()){
					ecApi.getAttachment4().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}
					
				else if(CategoryCons.GIRO_TABUNGAN_HARIAN == atch.getCategory()){
					ecApi.getAttachment5().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}
					
				else if(CategoryCons.MULTI_CURRENCY_BILLINGUAL == atch.getCategory()){
					ecApi.getAttachment6().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}
				
				else
					continue;
			}
		}
		
		return ecApi;
	}
	
	public EmailContentCASAApi getEmailContentCASACompany() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		EmailContent ec = emailContentDao.getEmailContent(EmailContentType.CASA_COMPANY);
		EmailContentCASAApi ecApi = new EmailContentCASAApi();
		EmailAttachmentApi eaApi1 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi2 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi3 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi4 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi5 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi6 = new EmailAttachmentApi();
		
		ecApi.setAttachment1(eaApi1);
		ecApi.setAttachment2(eaApi2);
		ecApi.setAttachment3(eaApi3);
		ecApi.setAttachment4(eaApi4);
		ecApi.setAttachment5(eaApi5);
		ecApi.setAttachment6(eaApi6);
		
		List<EmailAttachment> attachments = emailAttachmentDao.getAllCASACompany();
		
		if(ec!=null){
			ecApi.setBody(ec.getBody());
			ecApi.setFooter(ec.getFooter());
			ecApi.setHeader(ec.getHeader());
			ecApi.setId(ec.getId());
			ecApi.setPembuka1(ec.getPembukaPassword());
			ecApi.setPembuka2(ec.getPembukaNoPassword());
			ecApi.setPenutup(ec.getPenutup());
		}
		
		for(EmailAttachment atch : attachments){
			if(atch.getAttachment()!=null){
				if(CategoryCons.MULTI_CURRENCY_COMPANY == atch.getCategory()){
					ecApi.getAttachment1().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}
					
				else if(CategoryCons.GIRO_NON_KRK_COMPANY == atch.getCategory()){
					ecApi.getAttachment2().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}					
					
				else if(CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY == atch.getCategory()){
					ecApi.getAttachment3().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}					
					
				else if(CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY == atch.getCategory()){
					ecApi.getAttachment4().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}
					
				else if(CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY == atch.getCategory()){
					ecApi.getAttachment5().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}
					
				else if(CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY == atch.getCategory()){
					ecApi.getAttachment6().setOldAttachment(FileProcessing.getFileName(atch.getAttachment()));
				}
				
				else
					continue;
			}
		}
		
		return ecApi;
	}
	
	public EmailContentTemp getEmailContentTempCASA() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return emailContentTempDao.getEmailContentTemp(EmailContentType.CASA);
	}
	
	public EmailContentTemp getEmailContentTempCASACompany() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return emailContentTempDao.getEmailContentTemp(EmailContentType.CASA_COMPANY);
	}
	
	public EmailContentTemp getEmailContentTempCC() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return emailContentTempDao.getEmailContentTemp(EmailContentType.CC);
	}

	public EmailContentCASAApi getUnapprovedCASA() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		EmailContentCASAApi ecApi = new EmailContentCASAApi();
		EmailAttachmentApi eaApi1 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi2 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi3 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi4 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi5 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi6 = new EmailAttachmentApi();
		
		ecApi.setAttachment1(eaApi1);
		ecApi.setAttachment2(eaApi2);
		ecApi.setAttachment3(eaApi3);
		ecApi.setAttachment4(eaApi4);
		ecApi.setAttachment5(eaApi5);
		ecApi.setAttachment6(eaApi6);
		
		EmailContent oldEmailContent = emailContentDao.getEmailContent(EmailContentType.CASA);
		EmailAttachment attachment1 = new EmailAttachment();
		EmailAttachment attachment2 = new EmailAttachment();
		EmailAttachment attachment3 = new EmailAttachment();
		EmailAttachment attachment4 = new EmailAttachment();
		EmailAttachment attachment5 = new EmailAttachment();
		EmailAttachment attachment6 = new EmailAttachment();
		
		if(oldEmailContent!=null){
			ecApi.setOldBody(oldEmailContent.getBody());
			ecApi.setOldFooter(oldEmailContent.getFooter());
			ecApi.setOldHeader(oldEmailContent.getHeader());
			ecApi.setOldPembuka1(oldEmailContent.getPembukaPassword());
			ecApi.setOldPembuka2(oldEmailContent.getPembukaNoPassword());
			ecApi.setOldPenutup(oldEmailContent.getPenutup());
		}
		attachment1 = emailAttachmentDao.getByCategory(CategoryCons.MULTI_CURRENCY);
		ecApi.getAttachment1().setOldAttachment(attachment1!=null ? attachment1.getAttachment()!=null ? FileProcessing.getFileName(attachment1.getAttachment()) : "" : "");
		
		attachment2 = emailAttachmentDao.getByCategory(CategoryCons.GIRO_NON_KRK);
		ecApi.getAttachment2().setOldAttachment(attachment2!=null ? attachment2.getAttachment()!=null ? FileProcessing.getFileName(attachment2.getAttachment()) : "" : "");
		
		attachment3 = emailAttachmentDao.getByCategory(CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN);
		ecApi.getAttachment3().setOldAttachment(attachment3!=null ? attachment3.getAttachment()!=null ? FileProcessing.getFileName(attachment3.getAttachment()) : "" : "");
		
		attachment4 = emailAttachmentDao.getByCategory(CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN);
		ecApi.getAttachment4().setOldAttachment(attachment4!=null ? attachment4.getAttachment()!=null ? FileProcessing.getFileName(attachment4.getAttachment()) : "" : "");
		
		attachment5 = emailAttachmentDao.getByCategory(CategoryCons.GIRO_TABUNGAN_HARIAN);
		ecApi.getAttachment5().setOldAttachment(attachment5!=null ? attachment5.getAttachment()!=null ? FileProcessing.getFileName(attachment5.getAttachment()) : "" : "");
		
		attachment6 = emailAttachmentDao.getByCategory(CategoryCons.MULTI_CURRENCY_BILLINGUAL);
		ecApi.getAttachment6().setOldAttachment(attachment6!=null ? attachment6.getAttachment()!=null ? FileProcessing.getFileName(attachment6.getAttachment()) : "" : "");
		
		EmailContentTemp newEmailContent = getEmailContentTempCASA();
		if(newEmailContent!=null){
			ecApi.setBody(newEmailContent.getNewBody());
			ecApi.setFooter(newEmailContent.getNewFooter());
			ecApi.setHeader(newEmailContent.getNewHeader());
			ecApi.setPembuka1(newEmailContent.getNewPembukaPassword());
			ecApi.setPembuka2(newEmailContent.getNewPembukaNoPassword());
			ecApi.setPenutup(newEmailContent.getNewPenutup());
		} else {
			ecApi.setBody(oldEmailContent.getBody());
			ecApi.setFooter(oldEmailContent.getFooter());
			ecApi.setHeader(oldEmailContent.getHeader());
			ecApi.setPembuka1(oldEmailContent.getPembukaPassword());
			ecApi.setPembuka2(oldEmailContent.getPembukaNoPassword());
			ecApi.setPenutup(oldEmailContent.getPenutup());
		}
		
		String atch1name = "";
		String atch2name = "";
		String atch3name = "";
		String atch4name = "";
		String atch5name = "";
		String atch6name = "";
		
		if(attachment1!=null)
			atch1name = attachment1.getAttachment()!=null ? FileProcessing.getFileName(attachment1.getAttachment()) : "";
		if(attachment2!=null)
			atch2name = attachment2.getAttachment()!=null ? FileProcessing.getFileName(attachment2.getAttachment()) : "";
		if(attachment3!=null)
			atch3name = attachment3.getAttachment()!=null ? FileProcessing.getFileName(attachment3.getAttachment()) : "";
		if(attachment4!=null)
			atch4name = attachment4.getAttachment()!=null ? FileProcessing.getFileName(attachment4.getAttachment()) : "";
		if(attachment5!=null)
			atch5name = attachment5.getAttachment()!=null ? FileProcessing.getFileName(attachment5.getAttachment()) : "";
		if(attachment6!=null)
			atch6name = attachment6.getAttachment()!=null ? FileProcessing.getFileName(attachment6.getAttachment()) : "";
		
		//Set New Attachment
		EmailAttachmentTemp atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.MULTI_CURRENCY, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment1().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch1name);
		if(atchTemp!=null){
			ecApi.getAttachment1().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.GIRO_NON_KRK, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment2().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch2name);
		if(atchTemp!=null){
			ecApi.getAttachment2().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment3().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch3name);
		if(atchTemp!=null){
			ecApi.getAttachment3().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment4().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch4name);
		if(atchTemp!=null){
			ecApi.getAttachment4().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.GIRO_TABUNGAN_HARIAN, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment5().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch5name);
		if(atchTemp!=null){
			ecApi.getAttachment5().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.MULTI_CURRENCY_BILLINGUAL, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment6().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch6name);
		if(atchTemp!=null){
			ecApi.getAttachment6().setHighlight(true);
		}
		
		return ecApi;
	}
	
	public EmailContentCASAApi getUnapprovedCASACompany() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		EmailContentCASAApi ecApi = new EmailContentCASAApi();
		EmailAttachmentApi eaApi1 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi2 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi3 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi4 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi5 = new EmailAttachmentApi();
		EmailAttachmentApi eaApi6 = new EmailAttachmentApi();
		
		ecApi.setAttachment1(eaApi1);
		ecApi.setAttachment2(eaApi2);
		ecApi.setAttachment3(eaApi3);
		ecApi.setAttachment4(eaApi4);
		ecApi.setAttachment5(eaApi5);
		ecApi.setAttachment6(eaApi6);
		
		EmailContent oldEmailContent = emailContentDao.getEmailContent(EmailContentType.CASA_COMPANY);
		EmailAttachment attachment1 = new EmailAttachment();
		EmailAttachment attachment2 = new EmailAttachment();
		EmailAttachment attachment3 = new EmailAttachment();
		EmailAttachment attachment4 = new EmailAttachment();
		EmailAttachment attachment5 = new EmailAttachment();
		EmailAttachment attachment6 = new EmailAttachment();
		
		if(oldEmailContent!=null){
			ecApi.setOldBody(oldEmailContent.getBody());
			ecApi.setOldFooter(oldEmailContent.getFooter());
			ecApi.setOldHeader(oldEmailContent.getHeader());
			ecApi.setOldPembuka1(oldEmailContent.getPembukaPassword());
			ecApi.setOldPembuka2(oldEmailContent.getPembukaNoPassword());
			ecApi.setOldPenutup(oldEmailContent.getPenutup());
		}
		attachment1 = emailAttachmentDao.getByCategory(CategoryCons.MULTI_CURRENCY_COMPANY);
		ecApi.getAttachment1().setOldAttachment(attachment1!=null ? attachment1.getAttachment()!=null ? FileProcessing.getFileName(attachment1.getAttachment()) : "" : "");
		
		attachment2 = emailAttachmentDao.getByCategory(CategoryCons.GIRO_NON_KRK_COMPANY);
		ecApi.getAttachment2().setOldAttachment(attachment2!=null ? attachment2.getAttachment()!=null ? FileProcessing.getFileName(attachment2.getAttachment()) : "" : "");
		
		attachment3 = emailAttachmentDao.getByCategory(CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY);
		ecApi.getAttachment3().setOldAttachment(attachment3!=null ? attachment3.getAttachment()!=null ? FileProcessing.getFileName(attachment3.getAttachment()) : "" : "");
		
		attachment4 = emailAttachmentDao.getByCategory(CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY);
		ecApi.getAttachment4().setOldAttachment(attachment4!=null ? attachment4.getAttachment()!=null ? FileProcessing.getFileName(attachment4.getAttachment()) : "" : "");
		
		attachment5 = emailAttachmentDao.getByCategory(CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY);
		ecApi.getAttachment5().setOldAttachment(attachment5!=null ? attachment5.getAttachment()!=null ? FileProcessing.getFileName(attachment5.getAttachment()) : "" : "");
		
		attachment6 = emailAttachmentDao.getByCategory(CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY);
		ecApi.getAttachment6().setOldAttachment(attachment6!=null ? attachment6.getAttachment()!=null ? FileProcessing.getFileName(attachment6.getAttachment()) : "" : "");
		
		EmailContentTemp newEmailContent = getEmailContentTempCASACompany();
		if(newEmailContent!=null){
			ecApi.setBody(newEmailContent.getNewBody());
			ecApi.setFooter(newEmailContent.getNewFooter());
			ecApi.setHeader(newEmailContent.getNewHeader());
			ecApi.setPembuka1(newEmailContent.getNewPembukaPassword());
			ecApi.setPembuka2(newEmailContent.getNewPembukaNoPassword());
			ecApi.setPenutup(newEmailContent.getNewPenutup());
		} else {
			ecApi.setBody(oldEmailContent.getBody());
			ecApi.setFooter(oldEmailContent.getFooter());
			ecApi.setHeader(oldEmailContent.getHeader());
			ecApi.setPembuka1(oldEmailContent.getPembukaPassword());
			ecApi.setPembuka2(oldEmailContent.getPembukaNoPassword());
			ecApi.setPenutup(oldEmailContent.getPenutup());
		}
		
		String atch1name = "";
		String atch2name = "";
		String atch3name = "";
		String atch4name = "";
		String atch5name = "";
		String atch6name = "";
		
		if(attachment1!=null)
			atch1name = attachment1.getAttachment()!=null ? FileProcessing.getFileName(attachment1.getAttachment()) : "";
		if(attachment2!=null)
			atch2name = attachment2.getAttachment()!=null ? FileProcessing.getFileName(attachment2.getAttachment()) : "";
		if(attachment3!=null)
			atch3name = attachment3.getAttachment()!=null ? FileProcessing.getFileName(attachment3.getAttachment()) : "";
		if(attachment4!=null)
			atch4name = attachment4.getAttachment()!=null ? FileProcessing.getFileName(attachment4.getAttachment()) : "";
		if(attachment5!=null)
			atch5name = attachment5.getAttachment()!=null ? FileProcessing.getFileName(attachment5.getAttachment()) : "";
		if(attachment6!=null)
			atch6name = attachment6.getAttachment()!=null ? FileProcessing.getFileName(attachment6.getAttachment()) : "";
		
		//Set New Attachment
		EmailAttachmentTemp atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.MULTI_CURRENCY_COMPANY, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment1().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch1name);
		if(atchTemp!=null){
			ecApi.getAttachment1().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.GIRO_NON_KRK_COMPANY, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment2().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch2name);
		if(atchTemp!=null){
			ecApi.getAttachment2().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment3().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch3name);
		if(atchTemp!=null){
			ecApi.getAttachment3().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment4().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch4name);
		if(atchTemp!=null){
			ecApi.getAttachment4().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment5().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch5name);
		if(atchTemp!=null){
			ecApi.getAttachment5().setHighlight(true);
		}
		
		atchTemp = emailAttachmentTempDao.getByCategory(CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY, ReviewStatus.NOT_REVIEWED);
		ecApi.getAttachment6().setNewAttachment(atchTemp!=null ? atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "" : atch6name);
		if(atchTemp!=null){
			ecApi.getAttachment6().setHighlight(true);
		}
		
		return ecApi;
	}
	
	public EmailContentCCApi getUnapprovedCC() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{		
		EmailContentCCApi ecApi = new EmailContentCCApi();
		List<EmailAttachmentCCApi> attchs = new ArrayList<EmailAttachmentCCApi>();
		ecApi.setAttachments(attchs);
		
		EmailContent oldEmailContent = emailContentDao.getEmailContent(EmailContentType.CC);
		if(oldEmailContent!=null){
			ecApi.setOldBody(oldEmailContent.getBody());
			ecApi.setOldFooter(oldEmailContent.getFooter());
			ecApi.setOldHeader(oldEmailContent.getHeader());
			ecApi.setOldPembuka1(oldEmailContent.getPembukaPassword());
			ecApi.setOldPembuka2(oldEmailContent.getPembukaNoPassword());
			ecApi.setOldPenutup(oldEmailContent.getPenutup());
		}
		
		List<CCEmailAttachment> atchmnts = ccEmailAttachmentDao.getAllByCode();
		
		for(CCEmailAttachment attch : atchmnts){
			EmailAttachmentCCApi attchApi = new EmailAttachmentCCApi();
			attchApi.setOldAttachment(attch.getAttachment()!=null ? FileProcessing.getFileName(attch.getAttachment()) : "");
			attchApi.setNewAttachment(attch.getAttachment()!=null ? FileProcessing.getFileName(attch.getAttachment()) : "");
			
			CCEmailAttachmentTemp atchTemp = ccEmailAttachmentTempDao.getByBin(attch.getBin(), ReviewStatus.NOT_REVIEWED);
			if(atchTemp!=null){
				attchApi.setNewAttachment(atchTemp.getNewAttachment()!=null ? FileProcessing.getFileName(atchTemp.getNewAttachment()) : "");
				attchApi.setHighlight(true);
			}
			
			attchApi.setAttchCode(attch.getAttchCode());
			attchs.add(attchApi);
		}
		
		EmailContentTemp newEmailContent = getEmailContentTempCC();
		if(newEmailContent!=null){
			ecApi.setBody(newEmailContent.getNewBody());
			ecApi.setFooter(newEmailContent.getNewFooter());
			ecApi.setHeader(newEmailContent.getNewHeader());
			ecApi.setPembuka1(newEmailContent.getNewPembukaPassword());
			ecApi.setPembuka2(newEmailContent.getNewPembukaNoPassword());
			ecApi.setPenutup(newEmailContent.getNewPenutup());
		} else {
			ecApi.setBody(oldEmailContent.getBody());
			ecApi.setFooter(oldEmailContent.getFooter());
			ecApi.setHeader(oldEmailContent.getHeader());
			ecApi.setPembuka1(oldEmailContent.getPembukaPassword());
			ecApi.setPembuka2(oldEmailContent.getPembukaNoPassword());
			ecApi.setPenutup(oldEmailContent.getPenutup());
		}
		
		return ecApi;
	}
	
	public void approveCASA(String approvedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CASA);
		List<EmailAttachmentTemp> attachTemps = emailAttachmentTempDao.getAllCASA(ReviewStatus.NOT_REVIEWED);
		List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
		List<EmailAttachmentTemp> reviewedAttachTemps = new ArrayList<EmailAttachmentTemp>();
		
		for(EmailAttachmentTemp attachTemp : attachTemps){
			EmailAttachment updatedAttachment = emailAttachmentDao.getByCategory(attachTemp.getCategory());
			if(updatedAttachment == null) {
				updatedAttachment = new EmailAttachment();
				updatedAttachment.setCategory(attachTemp.getCategory());
			}
			updatedAttachment.setApprovedBy(approvedBy);
			updatedAttachment.setAttachment(attachTemp.getNewAttachment()!=null ? attachTemp.getNewAttachment() : "");
			updatedAttachment.setUpdatedBy(attachTemp.getUpdatedBy());
			updatedAttachment.setUpdatedDate(new Date());
			attachments.add(updatedAttachment);
			
			/*Ubah status menjadi Approved*/
			EmailAttachmentTemp reviewedTemp = new EmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.APPROVED);
			reviewedTemp.setReviewedBy(approvedBy);
			reviewedTemp.setShowNotifStatus(NotifStatus.SHOW);
			reviewedAttachTemps.add(reviewedTemp);
		}
		
		if(temp!=null){
			EmailContent emailContent = new EmailContent();
			emailContent.setBody(temp.getNewBody());
			emailContent.setFooter(temp.getNewFooter());
			emailContent.setHeader(temp.getNewHeader());
			emailContent.setId(temp.getEmailContentId());
			emailContent.setPembukaNoPassword(temp.getNewPembukaNoPassword());
			emailContent.setPembukaPassword(temp.getNewPembukaPassword());
			emailContent.setPenutup(temp.getNewPenutup());
			emailContent.setUpdatedDate(new Date());
			emailContent.setUpdatedBy(temp.getUpdatedBy());
			emailContent.setApprovedBy(approvedBy);
			emailContent.setType(temp.getType());
			
			emailContentDao.insertOrUpdate(emailContent);
			
			temp.setReviewedStatus(ReviewStatus.APPROVED);
			temp.setReviewedBy(approvedBy);
			temp.setShowNotifStatus(NotifStatus.SHOW);			
			emailContentTempDao.update(temp);
		}		
		
		emailAttachmentDao.insertOrUpdateBatch(attachments.toArray());		
		emailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void approveCASACompany(String approvedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CASA_COMPANY);
		List<EmailAttachmentTemp> attachTemps = emailAttachmentTempDao.getAllCASACompany(ReviewStatus.NOT_REVIEWED);
		List<EmailAttachment> attachments = new ArrayList<EmailAttachment>();
		List<EmailAttachmentTemp> reviewedAttachTemps = new ArrayList<EmailAttachmentTemp>();
		
		for(EmailAttachmentTemp attachTemp : attachTemps){
			EmailAttachment updatedAttachment = emailAttachmentDao.getByCategory(attachTemp.getCategory());
			if(updatedAttachment == null) {
				updatedAttachment = new EmailAttachment();
				updatedAttachment.setCategory(attachTemp.getCategory());
			}
			updatedAttachment.setApprovedBy(approvedBy);
			updatedAttachment.setAttachment(attachTemp.getNewAttachment()!=null ? attachTemp.getNewAttachment() : "");
			updatedAttachment.setUpdatedBy(attachTemp.getUpdatedBy());
			updatedAttachment.setUpdatedDate(new Date());
			attachments.add(updatedAttachment);
			
			/*Ubah status menjadi Approved*/
			EmailAttachmentTemp reviewedTemp = new EmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.APPROVED);
			reviewedTemp.setReviewedBy(approvedBy);
			reviewedTemp.setShowNotifStatus(NotifStatus.SHOW);
			reviewedAttachTemps.add(reviewedTemp);
		}
		
		if(temp!=null){
			EmailContent emailContent = new EmailContent();
			emailContent.setBody(temp.getNewBody());
			emailContent.setFooter(temp.getNewFooter());
			emailContent.setHeader(temp.getNewHeader());
			emailContent.setId(temp.getEmailContentId());
			emailContent.setPembukaNoPassword(temp.getNewPembukaNoPassword());
			emailContent.setPembukaPassword(temp.getNewPembukaPassword());
			emailContent.setPenutup(temp.getNewPenutup());
			emailContent.setUpdatedDate(new Date());
			emailContent.setUpdatedBy(temp.getUpdatedBy());
			emailContent.setApprovedBy(approvedBy);
			emailContent.setType(temp.getType());
			
			emailContentDao.insertOrUpdate(emailContent);
			
			temp.setReviewedStatus(ReviewStatus.APPROVED);
			temp.setReviewedBy(approvedBy);
			temp.setShowNotifStatus(NotifStatus.SHOW);			
			emailContentTempDao.update(temp);
		}		
		
		emailAttachmentDao.insertOrUpdateBatch(attachments.toArray());		
		emailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void approveCC(String approvedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CC);
		List<CCEmailAttachmentTemp> attachTemps = ccEmailAttachmentTempDao.getAll(ReviewStatus.NOT_REVIEWED);
		List<CCEmailAttachment> attachments = new ArrayList<CCEmailAttachment>();
		List<CCEmailAttachmentTemp> reviewedAttachTemps = new ArrayList<CCEmailAttachmentTemp>();
		
		for(CCEmailAttachmentTemp attachTemp : attachTemps){
			CCEmailAttachment updatedAttachment = ccEmailAttachmentDao.getByBin(attachTemp.getBin());
			if(updatedAttachment == null) {
				updatedAttachment = new CCEmailAttachment();
				updatedAttachment.setBin(attachTemp.getBin());			
			}
			updatedAttachment.setApprovedBy(approvedBy);
			updatedAttachment.setAttachment(attachTemp.getNewAttachment()!=null ? attachTemp.getNewAttachment() : "");
			updatedAttachment.setUpdatedBy(attachTemp.getUpdatedBy());
			updatedAttachment.setUpdatedDate(new Date());
			attachments.add(updatedAttachment);
			
			/*Update Status menjadi APPROVED*/
			CCEmailAttachmentTemp reviewedTemp = new CCEmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.APPROVED);
			reviewedTemp.setReviewedBy(approvedBy);
			reviewedTemp.setShowNotifStatus(NotifStatus.SHOW);
			reviewedAttachTemps.add(reviewedTemp);
		}
		
		if(temp!=null){
			EmailContent emailContent = new EmailContent();
			emailContent.setBody(temp.getNewBody());
			emailContent.setFooter(temp.getNewFooter());
			emailContent.setHeader(temp.getNewHeader());
			emailContent.setId(temp.getEmailContentId());
			emailContent.setPembukaNoPassword(temp.getNewPembukaNoPassword());
			emailContent.setPembukaPassword(temp.getNewPembukaPassword());
			emailContent.setPenutup(temp.getNewPenutup());
			emailContent.setUpdatedDate(new Date());
			emailContent.setUpdatedBy(temp.getUpdatedBy());
			emailContent.setApprovedBy(approvedBy);
			emailContent.setType(temp.getType());
			
			emailContentDao.insertOrUpdate(emailContent);
			
			temp.setReviewedStatus(ReviewStatus.APPROVED);
			temp.setReviewedBy(approvedBy);
			temp.setShowNotifStatus(NotifStatus.SHOW);			
			emailContentTempDao.update(temp);
		}		
		
		ccEmailAttachmentDao.insertOrUpdateBatch(attachments.toArray());		
		ccEmailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void rejectCASA(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi REJECTED*/
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CASA);
		if(temp!=null){
			temp.setReviewedStatus(ReviewStatus.REJECTED);
			temp.setReviewedBy(userId);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			emailContentTempDao.update(temp);
		}
		
		List<EmailAttachmentTemp> attachTemps = emailAttachmentTempDao.getAllCASA(ReviewStatus.NOT_REVIEWED);
		List<EmailAttachmentTemp> reviewedAttachTemps = new ArrayList<EmailAttachmentTemp>();
		for(EmailAttachmentTemp attachTemp : attachTemps){
			EmailAttachmentTemp reviewedTemp = new EmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.REJECTED);
			reviewedTemp.setReviewedBy(userId);
			reviewedTemp.setShowNotifStatus(NotifStatus.SHOW);
			reviewedAttachTemps.add(reviewedTemp);
		}
		emailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void rejectCASACompany(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi REJECTED*/
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CASA_COMPANY);
		if(temp!=null){
			temp.setReviewedStatus(ReviewStatus.REJECTED);
			temp.setReviewedBy(userId);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			emailContentTempDao.update(temp);
		}
		
		List<EmailAttachmentTemp> attachTemps = emailAttachmentTempDao.getAllCASACompany(ReviewStatus.NOT_REVIEWED);
		List<EmailAttachmentTemp> reviewedAttachTemps = new ArrayList<EmailAttachmentTemp>();
		for(EmailAttachmentTemp attachTemp : attachTemps){
			EmailAttachmentTemp reviewedTemp = new EmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.REJECTED);
			reviewedTemp.setReviewedBy(userId);
			reviewedTemp.setShowNotifStatus(NotifStatus.SHOW);
			reviewedAttachTemps.add(reviewedTemp);
		}
		emailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void rejectCC(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi REJECTED*/
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CC);
		if(temp!=null){
			temp.setReviewedStatus(ReviewStatus.REJECTED);
			temp.setReviewedBy(userId);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			emailContentTempDao.update(temp);
		}
		
		List<CCEmailAttachmentTemp> attachTemps = ccEmailAttachmentTempDao.getAll(ReviewStatus.NOT_REVIEWED);
		List<CCEmailAttachmentTemp> reviewedAttachTemps = new ArrayList<CCEmailAttachmentTemp>();
		for(CCEmailAttachmentTemp attachTemp : attachTemps){
			CCEmailAttachmentTemp reviewedTemp = new CCEmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.REJECTED);
			reviewedTemp.setReviewedBy(userId);
			reviewedTemp.setShowNotifStatus(NotifStatus.SHOW);
			reviewedAttachTemps.add(reviewedTemp);
		}
		ccEmailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void cancelCASA(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi CANCELED*/
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CASA);
		if(temp!=null){
			temp.setReviewedStatus(ReviewStatus.CANCELED);
			temp.setReviewedBy(userId);
			temp.setShowNotifStatus(NotifStatus.HIDE);
			emailContentTempDao.update(temp);
		}
		
		List<EmailAttachmentTemp> attachTemps = emailAttachmentTempDao.getAllCASA(ReviewStatus.NOT_REVIEWED);
		List<EmailAttachmentTemp> reviewedAttachTemps = new ArrayList<EmailAttachmentTemp>();
		for(EmailAttachmentTemp attachTemp : attachTemps){
			EmailAttachmentTemp reviewedTemp = new EmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.CANCELED);
			reviewedTemp.setReviewedBy(userId);
			reviewedTemp.setShowNotifStatus(NotifStatus.HIDE);
			reviewedAttachTemps.add(reviewedTemp);
		}
		emailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void cancelCASACompany(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi CANCELED*/
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CASA_COMPANY);
		if(temp!=null){
			temp.setReviewedStatus(ReviewStatus.CANCELED);
			temp.setReviewedBy(userId);
			temp.setShowNotifStatus(NotifStatus.HIDE);
			emailContentTempDao.update(temp);
		}
		
		List<EmailAttachmentTemp> attachTemps = emailAttachmentTempDao.getAllCASACompany(ReviewStatus.NOT_REVIEWED);
		List<EmailAttachmentTemp> reviewedAttachTemps = new ArrayList<EmailAttachmentTemp>();
		for(EmailAttachmentTemp attachTemp : attachTemps){
			EmailAttachmentTemp reviewedTemp = new EmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.CANCELED);
			reviewedTemp.setReviewedBy(userId);
			reviewedTemp.setShowNotifStatus(NotifStatus.HIDE);
			reviewedAttachTemps.add(reviewedTemp);
		}
		emailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void cancelCC(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {				
		/*Ubah status temp menjadi CANCELED*/
		EmailContentTemp temp = emailContentTempDao.getEmailContentTemp(EmailContentType.CC);
		if(temp!=null){
			temp.setReviewedStatus(ReviewStatus.CANCELED);
			temp.setReviewedBy(userId);
			temp.setShowNotifStatus(NotifStatus.HIDE);
			emailContentTempDao.update(temp);
		}
		
		List<CCEmailAttachmentTemp> attachTemps = ccEmailAttachmentTempDao.getAll(ReviewStatus.NOT_REVIEWED);
		List<CCEmailAttachmentTemp> reviewedAttachTemps = new ArrayList<CCEmailAttachmentTemp>();
		for(CCEmailAttachmentTemp attachTemp : attachTemps){
			CCEmailAttachmentTemp reviewedTemp = new CCEmailAttachmentTemp();
			reviewedTemp.setId(attachTemp.getId());
			reviewedTemp.setReviewedStatus(ReviewStatus.CANCELED);
			reviewedTemp.setReviewedBy(userId);
			reviewedTemp.setShowNotifStatus(NotifStatus.HIDE);
			reviewedAttachTemps.add(reviewedTemp);
		}
		ccEmailAttachmentTempDao.updateBatch(reviewedAttachTemps.toArray());
	}
	
	public void insertToTempCASA(EmailContentCASAApi ecApi, String updatedBy) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<EmailAttachmentTemp> attachTemps = new ArrayList<EmailAttachmentTemp>();
		
		EmailAttachmentTemp attach1 = null;
		if(ecApi.getAttachment1()!=null && ecApi.getAttachment1().getNewAttachment()!=null){
			attach1 = new EmailAttachmentTemp(CategoryCons.MULTI_CURRENCY, ecApi.getAttachment1().getOldAttachment(), ecApi.getAttachment1().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach1.setUpdatedBy(updatedBy);
		}
		
		EmailAttachmentTemp attach2 = null;
		if(ecApi.getAttachment2()!=null && ecApi.getAttachment2().getNewAttachment()!=null){
			attach2 = new EmailAttachmentTemp(CategoryCons.GIRO_NON_KRK, ecApi.getAttachment2().getOldAttachment(), ecApi.getAttachment2().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach2.setUpdatedBy(updatedBy);
		} 
		
		EmailAttachmentTemp attach3 = null;
		if(ecApi.getAttachment3()!=null && ecApi.getAttachment3().getNewAttachment()!=null){
			attach3 = new EmailAttachmentTemp(CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN, ecApi.getAttachment3().getOldAttachment(), ecApi.getAttachment3().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach3.setUpdatedBy(updatedBy);
		}
		
		EmailAttachmentTemp attach4 = null;
		if(ecApi.getAttachment4()!=null && ecApi.getAttachment4().getNewAttachment()!=null){
			attach4 = new EmailAttachmentTemp(CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN, ecApi.getAttachment4().getOldAttachment(), ecApi.getAttachment4().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach4.setUpdatedBy(updatedBy);
		}
		
		EmailAttachmentTemp attach5 = null;
		if(ecApi.getAttachment5()!=null && ecApi.getAttachment5().getNewAttachment()!=null){
			attach5 = new EmailAttachmentTemp(CategoryCons.GIRO_TABUNGAN_HARIAN, ecApi.getAttachment5().getOldAttachment(), ecApi.getAttachment5().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach5.setUpdatedBy(updatedBy);
		}
		
		EmailAttachmentTemp attach6 = null;
		if(ecApi.getAttachment6()!=null && ecApi.getAttachment6().getNewAttachment()!=null){
			attach6 = new EmailAttachmentTemp(CategoryCons.MULTI_CURRENCY_BILLINGUAL, ecApi.getAttachment6().getOldAttachment(), ecApi.getAttachment6().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach6.setUpdatedBy(updatedBy);
		}
		
		if(attach1!=null)
			attachTemps.add(attach1);
		if(attach2!=null)
			attachTemps.add(attach2);
		if(attach3!=null)
			attachTemps.add(attach3);
		if(attach4!=null)
			attachTemps.add(attach4);
		if(attach5!=null)
			attachTemps.add(attach5);
		if(attach6!=null)
			attachTemps.add(attach6);
		
		EmailContentTemp temp = new EmailContentTemp();
		temp.setEmailContentId(ecApi.getId());
		temp.setNewBody(ecApi.getBody());
		temp.setNewFooter(ecApi.getFooter());
		temp.setNewHeader(ecApi.getHeader());
		temp.setNewPembukaNoPassword(ecApi.getPembuka2());
		temp.setNewPembukaPassword(ecApi.getPembuka1());
		temp.setNewPenutup(ecApi.getPenutup());
		temp.setOldBody(ecApi.getOldBody());
		temp.setOldFooter(ecApi.getOldFooter());
		temp.setOldHeader(ecApi.getOldHeader());
		temp.setOldPembukaNoPassword(ecApi.getOldPembuka2());
		temp.setOldPembukaPassword(ecApi.getOldPembuka1());
		temp.setOldPenutup(ecApi.getOldPenutup());
		temp.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
		temp.setUpdatedBy(updatedBy);
		temp.setType(EmailContentType.CASA);
		
		emailAttachmentTempDao.createBatch(attachTemps.toArray());
		emailAttachmentTempDao.deleteAllReviewedCASA();
		emailContentTempDao.create(temp);
		emailContentTempDao.deleteReviewed(EmailContentType.CASA);
	}
	
	public void insertToTempCASACompany(EmailContentCASAApi ecApi, String updatedBy) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		List<EmailAttachmentTemp> attachTemps = new ArrayList<EmailAttachmentTemp>();
		
		EmailAttachmentTemp attach1 = null;
		if(ecApi.getAttachment1()!=null && ecApi.getAttachment1().getNewAttachment()!=null){
			attach1 = new EmailAttachmentTemp(CategoryCons.MULTI_CURRENCY_COMPANY, ecApi.getAttachment1().getOldAttachment(), ecApi.getAttachment1().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach1.setUpdatedBy(updatedBy);
		}
		
		EmailAttachmentTemp attach2 = null;
		if(ecApi.getAttachment2()!=null && ecApi.getAttachment2().getNewAttachment()!=null){
			attach2 = new EmailAttachmentTemp(CategoryCons.GIRO_NON_KRK_COMPANY, ecApi.getAttachment2().getOldAttachment(), ecApi.getAttachment2().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach2.setUpdatedBy(updatedBy);
		} 
		
		EmailAttachmentTemp attach3 = null;
		if(ecApi.getAttachment3()!=null && ecApi.getAttachment3().getNewAttachment()!=null){
			attach3 = new EmailAttachmentTemp(CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY, ecApi.getAttachment3().getOldAttachment(), ecApi.getAttachment3().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach3.setUpdatedBy(updatedBy);
		}
		
		EmailAttachmentTemp attach4 = null;
		if(ecApi.getAttachment4()!=null && ecApi.getAttachment4().getNewAttachment()!=null){
			attach4 = new EmailAttachmentTemp(CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY, ecApi.getAttachment4().getOldAttachment(), ecApi.getAttachment4().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach4.setUpdatedBy(updatedBy);
		}
		
		EmailAttachmentTemp attach5 = null;
		if(ecApi.getAttachment5()!=null && ecApi.getAttachment5().getNewAttachment()!=null){
			attach5 = new EmailAttachmentTemp(CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY, ecApi.getAttachment5().getOldAttachment(), ecApi.getAttachment5().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach5.setUpdatedBy(updatedBy);
		}
		
		EmailAttachmentTemp attach6 = null;
		if(ecApi.getAttachment6()!=null && ecApi.getAttachment6().getNewAttachment()!=null){
			attach6 = new EmailAttachmentTemp(CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY, ecApi.getAttachment6().getOldAttachment(), ecApi.getAttachment6().getNewAttachment(), ReviewStatus.NOT_REVIEWED);
			attach6.setUpdatedBy(updatedBy);
		}
		
		if(attach1!=null)
			attachTemps.add(attach1);
		if(attach2!=null)
			attachTemps.add(attach2);
		if(attach3!=null)
			attachTemps.add(attach3);
		if(attach4!=null)
			attachTemps.add(attach4);
		if(attach5!=null)
			attachTemps.add(attach5);
		if(attach6!=null)
			attachTemps.add(attach6);
		
		EmailContentTemp temp = new EmailContentTemp();
		temp.setEmailContentId(ecApi.getId());
		temp.setNewBody(ecApi.getBody());
		temp.setNewFooter(ecApi.getFooter());
		temp.setNewHeader(ecApi.getHeader());
		temp.setNewPembukaNoPassword(ecApi.getPembuka2());
		temp.setNewPembukaPassword(ecApi.getPembuka1());
		temp.setNewPenutup(ecApi.getPenutup());
		temp.setOldBody(ecApi.getOldBody());
		temp.setOldFooter(ecApi.getOldFooter());
		temp.setOldHeader(ecApi.getOldHeader());
		temp.setOldPembukaNoPassword(ecApi.getOldPembuka2());
		temp.setOldPembukaPassword(ecApi.getOldPembuka1());
		temp.setOldPenutup(ecApi.getOldPenutup());
		temp.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
		temp.setUpdatedBy(updatedBy);
		temp.setType(EmailContentType.CASA_COMPANY);
		
		emailAttachmentTempDao.createBatch(attachTemps.toArray());
		emailAttachmentTempDao.deleteAllReviewedCASACompany();
		emailContentTempDao.create(temp);
		emailContentTempDao.deleteReviewed(EmailContentType.CASA_COMPANY);
	}
	
	public void insertToTempCC(EmailContentCCApi ecApi, String updatedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{		
		List<CCEmailAttachmentTemp> attachTemps = new ArrayList<CCEmailAttachmentTemp>();
		
		for(EmailAttachmentCCApi attch : ecApi.getAttachments()){			
			if(attch!=null && attch.getNewAttachment()!=null){
				List<CCEmailAttachment> attchs = ccEmailAttachmentDao.getAttchsSameCode(attch.getAttchCode());
				
				for(CCEmailAttachment attch2 : attchs){
					CCEmailAttachmentTemp attachment = new CCEmailAttachmentTemp();
					attachment.setBin(attch2.getBin());
					attachment.setNewAttachment(attch.getNewAttachment());
					attachment.setOldAttachment(attch.getOldAttachment());
					attachment.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
					attachment.setUpdatedBy(updatedBy);
					attachTemps.add(attachment);
				}
			}
		}
				
		EmailContentTemp temp = new EmailContentTemp();
		temp.setEmailContentId(ecApi.getId());
		temp.setNewBody(ecApi.getBody());
		temp.setNewFooter(ecApi.getFooter());
		temp.setNewHeader(ecApi.getHeader());
		temp.setNewPembukaNoPassword(ecApi.getPembuka2());
		temp.setNewPembukaPassword(ecApi.getPembuka1());
		temp.setNewPenutup(ecApi.getPenutup());
		temp.setOldBody(ecApi.getOldBody());
		temp.setOldFooter(ecApi.getOldFooter());
		temp.setOldHeader(ecApi.getOldHeader());
		temp.setOldPembukaNoPassword(ecApi.getOldPembuka2());
		temp.setOldPembukaPassword(ecApi.getOldPembuka1());
		temp.setOldPenutup(ecApi.getOldPenutup());
		temp.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
		temp.setUpdatedBy(updatedBy);
		temp.setType(EmailContentType.CC);
		
		ccEmailAttachmentTempDao.createBatch(attachTemps.toArray());
		ccEmailAttachmentTempDao.deleteAllReviewed();
		emailContentTempDao.create(temp);
		emailContentTempDao.deleteReviewed(EmailContentType.CC);
	}
	
	public String getCASAUpdatedBy(){
		return emailContentTempDao.getUpdatedBy(EmailContentType.CASA);
	}
	
	public String getCASACompanyUpdatedBy(){
		return emailContentTempDao.getUpdatedBy(EmailContentType.CASA_COMPANY);
	}
	
	public String getCCUpdatedBy(){
		return emailContentTempDao.getUpdatedBy(EmailContentType.CC);
	}
	
	public String getCASAAttachmentUpdatedBy(){
		return emailAttachmentTempDao.getUpdatedBy();
	}
	
	public String getCASACompanyAttachmentUpdatedBy(){
		return emailAttachmentTempDao.getCasaCompanyUpdatedBy();
	}
	
	public String getCCAttachmentUpdatedBy(){
		return ccEmailAttachmentTempDao.getUpdatedBy();
	}
	
	public EmailAttachmentTemp getAttachmentTempByCategory(Integer category) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return emailAttachmentTempDao.getByCategory(category, ReviewStatus.NOT_REVIEWED);
	}
	
	public List<EmailAttachmentTemp> getAttachmentTempCASA() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return emailAttachmentTempDao.getAllCASA(ReviewStatus.NOT_REVIEWED);
	}
	
	public List<EmailAttachmentTemp> getAttachmentTempCASACompany() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return emailAttachmentTempDao.getAllCASACompany(ReviewStatus.NOT_REVIEWED);
	}
	
	public List<CCEmailAttachmentTemp> getAttachmentTempCC() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return ccEmailAttachmentTempDao.getAll(ReviewStatus.NOT_REVIEWED);
	}
	
	public CCEmailAttachmentTemp getAttachmentTempCCByBin(String bin) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return ccEmailAttachmentTempDao.getByBin(bin, ReviewStatus.NOT_REVIEWED);
	}
	
	public EmailContentTemp getByRequester(String userId, Integer notifStatus, Integer ecType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return emailContentTempDao.getByRequester(userId, notifStatus, ecType);
	}
	
	public List<EmailAttachmentTemp> getAttachmentTempCASAByRequester(String userId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return emailAttachmentTempDao.getAllCASAByRequester(userId, notifStatus);
	}
	
	public List<EmailAttachmentTemp> getAttachmentTempCASACompanyByRequester(String userId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return emailAttachmentTempDao.getAllCASACompanyByRequester(userId, notifStatus);
	}
	
	public List<CCEmailAttachmentTemp> getAttachmentTempCCByRequester(String userId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return ccEmailAttachmentTempDao.getAllByRequester(userId, notifStatus);
	}
	
	public void setNotifOff(String userId, Integer ecType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		EmailContentTemp temp = emailContentTempDao.getByRequester(userId, NotifStatus.SHOW, ecType);
		if(temp!=null){
			temp.setShowNotifStatus(NotifStatus.HIDE);
			emailContentTempDao.update(temp);
		}		
		
		if(ecType.equals(EmailContentType.CASA)){
			List<EmailAttachmentTemp> temps = emailAttachmentTempDao.getAllCASAByRequester(userId, NotifStatus.SHOW);
			List<EmailAttachmentTemp> notifiedTemps = new ArrayList<EmailAttachmentTemp>();
			for(EmailAttachmentTemp temp1 : temps){
				EmailAttachmentTemp notifiedTemp = new EmailAttachmentTemp();
				notifiedTemp.setId(temp1.getId());
				notifiedTemp.setShowNotifStatus(NotifStatus.HIDE);
				notifiedTemps.add(notifiedTemp);
			}
			
			emailAttachmentTempDao.updateBatch(notifiedTemps.toArray());
		} else if(ecType.equals(EmailContentType.CASA_COMPANY)){
			List<EmailAttachmentTemp> temps = emailAttachmentTempDao.getAllCASACompanyByRequester(userId, NotifStatus.SHOW);
			List<EmailAttachmentTemp> notifiedTemps = new ArrayList<EmailAttachmentTemp>();
			for(EmailAttachmentTemp temp1 : temps){
				EmailAttachmentTemp notifiedTemp = new EmailAttachmentTemp();
				notifiedTemp.setId(temp1.getId());
				notifiedTemp.setShowNotifStatus(NotifStatus.HIDE);
				notifiedTemps.add(notifiedTemp);
			}
			
			emailAttachmentTempDao.updateBatch(notifiedTemps.toArray());
		} else if(ecType.equals(EmailContentType.CC)){
			List<CCEmailAttachmentTemp> temps = ccEmailAttachmentTempDao.getAllByRequester(userId, NotifStatus.SHOW);
			List<CCEmailAttachmentTemp> notifiedTemps = new ArrayList<CCEmailAttachmentTemp>();
			
			for(CCEmailAttachmentTemp temp1 : temps){
				CCEmailAttachmentTemp notifiedTemp = new CCEmailAttachmentTemp();
				notifiedTemp.setId(temp1.getId());
				notifiedTemp.setShowNotifStatus(NotifStatus.HIDE);
				notifiedTemps.add(notifiedTemp);
			}
			
			ccEmailAttachmentTempDao.updateBatch(notifiedTemps.toArray());
		}
	}
	
	public int getTotalCCAttchCode(){
		return ccEmailAttachmentDao.getTotalCode();
	}
	
	public List<CCEmailAttachment> getAllCCAttchByCode() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return ccEmailAttachmentDao.getAllByCode();
	}
	
	public List<String> getAnotherBins(String bin, String attchCode){
		return ccEmailAttachmentDao.getAnotherBins(bin, attchCode);
	}
	
	public String getBinByCode(String attchCode){
		return ccEmailAttachmentDao.getBinByCode(attchCode);
	}
}
