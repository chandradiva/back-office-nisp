package com.optima.nisp.interceptor;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.nisp.service.ActivityLogService;
import com.optima.nisp.utility.CustomResponseWrapper;

@Configurable
@Component("hardcopyRequestFilter")
public class LoggingResponseFilter implements Filter {
	
	private static final Logger logger = Logger.getLogger("BackOffice Logger");
	
	@Autowired
	private ActivityLogService activityLogService;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		if(activityLogService==null){
////			CustomRequestWrapper requestWrapper = new CustomRequestWrapper((HttpServletRequest) request);
//			ServletContext servletContext = request.getServletContext();
//            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//            activityLogService = webApplicationContext.getBean(ActivityLogService.class);
//		}
		Date startTime = new Date(System.currentTimeMillis());
		CustomResponseWrapper responseWrapper = new CustomResponseWrapper((HttpServletResponse)response);
		chain.doFilter(request, responseWrapper);
		byte[] responseContent = responseWrapper.getDataStream();
		
//		String resp = new String(responseContent);
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode jsonNode = mapper.readTree(resp.toString());
//		
//		if(jsonNode.get("data")!=null){
//			String info = "";
//			if(jsonNode.get("data").get("hcReqIds")!=null){
//				String hcReqIds = jsonNode.get("data").get("hcReqIds").toString();
//				info += "<strong>Hardcopy Req Ids : "+hcReqIds+"</strong><br />";
//			}
//			if(jsonNode.get("data").get("batchId")!=null){
//				String batchId = jsonNode.get("data").get("batchId").asText();
//				info += "<strong>Batch ID : "+batchId+"</strong>";
//			}
//			
//			if( activityLogService != null )
//				activityLogService.insertUserActivity((HttpServletRequest)request, null, info);
//		}
		if( responseContent.length > 0 )
			response.getOutputStream().write(responseContent);
		String requesterIp = request.getRemoteAddr();
		String url = ((HttpServletRequest) request).getRequestURI();
	    String queryString = ((HttpServletRequest) request).getQueryString();
	    if( queryString != null )
		    url += "?"+queryString;
	    String method = ((HttpServletRequest)request).getMethod();
	    String contentTypeStr = request.getContentType();
	    String strReqParam = "N/A";
	    if ("POST".equalsIgnoreCase(method) && contentTypeStr != null && contentTypeStr.toLowerCase().startsWith("application/x-www-form-urlencoded")) {
		    strReqParam = getStrReqMap((HttpServletRequest)request);
	    }
	    Date endTime = new Date(System.currentTimeMillis());
		log(responseContent, requesterIp, url, method, contentTypeStr, strReqParam, startTime, endTime);	    
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
//		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
//	            filterConfig.getServletContext());
		// TODO Auto-generated method stub
//		ActivityLogService bean = WebApplicationContextUtils.
//				  getRequiredWebApplicationContext(filterConfig.getServletContext()).
//				  getBean(ActivityLogService.class);		
	}
	public ActivityLogService getActivityLogService() {
		return activityLogService;
	}

	public void setActivityLogService(ActivityLogService activityLogService) {
		this.activityLogService = activityLogService;
	}
	
	private void log(byte[] byteResponse, String requesterIp, 
			String url, String method, String responseContentType, String reqParam, Date startTime, Date endTime){
		long executeTime = endTime.getTime() - startTime.getTime();
	    String strLog = "url: "+url
			    +"\nmethod: "+method
			    +"\nrequester ip: "+requesterIp
			    +"\nrequest param: "+reqParam;
    	if( responseContentType != null && !responseContentType.startsWith("image") && byteResponse.length > 0){
    		strLog+="\nresponse content type: "+responseContentType
    				+"\nresponse: "+new String(byteResponse);			    		
    	}
    	strLog += "\nexecution time: "+executeTime+" ms";
    	logger.debug(strLog);	 
	}
	
	private String getStrReqMap(HttpServletRequest request){
		String requestMap = "";
		@SuppressWarnings("unchecked")
		Enumeration<String> paramNames = request.getParameterNames();
		while( paramNames.hasMoreElements() ){
			String paramName = (String)paramNames.nextElement();
			if( requestMap.length() > 0 )
				requestMap += "&";
			requestMap += paramName + "=";
			if( !paramName.equalsIgnoreCase("password") ){
				String[] values = request.getParameterValues(paramName);
				String value = "";
				for (String val : values) {
					if( value.length() > 0 )
						value += ",";
					value += val;
				}
				requestMap+=value;
			}
			
		}
		return requestMap;
	}
}
