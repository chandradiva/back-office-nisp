package com.optima.nisp.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

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

import com.optima.nisp.constanta.ParameterCons;
import com.optima.nisp.constanta.ReturnCode;
import com.optima.nisp.model.ApplicationParameter;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.utility.CommonUtils;
import com.optima.nisp.utility.HttpRequestUtil;

@Controller
@RequestMapping(value="tax")
@SessionAttributes("userSession")
public class TaxReportController {

	private static final Logger logger = Logger.getLogger(TaxReportController.class);
	
	@Resource(name="confProp")
	private Properties properties;
	
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showTax(Model model){
		ModelAndView mv = new ModelAndView("tax_main");
		try{
			if(model.containsAttribute("userSession")){
				Calendar cal = Calendar.getInstance();
				int month = cal.get(Calendar.MONTH);
				int year = cal.get(Calendar.YEAR);
				if( month == 0 ){
					year--;
					month = 11;
				}else
					month--;
				
				mv.addObject("years", getYears(year));
				mv.addObject("year_now", year);
				mv.addObject("month_now", month);
			}else {
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
	
	@RequestMapping(value="print", method = RequestMethod.POST)
	public @ResponseBody Response taxReportPrint(Model model, @RequestParam String cifKey, 
			@RequestParam int year, @RequestParam int fromMonth, @RequestParam int toMonth,
			@RequestParam int isPrint, @RequestParam(defaultValue="0") int oneStep){
		
		Response response = new Response();
		if( model.containsAttribute("userSession") ){
			try{
				String urlParameters = "cifKey="+cifKey+"&year="+year+"&fromMonth="+fromMonth
						+"&toMonth="+toMonth+"&isPrint="+isPrint+"&oneStep="+oneStep;
				String strContextPath = (String) properties.get("api_protocol") + "://"
						+ (String) properties.get("api_server");
				String strPort = (String) properties.get("api_port");
				if(!strPort.equals("80")){
					strContextPath += ":"+strPort;
				}
				strContextPath += (String) properties.get("api_context_path");
				
				ApplicationParameter appParam = applicationParameterService.getByKey(ParameterCons.AJAX_REQUEST_TIMEOUT);
				
				int arto = Integer.parseInt(appParam.getValue());
				
				String res = HttpRequestUtil.request(strContextPath+"tax/print", "POST", urlParameters, arto);
				
				response.setResultCode(ReturnCode.SUCCESS);
				response.setData(res);
			} catch (Exception e) {
				logger.error("Error", e);
				return CommonUtils.responseException(e);
			}
			
		}else{
			response.setResultCode(ReturnCode.SESSION_FAILED);
		}
		return response;
	}
	
	private List<Integer> getYears(int year){
		ArrayList<Integer> years = new ArrayList<Integer>();
		for(int i=0;i<5;i++)
			years.add(year-i);
		return years;
	}
}
