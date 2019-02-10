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
import com.optima.nisp.model.Button;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ButtonService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="button")
@SessionAttributes("userSession")
public class ButtonController {
	private static final Logger logger = Logger.getLogger(ButtonController.class);
	
	@Autowired
	private ButtonService buttonService;
	
	@RequestMapping(value="manage", method = RequestMethod.GET)
	public ModelAndView getManageMenuPage(Model model, HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){
				modelAndView.setViewName("manage-button");
			} else {
				modelAndView.setViewName("redirect-page");
				modelAndView.addObject("url","login");
			}
			
		}catch( Exception e){
			logger.error("Error", e);
			modelAndView.setViewName("redirect-page");
			modelAndView.addObject("url","login");
		}
		
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value="add", method = RequestMethod.POST)
	public Response insertButton(Model model, HttpServletRequest request, @RequestBody Button button){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){			
				buttonService.insert(button);
				response.setResultCode(1000);
				response.setMessage("Button Added Successfully");
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
	public Response deleteButton(Model model, HttpServletRequest request, @RequestParam("buttonId") Long buttonId){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				buttonService.delete(buttonId);
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
	@RequestMapping(value="get-all", method=RequestMethod.GET)
	public Response getAllButtons(Model model){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				resp.setData(buttonService.getAllButtons());
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
	
	@RequestMapping(value="mapping-to-group", method = RequestMethod.GET)
	public ModelAndView getMappingPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				mv.setViewName("mapping-button-to-group");
				mv.addObject("securityGroup", CommonCons.SECURITY_GROUP);
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
	@RequestMapping(value="bulk-update", method = RequestMethod.PUT)
	public Response bulkUpdate(Model model, HttpServletRequest request, @RequestBody List<Button> buttons){
		try{
			Response response = new Response();
			
			if(model.containsAttribute("userSession")){			
				buttonService.bulkUpdate(buttons);
				response.setResultCode(1000);
				response.setMessage("Buttons Updated Successfully");
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
