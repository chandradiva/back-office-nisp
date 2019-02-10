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
import com.optima.nisp.dao.RiwayatTransaksiParameterDao;
import com.optima.nisp.dao.RiwayatTransaksiParameterTempDao;
import com.optima.nisp.model.RiwayatTransaksiParameter;
import com.optima.nisp.model.RiwayatTransaksiParameterTemp;
import com.optima.nisp.model.api.RiwayatTransaksiParameterApi;

@Component
@Transactional
public class RiwayatTransaksiParameterService {

	@Autowired
	private RiwayatTransaksiParameterDao riwayatTransaksiParameterDao;
	
	@Autowired
	private RiwayatTransaksiParameterTempDao riwayatTransaksiParameterTempDao;
	
	public List<RiwayatTransaksiParameter> getAllAppParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return riwayatTransaksiParameterDao.getAllAppParams();
	}
	
	public void approveAppParams(String approvedBy) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<RiwayatTransaksiParameterTemp> temps = riwayatTransaksiParameterTempDao.getNotReviewed();
		
		for(RiwayatTransaksiParameterTemp temp : temps){
			RiwayatTransaksiParameter updatedParam = riwayatTransaksiParameterDao.getByKey(temp.getKey());
			if(updatedParam!=null){
				updatedParam.setValue(temp.getNewValue());
				updatedParam.setUpdatedBy(temp.getUpdatedBy());
				updatedParam.setUpdatedDate(new Date());
				updatedParam.setApprovedBy(approvedBy);
				riwayatTransaksiParameterDao.update(updatedParam);
			}
		}		
		
		/*Ubah status temp menjadi APPROVED*/
		List<RiwayatTransaksiParameterTemp> temps2 = riwayatTransaksiParameterTempDao.getNotReviewed();
		for(RiwayatTransaksiParameterTemp temp : temps2){
			temp.setReviewedBy(approvedBy);
			temp.setReviewedStatus(ReviewStatus.APPROVED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			riwayatTransaksiParameterTempDao.update(temp);
		}
	}
	
	public void rejectAppParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		/*Ubah status temp menjadi REJECTED*/
		List<RiwayatTransaksiParameterTemp> temps = riwayatTransaksiParameterTempDao.getNotReviewed();
		for(RiwayatTransaksiParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.REJECTED);
			temp.setShowNotifStatus(NotifStatus.SHOW);
			riwayatTransaksiParameterTempDao.update(temp);
		}
	}
	
	public void cancelAppParams(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{		
		/*Ubah status temp menjadi CANCELED*/
		List<RiwayatTransaksiParameterTemp> temps = riwayatTransaksiParameterTempDao.getNotReviewed();
		for(RiwayatTransaksiParameterTemp temp : temps){
			temp.setReviewedBy(userId);
			temp.setReviewedStatus(ReviewStatus.CANCELED);
			temp.setShowNotifStatus(NotifStatus.HIDE);
			riwayatTransaksiParameterTempDao.update(temp);
		}
	}
	
	public void insertToTemp(List<RiwayatTransaksiParameterApi> appParamsApi, String updatedBy) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		
		for(RiwayatTransaksiParameterApi appParamApi : appParamsApi){
			RiwayatTransaksiParameterTemp temp = new RiwayatTransaksiParameterTemp();
			temp.setKey(appParamApi.getKey());
			temp.setOldValue(appParamApi.getOldValue());
			temp.setReviewedStatus(ReviewStatus.NOT_REVIEWED);
			temp.setUpdatedBy(updatedBy);
			temp.setNewValue(appParamApi.getNewValue());
			
			riwayatTransaksiParameterTempDao.create(temp);
		}
		
		riwayatTransaksiParameterTempDao.deleteReviewed();
	}
	
	public RiwayatTransaksiParameter getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return riwayatTransaksiParameterDao.getByKey(key);
	}
	
	public List<RiwayatTransaksiParameterApi> getUnapprovedAppParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		List<RiwayatTransaksiParameterApi> appParams = new ArrayList<RiwayatTransaksiParameterApi>();
		
		List<RiwayatTransaksiParameter> allParams = riwayatTransaksiParameterDao.getAllAppParams();
		
		for(RiwayatTransaksiParameter param : allParams){
			RiwayatTransaksiParameterApi appParam = new RiwayatTransaksiParameterApi();
			appParam.setKey(param.getKey());
			appParam.setKeyDesc(param.getKeyDesc());
			appParam.setOldValue(param.getValue());
			
			RiwayatTransaksiParameterTemp temp = riwayatTransaksiParameterTempDao.getByKey(param.getKey());
			if(temp!=null)
				appParam.setNewValue(temp.getNewValue());
			else
				appParam.setNewValue(param.getValue());
			
			appParams.add(appParam);
		}
		
		return appParams;
	}
	
	public Integer getTotalNotReviewedRow(){
		return riwayatTransaksiParameterTempDao.getTotalNotReviewedRow();
	}
	
	public String getUpdatedBy(){
		return riwayatTransaksiParameterTempDao.getUpdatedBy();
	}
	
	public List<RiwayatTransaksiParameterTemp> getNotReviewed() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return riwayatTransaksiParameterTempDao.getNotReviewed();
	}
	
	public List<RiwayatTransaksiParameterTemp> getByRequester(String userId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return riwayatTransaksiParameterTempDao.getByRequester(userId, notifStatus);
	}
	
	public void setNotifOff(String userId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		List<RiwayatTransaksiParameterTemp> temps = riwayatTransaksiParameterTempDao.getByRequester(userId, NotifStatus.SHOW);
		
		for(RiwayatTransaksiParameterTemp temp : temps){
			temp.setShowNotifStatus(NotifStatus.HIDE);
			riwayatTransaksiParameterTempDao.update(temp);
		}		
	}
}
