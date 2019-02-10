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
import com.optima.nisp.dao.LaporanKonsolidasiParameterDao;
import com.optima.nisp.dao.LaporanKonsolidasiParameterTempDao;
import com.optima.nisp.model.LaporanKonsolidasiParameter;
import com.optima.nisp.model.LaporanKonsolidasiParameterTemp;
import com.optima.nisp.model.api.LaporanKonsolidasiParameterApi;

@Component
@Transactional
public class LaporanKonsolidasiParameterService {

	@Autowired
	private LaporanKonsolidasiParameterDao konsolidasiDao;
	
	@Autowired
	private LaporanKonsolidasiParameterTempDao konsolidasiTempDao;
	
	public List<LaporanKonsolidasiParameter> getAllAppParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return konsolidasiDao.getAllAppParams();
	}
	
	public void approveAppParams(String approvedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<LaporanKonsolidasiParameterTemp> temps = konsolidasiTempDao.getNotReviewed();
		
		for(LaporanKonsolidasiParameterTemp temp : temps){
			LaporanKonsolidasiParameter updatedParam = konsolidasiDao.getByKey(temp.getKey());
			if(updatedParam!=null){
				updatedParam.setValue(temp.getNewValue());
				updatedParam.setUpdatedBy(temp.getUpdatedBy());
				updatedParam.setUpdatedDate(new Date());
				updatedParam.setApprovedBy(approvedBy);
				konsolidasiDao.update(updatedParam);
			}
		}		
		
		/*Ubah status temp menjadi APPROVED*/
		List<LaporanKonsolidasiParameterTemp> temps2 = konsolidasiTempDao.getNotReviewed();
		for(LaporanKonsolidasiParameterTemp temp : temps2){
			temp.setReviewedBy(approvedBy);
			temp.setReviewedStatus(ReviewStatus.APPROVED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			konsolidasiTempDao.update(temp);
		}
	}
	
	public void rejectAppParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi REJECTED*/
		List<LaporanKonsolidasiParameterTemp> temps = konsolidasiTempDao.getNotReviewed();
		for(LaporanKonsolidasiParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.REJECTED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			konsolidasiTempDao.update(temp);
		}
	}
	
	public void cancelAppParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{		
		/*Ubah status temp menjadi CANCELED*/
		List<LaporanKonsolidasiParameterTemp> temps = konsolidasiTempDao.getNotReviewed();
		for(LaporanKonsolidasiParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.CANCELED);
			temp.setShowNotifStatus(NotifStatus.HIDE);
			konsolidasiTempDao.update(temp);
		}
	}
	
	public void insertToTemp(List<LaporanKonsolidasiParameterApi> appParamsApi, String updatedBy) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		
		for(LaporanKonsolidasiParameterApi appParamApi : appParamsApi){
			LaporanKonsolidasiParameterTemp temp = new LaporanKonsolidasiParameterTemp();
			temp.setKey(appParamApi.getKey());
			temp.setOldValue(appParamApi.getOldValue());
			temp.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
			temp.setUpdatedBy(updatedBy);
			temp.setNewValue(appParamApi.getNewValue());
			
			konsolidasiTempDao.create(temp);
		}
		
		konsolidasiTempDao.deleteReviewed();
	}
	
	public LaporanKonsolidasiParameter getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return konsolidasiDao.getByKey(key);
	}
	
	public List<LaporanKonsolidasiParameterApi> getUnapprovedAppParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<LaporanKonsolidasiParameterApi> appParams = new ArrayList<LaporanKonsolidasiParameterApi>();
		
		List<LaporanKonsolidasiParameter> allParams = konsolidasiDao.getAllAppParams();
		
		for(LaporanKonsolidasiParameter param : allParams){
			LaporanKonsolidasiParameterApi appParam = new LaporanKonsolidasiParameterApi();
			appParam.setKey(param.getKey());
			appParam.setKeyDesc(param.getKeyDesc());
			appParam.setOldValue(param.getValue());
			
			LaporanKonsolidasiParameterTemp temp = konsolidasiTempDao.getByKey(param.getKey());
			if(temp!=null)
				appParam.setNewValue(temp.getNewValue());
			else
				appParam.setNewValue(param.getValue());
			
			appParams.add(appParam);
		}
		
		return appParams;
	}
	
	public Integer getTotalNotReviewedRow(){
		return konsolidasiTempDao.getTotalNotReviewedRow();
	}
	
	public String getUpdatedBy(){
		return konsolidasiTempDao.getUpdatedBy();
	}
	
	public List<LaporanKonsolidasiParameterTemp> getNotReviewed() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return konsolidasiTempDao.getNotReviewed();
	}
	
	public List<LaporanKonsolidasiParameterTemp> getByRequester(String userId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return konsolidasiTempDao.getByRequester(userId, notifStatus);
	}
	
	public void setNotifOff(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		List<LaporanKonsolidasiParameterTemp> temps = konsolidasiTempDao.getByRequester(userId, NotifStatus.SHOW);
		
		for(LaporanKonsolidasiParameterTemp temp : temps){
			temp.setShowNotifStatus(NotifStatus.HIDE);
			konsolidasiTempDao.update(temp);
		}		
	}
	
}
