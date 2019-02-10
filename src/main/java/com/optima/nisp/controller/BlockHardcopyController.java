package com.optima.nisp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

import com.optima.nisp.constanta.BlockHardcopyStatus;
import com.optima.nisp.constanta.ParameterCons;
import com.optima.nisp.constanta.ReturnCode;
import com.optima.nisp.model.ApplicationParameter;
import com.optima.nisp.model.BlockHardcopy;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.service.BlockHardcopyService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="block-hardcopy")
@SessionAttributes("userSession")
public class BlockHardcopyController {
	private static final Logger logger = Logger.getLogger(BlockHardcopyController.class);
	
	@Autowired
	private BlockHardcopyService blockHardcopyService;
	
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@RequestMapping(value="manage-page")
	public ModelAndView getBlockUnblockPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){
				mv.setViewName("manage-block-hardcopy");
				
				mv.addObject("strDefRowCount", CommonUtils.getDefaultRowCount(applicationParameterService));				
				mv.addObject("blockedStatus", BlockHardcopyStatus.BLOCKED);
				mv.addObject("unblockedStatus", BlockHardcopyStatus.UNBLOCKED);
				mv.addObject("allStatus", BlockHardcopyStatus.ALL_STATUS);
				
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
	@RequestMapping(value="get-list", method=RequestMethod.GET)
	public Response getList(Model model, @RequestParam String name, @RequestParam String accountNumber, @RequestParam Integer status,
			@RequestParam int start, @RequestParam int length){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				List<BlockHardcopy> blockHcs = blockHardcopyService.getList(name, accountNumber, status, start, length);
				int totalRow = blockHardcopyService.getTotalRow(name, accountNumber, status);
				resp.setResultCode(ReturnCode.SUCCESS);
				resp.setMessage("Sukses");
				resp.setData(blockHcs);
				resp.setRecordsFiltered(totalRow);
				resp.setRecordsTotal(totalRow);
			} else {
				resp.setResultCode(ReturnCode.SESSION_FAILED);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="change-status", method=RequestMethod.PUT)
	public Response changeStatus(Model model, @RequestBody BlockHardcopy[] blockHcs){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				blockHardcopyService.changeStatus(blockHcs);
				resp.setResultCode(ReturnCode.SUCCESS);
				resp.setMessage("SUKSES\nPerubahan efektif pada statement bulan berikutnya.");
			} else {
				resp.setResultCode(ReturnCode.SESSION_FAILED);
				resp.setMessage("No Session");
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
}
