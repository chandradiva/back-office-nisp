package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optima.nisp.dao.HousekeepingLogDao;
import com.optima.nisp.model.HousekeepingLog;

@Service
public class HousekeepingLogService {
	@Autowired
	HousekeepingLogDao hklDao;
	
	public int getLogs(String strStartDate, String strEndDate, int status, String remarks, Integer start, Integer length, List<HousekeepingLog> res) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try{
			Date startDate = null;
			if( strStartDate.length() > 0 )
				startDate = sdf.parse(strStartDate);
			Date endDate = null;
			if( strEndDate.length() > 0 )
				endDate = sdf.parse(strEndDate);
			if( remarks.length() == 0 )
				remarks = null;
			return hklDao.getLogs(startDate, endDate, status, remarks, start+1, length, res);
		}catch( ParseException e){
			e.printStackTrace();
		}
		return 0;
	}
}
