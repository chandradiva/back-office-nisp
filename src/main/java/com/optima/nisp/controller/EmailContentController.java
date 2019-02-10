package com.optima.nisp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.nisp.constanta.CategoryCons;
import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.EmailContentType;
import com.optima.nisp.constanta.NotifStatus;
import com.optima.nisp.constanta.ParameterCons;
import com.optima.nisp.constanta.ReviewStatus;
import com.optima.nisp.constanta.UserSession;
import com.optima.nisp.model.CCEmailAttachment;
import com.optima.nisp.model.CCEmailAttachmentTemp;
import com.optima.nisp.model.EmailAttachmentTemp;
import com.optima.nisp.model.EmailContentTemp;
import com.optima.nisp.model.SystemParameter;
import com.optima.nisp.model.api.EmailAttachmentApi;
import com.optima.nisp.model.api.EmailAttachmentCCApi;
import com.optima.nisp.model.api.EmailContentCASAApi;
import com.optima.nisp.model.api.EmailContentCCApi;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.EmailContentService;
import com.optima.nisp.service.SystemParameterService;
import com.optima.nisp.utility.CommonUtils;
import com.optima.nisp.utility.FileProcessing;

@Controller
@RequestMapping(value="email-content")
@SessionAttributes("userSession")
public class EmailContentController {

	private static final Logger logger = Logger.getLogger(EmailContentController.class);
	
	@Autowired
	private EmailContentService emailContentService;
	
	@Autowired
	private SystemParameterService systemParameterService;
	
	@Resource(name="confProp")
	private Properties properties;
	
