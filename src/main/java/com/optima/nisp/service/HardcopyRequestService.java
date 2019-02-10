package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.dao.HardcopyRequestDao;
import com.optima.nisp.model.HardcopyRequest;

@Component
@Transactional
public class HardcopyRequestService {

	@Autowired
	private HardcopyRequestDao hardcopyRequestDao;
	
	public Integer getRequestTotalRow(String reqDateFrom, String reqDateTo, String periodeFrom, String periodeTo, String accountNumber){
		return hardcopyRequestDao.getRequestTotalRow(reqDateFrom, reqDateTo, periodeFrom, periodeTo, accountNumber);
	}
	
	public List<HardcopyRequest> getRequestList(String reqDateFrom, String reqDateTo, String periodeFrom, String periodeTo, String accountNumber, int start, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return hardcopyRequestDao.getList(reqDateFrom, reqDateTo, periodeFrom, periodeTo, accountNumber, start, length);
	}
	
	public List<HardcopyRequest> getHistoryList(String reqDateFrom, String reqDateTo, String periodeFrom, String periodeTo, String accountNumber, String batchId, Integer status, int start, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return hardcopyRequestDao.getHistoryList(reqDateFrom, reqDateTo, periodeFrom, periodeTo, accountNumber, batchId, status, start, length);
	}
	
	public Integer getHistoryTotalRow(String reqDateFrom, String reqDateTo, String periodeFrom, String periodeTo, String accountNumber, String batchId, Integer status){
		return hardcopyRequestDao.getHistoryTotalRow(reqDateFrom, reqDateTo, periodeFrom, periodeTo, accountNumber, batchId, status);
	}
	
	public HardcopyRequest getById(Long hcReqId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return hardcopyRequestDao.getById(hcReqId);
	}
	
	public Long getBatchId(Long hcReqId) {
		return hardcopyRequestDao.getBatchId(hcReqId);
	}
}
