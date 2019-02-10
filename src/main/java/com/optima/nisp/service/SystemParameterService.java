package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.constanta.NotifStatus;
import com.optima.nisp.constanta.ReviewStatus;
import com.optima.nisp.dao.SystemParameterDao;
import com.optima.nisp.dao.SystemParameterTempDao;
import com.optima.nisp.model.SystemParameter;
import com.optima.nisp.model.SystemParameterTemp;
import com.optima.nisp.model.api.SystemParameterApi;

@Component
@Transactional
public class SystemParameterService {

	@Autowired
	private SystemParameterDao systemParameterDao;
	
	@Autowired
	private SystemParameterTempDao systemParameterTempDao;

	public List<SystemParameter> getAllSysParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return systemParameterDao.getAllSysParams();
	}

	public void approveSysParams(String approvedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<SystemParameterTemp> temps = systemParameterTempDao.getNotReviewed();
		
		for(SystemParameterTemp temp : temps){
			SystemParameter updatedParam = systemParameterDao.getByKey(temp.getKey());
			if(updatedParam!=null){
				updatedParam.setValue(temp.getNewValue());
				updatedParam.setUpdatedBy(temp.getUpdatedBy());
				updatedParam.setUpdatedDate(new Date());
				updatedParam.setApprovedBy(approvedBy);
				systemParameterDao.update(updatedParam);
			}
		}		
		
		/*Ubah status temp menjadi APPROVED*/
		List<SystemParameterTemp> temps2 = systemParameterTempDao.getNotReviewed();
		for(SystemParameterTemp temp : temps2){
			temp.setReviewedBy(approvedBy);
			temp.setReviewedStatus(ReviewStatus.APPROVED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			systemParameterTempDao.update(temp);
		}
	}
	
	public void rejectSysParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi REJECTED*/
		List<SystemParameterTemp> temps = systemParameterTempDao.getNotReviewed();
		for(SystemParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.REJECTED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			systemParameterTempDao.update(temp);
		}
	}
	
	public void cancelSysParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{		
		/*Ubah status temp menjadi CANCELED*/
		List<SystemParameterTemp> temps = systemParameterTempDao.getNotReviewed();
		for(SystemParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.CANCELED);
			temp.setShowNotifStatus(NotifStatus.HIDE);
			systemParameterTempDao.update(temp);
		}
	}
	
	public void insertToTemp(List<SystemParameterApi> sysParamsApi, String updatedBy) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		
		for(SystemParameterApi sysParamApi : sysParamsApi){
			SystemParameterTemp temp = new SystemParameterTemp();
			temp.setKey(sysParamApi.getKey());
			temp.setOldValue(sysParamApi.getOldValue());
			temp.setNewValue(sysParamApi.getNewValue());
			temp.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
			temp.setUpdatedBy(updatedBy);
			systemParameterTempDao.create(temp);
		}
		
		systemParameterTempDao.deleteReviewed();
	}

	public SystemParameter getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return systemParameterDao.getByKey(key);
	}	
	
	public List<SystemParameterApi> getUnapprovedSysParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<SystemParameterApi> sysParams = new ArrayList<SystemParameterApi>();
		
		List<SystemParameter> allParams = systemParameterDao.getAllSysParams();
		
		for(SystemParameter param : allParams){
			SystemParameterApi sysParam = new SystemParameterApi();
			sysParam.setKey(param.getKey());
			sysParam.setKeyDesc(param.getKeyDesc());
			sysParam.setOldValue(param.getValue());
			
			SystemParameterTemp temp = systemParameterTempDao.getByKey(param.getKey());
			if(temp!=null)
				sysParam.setNewValue(temp.getNewValue());
			else
				sysParam.setNewValue(param.getValue());
			
			sysParams.add(sysParam);
		}
		
		return sysParams;
	}
	
	public Integer getTotalNotReviewedRow(){
		return systemParameterTempDao.getTotalNotReviewedRow();
	}
	
	public String getUpdatedBy(){
		return systemParameterTempDao.getUpdatedBy();
	}
	
	public List<SystemParameterTemp> getNotReviewed() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return systemParameterTempDao.getNotReviewed();
	}
	
	public List<SystemParameterTemp> getByRequester(String userId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return systemParameterTempDao.getByRequester(userId, notifStatus);
	}
	
	public void setNotifOff(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		List<SystemParameterTemp> temps = systemParameterTempDao.getByRequester(userId, NotifStatus.SHOW);
		
		for(SystemParameterTemp temp : temps){
			temp.setShowNotifStatus(NotifStatus.HIDE);
			systemParameterTempDao.update(temp);
		}		
	}
}