	@ResponseBody
	@RequestMapping(value="get", method=RequestMethod.GET)
	public Response getEmailContentCASA(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				EmailContentCASAApi emailContent = emailContentService.getEmailContentCASA();
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(emailContent);
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}		
	}
	
	@ResponseBody
	@RequestMapping(value="get-cc", method=RequestMethod.GET)
	public Response getEmailContentCC(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				EmailContentCCApi emailContent = emailContentService.getEmailContentCC();
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(emailContent);
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="get-casa-company", method=RequestMethod.GET)
	public Response getEmailContentCASACompany(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				EmailContentCASAApi emailContent = emailContentService.getEmailContentCASACompany();
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(emailContent);
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}		
	}
	
	@RequestMapping(value="manage-page")
	public ModelAndView getManageEmailContentPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				boolean showInfo = false;
				boolean showInfoReq = false;
				boolean showCancel = false;
				boolean showSave = false;
				boolean showNotif = false;
				String requestStatus = "";
				String reviewer = "";
				
				boolean showInfoCC = false;
				boolean showInfoReqCC = false;
				boolean showCancelCC = false;
				boolean showSaveCC = false;
				boolean showNotifCC = false;
				String requestStatusCC = "";
				String reviewerCC = "";
				
				boolean showInfoCasaCompany = false;
				boolean showInfoReqCasaCompany = false;
				boolean showCancelCasaCompany = false;
				boolean showSaveCasaCompany = false;
				boolean showNotifCasaCompany = false;
				String requestStatusCasaCompany = "";
				String reviewerCasaCompany = "";
				
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				SystemParameter sysParam = systemParameterService.getByKey(ParameterCons.TOTAL_SIZE_FILE_ATTACHMENT);
				Long maxSize = CommonCons.DEFAULT_MAX_ATTACHMENT_SIZE;
				EmailContentTemp ecCasaTemp = emailContentService.getEmailContentTempCASA();
				EmailContentTemp ecCasaTempCompany = emailContentService.getEmailContentTempCASACompany();
				List<EmailAttachmentTemp> attchCasaTemp = emailContentService.getAttachmentTempCASA();
				List<EmailAttachmentTemp> attchCasaTempCompany = emailContentService.getAttachmentTempCASACompany();
				EmailContentTemp ecCcTemp = emailContentService.getEmailContentTempCC();
				EmailAttachmentTemp attchCcTemp = emailContentService.getAttachmentTempByCategory(CategoryCons.CREDIT_CARD);
				
				if(sysParam!=null){
					if(sysParam.getValue() != null){
						try {
							maxSize = Long.parseLong(sysParam.getValue());
						} catch(Exception e){
							maxSize = CommonCons.DEFAULT_MAX_ATTACHMENT_SIZE;
						}
					}
				}
				
				if(ecCasaTemp!=null || attchCasaTemp.size()>0){
					if(ecCasaTemp!=null){
						if(ecCasaTemp.getUpdatedBy().equalsIgnoreCase(userSession.getUsername())){
							showInfo = true;
							showCancel = true;
						} else {
							showInfoReq = true;
						}					
					} else { // attchCasaTemp > 0
						if(attchCasaTemp.get(0).getUpdatedBy().equalsIgnoreCase(userSession.getUsername())){
							showInfo = true;
							showCancel = true;
						} else {
							showInfoReq = true;
						}					
					}				
				} else {
					showSave = true;
				}
				
				if(ecCasaTempCompany!=null || attchCasaTempCompany.size()>0){
					if(ecCasaTempCompany!=null){
						if(ecCasaTempCompany.getUpdatedBy().equalsIgnoreCase(userSession.getUsername())){
							showInfoCasaCompany = true;
							showCancelCasaCompany = true;
						} else {
							showInfoReqCasaCompany = true;
						}					
					} else { // attchCasaTemp > 0
						if(attchCasaTempCompany.get(0).getUpdatedBy().equalsIgnoreCase(userSession.getUsername())){
							showInfoCasaCompany = true;
							showCancelCasaCompany = true;
						} else {
							showInfoReqCasaCompany = true;
						}					
					}				
				} else {
					showSaveCasaCompany = true;
				}
				
				if(ecCcTemp!=null || attchCcTemp!=null){
					if(ecCcTemp != null){
						if(ecCcTemp.getUpdatedBy().equalsIgnoreCase(userSession.getUsername())){
							showInfoCC = true;
							showCancelCC = true;
						} else {
							showInfoReqCC = true;
						}
					} else {
						if(attchCcTemp.getUpdatedBy().equalsIgnoreCase(userSession.getUsername())){
							showInfoCC = true;
							showCancelCC = true;
						} else {
							showInfoReqCC = true;
						}
					}				
				} else {
					showSaveCC = true;
				}
				
				/*Kepentingan notifikasi*/
				//CASA
				List<EmailAttachmentTemp> temps = emailContentService.getAttachmentTempCASAByRequester(userSession.getUsername(), NotifStatus.SHOW);
				EmailContentTemp ecTemp = emailContentService.getByRequester(userSession.getUsername(), NotifStatus.SHOW, EmailContentType.CASA);
				
				if(temps.size()>0 || ecTemp!=null){				
					if(ecTemp!=null){
						requestStatus = ecTemp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewer = ecTemp.getReviewedBy();
					} else { //atchTemps > 0
						EmailAttachmentTemp temp = temps.get(0);
						requestStatus = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewer = temp.getReviewedBy();
					}
					
					showNotif = true;
				}
				
				/*Kepentingan notifikasi*/
				//CASA Company
				List<EmailAttachmentTemp> tempsCasaCompany = emailContentService.getAttachmentTempCASACompanyByRequester(userSession.getUsername(), NotifStatus.SHOW);
				EmailContentTemp ecTempCasaCompany = emailContentService.getByRequester(userSession.getUsername(), NotifStatus.SHOW, EmailContentType.CASA_COMPANY);
				
				if(tempsCasaCompany.size()>0 || ecTempCasaCompany!=null){				
					if(ecTempCasaCompany!=null){
						requestStatusCasaCompany = ecTempCasaCompany.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewerCasaCompany = ecTempCasaCompany.getReviewedBy();
					} else { //atchTemps > 0
						EmailAttachmentTemp temp = tempsCasaCompany.get(0);
						requestStatusCasaCompany = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewerCasaCompany = temp.getReviewedBy();
					}
					
					showNotifCasaCompany = true;
				}
				
				/*Kepentingan notifikasi*/
				//CC
				List<CCEmailAttachmentTemp> atchTempCC = emailContentService.getAttachmentTempCCByRequester(userSession.getUsername(), NotifStatus.SHOW);
				EmailContentTemp ecTempCC = emailContentService.getByRequester(userSession.getUsername(), NotifStatus.SHOW, EmailContentType.CC);
				
				if(atchTempCC.size()>0 || ecTempCC!=null){
					if(ecTempCC!=null){
						requestStatusCC = ecTempCC.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewerCC = ecTempCC.getReviewedBy();
					}
					else {	//atchTempCC!=null
						CCEmailAttachmentTemp temp = atchTempCC.get(0);
						requestStatusCC = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewerCC = temp.getReviewedBy();
					}
					showNotifCC = true;
				}
				
				/*Kepentingan CCAttachments*/
				List<CCEmailAttachment> ccAttchs = emailContentService.getAllCCAttchByCode();
				Map<String, String> binMap = new HashMap<String, String>();
				
				for (CCEmailAttachment ccEmailAttachment : ccAttchs) {
					List<String> anotherBins = emailContentService.getAnotherBins(ccEmailAttachment.getBin(), ccEmailAttachment.getAttchCode());
					
					String otherBin = "";
					for (String anotherBin : anotherBins) {
						otherBin += ", BIN " + anotherBin;
					}
					
					binMap.put(ccEmailAttachment.getBin(), otherBin);
				}
				
				mv.setViewName("manage-email-content");
				mv.addObject("showInfo", showInfo);
				mv.addObject("showInfoReq", showInfoReq);
				mv.addObject("showCancel", showCancel);
				mv.addObject("showSave", showSave);
				mv.addObject("showNotif", showNotif);
				mv.addObject("requestStatus", requestStatus);
				mv.addObject("reviewer", reviewer);
				
				mv.addObject("showInfoCasaCompany", showInfoCasaCompany);
				mv.addObject("showInfoReqCasaCompany", showInfoReqCasaCompany);
				mv.addObject("showCancelCasaCompany", showCancelCasaCompany);
				mv.addObject("showSaveCasaCompany", showSaveCasaCompany);
				mv.addObject("showNotifCasaCompany", showNotifCasaCompany);
				mv.addObject("requestStatusCasaCompany", requestStatusCasaCompany);
				mv.addObject("reviewerCasaCompany", reviewerCasaCompany);
				
				mv.addObject("showInfoCC", showInfoCC);
				mv.addObject("showInfoReqCC", showInfoReqCC);
				mv.addObject("showCancelCC", showCancelCC);
				mv.addObject("showSaveCC", showSaveCC);
				mv.addObject("showNotifCC", showNotifCC);
				mv.addObject("requestStatusCC", requestStatusCC);
				mv.addObject("reviewerCC", reviewerCC);
				
				mv.addObject("maxSize", maxSize);
				
				mv.addObject("mcyId", CategoryCons.MULTI_CURRENCY);
				mv.addObject("giro1Id", CategoryCons.GIRO_NON_KRK);
				mv.addObject("giro2Id", CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN);
				mv.addObject("giro3Id", CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN);
				mv.addObject("giro4Id", CategoryCons.GIRO_TABUNGAN_HARIAN);
				mv.addObject("mcbId", CategoryCons.MULTI_CURRENCY_BILLINGUAL);
				
				mv.addObject("mcyIdCasaCompany", CategoryCons.MULTI_CURRENCY_COMPANY);
				mv.addObject("giro1IdCasaCompany", CategoryCons.GIRO_NON_KRK_COMPANY);
				mv.addObject("giro2IdCasaCompany", CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY);
				mv.addObject("giro3IdCasaCompany", CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY);
				mv.addObject("giro4IdCasaCompany", CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY);
				mv.addObject("mcbIdCasaCompany", CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY);
				
				mv.addObject("ccAttchs", ccAttchs);
				mv.addObject("binMap", binMap);
			} else {
				mv.setViewName("redirect-page");
				mv.addObject("url","login");
			}
			
			
		}catch( Exception e){
			e.printStackTrace();
			logger.error("Error", e);
			mv.setViewName("redirect-page");
			mv.addObject("url","login");
		}
		
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="save1", method=RequestMethod.PUT)
	public Response updateEmailContent(Model model, HttpServletRequest request, @RequestBody EmailContentCASAApi emailContent){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(emailContentService.getEmailContentTempCASA()==null && emailContentService.getAttachmentTempCASA().size()==0){	//Apakah sedang tidak ada request perubahan data?
					/*Set Attachment*/
					if(emailContent.getAttachment1().getNewAttachment().equals(emailContent.getAttachment1().getOldAttachment()))
						emailContent.getAttachment1().setNewAttachment(null);
					else //Berarti attachment 1 dihapus
						emailContent.getAttachment1().setNewAttachment("");
					
					if(emailContent.getAttachment2().getNewAttachment().equals(emailContent.getAttachment2().getOldAttachment()))
						emailContent.getAttachment2().setNewAttachment(null);
					else //Berarti attachment 2 dihapus
						emailContent.getAttachment2().setNewAttachment("");
					
					if(emailContent.getAttachment3().getNewAttachment().equals(emailContent.getAttachment3().getOldAttachment()))
						emailContent.getAttachment3().setNewAttachment(null);
					else //Berarti attachment 3 dihapus
						emailContent.getAttachment3().setNewAttachment("");
					
					if(emailContent.getAttachment4().getNewAttachment().equals(emailContent.getAttachment4().getOldAttachment()))
						emailContent.getAttachment4().setNewAttachment(null);
					else //Berarti attachment 4 dihapus
						emailContent.getAttachment4().setNewAttachment("");
					
					if(emailContent.getAttachment5().getNewAttachment().equals(emailContent.getAttachment5().getOldAttachment()))
						emailContent.getAttachment5().setNewAttachment(null);
					else //Berarti attachment 5 dihapus
						emailContent.getAttachment5().setNewAttachment("");
					
					if(emailContent.getAttachment6().getNewAttachment().equals(emailContent.getAttachment6().getOldAttachment()))
						emailContent.getAttachment6().setNewAttachment(null);
					else //Berarti attachment 6 dihapus
						emailContent.getAttachment6().setNewAttachment("");
					
					if(emailContent.getId()==-1){
						emailContent.setId(null);
					}
					
					emailContentService.insertToTempCASA(emailContent, userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses request update data");
				}			
				else {
					resp.setMessage("Maaf, saat ini sedang ada request perubahan data");
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="save1-casa-company", method=RequestMethod.PUT)
	public Response updateEmailContentCasaCompany(Model model, HttpServletRequest request, @RequestBody EmailContentCASAApi emailContent){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(emailContentService.getEmailContentTempCASACompany()==null && emailContentService.getAttachmentTempCASACompany().size()==0){	//Apakah sedang tidak ada request perubahan data?
					/*Set Attachment*/
					if(emailContent.getAttachment1().getNewAttachment().equals(emailContent.getAttachment1().getOldAttachment()))
						emailContent.getAttachment1().setNewAttachment(null);
					else //Berarti attachment 1 dihapus
						emailContent.getAttachment1().setNewAttachment("");
					
					if(emailContent.getAttachment2().getNewAttachment().equals(emailContent.getAttachment2().getOldAttachment()))
						emailContent.getAttachment2().setNewAttachment(null);
					else //Berarti attachment 2 dihapus
						emailContent.getAttachment2().setNewAttachment("");
					
					if(emailContent.getAttachment3().getNewAttachment().equals(emailContent.getAttachment3().getOldAttachment()))
						emailContent.getAttachment3().setNewAttachment(null);
					else //Berarti attachment 3 dihapus
						emailContent.getAttachment3().setNewAttachment("");
					
					if(emailContent.getAttachment4().getNewAttachment().equals(emailContent.getAttachment4().getOldAttachment()))
						emailContent.getAttachment4().setNewAttachment(null);
					else //Berarti attachment 4 dihapus
						emailContent.getAttachment4().setNewAttachment("");
					
					if(emailContent.getAttachment5().getNewAttachment().equals(emailContent.getAttachment5().getOldAttachment()))
						emailContent.getAttachment5().setNewAttachment(null);
					else //Berarti attachment 5 dihapus
						emailContent.getAttachment5().setNewAttachment("");
					
					if(emailContent.getAttachment6().getNewAttachment().equals(emailContent.getAttachment6().getOldAttachment()))
						emailContent.getAttachment6().setNewAttachment(null);
					else //Berarti attachment 6 dihapus
						emailContent.getAttachment6().setNewAttachment("");
					
					if(emailContent.getId()==-1){
						emailContent.setId(null);
					}
					
					emailContentService.insertToTempCASACompany(emailContent, userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses request update data");
				}			
				else {
					resp.setMessage("Maaf, saat ini sedang ada request perubahan data");
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="save1-cc", method=RequestMethod.PUT)
	public Response updateEmailContentCC(Model model, HttpServletRequest request, @RequestBody EmailContentCCApi emailContent){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(emailContentService.getEmailContentTempCC()==null && emailContentService.getAttachmentTempCC().size()==0){	//Apakah sedang tidak ada request perubahan data?					
					for(EmailAttachmentCCApi ccAttch : emailContent.getAttachments()){
						if(ccAttch.getNewAttachment().equals(ccAttch.getOldAttachment())){
							ccAttch.setNewAttachment(null);
						}
						else {
							ccAttch.setNewAttachment("");	//attachment dihapus
						}
					}
					
					if(emailContent.getId()==-1){
						emailContent.setId(null);
					}
					
					emailContentService.insertToTempCC(emailContent, userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses");
				} else {
					resp.setMessage("Maaf, saat ini sedang ada request perubahan data");
				}	
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/save2", method = RequestMethod.POST)
	public Response upload(Model model, MultipartHttpServletRequest request, HttpServletResponse response) {                 
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(emailContentService.getEmailContentTempCASA()==null && emailContentService.getAttachmentTempCASA().size()==0){	//Apakah sedang tidak ada request perubahan data?
					Map<String, Object> params = request.getParameterMap();
					EmailContentCASAApi emailContent = new EmailContentCASAApi();
					EmailAttachmentApi eaApi1 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi2 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi3 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi4 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi5 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi6 = new EmailAttachmentApi();
					String folderPath = (String) properties.get("email_attch_dir");
					SystemParameter sysParam = systemParameterService.getByKey(ParameterCons.TOTAL_SIZE_FILE_ATTACHMENT);
					long maxSize = Long.parseLong(sysParam.getValue());
					
					Iterator<String> itr =  request.getFileNames();		
					
					while (itr.hasNext()) {
				        String key = (String) itr.next();
				        			        
				        MultipartFile mpf = request.getFile(key);
				        params.put(key, mpf);
				        
				        String allowedContentType = "application/pdf";
						if(!mpf.getContentType().equals(allowedContentType)){
							resp.setMessage("Format File tidak diijinkan. Hanya PDF yang diperbolehkan");
							resp.setResultCode(1001);
							return resp;
						}
						
						long fileSize = mpf.getSize()/1024;
						if(fileSize > maxSize){
							resp.setMessage("Ukuran File melebihi batas yang ditetapkan.\nUkuran file maksimal yang "
											+ "diperbolehkan adalah " + maxSize + " KB");
							resp.setResultCode(1002);
							return resp;
						}
				    }	
					
					emailContent.setBody(((String[])params.get("body"))[0]);
					emailContent.setFooter(((String[])params.get("footer"))[0]);
					emailContent.setHeader(((String[])params.get("header"))[0]);
					emailContent.setId(Long.parseLong(((String[])params.get("id"))[0]));	
					emailContent.setOldBody(((String[]) params.get("oldBody"))[0]);
					emailContent.setOldHeader(((String[]) params.get("oldHeader"))[0]);
					emailContent.setOldPembuka1(((String[]) params.get("oldPembuka1"))[0]);
					emailContent.setOldPembuka2(((String[]) params.get("oldPembuka2"))[0]);
					emailContent.setOldPenutup(((String[]) params.get("oldPenutup"))[0]);
					emailContent.setOldFooter(((String[]) params.get("oldFooter"))[0]);
					emailContent.setPembuka1(((String[]) params.get("pembuka1"))[0]);
					emailContent.setPembuka2(((String[]) params.get("pembuka2"))[0]);
					emailContent.setPenutup(((String[]) params.get("penutup"))[0]);
					eaApi1.setOldAttachment(((String[]) params.get("oldAttachment1"))[0]);
					eaApi2.setOldAttachment(((String[]) params.get("oldAttachment2"))[0]);
					eaApi3.setOldAttachment(((String[]) params.get("oldAttachment3"))[0]);
					eaApi4.setOldAttachment(((String[]) params.get("oldAttachment4"))[0]);
					eaApi5.setOldAttachment(((String[]) params.get("oldAttachment5"))[0]);
					eaApi6.setOldAttachment(((String[]) params.get("oldAttachment6"))[0]);
					
					emailContent.setAttachment1(eaApi1);
					emailContent.setAttachment2(eaApi2);
					emailContent.setAttachment3(eaApi3);
					emailContent.setAttachment4(eaApi4);
					emailContent.setAttachment5(eaApi5);
					emailContent.setAttachment6(eaApi6);
					
					String atch1 = ((String[]) params.get("newAttachment1"))[0];
					String atch2 = ((String[]) params.get("newAttachment2"))[0];
					String atch3 = ((String[]) params.get("newAttachment3"))[0];
					String atch4 = ((String[]) params.get("newAttachment4"))[0];
					String atch5 = ((String[]) params.get("newAttachment5"))[0];
					String atch6 = ((String[]) params.get("newAttachment6"))[0];
					
					/*Set Attachment jika attachment dihapus*/
					if(atch1.equals(emailContent.getAttachment1().getOldAttachment()))
						emailContent.getAttachment1().setNewAttachment(null);
					else //Berarti attachment 1 dihapus	atau di update
						emailContent.getAttachment1().setNewAttachment("");
					
					if(atch2.equals(emailContent.getAttachment2().getOldAttachment()))
						emailContent.getAttachment2().setNewAttachment(null);
					else //Berarti attachment 2 dihapus	atau di update
						emailContent.getAttachment2().setNewAttachment("");
					
					if(atch3.equals(emailContent.getAttachment3().getOldAttachment()))
						emailContent.getAttachment3().setNewAttachment(null);
					else //Berarti attachment 3 dihapus	atau di update
						emailContent.getAttachment3().setNewAttachment("");
					
					if(atch4.equals(emailContent.getAttachment4().getOldAttachment()))
						emailContent.getAttachment4().setNewAttachment(null);
					else //Berarti attachment 4 dihapus	atau di update
						emailContent.getAttachment4().setNewAttachment("");
					
					if(atch5.equals(emailContent.getAttachment5().getOldAttachment()))
						emailContent.getAttachment5().setNewAttachment(null);
					else //Berarti attachment 5 dihapus	atau di update
						emailContent.getAttachment5().setNewAttachment("");
					
					if(atch6.equals(emailContent.getAttachment6().getOldAttachment()))
						emailContent.getAttachment6().setNewAttachment(null);
					else //Berarti attachment 6 dihapus	atau di update
						emailContent.getAttachment6().setNewAttachment("");
					
					//Set Attachment jika ada update attachment
					if(params.containsKey("attachment1")){
						MultipartFile mpf = (MultipartFile) params.get("attachment1");
						emailContent.getAttachment1().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment2")){
						MultipartFile mpf = (MultipartFile) params.get("attachment2");
						emailContent.getAttachment2().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment3")){
						MultipartFile mpf = (MultipartFile) params.get("attachment3");
						emailContent.getAttachment3().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment4")){
						MultipartFile mpf = (MultipartFile) params.get("attachment4");
						emailContent.getAttachment4().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment5")){
						MultipartFile mpf = (MultipartFile) params.get("attachment5");
						emailContent.getAttachment5().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment6")){
						MultipartFile mpf = (MultipartFile) params.get("attachment6");
						emailContent.getAttachment6().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					
					if(emailContent.getId()==-1){
						emailContent.setId(null);
					}
					
					emailContentService.insertToTempCASA(emailContent, userSession.getUsername());			
					resp.setResultCode(1000);
					resp.setMessage("Sukses update data");
				}
				else {
					resp.setMessage("Maaf, saat ini sedang ada request perubahan data");
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/save2-casa-company", method = RequestMethod.POST)
	public Response uploadCasaCompany(Model model, MultipartHttpServletRequest request, HttpServletResponse response) {                 
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(emailContentService.getEmailContentTempCASACompany()==null && emailContentService.getAttachmentTempCASACompany().size()==0){	//Apakah sedang tidak ada request perubahan data?
					Map<String, Object> params = request.getParameterMap();
					EmailContentCASAApi emailContent = new EmailContentCASAApi();
					EmailAttachmentApi eaApi1 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi2 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi3 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi4 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi5 = new EmailAttachmentApi();
					EmailAttachmentApi eaApi6 = new EmailAttachmentApi();
					String folderPath = (String) properties.get("email_attch_dir");
					SystemParameter sysParam = systemParameterService.getByKey(ParameterCons.TOTAL_SIZE_FILE_ATTACHMENT);
					long maxSize = Long.parseLong(sysParam.getValue());
					
					Iterator<String> itr =  request.getFileNames();		
					
					while (itr.hasNext()) {
				        String key = (String) itr.next();
				        			        
				        MultipartFile mpf = request.getFile(key);
				        params.put(key, mpf);
				        
				        String allowedContentType = "application/pdf";
						if(!mpf.getContentType().equals(allowedContentType)){
							resp.setMessage("Format File tidak diijinkan. Hanya PDF yang diperbolehkan");
							resp.setResultCode(1001);
							return resp;
						}
						
						long fileSize = mpf.getSize()/1024;
						if(fileSize > maxSize){
							resp.setMessage("Ukuran File melebihi batas yang ditetapkan.\nUkuran file maksimal yang "
											+ "diperbolehkan adalah " + maxSize + " KB");
							resp.setResultCode(1002);
							return resp;
						}
				    }	
					
					emailContent.setBody(((String[])params.get("body"))[0]);
					emailContent.setFooter(((String[])params.get("footer"))[0]);
					emailContent.setHeader(((String[])params.get("header"))[0]);
					emailContent.setId(Long.parseLong(((String[])params.get("id"))[0]));	
					emailContent.setOldBody(((String[]) params.get("oldBody"))[0]);
					emailContent.setOldHeader(((String[]) params.get("oldHeader"))[0]);
					emailContent.setOldPembuka1(((String[]) params.get("oldPembuka1"))[0]);
					emailContent.setOldPembuka2(((String[]) params.get("oldPembuka2"))[0]);
					emailContent.setOldPenutup(((String[]) params.get("oldPenutup"))[0]);
					emailContent.setOldFooter(((String[]) params.get("oldFooter"))[0]);
					emailContent.setPembuka1(((String[]) params.get("pembuka1"))[0]);
					emailContent.setPembuka2(((String[]) params.get("pembuka2"))[0]);
					emailContent.setPenutup(((String[]) params.get("penutup"))[0]);
					eaApi1.setOldAttachment(((String[]) params.get("oldAttachment1"))[0]);
					eaApi2.setOldAttachment(((String[]) params.get("oldAttachment2"))[0]);
					eaApi3.setOldAttachment(((String[]) params.get("oldAttachment3"))[0]);
					eaApi4.setOldAttachment(((String[]) params.get("oldAttachment4"))[0]);
					eaApi5.setOldAttachment(((String[]) params.get("oldAttachment5"))[0]);
					eaApi6.setOldAttachment(((String[]) params.get("oldAttachment6"))[0]);
					
					emailContent.setAttachment1(eaApi1);
					emailContent.setAttachment2(eaApi2);
					emailContent.setAttachment3(eaApi3);
					emailContent.setAttachment4(eaApi4);
					emailContent.setAttachment5(eaApi5);
					emailContent.setAttachment6(eaApi6);
					
					String atch1 = ((String[]) params.get("newAttachment1"))[0];
					String atch2 = ((String[]) params.get("newAttachment2"))[0];
					String atch3 = ((String[]) params.get("newAttachment3"))[0];
					String atch4 = ((String[]) params.get("newAttachment4"))[0];
					String atch5 = ((String[]) params.get("newAttachment5"))[0];
					String atch6 = ((String[]) params.get("newAttachment6"))[0];
					
					/*Set Attachment jika attachment dihapus*/
					if(atch1.equals(emailContent.getAttachment1().getOldAttachment()))
						emailContent.getAttachment1().setNewAttachment(null);
					else //Berarti attachment 1 dihapus	atau di update
						emailContent.getAttachment1().setNewAttachment("");
					
					if(atch2.equals(emailContent.getAttachment2().getOldAttachment()))
						emailContent.getAttachment2().setNewAttachment(null);
					else //Berarti attachment 2 dihapus	atau di update
						emailContent.getAttachment2().setNewAttachment("");
					
					if(atch3.equals(emailContent.getAttachment3().getOldAttachment()))
						emailContent.getAttachment3().setNewAttachment(null);
					else //Berarti attachment 3 dihapus	atau di update
						emailContent.getAttachment3().setNewAttachment("");
					
					if(atch4.equals(emailContent.getAttachment4().getOldAttachment()))
						emailContent.getAttachment4().setNewAttachment(null);
					else //Berarti attachment 4 dihapus	atau di update
						emailContent.getAttachment4().setNewAttachment("");
					
					if(atch5.equals(emailContent.getAttachment5().getOldAttachment()))
						emailContent.getAttachment5().setNewAttachment(null);
					else //Berarti attachment 5 dihapus	atau di update
						emailContent.getAttachment5().setNewAttachment("");
					
					if(atch6.equals(emailContent.getAttachment6().getOldAttachment()))
						emailContent.getAttachment6().setNewAttachment(null);
					else //Berarti attachment 6 dihapus	atau di update
						emailContent.getAttachment6().setNewAttachment("");
					
					//Set Attachment jika ada update attachment
					if(params.containsKey("attachment1")){
						MultipartFile mpf = (MultipartFile) params.get("attachment1");
						emailContent.getAttachment1().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment2")){
						MultipartFile mpf = (MultipartFile) params.get("attachment2");
						emailContent.getAttachment2().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment3")){
						MultipartFile mpf = (MultipartFile) params.get("attachment3");
						emailContent.getAttachment3().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment4")){
						MultipartFile mpf = (MultipartFile) params.get("attachment4");
						emailContent.getAttachment4().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment5")){
						MultipartFile mpf = (MultipartFile) params.get("attachment5");
						emailContent.getAttachment5().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					if(params.containsKey("attachment6")){
						MultipartFile mpf = (MultipartFile) params.get("attachment6");
						emailContent.getAttachment6().setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
						FileProcessing.saveEmailContentAttachment(mpf, folderPath);
					}
					
					if(emailContent.getId()==-1){
						emailContent.setId(null);
					}
					
					emailContentService.insertToTempCASACompany(emailContent, userSession.getUsername());			
					resp.setResultCode(1000);
					resp.setMessage("Sukses update data");
				}
				else {
					resp.setMessage("Maaf, saat ini sedang ada request perubahan data");
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/save2-cc", method = RequestMethod.POST)
	public Response uploadCC(Model model, MultipartHttpServletRequest request, HttpServletResponse response) {     
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(emailContentService.getEmailContentTempCC()==null && emailContentService.getAttachmentTempCC().size()==0){	//Apakah sedang tidak ada request perubahan data?
					Map<String, Object> params = request.getParameterMap();
					EmailContentCCApi emailContent = new EmailContentCCApi();
					List<EmailAttachmentCCApi> attchs = new ArrayList<EmailAttachmentCCApi>();
					String folderPath = (String) properties.get("email_attch_dir");
					SystemParameter sysParam = systemParameterService.getByKey(ParameterCons.TOTAL_SIZE_FILE_ATTACHMENT);
					long maxSize = Long.parseLong(sysParam.getValue());
					
					emailContent.setAttachments(attchs);
					
					Iterator<String> itr =  request.getFileNames();		
					while (itr.hasNext()) {
				        String key = (String) itr.next();
				        			        
				        MultipartFile mpf = request.getFile(key);
				        params.put(key, mpf);
				        
				        String allowedContentType = "application/pdf";
						if(!mpf.getContentType().equals(allowedContentType)){
							resp.setMessage("Format File tidak diijinkan. Hanya PDF yang diperbolehkan");
							resp.setResultCode(1001);
							return resp;
						}
						
						long fileSize = mpf.getSize()/1024;
						if(fileSize > maxSize){
							resp.setMessage("Ukuran File melebihi batas yang ditetapkan.\nUkuran file maksimal yang "
											+ "diperbolehkan adalah " + maxSize + " KB");
							resp.setResultCode(1002);
							return resp;
						}
				    }	
					
					emailContent.setBody(((String[])params.get("body"))[0]);
					emailContent.setFooter(((String[])params.get("footer"))[0]);
					emailContent.setHeader(((String[])params.get("header"))[0]);
					emailContent.setId(Long.parseLong(((String[])params.get("id"))[0]));	
					emailContent.setOldBody(((String[]) params.get("oldBody"))[0]);
					emailContent.setOldHeader(((String[]) params.get("oldHeader"))[0]);
					emailContent.setOldPembuka1(((String[]) params.get("oldPembuka1"))[0]);
					emailContent.setOldPembuka2(((String[]) params.get("oldPembuka2"))[0]);
					emailContent.setOldPenutup(((String[]) params.get("oldPenutup"))[0]);
					emailContent.setOldFooter(((String[]) params.get("oldFooter"))[0]);
					emailContent.setPembuka1(((String[]) params.get("pembuka1"))[0]);
					emailContent.setPembuka2(((String[]) params.get("pembuka2"))[0]);
					emailContent.setPenutup(((String[]) params.get("penutup"))[0]);
					
					String attchCodes = ((String[]) params.get("attchCodes"))[0]; 
					if(!attchCodes.isEmpty()){
						String attchCodesArr[] = attchCodes.split("---");
						for(String attchCode : attchCodesArr){
							EmailAttachmentCCApi attch = new EmailAttachmentCCApi();
							attch.setOldAttachment(((String[]) params.get("oldAttachment"+attchCode))[0]);
							
							String newAttch = ((String[]) params.get("newAttachment"+attchCode))[0];
							if(newAttch.equals(attch.getOldAttachment())){
								attch.setNewAttachment(null);
							}
							else { //Berarti attachment dihapus atau di update
								attch.setNewAttachment("");
							}
							
							//Set Attachment jika ada update attachment
							if(params.containsKey("attachment"+attchCode)){
								MultipartFile mpf = (MultipartFile) params.get("attachment"+attchCode);
								attch.setNewAttachment(folderPath + CommonCons.FILE_SEPARATOR + mpf.getOriginalFilename());
								FileProcessing.saveEmailContentAttachment(mpf, folderPath);
							}
							
							attch.setAttchCode(attchCode);
							attchs.add(attch);
						}
					}
					
					if(emailContent.getId()==-1){
						emailContent.setId(null);
					}
					
					emailContentService.insertToTempCC(emailContent, userSession.getUsername());			
					resp.setResultCode(1000);
					resp.setMessage("Sukses update data");
				}
				else {
					resp.setMessage("Maaf, saat ini sedang ada request perubahan data");
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="approve", method=RequestMethod.PUT)
	public Response approve(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				emailContentService.approveCASA(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses..\nPerubahan data telah disimpan");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="approve-casa-company", method=RequestMethod.PUT)
	public Response approveCasaCompany(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				emailContentService.approveCASACompany(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses..\nPerubahan data telah disimpan");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="approve-cc", method=RequestMethod.PUT)
	public Response approveCC(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				emailContentService.approveCC(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses..\nPerubahan data telah disimpan");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="reject-casa-company", method=RequestMethod.PUT)
	public Response rejectCasaCompany(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				emailContentService.rejectCASACompany(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses.. \nPerubahan data telah ditolak");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="reject", method=RequestMethod.PUT)
	public Response reject(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				emailContentService.rejectCASA(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses.. \nPerubahan data telah ditolak");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="reject-cc", method=RequestMethod.PUT)
	public Response rejectCC(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				emailContentService.rejectCC(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses.. \nPerubahan data telah ditolak");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="cancel", method=RequestMethod.PUT)
	public Response cancel(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				emailContentService.cancelCASA(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses.. \nPermintaan perubahan data telah dibatalkan");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}	
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="cancel-casa-company", method=RequestMethod.PUT)
	public Response cancelCasaCompany(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				emailContentService.cancelCASACompany(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses.. \nPermintaan perubahan data telah dibatalkan");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}	
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="cancel-cc", method=RequestMethod.PUT)
	public Response cancelCC(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				emailContentService.cancelCC(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses.. \nPermintaan perubahan data telah dibatalkan");
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}	
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@RequestMapping(value="review-page")
	public ModelAndView getReviewPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){	
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				boolean showInfo = true;
				boolean showButtons = false;
				boolean showRequestInfo = false;
				boolean showNotif = false;
				String requestStatus = "";
				String reviewer = "";
				
				boolean showInfoCasaCompany = true;
				boolean showButtonsCasaCompany = false;
				boolean showRequestInfoCasaCompany = false;
				boolean showNotifCasaCompany = false;
				String requestStatusCasaCompany = "";
				String reviewerCasaCompany = "";
				
				boolean showInfoCC = true;
				boolean showButtonsCC = false;
				boolean showRequestInfoCC = false;
				boolean showNotifCC = false;
				String requestStatusCC = "";
				String reviewerCC = "";
				
				if(emailContentService.getEmailContentTempCASA()!=null || emailContentService.getAttachmentTempCASA()!=null && emailContentService.getAttachmentTempCASA().size()>0){
					showInfo = false;
				}
				
				if(emailContentService.getEmailContentTempCASACompany()!=null || emailContentService.getAttachmentTempCASACompany()!=null && emailContentService.getAttachmentTempCASACompany().size()>0){
					showInfoCasaCompany = false;
				}
				
				if(emailContentService.getEmailContentTempCC()!=null || emailContentService.getAttachmentTempCC().size()>0){
					showInfoCC = false;
				}
				
				String updatedBy = emailContentService.getCASAUpdatedBy();
				if(updatedBy==null){
					updatedBy = emailContentService.getCASAAttachmentUpdatedBy();
				}
				if(updatedBy!=null && !updatedBy.equalsIgnoreCase(userSession.getUsername()) && showInfo==false){
					showButtons = true;
				} else {
					if(updatedBy==null && !userSession.getUsername().isEmpty() && showInfo==false)
						showButtons = true;
				}
				
				String updatedCasaCompanyBy = emailContentService.getCASACompanyUpdatedBy();
				if(updatedCasaCompanyBy==null){
					updatedCasaCompanyBy = emailContentService.getCASACompanyAttachmentUpdatedBy();
				}
				if(updatedCasaCompanyBy!=null && !updatedCasaCompanyBy.equalsIgnoreCase(userSession.getUsername()) && showInfoCasaCompany==false){
					showButtonsCasaCompany = true;
				} else {
					if(updatedCasaCompanyBy==null && !userSession.getUsername().isEmpty() && showInfoCasaCompany==false)
						showButtonsCasaCompany = true;
				}
				
				String updatedByCC = emailContentService.getCCUpdatedBy();
				if(updatedByCC==null){
					updatedByCC = emailContentService.getCCAttachmentUpdatedBy();
				}
				if(updatedByCC!=null && !updatedByCC.equalsIgnoreCase(userSession.getUsername()) && showInfoCC==false){
					showButtonsCC = true;
				} else {
					if(updatedByCC==null && !userSession.getUsername().isEmpty() && showInfoCC==false)
						showButtonsCC = true;
				}
				
				if(showInfo==false){
					if(!updatedBy.equalsIgnoreCase(userSession.getUsername()))
						showRequestInfo = true;
				}
				
				if(showInfoCasaCompany==false){
					if(!updatedCasaCompanyBy.equalsIgnoreCase(userSession.getUsername()))
						showRequestInfoCasaCompany = true;
				}
				
				if(showInfoCC==false){
					if(!updatedByCC.equalsIgnoreCase(userSession.getUsername()))
						showRequestInfoCC = true;
				}
				
				/*Kepentingan notifikasi*/
				//CASA
				List<EmailAttachmentTemp> temps = emailContentService.getAttachmentTempCASAByRequester(userSession.getUsername(), NotifStatus.SHOW);
				EmailContentTemp ecTemp = emailContentService.getByRequester(userSession.getUsername(), NotifStatus.SHOW, EmailContentType.CASA);
				
				if(temps.size()>0 || ecTemp!=null){				
					if(ecTemp!=null){
						requestStatus = ecTemp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewer = ecTemp.getReviewedBy();
					} else { //atchTemps > 0
						EmailAttachmentTemp temp = temps.get(0);
						requestStatus = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewer = temp.getReviewedBy();
					}
					
					showNotif = true;
				}
				
				
				/*Kepentingan notifikasi*/
				//CASA Company
				List<EmailAttachmentTemp> tempsCasaCompany = emailContentService.getAttachmentTempCASACompanyByRequester(userSession.getUsername(), NotifStatus.SHOW);
				EmailContentTemp ecTempCasaCompany = emailContentService.getByRequester(userSession.getUsername(), NotifStatus.SHOW, EmailContentType.CASA_COMPANY);
				
				if(tempsCasaCompany.size()>0 || ecTempCasaCompany!=null){				
					if(ecTempCasaCompany!=null){
						requestStatusCasaCompany = ecTempCasaCompany.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewerCasaCompany = ecTempCasaCompany.getReviewedBy();
					} else { //atchTemps > 0
						EmailAttachmentTemp temp = temps.get(0);
						requestStatusCasaCompany = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewerCasaCompany = temp.getReviewedBy();
					}
					
					showNotifCasaCompany = true;
				}
				
				/*Kepentingan notifikasi*/
				//CC
				List<CCEmailAttachmentTemp> atchTempCC = emailContentService.getAttachmentTempCCByRequester(userSession.getUsername(), NotifStatus.SHOW);
				EmailContentTemp ecTempCC = emailContentService.getByRequester(userSession.getUsername(), NotifStatus.SHOW, EmailContentType.CC);
				
				if(atchTempCC.size()>0 || ecTempCC!=null){
					if(ecTempCC!=null){
						requestStatusCC = ecTempCC.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewerCC = ecTempCC.getReviewedBy();
					}
					else {	//atchTempCC!=null
						CCEmailAttachmentTemp temp = atchTempCC.get(0);
						requestStatusCC = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
						reviewerCC = temp.getReviewedBy();
					}
					showNotifCC = true;
				}
				
				/*Kepentingan CCAttachments*/
				List<CCEmailAttachment> ccAttchs = emailContentService.getAllCCAttchByCode();
				
				mv.setViewName("review-email-content");
				mv.addObject("showInfo",showInfo);
				mv.addObject("showButtons", showButtons);
				mv.addObject("showRequestInfo",showRequestInfo);
				mv.addObject("requestBy", updatedBy);
				mv.addObject("showNotif", showNotif);
				mv.addObject("requestStatus", requestStatus);
				mv.addObject("reviewer", reviewer);
				
				mv.addObject("showInfoCasaCompany",showInfoCasaCompany);
				mv.addObject("showButtonsCasaCompany", showButtonsCasaCompany);
				mv.addObject("showRequestInfoCasaCompany",showRequestInfoCasaCompany);
				mv.addObject("requestByCasaCompany", updatedCasaCompanyBy);
				mv.addObject("showNotifCasaCompany", showNotifCasaCompany);
				mv.addObject("requestStatusCasaCompany", requestStatusCasaCompany);
				mv.addObject("reviewerCasaCompany", reviewerCasaCompany);
				
				mv.addObject("showInfoCC",showInfoCC);
				mv.addObject("showButtonsCC", showButtonsCC);
				mv.addObject("showRequestInfoCC",showRequestInfoCC);
				mv.addObject("requestByCC", updatedByCC);
				mv.addObject("showNotifCC", showNotifCC);
				mv.addObject("requestStatusCC", requestStatusCC);
				mv.addObject("reviewerCC", reviewerCC);
				
				mv.addObject("mcyId", CategoryCons.MULTI_CURRENCY);
				mv.addObject("giro1Id", CategoryCons.GIRO_NON_KRK);
				mv.addObject("giro2Id", CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN);
				mv.addObject("giro3Id", CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN);
				mv.addObject("giro4Id", CategoryCons.GIRO_TABUNGAN_HARIAN);
				mv.addObject("mcbId", CategoryCons.MULTI_CURRENCY_BILLINGUAL);
				
				mv.addObject("mcyId", CategoryCons.MULTI_CURRENCY_COMPANY);
				mv.addObject("giro1Id", CategoryCons.GIRO_NON_KRK_COMPANY);
				mv.addObject("giro2Id", CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY);
				mv.addObject("giro3Id", CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY);
				mv.addObject("giro4Id", CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY);
				mv.addObject("mcbId", CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY);
				
				mv.addObject("ccAttchs", ccAttchs);
			} else {
				mv.setViewName("redirect-page");
				mv.addObject("url","login");
			}		
						
		}catch( Exception e){
			logger.error("Error", e);
			mv.setViewName("redirect-page");
			mv.addObject("url","login");
		}
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="get-unapproved", method=RequestMethod.GET)
	public Response getUnapproved(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				try {
					EmailContentCASAApi emailContents = emailContentService.getUnapprovedCASA();
					resp.setResultCode(1000);
					resp.setMessage("Sukses get data");
					resp.setData(emailContents);
				} catch(Exception e){
					resp.setResultCode(0);
					resp.setMessage(e.getMessage());
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="get-unapproved-casa-company", method=RequestMethod.GET)
	public Response getUnapprovedCasaCompany(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				try {
					EmailContentCASAApi emailContents = emailContentService.getUnapprovedCASACompany();
					resp.setResultCode(1000);
					resp.setMessage("Sukses get data");
					resp.setData(emailContents);
				} catch(Exception e){
					resp.setResultCode(0);
					resp.setMessage(e.getMessage());
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	
	@ResponseBody
	@RequestMapping(value="get-unapproved-cc", method=RequestMethod.GET)
	public Response getUnapprovedCC(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				try {
					EmailContentCCApi emailContents = emailContentService.getUnapprovedCC();
					resp.setResultCode(1000);
					resp.setMessage("Sukses get data");
					resp.setData(emailContents);
				} catch(Exception e){
					resp.setResultCode(0);
					resp.setMessage(e.getMessage());
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@RequestMapping(value="open-file")
	public void openFile(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam("catId") Integer catId){
		try{
			if(model.containsAttribute("userSession")){			
				EmailAttachmentTemp atchTemp = emailContentService.getAttachmentTempByCategory(catId);
				
				if(atchTemp!=null){
					String filename = FileProcessing.getFileName(atchTemp.getNewAttachment());
					
					File file = new File(atchTemp.getNewAttachment());
					InputStream is = new FileInputStream(file);
					
					ServletOutputStream outServlet = response.getOutputStream();
					
					byte [] buffer = new byte[10240];
					int bytesRead = 0;
					while((bytesRead = is.read(buffer)) != -1){
						outServlet.write(buffer, 0, bytesRead);
					}
					
					response.setContentType("application/pdf");
					response.setHeader("Content-disposition", "inline; filename=" + filename);
					response.setContentLength(buffer.length);
					is.close();
				}
			}
		}catch( Exception e){
			logger.error("Error", e);
			try{
				ObjectMapper mapper = new ObjectMapper();
				PrintWriter out = response.getWriter();				
				out.print(mapper.writeValueAsString(CommonUtils.responseException(e)));
			}catch( Exception e1){
				logger.error("Error", e1);
			}
		}		
	}
	
	@RequestMapping(value="open-file-cc")
	public void openFile(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam("attchCode") String attchCode){
		try{
			if(model.containsAttribute("userSession")){			
				String bin = emailContentService.getBinByCode(attchCode);
				CCEmailAttachmentTemp atchTemp = emailContentService.getAttachmentTempCCByBin(bin);
								
				if(atchTemp!=null){
					String filename = FileProcessing.getFileName(atchTemp.getNewAttachment());
					
					File file = new File(atchTemp.getNewAttachment());
					InputStream is = new FileInputStream(file);
					
					ServletOutputStream outServlet = response.getOutputStream();
					
					byte [] buffer = new byte[10240];
					int bytesRead = 0;
					while((bytesRead = is.read(buffer)) != -1){
						outServlet.write(buffer, 0, bytesRead);
					}
					
					response.setContentType("application/pdf");
					response.setHeader("Content-disposition", "inline; filename=" + filename);
					response.setContentLength(buffer.length);
					is.close();
				}
			}
		}catch( Exception e){
			logger.error("Error", e);
			try{
				ObjectMapper mapper = new ObjectMapper();
				PrintWriter out = response.getWriter();				
				out.print(mapper.writeValueAsString(CommonUtils.responseException(e)));
			}catch( Exception e1){
				logger.error("Error", e1);
			}
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="set-notif-off", method=RequestMethod.PUT)
	public Response setNotifOff(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				try {
					UserSession userSession = (UserSession) model.asMap().get("userSession");
					emailContentService.setNotifOff(userSession.getUsername(), EmailContentType.CASA);
					resp.setResultCode(1000);
					resp.setMessage("Sukses");
				} catch(Exception e){
					resp.setResultCode(0);
					resp.setMessage(e.getMessage());
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
				
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="set-notif-off-casa-company", method=RequestMethod.PUT)
	public Response setNotifOffCasaCompany(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				try {
					UserSession userSession = (UserSession) model.asMap().get("userSession");
					emailContentService.setNotifOff(userSession.getUsername(), EmailContentType.CASA_COMPANY);
					resp.setResultCode(1000);
					resp.setMessage("Sukses");
				} catch(Exception e){
					resp.setResultCode(0);
					resp.setMessage(e.getMessage());
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
				
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="set-notif-off-cc", method=RequestMethod.PUT)
	public Response setNotifOffCC(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				try {
					UserSession userSession = (UserSession) model.asMap().get("userSession");
					emailContentService.setNotifOff(userSession.getUsername(), EmailContentType.CC);
					resp.setResultCode(1000);
					resp.setMessage("Sukses");
				} catch(Exception e){
					resp.setResultCode(0);
					resp.setMessage(e.getMessage());
				}
			} else {
				resp.setResultCode(5001);
				resp.setMessage("No Session");
			}
				
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
}
