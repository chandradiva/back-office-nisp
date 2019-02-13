package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.KonsolidasiBlacklist;

@Component
public class KonsolidasiBlacklistDao extends ParentDao<KonsolidasiBlacklist> {

	public List<KonsolidasiBlacklist> getAllData(String cifKey, int page, int totalPerPage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		int batasBawah = 1 + totalPerPage * (page - 1);
		int batasAtas = totalPerPage * page;
		
		String sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM ( SELECT * FROM " + CommonCons.FE_DB_SCHEMA + ".BO_TBL_KONSOLIDASI_BLACKLIST "
				+ "WHERE (CIF_ORI LIKE '%" + cifKey + "%' " + (cifKey.length() > 0 ? "" : "OR CIF_ORI IS NULL") + ") "
				+ "ORDER BY FLAG DESC, CIF_ORI) Q) WHERE R >= "+batasBawah+" AND R <= " + batasAtas;
		
		ArrayList<KonsolidasiBlacklist> res = new ArrayList<KonsolidasiBlacklist>();
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			KonsolidasiBlacklist se = serialize(KonsolidasiBlacklist.class, row);
			res.add(se);
		}
				
		return res;
	}
	
	public int getTotalRow(String cifKey) {
		String sql;
		
		sql = "SELECT COUNT(*) TOTAL FROM "
				+ "(SELECT * FROM "+CommonCons.FE_DB_SCHEMA + ".BO_TBL_KONSOLIDASI_BLACKLIST WHERE "
				+ "(CIF_ORI LIKE '%" + cifKey + "%' " + (cifKey.length() > 0 ? "" : "OR CIF_ORI IS NULL") + "))";
		
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if (rows.size() > 0){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public List<KonsolidasiBlacklist> getAllBlacklist() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<KonsolidasiBlacklist> res = new ArrayList<KonsolidasiBlacklist>();
		String sql = "SELECT * FROM " + CommonCons.FE_DB_SCHEMA + ".BO_TBL_KONSOLIDASI_BLACKLIST WHERE FLAG = 1 ORDER BY CIF_PAD";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			KonsolidasiBlacklist param = serialize(KonsolidasiBlacklist.class, row);
			res.add(param);
		}
		
		return res;
	}
	
	public List<KonsolidasiBlacklist> getAllNotBlacklist() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<KonsolidasiBlacklist> res = new ArrayList<KonsolidasiBlacklist>();
		String sql = "SELECT * FROM " + CommonCons.FE_DB_SCHEMA + ".BO_TBL_KONSOLIDASI_BLACKLIST WHERE FLAG = 0 ORDER BY CIF_PAD";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			KonsolidasiBlacklist param = serialize(KonsolidasiBlacklist.class, row);
			res.add(param);
		}
		
		return res;
	}
	
	public KonsolidasiBlacklist getByID(Long id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM " + CommonCons.FE_DB_SCHEMA + ".BO_TBL_KONSOLIDASI_BLACKLIST WHERE ID = ?";
		
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if (rows.size() == 0) {
			return null;
		}
		
		return serialize(KonsolidasiBlacklist.class, rows.get(0));		
	}
	
	public KonsolidasiBlacklist getByCIF(String cif) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM " + CommonCons.FE_DB_SCHEMA + ".BO_TBL_KONSOLIDASI_BLACKLIST WHERE CIF_PAD = ?";
		
		List<Object> args = new ArrayList<Object>();
		args.add(cif);
		
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if (rows.size() == 0) {
			return null;
		}
		
		return serialize(KonsolidasiBlacklist.class, rows.get(0));		
	}
	
}
