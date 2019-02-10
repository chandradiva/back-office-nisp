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
import com.optima.nisp.dao.ContentParameterDao;
import com.optima.nisp.dao.ContentParameterTempDao;
import com.optima.nisp.model.ContentParameter;
import com.optima.nisp.model.ContentParameterTemp;
import com.optima.nisp.model.api.ContentParameterApi;

@Component
@Transactional
public class ContentParameterService {

	@Autowired
	private ContentParameterDao contentParameterDao;
	
	@Autowired
	private ContentParameterTempDao contentParameterTempDao;
	
	public List<ContentParameter> getAllContentParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return contentParameterDao.getAllContentParams();
	}

	public void approveContentParams(String approvedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<ContentParameterTemp> temps = contentParameterTempDao.getNotReviewed();
		
		for(ContentParameterTemp temp : temps){
			ContentParameter updatedParam = contentParameterDao.getByKey(temp.getKey());
			if(updatedParam!=null){
				updatedParam.setValue(temp.getNewValue());
				updatedParam.setUpdatedBy(temp.getUpdatedBy());
				updatedParam.setUpdatedDate(new Date());
				updatedParam.setApprovedBy(approvedBy);
				contentParameterDao.update(updatedParam);
			}
		}		
		
		/*Ubah status temp menjadi APPROVED*/
		List<ContentParameterTemp> temps2 = contentParameterTempDao.getNotReviewed();
		for(ContentParameterTemp temp : temps2){
			temp.setReviewedBy(approvedBy);
			temp.setReviewedStatus(ReviewStatus.APPROVED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			contentParameterDao.update(temp);
		}
	}
	
	public void rejectContentParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{		
		/*Ubah status temp menjadi REJECTED*/
		List<ContentParameterTemp> temps = contentParameterTempDao.getNotReviewed();
		for(ContentParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.REJECTED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			contentParameterDao.update(temp);
		}
	}
	
	public void cancelContentParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{		
		/*Ubah status temp menjadi CANCELED*/
		List<ContentParameterTemp> temps = contentParameterTempDao.getNotReviewed();
		for(ContentParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.CANCELED);
			temp.setShowNotifStatus(NotifStatus.HIDE);
			contentParameterDao.update(temp);
		}
	}
	
	public void insertToTemp(List<ContentParameterApi> contentParamsApi, String updatedBy) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		
		for(ContentParameterApi contentParamApi : contentParamsApi){
			ContentParameterTemp temp = new ContentParameterTemp();
			temp.setKey(contentParamApi.getKey());
			temp.setOldValue(contentParamApi.getOldValue());
			temp.setNewValue(contentParamApi.getNewValue());
			temp.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
			temp.setUpdatedBy(updatedBy);
			contentParameterTempDao.create(temp);
		}
		
		contentParameterTempDao.deleteReviewed();
	}
	
	public ContentParameter getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return contentParameterDao.getByKey(key);
	}
	
	public List<ContentParameterApi> getUnapprovedContentParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<ContentParameterApi> contentParams = new ArrayList<ContentParameterApi>();
		
		List<ContentParameter> allParams = contentParameterDao.getAllContentParams();
		
		for(ContentParameter param : allParams){
			ContentParameterApi contentParam = new ContentParameterApi();
			contentParam.setKey(param.getKey());
			contentParam.setKeyDesc(param.getKeyDesc());
			contentParam.setOldValue(param.getValue());
			
			ContentParameterTemp temp = contentParameterTempDao.getByKey(param.getKey());
			if(temp!=null)
				contentParam.setNewValue(temp.getNewValue());
			else
				contentParam.setNewValue(param.getValue());
			
			contentParams.add(contentParam);
		}
		
		return contentParams;
	}
	
	public Integer getTotalNotReviewedRow(){
		return contentParameterTempDao.getTotalNotReviewedRow();
	}
	
	public String getUpdatedBy(){
		return contentParameterTempDao.getUpdatedBy();
	}
	
	public List<ContentParameterTemp> getNotReviewed() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return contentParameterTempDao.getNotReviewed();
	}
	
	public List<ContentParameterTemp> getByRequester(String userId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return contentParameterTempDao.getByRequester(userId, notifStatus);
	}
	
	public void setNotifOff(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		List<ContentParameterTemp> temps = contentParameterTempDao.getByRequester(userId, NotifStatus.SHOW);
		
		for(ContentParameterTemp temp : temps){
			temp.setShowNotifStatus(NotifStatus.HIDE);
			contentParameterTempDao.update(temp);
		}
	}
}
