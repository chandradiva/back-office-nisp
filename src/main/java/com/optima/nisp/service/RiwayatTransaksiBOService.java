package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.dao.RiwayatTransaksiBODao;
import com.optima.nisp.model.RekeningListBOView;

@Component
@Transactional
public class RiwayatTransaksiBOService {
	@Autowired
	private RiwayatTransaksiBODao riwayatTransaksiBODao;
	
	public List<RekeningListBOView> getRekeningList(int start, int length, String cifKey, String nomorRekening,
			String namaNasabah, String currency) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return riwayatTransaksiBODao.getRekeningList(start, length, cifKey, nomorRekening, namaNasabah, currency);
	}
	
	public int getRekeningListTotalRow(int start, int length, String cifKey, String nomorRekening,
			String namaNasabah, String currency) {
		return riwayatTransaksiBODao.getRekeningListCount(start, length, cifKey, nomorRekening, namaNasabah, currency);
	}
}
