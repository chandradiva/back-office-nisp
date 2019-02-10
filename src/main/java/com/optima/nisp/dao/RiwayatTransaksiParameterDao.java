package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.RiwayatTransaksiParameter;

@Component
public class RiwayatTransaksiParameterDao extends ParentDao<RiwayatTransaksiParameter> {

	public List<RiwayatTransaksiParameter> getAllAppParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<RiwayatTransaksiParameter> res = new ArrayList<RiwayatTransaksiParameter>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM ORDER BY KEY";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			RiwayatTransaksiParameter param = serialize(RiwayatTransaksiParameter.class, row);
			res.add(param);
		}
		
		return res;
	}
	
	public RiwayatTransaksiParameter getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM WHERE KEY = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(key);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		
		return serialize(RiwayatTransaksiParameter.class, rows.get(0));		
	}
}
