package com.optima.nisp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.ModelAndView;

import com.optima.nisp.constanta.CategoryCons;
import com.optima.nisp.constanta.ParameterCons;
import com.optima.nisp.constanta.ReturnCode;
import com.optima.nisp.constanta.SendEmailStatus;
import com.optima.nisp.constanta.UserSession;
import com.optima.nisp.model.ApplicationParameter;
import com.optima.nisp.model.Button;
import com.optima.nisp.model.Group;
import com.optima.nisp.model.SendEmail;
import com.optima.nisp.model.SendEmailLog;
import com.optima.nisp.response.Response;
import com.optima.nisp.response.SendEmailLogResponse;
import com.optima.nisp.response.SendEmailResponse;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.service.ButtonService;
import com.optima.nisp.service.GroupService;
import com.optima.nisp.service.SendEmailService;
import com.optima.nisp.utility.CommonUtils;
import com.optima.nisp.utility.FileProcessing;
import com.optima.nisp.utility.HttpRequestUtil;

@Controller
@RequestMapping(value="send-email")
@SessionAttributes("userSession")
public class SendEmailController {
	private static final Logger logger = Logger.getLogger(SendEmailController.class);
	
	@Autowired
	private SendEmailService sendEmailService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private ButtonService buttonService;
	
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@Resource(name="confProp")
	private Properties properties;
	
