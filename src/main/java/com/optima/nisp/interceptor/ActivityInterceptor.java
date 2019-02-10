package com.optima.nisp.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.UserSession;
import com.optima.nisp.model.Button;
import com.optima.nisp.model.Menu;
import com.optima.nisp.service.ActivityLogService;
import com.optima.nisp.service.AuditTrailService;
import com.optima.nisp.service.ButtonService;
import com.optima.nisp.service.MenuService;
import com.optima.nisp.utility.CustomRequestWrapper;
import com.optima.nisp.utility.CustomResponseWrapper;

public class ActivityInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(ActivityInterceptor.class);

	@Autowired
	private ActivityLogService activityLogService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private ButtonService buttonService;
	
	@Autowired
	private AuditTrailService auditTrailService;

	private CustomRequestWrapper customRequestWrapper;

	// before the actual handler will be executed
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//		try {
//			customRequestWrapper = new CustomRequestWrapper(request);
//			String requestMap = "";
			Date startTime = new Date(System.currentTimeMillis());

			request.setAttribute("startTime", startTime);
			
			String actionName = request.getServletPath();
			if(actionName.length()>1)
				actionName = actionName.substring(1);
			
			if( actionName.equals(CommonCons.LOGOUT_URL)
				 || actionName.equals(CommonCons.RIWAYAT_TRANSAKSI_TAMPIL_URL)
				 || actionName.equals(CommonCons.RIWAYAT_TRANSAKSI_DOWNLOAD_URL) 
			){
				activityLogService.insertUserActivity(request, customRequestWrapper, "-");
			}
//			// log it
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage(), e);
//		}
//
		return super.preHandle(request, response, handler);
		
	}

	// after the handler is executed
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		try {
			customRequestWrapper = null;
			if(request instanceof CustomRequestWrapper){
				customRequestWrapper = (CustomRequestWrapper) request;
			}
						
			String actionName = request.getServletPath();
			if( actionName != null && actionName.length() > 1 )
				actionName = actionName.substring(1);	//substring(1) to remove first '/'
			
			boolean urlHcReqs = false;
			String[] hcReqUrls = new String[3];
			hcReqUrls[0] = "hardcopy-request/cancel";
			hcReqUrls[1] = "hardcopy-request/process";
			hcReqUrls[2] = "hardcopy-request/reprocess";
			
			for(String hcReqUrl : hcReqUrls){
				if(hcReqUrl.equals(actionName)){
					urlHcReqs = true;
					break;
				}
			}
			
			Menu menu = menuService.getByLink(actionName);
			Button button = buttonService.getByButtonUrl(actionName);
			
			if((menu!=null || button!=null || actionName.equals(CommonCons.LOGIN_URL)) && !urlHcReqs){
				boolean isInsert = false;
				
				if(menu!=null && menu.getAction()!=null && !menu.getAction().isEmpty()){
					isInsert = true;
				}
				else if(button!=null && button.getButtonAction()!=null && !button.getButtonAction().isEmpty()){
					isInsert = true;
				} else if(actionName.equals(CommonCons.LOGIN_URL)){
					isInsert = true;
				}
				
				if(isInsert){
					Date startTime = (Date) request.getAttribute("startTime");
					String requestMap = activityLogService.getStrReqMap(request);
					String information = auditTrailService.getInformation(requestMap);
					
					Date endTime = new Date(System.currentTimeMillis());
					long executeTime = endTime.getTime() - startTime.getTime();

					if (customRequestWrapper != null && customRequestWrapper.getBody()!=null) {
						requestMap = customRequestWrapper.getBody();
						information = auditTrailService.getInformation(requestMap);
					}
					
//					information = StringEscapeUtils.escapeHtml(information);
					activityLogService.insertUserActivity(request, customRequestWrapper, information);
					
					// log it
//					if (logger.isDebugEnabled()) {
//						logger.debug("Action Path " + actionName);
//						logger.debug("[" + handler + "] executeTime : " + executeTime + "ms");
//					}
				}
			}			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub
//		super.afterCompletion(request, response, handler, ex);
//		customRequestWrapper = new CustomRequestWrapper(request);
	}

}
