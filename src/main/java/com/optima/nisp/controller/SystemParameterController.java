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
import com.optima.nisp.model.SystemParameter;
import com.optima.nisp.model.SystemParameterTemp;
import com.optima.nisp.model.api.SystemParameterApi;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.SystemParameterService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="sys-param")
@SessionAttributes("userSession")
public class SystemParameterController {
	private static final Logger logger = Logger.getLogger(SystemParameterController.class);
	
	@Autowired
	private SystemParameterService systemParameterService;
	
	@RequestMapping(value="manage")
	public ModelAndView getManageAppParamPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("manage-sysparam");
		try{
			if(model.containsAttribute("userSession")){			
				boolean showCancel = false;
				boolean showSave = false;
				boolean showInfoRequest = false;
				boolean showNotif = false;
				String requestStatus = "";
				String reviewer = "";
				
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				List<SystemParameterTemp> temps = systemParameterService.getNotReviewed();
				
				if(temps.size()>0){
					if(temps.get(0).getUpdatedBy().equalsIgnoreCase(userSession.getUsername())){	//Jika bukan yang melakukan request
						showCancel = true;
					} else {
						showInfoRequest = true;
					}
				}				
				else {
					showSave = true;
				}
				
				List<SystemParameterTemp> systemps = systemParameterService.getByRequester(userSession.getUsername(), NotifStatus.SHOW);
				
				if(systemps.size()>0){
					SystemParameterTemp temp = systemps.get(0);
					
					showNotif = true;
					requestStatus = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
					reviewer = temp.getReviewedBy();
				}
				
				mv.addObject("showCancel",showCancel);
				mv.addObject("showSave", showSave);
				mv.addObject("showInfoRequest", showInfoRequest);
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
	@RequestMapping(value="get", method=RequestMethod.GET)
	public Response getSysParams(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				try {
					List<SystemParameter> sysParams = systemParameterService.getAllSysParams();
					resp.setResultCode(1000);
					resp.setMessage("Sukses get data");
					resp.setData(sysParams);
				} catch(Exception e){
					resp.setResultCode(0);
					resp.setMessage(e.getMessage());
				}
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
	@RequestMapping(value="update", method=RequestMethod.PUT)
	public Response updateSysParams(Model model, HttpServletRequest request, @RequestBody List<SystemParameterApi> sysParams){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){		
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(systemParameterService.getTotalNotReviewedRow()==0){	//Tidak ada request perubahan data
					systemParameterService.insertToTemp(sysParams, userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses.. \nPermintaan perubahan data telah disimpan");
					resp.setData(sysParams);
				} else {
					resp.setResultCode(5005);
					resp.setMessage("Maaf, saat ini sedang ada request perubahan data");
				}	
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
	
	@RequestMapping(value="review-page")
	public ModelAndView getInspectionPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("review-sys-param");
		try{
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				boolean showInfo = true;
				boolean showButtons = false;
				boolean showRequestInfo = false;
				boolean showNotif = false;
				String requestStatus = "";
				String reviewer = "";
				
				if(systemParameterService.getTotalNotReviewedRow()>0)
					showInfo = false;
				
				String updatedBy = systemParameterService.getUpdatedBy();
				if(updatedBy!=null && !updatedBy.equalsIgnoreCase(userSession.getUsername()) && showInfo==false){
					showButtons = true;
				} else {
					if(updatedBy==null && !userSession.getUsername().isEmpty() && showInfo==false)
						showButtons = true;
				}
				
				if(showInfo==false){
					if(!updatedBy.equalsIgnoreCase(userSession.getUsername()))
						showRequestInfo = true;
				}
				
				List<SystemParameterTemp> systemps = systemParameterService.getByRequester(userSession.getUsername(), NotifStatus.SHOW);
				if(systemps.size()>0){
					SystemParameterTemp temp = systemps.get(0);
					
					showNotif = true;
					requestStatus = temp.getReviewedStatus().equals(ReviewStatus.APPROVED) ? "disetujui" : "ditolak";
					reviewer = temp.getReviewedBy();
				}
				
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
					List<SystemParameterApi> sysParams = systemParameterService.getUnapprovedSysParams();
					resp.setResultCode(1000);
					resp.setMessage("Sukses get data");
					resp.setData(sysParams);
				} catch(Exception e){
					resp.setResultCode(0);
					resp.setMessage(e.getMessage());
				}
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
	@RequestMapping(value="approve", method=RequestMethod.PUT)
	public Response approveSysParams(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){	
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				
				if(systemParameterService.getTotalNotReviewedRow()>0){
					systemParameterService.approveSysParams(userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses.. \nData System Parameter telah berubah");
				} else {
					resp.setResultCode(0);
					resp.setMessage("Warning!\n Tidak ada permintaan perubahan data");
				}
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
	@RequestMapping(value="reject", method=RequestMethod.PUT)
	public Response rejectSysParams(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				if(systemParameterService.getTotalNotReviewedRow()>0){
					systemParameterService.rejectSysParams(userSession.getUsername());
					resp.setResultCode(1000);
					resp.setMessage("Sukses.. \nPerubahan data System Parameter telah ditolak");
				} else {
					resp.setResultCode(0);
					resp.setMessage("Warning!\n Tidak ada permintaan perubahan data");
				}	
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
	@RequestMapping(value="cancel", method=RequestMethod.PUT)
	public Response cancelRequest(Model model, HttpServletRequest request){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				systemParameterService.cancelSysParams(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses.. \nPermintaan perubahan data telah dibatalkan");
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
	@RequestMapping(value="set-notif-off", method=RequestMethod.PUT)
	public Response setNotifOff(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				systemParameterService.setNotifOff(userSession.getUsername());
				resp.setResultCode(1000);
				resp.setMessage("Sukses");
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
