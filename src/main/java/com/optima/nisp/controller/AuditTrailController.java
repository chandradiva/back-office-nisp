package com.optima.nisp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.optima.nisp.response.AuditTrailResponse;
import com.optima.nisp.response.AuditTrailWSResponse;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.service.AuditTrailService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="audit-trail")
@SessionAttributes("userSession")
public class AuditTrailController {
	private static final Logger logger = Logger.getLogger(AuditTrailController.class);
	
	@Autowired
	private AuditTrailService auditTrailService;
	
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@RequestMapping(value="monitor-page")
	public ModelAndView getMonitorPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				mv.setViewName("audit-trail");
				mv.addObject("strDefRowCount", CommonUtils.getDefaultRowCount(applicationParameterService));
			} else {
				mv.setViewName("redirect-page");				
				mv.addObject("url","/login");
			}
			
		}catch( Exception e){
			logger.error("Error", e);
			mv.setViewName("redirect-page");
			mv.addObject("url","login");
		}
		
		return mv;
	}
	
	@RequestMapping(value="monitor-ws-page")
	public ModelAndView getMonitorWSPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				mv.setViewName("audit-trail-webstatement");
				mv.addObject("strDefRowCount", CommonUtils.getDefaultRowCount(applicationParameterService));
			} else {
				mv.setViewName("redirect-page");
				mv.addObject("url","/login");
			}
			
		}catch( Exception e){
			logger.error("Error", e);
			mv.setViewName("redirect-page");
			mv.addObject("url","login");
		}
		
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="get-all", method=RequestMethod.GET)
	public Response getAll(Model model, @RequestParam("start") int start, @RequestParam("length") int length, @RequestParam String username, @RequestParam String groups,
						@RequestParam String atDateFrom, @RequestParam String atDateTo, @RequestParam String action, @RequestParam String information){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				List<AuditTrailResponse> auditTrailsResponse = auditTrailService.getBackOfficeViews(start,length, username, groups, atDateFrom, atDateTo, action, information);
				
				int totalRow = auditTrailService.getTotalRow(username, groups, atDateFrom, atDateTo, action, information);
				resp.setData(auditTrailsResponse);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setRecordsTotal(totalRow);
				resp.setRecordsFiltered(totalRow);
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
	@RequestMapping(value="get-all-webstatement", method=RequestMethod.GET)
	public Response getAllWebStatement(Model model, @RequestParam("start") int start, @RequestParam("length") int length, @RequestParam String username,
			@RequestParam String atDateFrom, @RequestParam String atDateTo, @RequestParam String urlPath, 
			@RequestParam String reqParam, @RequestParam String information, @RequestParam String cifKey, @RequestParam String activity){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				List<AuditTrailWSResponse> auditTrailsResponse = auditTrailService.getWebStatementViews(start,length,username,atDateFrom,atDateTo,urlPath,reqParam, information, cifKey, activity);
				int totalRow = auditTrailService.getWSTotalRow(username,atDateFrom,atDateTo,urlPath,reqParam, information, cifKey, activity);
				
				resp.setData(auditTrailsResponse);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setRecordsTotal(totalRow);
				resp.setRecordsFiltered(totalRow);
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
