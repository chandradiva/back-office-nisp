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
import com.optima.nisp.dao.ApplicationParameterDao;
import com.optima.nisp.dao.ApplicationParameterTempDao;
import com.optima.nisp.model.ApplicationParameter;
import com.optima.nisp.model.ApplicationParameterTemp;
import com.optima.nisp.model.api.ApplicationParameterApi;

@Component
@Transactional
public class ApplicationParameterService {

	@Autowired
	private ApplicationParameterDao applicationParameterDao;
	
	@Autowired
	private ApplicationParameterTempDao applicationParameterTempDao;

	public List<ApplicationParameter> getAllAppParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return applicationParameterDao.getAllAppParams();
	}

	public void approveAppParams(String approvedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<ApplicationParameterTemp> temps = applicationParameterTempDao.getNotReviewed();
		
		for(ApplicationParameterTemp temp : temps){
			ApplicationParameter updatedParam = applicationParameterDao.getByKey(temp.getKey());
			if(updatedParam!=null){
				updatedParam.setValue(temp.getNewValue());
				updatedParam.setUpdatedBy(temp.getUpdatedBy());
				updatedParam.setUpdatedDate(new Date());
				updatedParam.setApprovedBy(approvedBy);
				applicationParameterDao.update(updatedParam);
			}
		}		
		
		/*Ubah status temp menjadi APPROVED*/
		List<ApplicationParameterTemp> temps2 = applicationParameterTempDao.getNotReviewed();
		for(ApplicationParameterTemp temp : temps2){
			temp.setReviewedBy(approvedBy);
			temp.setReviewedStatus(ReviewStatus.APPROVED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			applicationParameterTempDao.update(temp);
		}
	}
	
	public void rejectAppParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi REJECTED*/
		List<ApplicationParameterTemp> temps = applicationParameterTempDao.getNotReviewed();
		for(ApplicationParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.REJECTED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			applicationParameterTempDao.update(temp);
		}
	}
	
	public void cancelAppParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{		
		/*Ubah status temp menjadi CANCELED*/
		List<ApplicationParameterTemp> temps = applicationParameterTempDao.getNotReviewed();
		for(ApplicationParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.CANCELED);
			temp.setShowNotifStatus(NotifStatus.HIDE);
			applicationParameterTempDao.update(temp);
		}
	}
	
	public void insertToTemp(List<ApplicationParameterApi> appParamsApi, String updatedBy) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		
		for(ApplicationParameterApi appParamApi : appParamsApi){
			ApplicationParameterTemp temp = new ApplicationParameterTemp();
			temp.setKey(appParamApi.getKey());
			temp.setOldValue(appParamApi.getOldValue());
			temp.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
			temp.setUpdatedBy(updatedBy);
			temp.setNewValue(appParamApi.getNewValue());
			
			applicationParameterTempDao.create(temp);
		}
		
		applicationParameterTempDao.deleteReviewed();
	}
	
	public ApplicationParameter getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return applicationParameterDao.getByKey(key);
	}
	
	public List<ApplicationParameterApi> getUnapprovedAppParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<ApplicationParameterApi> appParams = new ArrayList<ApplicationParameterApi>();
		
		List<ApplicationParameter> allParams = applicationParameterDao.getAllAppParams();
		
		for(ApplicationParameter param : allParams){
			ApplicationParameterApi appParam = new ApplicationParameterApi();
			appParam.setKey(param.getKey());
			appParam.setKeyDesc(param.getKeyDesc());
			appParam.setOldValue(param.getValue());
			appParam.setValueType(param.getValueType());
			
			ApplicationParameterTemp temp = applicationParameterTempDao.getByKey(param.getKey());
			if(temp!=null)
				appParam.setNewValue(temp.getNewValue());
			else
				appParam.setNewValue(param.getValue());
			
			appParams.add(appParam);
		}
		
		return appParams;
	}
	
	public Integer getTotalNotReviewedRow(){
		return applicationParameterTempDao.getTotalNotReviewedRow();
	}
	
	public String getUpdatedBy(){
		return applicationParameterTempDao.getUpdatedBy();
	}
	
	public List<ApplicationParameterTemp> getNotReviewed() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return applicationParameterTempDao.getNotReviewed();
	}
	
	public List<ApplicationParameterTemp> getByRequester(String userId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return applicationParameterTempDao.getByRequester(userId, notifStatus);
	}
	
	public void setNotifOff(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		List<ApplicationParameterTemp> temps = applicationParameterTempDao.getByRequester(userId, NotifStatus.SHOW);
		
		for(ApplicationParameterTemp temp : temps){
			temp.setShowNotifStatus(NotifStatus.HIDE);
			applicationParameterTempDao.update(temp);
		}		
	}
}
