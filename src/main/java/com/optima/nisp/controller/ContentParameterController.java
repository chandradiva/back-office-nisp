package com.optima.nisp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.optima.nisp.constanta.NotifStatus;
import com.optima.nisp.constanta.ReviewStatus;
import com.optima.nisp.constanta.UserSession;
import com.optima.nisp.model.ContentParameter;
import com.optima.nisp.model.ContentParameterTemp;
import com.optima.nisp.model.api.ContentParameterApi;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ContentParameterService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="content-param")
@SessionAttributes("userSession")
public class ContentParameterController {

	private static final Logger logger = Logger.getLogger( ContentParameterController.class );
	
	@Autowired
	private ContentParameterService contentParameterService;
	
	@RequestMapping(value="manage")
	public ModelAndView getManageContentParamPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){
				boolean showCancel = false;
				boolean showSave = false;
				boolean showInfoRequest = false;
				boolean showNotif = false;
				String requestStatus = "";
				String reviewer = "";
				
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				List<ContentParameterTemp> appTemps = contentParameterService.getNotReviewed();
				
				if(appTemps.size()>0){
					if(appTemps.get(0).getUpdatedBy().equalsIgnoreCase(userSession.getUsername())){ //Jika yang melakukan request
						showCancel = true;
					} else {
						showInfoRequest = true;
					}
				}				
				else {
					showSave = true;
				}
				
				List<ContentParameterTemp> temps = contentParameterService.getByRequester(userSession.getUsername(), NotifStatus.SHOW);
				
				if(temps.size()>0){
					ContentParameterTemp temp = temps.get(0);
					
					showNotif = true;
					requestStatus = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
					reviewer = temp.getReviewedBy();
				}
				
				mv.setViewName("manage-contentparam");
				mv.addObject("showCancel",showCancel);
				mv.addObject("showSave", showSave);
				mv.addObject("showNotif", showNotif);
				mv.addObject("showInfoRequest", showInfoRequest);
				mv.addObject("requestStatus",requestStatus);
				mv.addObject("reviewer", reviewer);
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
	@RequestMapping(value="get", method=RequestMethod.GET)
	public Response getContentParams(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				try {
					List<ContentParameter> appParams = contentParameterService.getAllContentParams();
					resp.setResultCode(1000);
					resp.setMessage("Sukses get data");
					resp.setData(appParams);
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
	@RequestMapping(value="update", method={RequestMethod.PUT,RequestMethod.POST})
	public Response updateContentParams(Model model, @RequestBody List<ContentParameterApi> contentParams, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(contentParameterService.getTotalNotReviewedRow()==0){	//Tidak ada request perubahan data
					contentParameterService.insertToTemp(contentParams, userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses.. \nPermintaan perubahan data telah disimpan");
					resp.setData(contentParams);
				} else {
					resp.setResultCode(5005);
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
	
	@RequestMapping(value="review-page")
	public ModelAndView getInspectionPage(Model model, HttpServletRequest request){
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
				
				if(contentParameterService.getTotalNotReviewedRow()>0)
					showInfo = false;
				
				String updatedBy = contentParameterService.getUpdatedBy();
				if(updatedBy!=null && !updatedBy.equalsIgnoreCase(userSession.getUsername()) && showInfo == false){
					showButtons = true;
				} else {
					if(updatedBy==null && !userSession.getUsername().isEmpty() && showInfo==false)
						showButtons = true;
				}
				
				if(showInfo==false){
					if(!updatedBy.equalsIgnoreCase(userSession.getUsername()))
						showRequestInfo = true;
				}
				
				List<ContentParameterTemp> temps = contentParameterService.getByRequester(userSession.getUsername(), NotifStatus.SHOW);
				
				if(temps.size()>0){
					ContentParameterTemp temp = temps.get(0);
					
					showNotif = true;
					requestStatus = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
					reviewer = temp.getReviewedBy();
				}
				
				mv.setViewName("review-content-param");
				mv.addObject("showInfo",showInfo);
				mv.addObject("showButtons", showButtons);
				mv.addObject("showRequestInfo",showRequestInfo);
				mv.addObject("requestBy", updatedBy);
				mv.addObject("showNotif", showNotif);
				mv.addObject("requestStatus",requestStatus);
				mv.addObject("reviewer", reviewer);
				
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
	public Response getUnapprovedParams(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				try {
					List<ContentParameterApi> contentParams = contentParameterService.getUnapprovedContentParams();
					resp.setResultCode(1000);
					resp.setMessage("Sukses get data");
					resp.setData(contentParams);
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
	@RequestMapping(value="approve", method=RequestMethod.PUT)
	public Response approveContentParams(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(contentParameterService.getTotalNotReviewedRow()>0){
					contentParameterService.approveContentParams(userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses.. \nData Application Parameter telah berubah");
				} else {
					resp.setResultCode(0);
					resp.setMessage("Warning!\n Tidak ada permintaan perubahan data");
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
	@RequestMapping(value="reject", method=RequestMethod.PUT)
	public Response rejectContentParams(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){					
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				if(contentParameterService.getTotalNotReviewedRow()>0){
					contentParameterService.rejectContentParams(userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses.. \nPerubahan data Application Parameter telah ditolak");
				} else {
					resp.setResultCode(0);
					resp.setMessage("Warning!\n Tidak ada permintaan perubahan data");
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
	@RequestMapping(value="cancel", method=RequestMethod.PUT)
	public Response cancelRequest(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				contentParameterService.cancelContentParams(userSession.getUsername());
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
	@RequestMapping(value="set-notif-off", method=RequestMethod.PUT)
	public Response setNotifOff(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				try {
					UserSession userSession = (UserSession) model.asMap().get("userSession");
					contentParameterService.setNotifOff(userSession.getUsername());
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
