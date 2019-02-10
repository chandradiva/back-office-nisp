package com.optima.nisp.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optima.nisp.dao.BoLastLoginDao;

@Service
public class BoLastLoginService {
	@Autowired
	BoLastLoginDao dao;
	
	public void save(String username){
		dao.save(username);
	}
	
	public Date getLastLogin(String username){
		return dao.getLastLogin(username);
	}
}
