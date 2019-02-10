package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.BlockHardcopyStatus;
import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.BlockHardcopy;

@Component
public class BlockHardcopyDao extends ParentDao<BlockHardcopy> {

	public List<BlockHardcopy> getList(String name, String accountNumber, Integer status, int start, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		name = name.toLowerCase().trim();
		
		boolean isAddStatus = false;
		if(status != BlockHardcopyStatus.ALL_STATUS){
			isAddStatus = true;
		}
		
		ArrayList<BlockHardcopy> res = new ArrayList<BlockHardcopy>();
		String sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM "
				+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_BLOCK_HC "
				+ "WHERE LOWER(NAME) LIKE '%"+name+"%' AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' " + (isAddStatus ? "AND STATUS = "+status+" " : "")
				+ "ORDER BY NAME) Q) WHERE R > "+start+" AND R <= " +(start+length);
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			BlockHardcopy blockHc = serialize(BlockHardcopy.class, row);
			res.add(blockHc);
		}
		return res;
	}
	
	public int getTotalRow(String name, String accountNumber, Integer status){
		name = name.toLowerCase().trim();
		
		boolean isAddStatus = false;
		if(status != BlockHardcopyStatus.ALL_STATUS){
			isAddStatus = true;
		}
		
		String sql = "SELECT COUNT(*) TOTAL FROM "+CommonCons.DB_SCHEMA+".BO_TBL_BLOCK_HC "
				+ "WHERE LOWER(NAME) LIKE '%"+name+"%' AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' " + (isAddStatus ? "AND STATUS = "+status+" " : "");
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
}
