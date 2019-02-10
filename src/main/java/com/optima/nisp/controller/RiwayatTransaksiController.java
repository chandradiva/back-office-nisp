package com.optima.nisp.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.optima.nisp.model.RekeningListBOView;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.service.RiwayatTransaksiBOService;
import com.optima.nisp.utility.CommonUtils;
import com.optima.nisp.utility.HttpRequestUtil;

@Controller
@RequestMapping(value="riwayat-transaksi")
@SessionAttributes("userSession")
public class RiwayatTransaksiController {
	private static final Logger logger = Logger.getLogger(RiwayatTransaksiController.class);
	
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@Autowired
	private RiwayatTransaksiBOService riwayatTransaksiBOService;
	
	@Resource(name="confProp")
	private Properties properties;
	
	@RequestMapping(value="")
	public ModelAndView getRiwayatTransaksiMainPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try{
			if(model.containsAttribute("userSession")){			
				mv.setViewName("riwayat-transaksi");
				mv.addObject("strDefRowCount", CommonUtils.getDefaultRowCount(applicationParameterService));
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar aMonthAgo = Calendar.getInstance();
				aMonthAgo.add(Calendar.MONTH, -1);
				String strToday = sdf.format(new Date());
				String strAMonthAgo = sdf.format(aMonthAgo.getTime());
				mv.addObject("strToday", strToday);
				mv.addObject("strAMonthAgo", strAMonthAgo);
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
	public Response getAll(Model model, @RequestParam("start") int start, @RequestParam("length") int length, @RequestParam String cifKey, @RequestParam String nomorRekening,
						@RequestParam String namaNasabah, @RequestParam String currency){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				List<RekeningListBOView> rekList = riwayatTransaksiBOService.getRekeningList(start, length, cifKey, nomorRekening, namaNasabah, currency);
				
				int totalRow = riwayatTransaksiBOService.getRekeningListTotalRow(start, length, cifKey, nomorRekening, namaNasabah, currency);
				resp.setData(rekList);
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
	
	private Response doRiwayatTransaksiDoc(Model model, String cif, String startDate, String endDate,
		String nomorRekening, String currency, String productName
		, String namaNasabah1, String namaNasabah2, String namaNasabah3, String branchName, String branchCode
		, String accountType, int flag
		, HttpServletResponse servletResponse){
		try {
			Response response = new Response();
			if( model.containsAttribute("userSession") ){
				String strContextPath = (String) properties.get("api_protocol") + "://"
						+ (String) properties.get("api_server");
				String strPort = (String) properties.get("api_port");
				if(!strPort.equals("80")){
					strContextPath += ":"+strPort;
				}
				strContextPath += (String) properties.get("api_context_path");
				
				strContextPath += "dailystatement/print-passbook";
				
				String urlParameters = "cif="+cif+"&accountNumber="+nomorRekening+"&currency="+currency+"&startDate="+startDate+"&endDate="+endDate
						+"&isPrint=1&oneStep=0&productName="+productName+"&namaNasabah1="+namaNasabah1+"&namaNasabah2="+namaNasabah2+"&namaNasabah3="+namaNasabah3
						+"&branchName="+branchName+"&branchCode="+branchCode+"&accountType="+accountType+"&flag="+flag;
				
				ApplicationParameter appParam = applicationParameterService.getByKey(ParameterCons.AJAX_REQUEST_TIMEOUT);
				int arto = Integer.parseInt(appParam.getValue());
				String res = HttpRequestUtil.request(strContextPath, "POST", urlParameters, arto);
				
				HashMap<String, String>data = new HashMap<String, String>();
				data.put("res", res);
				response.setData(data);
				response.setResultCode(ReturnCode.SUCCESS);
			}else {
				response.setResultCode(ReturnCode.SESSION_FAILED);
			}
			
			return response;
		} catch (Exception e) {
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}	
	}
	@RequestMapping(value = "riwayat_transaksi_tampil", method = RequestMethod.POST)
	public @ResponseBody Response riwayatTransaksiTampil(Model model, @RequestParam String cif, @RequestParam String startDate, @RequestParam String endDate,
		@RequestParam String nomorRekening, @RequestParam String currency, @RequestParam String productName
		, @RequestParam String namaNasabah1, @RequestParam String namaNasabah2, @RequestParam String namaNasabah3, @RequestParam String branchName, @RequestParam String branchCode
		, @RequestParam String accountType, int flag
		, HttpServletResponse servletResponse) {
		
		return doRiwayatTransaksiDoc(model, cif, startDate, endDate, nomorRekening, currency, productName, namaNasabah1, namaNasabah2, namaNasabah3, branchName, branchCode, accountType, flag, servletResponse);
	}
	
	@RequestMapping(value = "riwayat_transaksi_download", method = RequestMethod.POST)
	public @ResponseBody Response riwayatTransaksiDownload(Model model, @RequestParam String cif, @RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String nomorRekening, @RequestParam String currency, @RequestParam String productName
			, @RequestParam String namaNasabah1, @RequestParam String namaNasabah2, @RequestParam String namaNasabah3, @RequestParam String branchName, @RequestParam String branchCode
			, @RequestParam String accountType, int flag
			, HttpServletResponse servletResponse) {
		return doRiwayatTransaksiDoc(model, cif, startDate, endDate, nomorRekening, currency, productName, namaNasabah1, namaNasabah2, namaNasabah3, branchName, branchCode, accountType, flag, servletResponse);				
	}
}
