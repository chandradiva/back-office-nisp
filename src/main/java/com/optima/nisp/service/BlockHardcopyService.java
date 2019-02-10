package com.optima.nisp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.model.BlockHardcopy;
import com.optima.nisp.dao.BlockHardcopyDao;

@Component
@Transactional
public class BlockHardcopyService {

	@Autowired
	private BlockHardcopyDao blockHardcopyDao;
	
	public List<BlockHardcopy> getList(String name, String accountNumber, Integer status, int start, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		return blockHardcopyDao.getList(name, accountNumber, status, start, length);
	}
	
	public int getTotalRow(String name, String accountNumber, Integer status){
		return blockHardcopyDao.getTotalRow(name, accountNumber, status);
	}
	
	public void changeStatus(BlockHardcopy[] blockHcs) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		blockHardcopyDao.updateBatch(blockHcs);
	}
}