	@RequestMapping(value="manage-page")
	public ModelAndView getManageSendEmailPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("monitoring-statement");
		try{
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				Long[] sessionGroupIds = userSession.getGroupIds();
				
				Button resendEmailBtn = buttonService.getByButtonUrl("send-email/resend");
				
				/*ID button-button yang ada pada page ini*/
				Long resendEmailBtnId = resendEmailBtn!=null ? resendEmailBtn.getButtonId() : -1;

				boolean showResendBtn = false;
				
				List<Group> resendBtnGroups = groupService.getByButtonId(resendEmailBtnId);

				if(hasIntersection(sessionGroupIds, resendBtnGroups))
					showResendBtn = true;
				
				mv.addObject("defaultStatus", SendEmailStatus.ALL_STATUS);
				mv.addObject("sentStatus", SendEmailStatus.SENT);
				mv.addObject("failedStatus", SendEmailStatus.FAILED);
				mv.addObject("expiredStatus", SendEmailStatus.EXPIRED);
				mv.addObject("queueStatus", SendEmailStatus.QUEUE);
				mv.addObject("processingStatus", SendEmailStatus.PROCESSING);
				mv.addObject("readStatus", SendEmailStatus.READ);
				mv.addObject("showResendButton", showResendBtn);
				mv.addObject("strDefRowCountOptions", CommonUtils.getDefaultRowCountOptions(applicationParameterService));
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
	
	@RequestMapping(value="validasi-page")
	public ModelAndView getValidasiPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("validasi-statement");
		try{
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				Long[] sessionGroupIds = userSession.getGroupIds();
				
				Button sendEmailBtn = buttonService.getByButtonUrl("send-email/send"); 
				
				/*ID button-button yang ada pada page ini*/
				Long sendEmailBtnId = sendEmailBtn!=null ? sendEmailBtn.getButtonId() : -1;

				boolean showSendBtn = false;
				
				List<Group> sendBtnGroups = groupService.getByButtonId(sendEmailBtnId);

				if(hasIntersection(sessionGroupIds, sendBtnGroups))
					showSendBtn = true;
				
				mv.addObject("notSentStatus", SendEmailStatus.NOT_SENT);
				mv.addObject("showSendButton", showSendBtn);
				mv.addObject("strDefRowCountOptions", CommonUtils.getDefaultRowCountOptions(applicationParameterService));
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
	
	private boolean hasIntersection(Long[] sessionGroupIds, List<Group> buttonGroups){
		for(Group g : buttonGroups){
			for(long sgid : sessionGroupIds){
				if(sgid == g.getGroupId())
					return true;
			}
		}
		
		return false;
	}
	
	@ResponseBody
	@RequestMapping(value="list", method=RequestMethod.GET)
	public Response getListEmail(Model model, @RequestParam("cifKey") String cifKey, @RequestParam("accountNumber") String accountNumber,
							@RequestParam("periode") String periode, @RequestParam("status") int status, @RequestParam("current") int pageNumber, @RequestParam("size") int totalPerPage){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				List<SendEmailResponse> emails = sendEmailService.getEmails(cifKey, accountNumber, periode, status, pageNumber, totalPerPage);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(emails);	
			} else {
				resp.setMessage("no session");
				resp.setResultCode(5001);
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="get-row", method=RequestMethod.GET)
	public Response getTotalRow(Model model, @RequestParam("cifKey") String cifKey, @RequestParam("accountNumber") String accountNumber,
							@RequestParam("periode") String periode, @RequestParam("status") int status){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				int totalRow = sendEmailService.getTotalRow(cifKey, accountNumber, periode, status);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(totalRow);
			} else {
				resp.setMessage("no session");
				resp.setResultCode(5001);
			}	
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="get-row-whitelist", method=RequestMethod.GET)
	public Response getTotalRowWhitelist(Model model, @RequestParam("cifKey") String cifKey, @RequestParam("accountNumber") String accountNumber,
							@RequestParam("periode") String periode, @RequestParam("status") int status){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				int totalRow = sendEmailService.getWhitelistTotalRow(cifKey, accountNumber, periode, status);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(totalRow);
			} else {
				resp.setMessage("no session");
				resp.setResultCode(5001);
			}			
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="send", method=RequestMethod.POST)
	public Response send(Model model, @RequestBody Long[] listId){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				List<SendEmail> emails = new ArrayList<SendEmail>();
//				
//				for(Long id : listId){
//					SendEmail email = sendEmailService.getById(id);
//					if(email!=null)
//						emails.add(email);
//				}
//				
//				sendEmailService.sendAll(emails);
				sendEmailService.addToQueue(listId);
				sendEmailService.sendNextQueue();
				resp.setResultCode(ReturnCode.SUCCESS);
				resp.setMessage("Berhasil mendaftarkan pengiriman email ke antrian.\nProgress pengiriman email dapat dilihat di menu Send Email Log.");
			} else {
				resp.setMessage("no session");
				resp.setResultCode(ReturnCode.SESSION_FAILED);
			}			
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="resend", method=RequestMethod.POST)
	public Response resend(Model model, HttpServletRequest request, @RequestBody Long[] listId){
		return send(model,listId);
	}
	
	@ResponseBody
	@RequestMapping(value="send-all", method=RequestMethod.POST)
	public Response sendAll(Model model, HttpServletRequest request, @RequestBody SendEmailResponse sendEmail){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				List<SendEmail> emails = sendEmailService.getEmails(sendEmail.getCifKey(), 
						sendEmail.getAccountNumber(), sendEmail.getPeriode(), sendEmail.getStatus());
			Long[] listId = new Long[emails.size()];
			int i=0;
			for (SendEmail se : emails) {
				listId[i] = se.getId();
				i++;
			}
			sendEmailService.addToQueue(listId);
			sendEmailService.sendNextQueue();
			resp.setResultCode(ReturnCode.SUCCESS);
			resp.setMessage("Berhasil mendaftarkan pengiriman email ke antrian.\nProgress pengiriman email dapat dilihat di menu Send Email Log.");
				} else {
					resp.setMessage("no session");
					resp.setResultCode(5001);
				}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="resend-all", method=RequestMethod.POST)
	public Response resendAll(Model model, HttpServletRequest request, @RequestBody SendEmailResponse sendEmail){		
		return sendAll(model, request, sendEmail);
	}
	
	@RequestMapping(value="log-page")
	public ModelAndView getSendEmailLogPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){
				mv.setViewName("send-email-log");
				mv.addObject("strDefRowCount", CommonUtils.getDefaultRowCount(applicationParameterService));
				mv.addObject("defaultStatus",SendEmailStatus.ALL_STATUS);
				mv.addObject("notSentStatus",SendEmailStatus.NOT_SENT);
				mv.addObject("sentStatus",SendEmailStatus.SENT);
				mv.addObject("failedStatus",SendEmailStatus.FAILED);
				mv.addObject("expiredStatus",SendEmailStatus.EXPIRED);
				mv.addObject("queueStatus", SendEmailStatus.QUEUE);
				mv.addObject("processingStatus", SendEmailStatus.PROCESSING);
				mv.addObject("readStatus", SendEmailStatus.READ);
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
	@RequestMapping(value="get-log-list", method=RequestMethod.GET)
	public Response getLogList(Model model, HttpServletRequest request, @RequestParam int start, 
			@RequestParam int length, @RequestParam String subject, @RequestParam String mailTo,
			@RequestParam String tglKirimFrom, @RequestParam String tglKirimTo, @RequestParam int status){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				List <SendEmailLog> logs = sendEmailService.getLogList(subject, mailTo, tglKirimFrom, tglKirimTo, status, start, length);
				List <SendEmailLogResponse> logsResponse = new ArrayList<SendEmailLogResponse>();
				Integer totalRow = sendEmailService.getTotalLogRow(subject, mailTo, tglKirimFrom, tglKirimTo, status);
				int number = 1;
				
				for(SendEmailLog log : logs){
					SendEmailLogResponse logResp = new SendEmailLogResponse();
					logResp.setNumber(start+number);
					logResp.setId(log.getId());
					logResp.setMailTo(log.getMailTo());
					logResp.setSubject(log.getSubject());
					logResp.setTime(log.getTime());
					logResp.setCifKey(log.getCifKey());
					logResp.setNamaRekening(log.getNamaRekening());
					logResp.setAccountNumber(log.getAccountNumber());
					
					if(log.getStatus()==SendEmailStatus.SENT)
						logResp.setStatus("Terkirim");
					else if(log.getStatus()==SendEmailStatus.FAILED)
						logResp.setStatus("Gagal Kirim");
					else if(log.getStatus()==SendEmailStatus.EXPIRED)
						logResp.setStatus("Expired");
					else if(log.getStatus()==SendEmailStatus.QUEUE)
						logResp.setStatus("Queue");
					else if(log.getStatus()==SendEmailStatus.PROCESSING)
						logResp.setStatus("Processing");
					else if(log.getStatus()==SendEmailStatus.READ)
						logResp.setStatus("Dibaca");
					else
						logResp.setStatus("");
					
					logsResponse.add(logResp);
					number++;
				}				
				
				resp.setData(logsResponse);
				resp.setRecordsFiltered(totalRow);
				resp.setRecordsTotal(totalRow);				
				resp.setResultCode(1000);
				resp.setMessage("Success");
			} else {
				resp.setMessage("no session");
				resp.setResultCode(5001);
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@RequestMapping(value="open-file")
	@ResponseBody
	public Response openFile(Model model, @RequestParam("id") Long id, @RequestParam int isReprint){		
		try{
			Response response = new Response();
			if(model.containsAttribute("userSession")){			
				String strContextPath = (String) properties.get("api_protocol") + "://"
						+ (String) properties.get("api_server");
				String strPort = (String) properties.get("api_port");
				if(!strPort.equals("80")){
					strContextPath += ":"+strPort;
				}
				strContextPath += (String) properties.get("api_context_path");
				
				SendEmail sendEmail = sendEmailService.getById(id);
				String tglCetakStr = formatDate(sendEmail.getTglCetak());
				
				ApplicationParameter appParam = applicationParameterService.getByKey(ParameterCons.AJAX_REQUEST_TIMEOUT);
				
				if(sendEmail!=null){
					int category = sendEmail.getCategory();
					String filename = FileProcessing.getFileName(sendEmail.getFilename());
					String urlParameters;
					int arto = Integer.parseInt(appParam.getValue());
					
					if(category==CategoryCons.CREDIT_CARD){
						urlParameters = "&accountNumber="+sendEmail.getAccountNumber()+"&year="+sendEmail.getPeriode().substring(0, 4)+"&month="+sendEmail.getPeriode().substring(4, 6)+"&isPrint=1&isReprint="+isReprint;
						strContextPath+="creditcard/print-billingstatement";
					}
					else {
						urlParameters = "&accountNumber="+sendEmail.getAccountNumber()+"&periode="+sendEmail.getPeriode()+"&category="+sendEmail.getCategory()+"&isPrint=1&isReprint="+isReprint+"&tglCetakStr="+tglCetakStr;
						strContextPath+="statement/print";
					}
					
					String res = HttpRequestUtil.request(strContextPath, "POST", urlParameters, arto);
					
					HashMap<String, String>data = new HashMap<String, String>();
					data.put("res", res);
					data.put("filename", filename);
					response.setData(data);
					response.setResultCode(ReturnCode.SUCCESS);
					
				}
			}else{
				response.setResultCode(ReturnCode.SESSION_FAILED);
			}
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	private String formatDate(Date param) {
		DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
		String result = "";
		
		try {
			result = df.format(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
