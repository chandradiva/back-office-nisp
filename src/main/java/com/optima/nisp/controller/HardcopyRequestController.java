package com.optima.nisp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.nisp.constanta.HardcopyRequestStatus;
import com.optima.nisp.constanta.ParameterCons;
import com.optima.nisp.model.ApplicationParameter;
import com.optima.nisp.model.HardcopyRequest;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.service.HardcopyRequestService;
import com.optima.nisp.utility.CommonUtils;
import com.optima.nisp.utility.HttpRequestUtil;

@Controller
@RequestMapping(value="hardcopy-request")
@SessionAttributes("userSession")
public class HardcopyRequestController {
	private static final Logger logger = Logger.getLogger(HardcopyRequestController.class);
	
	@Autowired
	private HardcopyRequestService hardcopyRequestService;
	
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@Resource(name="confProp")
	private Properties properties;
	
	@RequestMapping(value="request-page", method = RequestMethod.GET)
	public ModelAndView getRequestPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){
				mv.setViewName("hardcopy-request");
				mv.addObject("strDefRowCount", CommonUtils.getDefaultRowCount(applicationParameterService));
				mv.addObject("defaultReqFrom","");
				mv.addObject("defaultReqTo","");
				mv.addObject("defaultPeriodeFrom","");
				mv.addObject("defaultPeriodeTo","");
				mv.addObject("defaultAccountNumber", "");
				mv.addObject("pendingStatus", HardcopyRequestStatus.PENDING);
				mv.addObject("processedStatus", HardcopyRequestStatus.PROCESSED);
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
	
	@RequestMapping(value="history-page", method = RequestMethod.GET)
	public ModelAndView getHistoryPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){
				mv.setViewName("hardcopy-request-history");
				mv.addObject("strDefRowCount", CommonUtils.getDefaultRowCount(applicationParameterService));
				mv.addObject("defaultReqFrom","");
				mv.addObject("defaultReqTo","");
				mv.addObject("defaultPeriodeFrom","");
				mv.addObject("defaultPeriodeTo","");
				mv.addObject("defaultAccountNumber", "");
				mv.addObject("defaultBatchId", "");
				mv.addObject("defaultStatus", HardcopyRequestStatus.ALL_STATUS);
				mv.addObject("pendingStatus", HardcopyRequestStatus.PENDING);
				mv.addObject("processedStatus", HardcopyRequestStatus.PROCESSED);
				mv.addObject("canceledStatus", HardcopyRequestStatus.CANCELED);
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
	public Response getRequestList(Model model, HttpServletRequest request, @RequestParam("start") int start, 
			@RequestParam("length") int length, @RequestParam("reqFrom") String reqFrom, @RequestParam("reqTo") String reqTo,
			@RequestParam("periodeFrom") String periodeFrom, @RequestParam("periodeTo") String periodeTo, 
			@RequestParam("accountNumber") String accountNumber){
		
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				List<HardcopyRequest> hcRequests = hardcopyRequestService.getRequestList(reqFrom, reqTo, periodeFrom, periodeTo, accountNumber, start, length);
				Integer totalRow = hardcopyRequestService.getRequestTotalRow(reqFrom, reqTo, periodeFrom, periodeTo, accountNumber);
						
				for(HardcopyRequest hcReq : hcRequests){
					DateFormat format = new SimpleDateFormat("yyyyMM");
					Date date = format.parse(hcReq.getPeriode());
					hcReq.setPeriode(CommonUtils.getStrDate(date, "MMMM yyyy", Locale.forLanguageTag("id-ID")));
				}
				
				resp.setData(hcRequests);
				resp.setRecordsFiltered(totalRow);
				resp.setRecordsTotal(totalRow);				
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
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
	@RequestMapping(value="get-history-list", method=RequestMethod.GET)
	public Response getHistoryList(Model model, HttpServletRequest request, @RequestParam("start") int start, 
			@RequestParam("length") int length, @RequestParam("reqFrom") String reqFrom, @RequestParam("reqTo") String reqTo,
			@RequestParam("periodeFrom") String periodeFrom, @RequestParam("periodeTo") String periodeTo, 
			@RequestParam("accountNumber") String accountNumber, @RequestParam("batchId") String batchId,
			@RequestParam("status") Integer status){
		
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				List<HardcopyRequest> hcRequests = hardcopyRequestService.getHistoryList(reqFrom, reqTo, periodeFrom, periodeTo, accountNumber, batchId, status, start, length);
				Integer totalRow = hardcopyRequestService.getHistoryTotalRow(reqFrom, reqTo, periodeFrom, periodeTo, accountNumber, batchId, status);
						
				for(HardcopyRequest hcReq : hcRequests){
					DateFormat format = new SimpleDateFormat("yyyyMM");
					Date date = format.parse(hcReq.getPeriode());
					hcReq.setPeriode(CommonUtils.getStrDate(date, "MMMM yyyy", Locale.forLanguageTag("id-ID")));
				}
				
				resp.setData(hcRequests);
				resp.setRecordsFiltered(totalRow);
				resp.setRecordsTotal(totalRow);				
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
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
	
	@RequestMapping(value="cancel", method=RequestMethod.PUT)
	public @ResponseBody Response cancelRequest(Model model, HttpServletRequest request, @RequestParam("hcReqIds") List<Long> hcReqIds){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				ApplicationParameter appParam = applicationParameterService.getByKey(ParameterCons.AJAX_REQUEST_TIMEOUT);
				
				String strContextPath = (String) properties.get("api_protocol") + "://"
						+ (String) properties.get("api_server");
				String strPort = (String) properties.get("api_port");
				if(!strPort.equals("80")){
					strContextPath += ":"+strPort;
				}
				strContextPath += (String) properties.get("api_context_path");
				
				JSONArray arr = new JSONArray();
				for(Long hcReqId : hcReqIds){
					arr.put(hcReqId);
				}
				
				String response = "";
				if(appParam!=null && appParam.getValue()!=null)
					response = HttpRequestUtil.requestWithJson(strContextPath+"statement/cancel-req-hc", "POST", arr.toString(), Integer.parseInt(appParam.getValue()));
				else
					response = HttpRequestUtil.requestWithJson(strContextPath+"statement/cancel-req-hc", "POST", arr.toString());
				
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(response.toString());
				
				resp.setResultCode(1000);
				resp.setMessage("success");
				resp.setData(jsonNode.get("data"));
				
				if( jsonNode.get("resultCode").asInt() == 1000){
					resp.setResultCode(1000);
					resp.setMessage(jsonNode.get("message").asText());
					resp.setData(jsonNode.get("data"));
				} else {
					resp.setResultCode(5000);
					resp.setMessage(jsonNode.get("message").asText());
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
	@RequestMapping(value="process", method=RequestMethod.PUT)
	public Response processRequest(Model model, HttpServletRequest request, @RequestParam String password, @RequestParam("hcReqIds") List<Long> hcReqIds){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){			
				List<HardcopyRequest> hcRequests = new ArrayList<HardcopyRequest>();
				
				for(Long id : hcReqIds){
					HardcopyRequest hcReq = hardcopyRequestService.getById(id);
					if(hcReq!=null)
						hcRequests.add(hcReq);
				}
				
				ApplicationParameter appParam = applicationParameterService.getByKey(ParameterCons.AJAX_REQUEST_TIMEOUT);
				
				String strContextPath = (String) properties.get("api_protocol") + "://"
						+ (String) properties.get("api_server");
				String strPort = (String) properties.get("api_port");
				if(!strPort.equals("80")){
					strContextPath += ":"+strPort;
				}
				strContextPath += (String) properties.get("api_context_path");
				
				JSONObject hcObj = new JSONObject();
				JSONArray arr = new JSONArray();
				for(HardcopyRequest hcReq : hcRequests){
					JSONObject obj = new JSONObject();
					obj.put("id", hcReq.getId());
					obj.put("accountNumber", hcReq.getAccountNumber());
					obj.put("periode", hcReq.getPeriode());
					obj.put("address", hcReq.getMailingAddress());
					arr.put(obj);
				}
				
				hcObj.put("password", password);
				hcObj.put("details", arr);
				
				String response = "";
				if(appParam!=null && appParam.getValue()!=null)
					response = HttpRequestUtil.requestWithJson(strContextPath+"statement/print-hc", "POST", hcObj.toString(), Integer.parseInt(appParam.getValue()));
				else
					response = HttpRequestUtil.requestWithJson(strContextPath+"statement/print-hc", "POST", hcObj.toString());
				
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(response.toString());
				
				if( jsonNode.get("resultCode").asInt() == 1000){
					resp.setResultCode(1000);
					resp.setMessage(jsonNode.get("message").asText());
					//resp.setData(jsonNode.get("data").get("filename"));
				} else {
					resp.setResultCode(5000);
					resp.setMessage(jsonNode.get("message").asText());
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
	@RequestMapping(value="reprocess", method=RequestMethod.PUT)
	public Response reProcessRequest(Model model, HttpServletRequest request, @RequestParam String password, @RequestParam("hcReqIds") List<Long> hcReqIds){
		return processRequest(model, request, password, hcReqIds);
	}
}
