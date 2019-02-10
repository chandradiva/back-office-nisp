package com.optima.nisp.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("userSession")
public class IndexController {
	private static final Logger logger = Logger.getLogger(IndexController.class);
	
	@RequestMapping(value = "/dashboard")
	public ModelAndView getDashboard(Model model){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){
				mv.setViewName("dashboard");
			} else {
				mv.setViewName("redirect:/login");
			}
			
		}catch( Exception e){
			logger.error("Error", e);
			mv.setViewName("redirect-page");
			mv.addObject("url","login");
		}
		
		return mv;
	}
}
