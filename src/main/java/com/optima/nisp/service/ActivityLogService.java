package com.optima.nisp.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.UserSession;
import com.optima.nisp.dao.ActivityLogDao;
import com.optima.nisp.utility.CustomRequestWrapper;

public class ActivityLogService {
	@Autowired
	ActivityLogDao activityLogDao;
	
//	public void insertUrlMapping(Map<String, String> map){
//		activityLogDao.insertUrlMapping(map);
//	}
//
//	public void insertUrlMapping(String[] urlMapping, String[] actionName) {
//		activityLogDao.insertUrlMapping(urlMapping,actionName);
//		
//	}
	
	public void insertUserActivity(String urlPath,String reqMethod, String userID, String groupName, Date startTime,Date finishTime,String sessionId, String requestMap,String source, String ipAddress, String browser, String information){
		
		activityLogDao.insertUserActivity(urlPath, reqMethod, userID, groupName, startTime, finishTime,sessionId,requestMap,source,ipAddress,browser, information);
		
	}
	
	public void insertUserActivity(HttpServletRequest request, CustomRequestWrapper customRequestWrapper, String information){
		String actionName = request.getServletPath();
		
		if( actionName != null && actionName.length() > 1 )
			actionName = actionName.substring(1);	//substring(1) to remove first '/'
		
		String ipAddress = request.getRemoteAddr();
		String browser = request.getHeader("User-Agent");	
		Date startTime = (Date) request.getAttribute("startTime");
		Date endTime = new Date(System.currentTimeMillis());
		String requestMap = getStrReqMap(request);		

		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		
		if (customRequestWrapper != null && customRequestWrapper.getBody()!=null) {
			requestMap = customRequestWrapper.getBody();
		}
		
		if (actionName.equals(CommonCons.RIWAYAT_TRANSAKSI_TAMPIL_URL)
				|| actionName.equals(CommonCons.RIWAYAT_TRANSAKSI_DOWNLOAD_URL)) {
			information = requestMap;
		}
				
		insertUserActivity(actionName, request.getMethod(), ((userSession != null) ? userSession.getUsername() : ""), ((userSession != null) ? userSession.getGroupName() : ""), startTime, endTime, request.getSession().getId(), requestMap, CommonCons.SERVER_ID,ipAddress,browser, information);
	}
	
	public String getStrReqMap(HttpServletRequest request){
		String requestMap = (String) request.getAttribute("requestMap");
		Map<Object, Object> reqMap = request.getParameterMap();				
		for (Object key : reqMap.keySet()) {
			String keyString = key.toString();
			if (keyString != null && !keyString.isEmpty()) {
				String[] value = (String[]) reqMap.get(keyString);
				requestMap += "|";
				requestMap += keyString + "=";
				for (String val : value) {
					requestMap += val + ",";
				}
				requestMap += "|";
			}
		}
		
		return requestMap;
	}

	public ActivityLogDao getActivityLogDao() {
		return activityLogDao;
	}

	public void setActivityLogDao(ActivityLogDao activityLogDao) {
		this.activityLogDao = activityLogDao;
	}
	
	
}
