package com.optima.nisp.controller;

import java.util.ArrayList;
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

import com.optima.nisp.constanta.ReturnCode;
import com.optima.nisp.model.HousekeepingLog;
import com.optima.nisp.response.HouseKeepingResponse;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.service.HousekeepingLogService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="house-keeping")
@SessionAttributes("userSession")
public class HouseKeepingController {
	private static final Logger logger = Logger.getLogger(HouseKeepingController.class);
	
	@Autowired
	HousekeepingLogService hklService;
	
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@RequestMapping(value="log-page")
	public ModelAndView logPage(Model model){
		ModelAndView mv = new ModelAndView("house-keeping-log");
		try{
			if(model.containsAttribute("userSession")){
				mv.addObject("strDefRowCount", CommonUtils.getDefaultRowCount(applicationParameterService));
			}else{
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
			@RequestParam int length, @RequestParam String proses,
			@RequestParam String strWaktuMulai, @RequestParam String strWaktuSelesai, @RequestParam int status){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				List<HousekeepingLog> logs = new ArrayList<HousekeepingLog>();
				int totalRow = hklService.getLogs(strWaktuMulai, strWaktuSelesai, status, proses, start, length, logs);
				List<HouseKeepingResponse> logsResponse = new ArrayList<HouseKeepingResponse>();
				int number = 1;
				
				for(HousekeepingLog log : logs){
					HouseKeepingResponse logResp = new HouseKeepingResponse();
					logResp.setNumber(start+number);
					logResp.setProses(log.getRemarks());
					logResp.setWaktuMulai(log.getStartDate());
					logResp.setWaktuSelesai(log.getEndDate());
					logResp.setStatus(log.getStatus()==1?"Sukses":"Gagal");
					
					logsResponse.add(logResp);
					number++;
				}				
				
				resp.setData(logsResponse);
				resp.setRecordsFiltered(totalRow);
				resp.setRecordsTotal(totalRow);				
				resp.setResultCode(ReturnCode.SUCCESS);
				resp.setMessage("Success");
			} else {
				resp.setMessage("Session Failed");
				resp.setResultCode(ReturnCode.SESSION_FAILED);
			}		
			
			return resp;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
}
