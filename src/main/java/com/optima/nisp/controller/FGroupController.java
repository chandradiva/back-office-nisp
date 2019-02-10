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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.FGroup;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.FGroupService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="fgroup")
@SessionAttributes("userSession")
public class FGroupController {
	private static final Logger logger = Logger.getLogger(FGroupController.class);
	
	@Autowired
	private FGroupService fgroupService;
	
	@RequestMapping(value="manage", method = RequestMethod.GET)
	public ModelAndView getManageFGroupPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){		
				mv.addObject("secAdmin", CommonCons.SECURITY_FGROUP);
				mv.setViewName("manage-fgroup");
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
	@RequestMapping(value="add", method = RequestMethod.POST)
	public Response insertFGroup(Model model, HttpServletRequest request, @RequestBody FGroup fgroup){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){			
				fgroupService.insert(fgroup);
				response.setResultCode(1000);
				response.setMessage("FGroup Added Successfully");
			} else {
				response.setResultCode(5001);
				response.setMessage("No Session");
			}		
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="delete", method = RequestMethod.DELETE)
	public Response deleteFGroup(Model model, HttpServletRequest request, @RequestParam("fgroupId") Long fgroupId){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				fgroupService.delete(fgroupId);
				resp.setResultCode(1000);
				resp.setMessage("Delete Succeeded");
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
	@RequestMapping(value="update", method = RequestMethod.PUT)
	public Response updateFGroup(Model model, HttpServletRequest request, @RequestBody FGroup fgroup){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				fgroupService.update(fgroup);
				resp.setResultCode(1000);
				resp.setMessage("Update Succeeded");
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
	@RequestMapping(value="get-all", method=RequestMethod.GET)
	public Response getAllFGroups(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				resp.setData(fgroupService.getAllFGroups());
				resp.setResultCode(1000);
				resp.setMessage("Get Succeeded");
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
	@RequestMapping(value="get-all-no-security", method=RequestMethod.GET)
	public Response getAllFGroupsNoSecurity(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				resp.setData(fgroupService.getAllFGroupsNoSecurity());
				resp.setResultCode(1000);
				resp.setMessage("Get Succeeded");
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
	@RequestMapping(value="get", method=RequestMethod.GET)
	public Response getByGroup(Model model, @RequestParam("groupId") Long groupId){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				resp.setData(fgroupService.getByGroupId(groupId));
				resp.setResultCode(1000);
				resp.setMessage("Get Succeeded");
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
	@RequestMapping(value="bulk-update", method = RequestMethod.PUT)
	public Response bulkUpdate(Model model, HttpServletRequest request, @RequestBody List<FGroup> fgroups){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){			
				fgroupService.bulkUpdate(fgroups);
				response.setResultCode(1000);
				response.setMessage("FGroups Updated Successfully");
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}		
			
			return response;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
}
