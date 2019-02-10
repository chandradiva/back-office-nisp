package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.dao.KonsolidasiBlacklistDao;
import com.optima.nisp.model.KonsolidasiBlacklist;
import com.optima.nisp.model.api.KonsolidasiBlacklistApi;
import com.optima.nisp.utility.StringUtils;

@Component
@Transactional
public class KonsolidasiBlacklistService {

	@Autowired
	private KonsolidasiBlacklistDao blacklistDao;
	
	public List<KonsolidasiBlacklist> getAllData(String cifKey, int page, int totalPerPage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return blacklistDao.getAllData(cifKey, page, totalPerPage);
	}
	
	public int getTotalRow(String cifKey) {
		return blacklistDao.getTotalRow(cifKey);
	}
	
	public List<KonsolidasiBlacklist> getAllBlacklist() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return blacklistDao.getAllBlacklist();
	}
	
	public List<KonsolidasiBlacklist> getAllNotBlacklist() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return blacklistDao.getAllNotBlacklist();
	}
	
	public KonsolidasiBlacklist getByID(Long id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		return getByID(id);
	}
	
	public void createNew(KonsolidasiBlacklistApi data) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		KonsolidasiBlacklist newData = new KonsolidasiBlacklist();
		
		newData.setCifOri(data.getCif());
		newData.setCifPad(data.getCif());
		newData.setCreatedDate(new Date());
		newData.setFlag(new Long(1));
		
		blacklistDao.create(newData);
	}
	
	public void insertData(KonsolidasiBlacklistApi req) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		KonsolidasiBlacklist blacklist = blacklistDao.getByCIF(req.getCif());
		if (blacklist == null) {
			blacklist = new KonsolidasiBlacklist();
			blacklist.setCifOri(req.getCif());
			blacklist.setCifPad(StringUtils.normalizeCifKey(req.getCif()));
			blacklist.setCreatedDate(new Date());
			blacklist.setFlag(new Long(1));
			
			blacklistDao.create(blacklist);
		} else {
			blacklist.setUpdatedDate(new Date());
			blacklist.setFlag(new Long(1));
			
			blacklistDao.update(blacklist);
		}
		
	}
}
