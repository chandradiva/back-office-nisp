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

import com.optima.nisp.constanta.UserSession;
import com.optima.nisp.model.Button;
import com.optima.nisp.model.Group;
import com.optima.nisp.model.KonsolidasiBlacklist;
import com.optima.nisp.model.api.KonsolidasiBlacklistApi;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.service.ButtonService;
import com.optima.nisp.service.GroupService;
import com.optima.nisp.service.KonsolidasiBlacklistService;
import com.optima.nisp.utility.CommonUtils;

@Controller
@RequestMapping(value="konsolidasi-blacklist")
@SessionAttributes("userSession")
public class KonsolidasiBlacklistController {

	private static final Logger logger = Logger.getLogger(KonsolidasiBlacklistController.class);
	
	@Autowired
	private KonsolidasiBlacklistService blacklistService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private ButtonService buttonService;
	
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@RequestMapping(value = "manage-page")
	public ModelAndView getManageKonsolidasiBlacklistPage(Model model, HttpServletRequest request){
		ModelAndView mv = new ModelAndView("manage-blacklist");
		try{
			if(model.containsAttribute("userSession")){
				UserSession userSession = (UserSession) model.asMap().get("userSession");
				Long[] sessionGroupIds = userSession.getGroupIds();
				
				Button resendEmailBtn = buttonService.getByButtonUrl("konsolidasi-blacklist/submit");
				
				/*ID button-button yang ada pada page ini*/
				Long resendEmailBtnId = resendEmailBtn!=null ? resendEmailBtn.getButtonId() : -1;

				boolean showResendBtn = false;
				
				List<Group> resendBtnGroups = groupService.getByButtonId(resendEmailBtnId);

				if(hasIntersection(sessionGroupIds, resendBtnGroups))
					showResendBtn = true;
				
				mv.addObject("showResendButton", showResendBtn);
				mv.addObject("strDefRowCountOptions", CommonUtils.getDefaultRowCountOptions(applicationParameterService));
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
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Response getListEmail(Model model, 
			@RequestParam("cifKey") String cifKey, 
			@RequestParam("current") int pageNumber, 
			@RequestParam("size") int totalPerPage) {
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				List<KonsolidasiBlacklist> datas = blacklistService.getAllData(cifKey, pageNumber, totalPerPage);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(datas);	
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
	@RequestMapping(value = "get-row", method = RequestMethod.GET)
	public Response getTotalRow(Model model, 
			@RequestParam("cifKey") String cifKey){
		try{
			Response resp = new Response();
			
			if(model.containsAttribute("userSession")){
				int totalRow = blacklistService.getTotalRow(cifKey);
				resp.setResultCode(1000);
				resp.setMessage("Sukses get data");
				resp.setData(totalRow);
			} else {
				resp.setMessage("no session");
				resp.setResultCode(5001);
			}	
			return resp;
		} catch(Exception e) {
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "submit", method = RequestMethod.POST)
	public Response insertMenu(Model model, HttpServletRequest request, @RequestBody KonsolidasiBlacklistApi req) {
		try {
			Response response = new Response();
			
			if (model.containsAttribute("userSession")) {		
				blacklistService.insertData(req);
				response.setResultCode(1000);
				response.setMessage("Blacklist Added Successfully");
			} else {
				response.setMessage("no session");
				response.setResultCode(5001);
			}			
			
			return response;
		} catch (Exception e) {
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	private boolean hasIntersection(Long[] sessionGroupIds, List<Group> buttonGroups){
		for(Group g : buttonGroups){
			for(long sgid : sessionGroupIds){
				if(sgid == g.getGroupId())
					return true;
			}
		}
		
		return false;
	}
}
