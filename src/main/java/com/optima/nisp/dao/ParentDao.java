package com.optima.nisp.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;

public abstract class ParentDao<T> {
	
	private static final Logger logger = Logger.getLogger(ParentDao.class);
	
	protected JdbcTemplate template;
	
	private DataSource apiDataSource;
	
	@Autowired
	public void setApiDataSource(DataSource apiDataSource){
		this.apiDataSource = apiDataSource;
		this.template = new JdbcTemplate(this.apiDataSource);
	}
	
	@Transactional
	public void createBatch(Object dataArray[]) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if( dataArray.length > 0 ){
			Class<?> type = dataArray[0].getClass();
			Class<?>[] noparam = new Class<?>[0];
			Class<?>[] param = new Class[1];
			Date today = null;
			String sql = null;
			List<Object[]> argsList = new ArrayList<Object[]>();
			for(int i = 0; i<dataArray.length; i++){
				if( type.isAnnotationPresent(NISPEntity.class) ){
					NISPEntity ann = (NISPEntity)type.getAnnotation(NISPEntity.class);
					String tableName = ann.tableName();
					String dbSchema = ann.dbSchema();
					String columnName = null;
					String idFieldName = "";
					final ArrayList<Object> args = new ArrayList<Object>();
					Field[] listField = null;
					List<Field> allField = null;
					if (type.getSuperclass() != null) {
						listField = type.getSuperclass().getDeclaredFields();
						allField = new ArrayList<Field>(Arrays.asList(listField));
					}
					
					if (allField != null) {
						allField.addAll(Arrays.asList(type.getDeclaredFields()));
					} else {
						allField = Arrays.asList(type.getDeclaredFields());
					}
					
					for (Field f : allField) {
						today = new Date();
						
						if( f.isAnnotationPresent(NISPEntityId.class) ){
							idFieldName = f.getName();
						}
						if( f.isAnnotationPresent(NISPEntity.class) ){
							ann = (NISPEntity)f.getAnnotation(NISPEntity.class);
							
							if( columnName == null )
								columnName = ann.columnName();
							else	
								columnName += ","+ann.columnName();
							
							String fieldName = f.getName();
							if( fieldName.equals(idFieldName)){
								idFieldName = ann.columnName();
							}
							String getter = "get"+Character.toUpperCase(fieldName.charAt(0));
							if( fieldName.length() > 1 )
								getter+=fieldName.substring(1);
							
							Method method = type.getMethod(getter, noparam);
							if (method.getName().equals("getUpdatedDate")) {
								if (!tableName.startsWith("CC_")) {
									param[0] = f.getType();
									method = type.getDeclaredMethod("setUpdatedDate", param);
									method.invoke(dataArray[i], new Object[]{ null });
									
//									method = type.getMethod(getter, noparam);
									args.add(null);
								} else {
									param[0] = f.getType();
									method = type.getDeclaredMethod("setUpdatedDate", param);
									method.invoke(dataArray[i], today);
									
//									method = type.getMethod(getter, noparam);
									args.add(today);
								}
							} else if (method.getName().equals("getCreatedDate")) {
								param[0] = f.getType();
								method = type.getDeclaredMethod("setCreatedDate", param);
								method.invoke(dataArray[i], today);
								
								method = type.getMethod(getter, noparam);
								args.add(today);
							} else {
								args.add(method.invoke(dataArray[i]));
							}
															
						}
					}
					if( columnName != null && sql == null ){
						sql = "INSERT INTO "+dbSchema+"."+tableName+"("+columnName+") VALUES(";
						for(int j=0;j<args.size();j++){
							if( j > 0 )
								sql+=",";
							sql+="?";								
						}
						sql+=")";
					}			
					argsList.add(args.toArray());
				}				
			}
			template.batchUpdate(sql, argsList);
		}
	}
	
	@Transactional
	public Long create(Object data) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> type = data.getClass();
		Class<?>[] noparam = new Class<?>[0];
		Class<?>[] param = new Class[1];
		KeyHolder keyHodler = new GeneratedKeyHolder();
		
		Date today = null;
		if( type.isAnnotationPresent(NISPEntity.class) ){
			NISPEntity ann = (NISPEntity)type.getAnnotation(NISPEntity.class);
			String tableName = ann.tableName();
			String dbSchema = ann.dbSchema();
			String columnName = null;
			String idFieldName = "";
			final ArrayList<Object> args = new ArrayList<Object>();
			Field[] listField = null;
			List<Field> allField = null;
			if (type.getSuperclass() != null) {
				listField = type.getSuperclass().getDeclaredFields();
				allField = new ArrayList<Field>(Arrays.asList(listField));
			}
			
			if (allField != null) {
				allField.addAll(Arrays.asList(type.getDeclaredFields()));
			} else {
				allField = Arrays.asList(type.getDeclaredFields());
			}
			ArrayList<Integer> argsType = new ArrayList<Integer>();
			for (Field f : allField) {
				today = new Date();
				
				if( f.isAnnotationPresent(NISPEntityId.class) ){
					idFieldName = f.getName();
				}
				if( f.isAnnotationPresent(NISPEntity.class) ){
					ann = (NISPEntity)f.getAnnotation(NISPEntity.class);
					
					if( columnName == null )
						columnName = ann.columnName();
					else	
						columnName += ","+ann.columnName();
					
					String fieldName = f.getName();
					if( fieldName.equals(idFieldName)){
						idFieldName = ann.columnName();
					}
					String getter = "get"+Character.toUpperCase(fieldName.charAt(0));
					if( fieldName.length() > 1 )
						getter+=fieldName.substring(1);
					
					Method method = type.getMethod(getter, noparam);
					if (method.getName().equals("getUpdatedDate")) {
						if (!tableName.startsWith("CC_")) {
							param[0] = f.getType();
							method = type.getDeclaredMethod("setUpdatedDate", param);
							method.invoke(data, new Object[]{ null });
							
							method = type.getMethod(getter, noparam);
							args.add(method.invoke(data));
						} else {
							param[0] = f.getType();
							method = type.getDeclaredMethod("setUpdatedDate", param);
							method.invoke(data, today);
							
							method = type.getMethod(getter, noparam);
							args.add(method.invoke(data));
						}
					} else if (method.getName().equals("getCreatedDate")) {
						param[0] = f.getType();
						method = type.getDeclaredMethod("setCreatedDate", param);
						method.invoke(data, today);
						
						method = type.getMethod(getter, noparam);
						args.add(method.invoke(data));
					} else {
						args.add(method.invoke(data));
					}
					
					argsType.add(getSqlType(f.getType().getSimpleName()));
				}
			}
			if( columnName != null ){
				String sql = "INSERT INTO "+dbSchema+"."+tableName+"("+columnName+") VALUES(";
				final int[] types = new int[argsType.size()];
				for(int i=0;i<args.size();i++){
					if( i > 0 )
						sql+=",";
					sql+="?";
					types[i] = argsType.get(i);
				}
				sql+=")";
				final String fSql = sql;
				final String fIdFieldName = idFieldName;
				template.update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(fSql, new String[] {fIdFieldName});
						int i = 0;
	                    for ( Object data : args.toArray()) {
	                    	if( data == null ){
	                    		ps.setObject(i+1, null);
	                    	}else if( types[i] == Types.BIGINT ){
								ps.setLong(i+1, (long)data);
							}else if( types[i] == Types.VARCHAR ){
								ps.setString(i+1, data.toString());
							}else if( types[i] == Types.DATE ){
								ps.setDate(i+1, new java.sql.Date(((Date)data).getTime()));
							}else if( types[i] == Types.INTEGER ){
								ps.setInt(i+1, (int)data);
							}else if( types[i] == Types.DOUBLE ){
								ps.setDouble(i+1, (double)data);
							}else if( types[i] == Types.FLOAT ){
								ps.setFloat(i+1, (float)data);
							} else if (types[i] == Types.CHAR) {
								ps.setString(i+1, ((Character)data).toString());
							}
	                    	i++;
						}
	                    return ps;
					}
				},keyHodler);
			}								
		}
		
		try {
			long res = keyHodler.getKey().longValue();
			
			return res;
		} catch (DataRetrievalFailureException e) {
			return null;
		} catch (Exception e) {
			logger.error("Error", e);
			return null;
		}
	}
	
	@Transactional
	public void updateBatch(Object[] dataArray) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if( dataArray.length > 0 ){
			Class<?> type = dataArray[0].getClass();
			Class<?>[] noparam = new Class<?>[0];
			String sql = null;
			List<Object[]> argsList = new ArrayList<Object[]>();
			for(int i=0;i<dataArray.length;i++){
				String keysWhere="";
				if( type.isAnnotationPresent(NISPEntity.class) ){
					NISPEntity ann = (NISPEntity)type.getAnnotation(NISPEntity.class);
					String tableName = ann.tableName();
					String dbSchema = ann.dbSchema();
					String columnName = null;
					final ArrayList<Object> args = new ArrayList<Object>();
					Field[] listField = null;
					List<Field> allField = null;
					if (type.getSuperclass() != null) {
						listField = type.getSuperclass().getDeclaredFields();
						allField = new ArrayList<Field>(Arrays.asList(listField));
					}
					
					if (allField != null) {
						allField.addAll(Arrays.asList(type.getDeclaredFields()));
					} else {
						allField = Arrays.asList(type.getDeclaredFields());
					}
					Object idValue = null;
					Hashtable<String, Object>keys = new Hashtable<String, Object>();
					ArrayList<Object> keyArgs = new ArrayList<Object>();
					for (Field f : allField) {
						if(f.isAnnotationPresent(NISPEntityId.class) ){
							String fieldName = f.getName();						
							String getter = "get"+Character.toUpperCase(fieldName.charAt(0));
							if( fieldName.length() > 1 )
								getter+=fieldName.substring(1);
							Method method = type.getMethod(getter, noparam);
							idValue = method.invoke(dataArray[i]);
							if( idValue != null )
								keys.put(fieldName, idValue);
						}
						if( f.isAnnotationPresent(NISPEntity.class) ){
							ann = (NISPEntity)f.getAnnotation(NISPEntity.class);
							String fieldName = f.getName();
							idValue = keys.get(fieldName);
							if( idValue != null ){
								if( keysWhere.length() > 0 )
									keysWhere += " AND ";
								keysWhere += ann.columnName()+"=?";
								keyArgs.add(idValue);
								continue;
							}
							
							String getter = "get"+Character.toUpperCase(fieldName.charAt(0));
							if( fieldName.length() > 1 )
								getter+=fieldName.substring(1);
							
							Method method = type.getMethod(getter, noparam);
							Object value = null;
							value = method.invoke(dataArray[i]);
							if( value != null ){
								
								if( columnName == null )
									columnName = ann.columnName()+"=?";
								else
									columnName += ","+ann.columnName()+"=?";
								
								if (value instanceof Character)
									args.add(value.toString());
								else
									args.add(value);																
							}
							
//							if (method.getName().equals("getUpdatedDate")) {
//								param[0] = f.getType();
//								method = type.getDeclaredMethod("setUpdatedDate", param);
//								method.invoke(data, new Date());
//								
//								method = type.getMethod(getter, noparam);
//								args.add(method.invoke(data));
//								
//								argsType.add(sqlType);
//							} else {
//								value = method.invoke(data);
//								if( value != null ){
//									
//									if( columnName == null )
//										columnName = ann.columnName()+"=?";
//									else
//										columnName += ","+ann.columnName()+"=?";
//									
//									args.add(value);							
//									argsType.add(sqlType);
//									
//								}
//							}
						}
					}
					if( columnName != null && keysWhere.length() > 0 && sql == null ){
//						args.add(idValue);
//						argsType.add(idType);
						sql = "UPDATE "+dbSchema+"."+tableName+" SET "+columnName+" WHERE "+keysWhere;										
					}
					args.addAll(keyArgs);
					argsList.add(args.toArray());
				}
			}
			template.batchUpdate(sql, argsList);
		}
		
	}
	
	@Transactional
	public void update(Object data) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> type = data.getClass();
		Class<?>[] noparam = new Class<?>[0];
		String keysWhere="";
		if( type.isAnnotationPresent(NISPEntity.class) ){
			NISPEntity ann = (NISPEntity)type.getAnnotation(NISPEntity.class);
			String tableName = ann.tableName();
			String dbSchema = ann.dbSchema();
			String columnName = null;
			final ArrayList<Object> args = new ArrayList<Object>();
			Field[] listField = null;
			List<Field> allField = null;
			if (type.getSuperclass() != null) {
				listField = type.getSuperclass().getDeclaredFields();
				allField = new ArrayList<Field>(Arrays.asList(listField));
			}
			
			if (allField != null) {
				allField.addAll(Arrays.asList(type.getDeclaredFields()));
			} else {
				allField = Arrays.asList(type.getDeclaredFields());
			}
			Object idValue = null;
			Hashtable<String, Object>keys = new Hashtable<String, Object>();
			for (Field f : allField) {
				if(f.isAnnotationPresent(NISPEntityId.class) ){
					String fieldName = f.getName();						
					String getter = "get"+Character.toUpperCase(fieldName.charAt(0));
					if( fieldName.length() > 1 )
						getter+=fieldName.substring(1);
					Method method = type.getMethod(getter, noparam);
					idValue = method.invoke(data);
					if( idValue != null )
						keys.put(fieldName, idValue);
				}
				if( f.isAnnotationPresent(NISPEntity.class) ){
					ann = (NISPEntity)f.getAnnotation(NISPEntity.class);
					String fieldName = f.getName();
					int sqlType = getSqlType(f.getType().getSimpleName());
					idValue = keys.get(fieldName);
					if( idValue != null ){
						if( keysWhere.length() > 0 )
							keysWhere += " AND ";
						if( sqlType == Types.VARCHAR )
							keysWhere += ann.columnName()+"='"+idValue+"'";
						else
							keysWhere += ann.columnName()+"="+idValue;
						continue;
					}
					
					String getter = "get"+Character.toUpperCase(fieldName.charAt(0));
					if( fieldName.length() > 1 )
						getter+=fieldName.substring(1);
					
					Method method = type.getMethod(getter, noparam);
					Object value = null;
					value = method.invoke(data);
					if( value != null ){
						
						if( columnName == null )
							columnName = ann.columnName()+"=?";
						else
							columnName += ","+ann.columnName()+"=?";
						
						if (value instanceof Character)
							args.add(value.toString());
						else
							args.add(value);
						
					}
					
//					if (method.getName().equals("getUpdatedDate")) {
//						param[0] = f.getType();
//						method = type.getDeclaredMethod("setUpdatedDate", param);
//						method.invoke(data, new Date());
//						
//						method = type.getMethod(getter, noparam);
//						args.add(method.invoke(data));
//						
//						argsType.add(sqlType);
//					} else {
//						value = method.invoke(data);
//						if( value != null ){
//							
//							if( columnName == null )
//								columnName = ann.columnName()+"=?";
//							else
//								columnName += ","+ann.columnName()+"=?";
//							
//							args.add(value);							
//							argsType.add(sqlType);
//							
//						}
//					}
				}
			}
			if( columnName != null && keysWhere.length() > 0 ){
//				args.add(idValue);
//				argsType.add(idType);
				String sql = "UPDATE "+dbSchema+"."+tableName+" SET "+columnName+" WHERE "+keysWhere;
				template.update(sql, args.toArray());
			}								
		}
	}
	
	@Transactional
	public void insertOrUpdateBatch(Object[] dataArray) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if( dataArray.length > 0 ){
			Class<?> type = dataArray[0].getClass();
			Class<?>[] noparam = new Class<?>[0];
			List<Object> updateData = new ArrayList<Object>();
			List<Object> createData = new ArrayList<Object>();
			for(int i=0;i<dataArray.length;i++){
				if( type.isAnnotationPresent(NISPEntity.class) ){
					NISPEntity ann = (NISPEntity)type.getAnnotation(NISPEntity.class);
					String tableName = ann.tableName();
					String dbSchema = ann.dbSchema();
					Field[] listField = null;
					List<Field> allField = null;
					if (type.getSuperclass() != null) {
						listField = type.getSuperclass().getDeclaredFields();
						allField = new ArrayList<Field>(Arrays.asList(listField));
					}
					
					if (allField != null) {
						allField.addAll(Arrays.asList(type.getDeclaredFields()));
					} else {
						allField = Arrays.asList(type.getDeclaredFields());
					}
					Hashtable<String, Object> keys = new Hashtable<String, Object>();
					String keysWhere = "";
					for (Field f : allField) {
						if(f.isAnnotationPresent(NISPEntityId.class) ){
							String fieldName = f.getName();						
							String getter = "get"+Character.toUpperCase(fieldName.charAt(0));
							if( fieldName.length() > 1 )
								getter+=fieldName.substring(1);
							Method method = type.getMethod(getter, noparam);
							Object idValue = method.invoke(dataArray[i]);
							
							if (idValue != null) {
								keys.put(fieldName, idValue);
							}
							
							if( f.isAnnotationPresent(NISPEntity.class) ){
								ann = (NISPEntity)f.getAnnotation(NISPEntity.class);
								fieldName = f.getName();
								int sqlType = getSqlType(f.getType().getSimpleName());
								idValue = keys.get(fieldName);
								if( idValue != null ){
									if( keysWhere.length() > 0 )
										keysWhere+=" AND ";
									if( sqlType == Types.VARCHAR )
										keysWhere += ann.columnName()+"='"+idValue+"'";
									else
										keysWhere += ann.columnName()+"="+idValue;								
								}						
							}
						}					
					}
					if( keysWhere.length() > 0 ){
						String sql = "SELECT COUNT(*) FROM "+dbSchema+"."+tableName+" WHERE "+keysWhere;
						int count = template.queryForObject(sql, Integer.class);
						if( count == 0 )
							createData.add(dataArray[i]);
						else
							updateData.add(dataArray[i]);
					}else
						createData.add(dataArray[i]);
				}	
			}
			updateBatch(updateData.toArray());
			createBatch(createData.toArray());
			
		}
		
	}
	
	@Transactional
	public Long insertOrUpdate(Object data) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Class<?> type = data.getClass();
		Class<?>[] noparam = new Class<?>[0];
		Long id = null;
		if( type.isAnnotationPresent(NISPEntity.class) ){
			NISPEntity ann = (NISPEntity)type.getAnnotation(NISPEntity.class);
			String tableName = ann.tableName();
			String dbSchema = ann.dbSchema();
			Field[] listField = null;
			List<Field> allField = null;
			if (type.getSuperclass() != null) {
				listField = type.getSuperclass().getDeclaredFields();
				allField = new ArrayList<Field>(Arrays.asList(listField));
			}
			
			if (allField != null) {
				allField.addAll(Arrays.asList(type.getDeclaredFields()));
			} else {
				allField = Arrays.asList(type.getDeclaredFields());
			}
			Hashtable<String, Object> keys = new Hashtable<String, Object>();
			String keysWhere = "";
			for (Field f : allField) {
				if(f.isAnnotationPresent(NISPEntityId.class) ){
					String fieldName = f.getName();						
					String getter = "get"+Character.toUpperCase(fieldName.charAt(0));
					if( fieldName.length() > 1 )
						getter+=fieldName.substring(1);
					Method method = type.getMethod(getter, noparam);
					Object idValue = method.invoke(data);
					
					if (idValue != null) {
						keys.put(fieldName, idValue);
					}
					
					if( f.isAnnotationPresent(NISPEntity.class) ){
						ann = (NISPEntity)f.getAnnotation(NISPEntity.class);
						fieldName = f.getName();
						int sqlType = getSqlType(f.getType().getSimpleName());
						idValue = keys.get(fieldName);
						if( idValue != null ){
							if( keysWhere.length() > 0 )
								keysWhere+=" AND ";
							if( sqlType == Types.VARCHAR )
								keysWhere += ann.columnName()+"='"+idValue+"'";
							else
								keysWhere += ann.columnName()+"="+idValue;								
						}						
					}
				}					
			}
			if( keysWhere.length() > 0 ){
				String sql = "SELECT COUNT(*) FROM "+dbSchema+"."+tableName+" WHERE "+keysWhere;
				int count = template.queryForObject(sql, Integer.class);
				if( count == 0 )
					id = create(data);
				else
					update(data);
			}else
				id = create(data);
		}
		
		return id;
	}
	
	private int getSqlType(String simpleType){
		if( simpleType.equalsIgnoreCase("string") ){
			return Types.VARCHAR;
		}else if( simpleType.equalsIgnoreCase("long") ){
			return Types.BIGINT;
		}else if( simpleType.equalsIgnoreCase("integer") || simpleType.equalsIgnoreCase("int")){
			return Types.INTEGER;
		}else if( simpleType.equalsIgnoreCase("date")){
			return Types.DATE;
		}else if( simpleType.equalsIgnoreCase("float")){
			return Types.FLOAT;
		}else if( simpleType.equalsIgnoreCase("double")){
			return Types.DOUBLE;
		}else if( simpleType.toLowerCase().startsWith("char")){
			return Types.VARCHAR;
		}
		return -1;
	}
	
	protected <K> K serialize(Class<K> clazz, Map<String, Object> row) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		Class<?>[] param = new Class<?>[1];
		K res = null;
		res = clazz.newInstance();
		Field[] listField = null;
		List<Field> allField = null;
		if (clazz.getSuperclass() != null) {
			listField = clazz.getSuperclass().getDeclaredFields();
			allField = new ArrayList<Field>(Arrays.asList(listField));
		}
		
		if (allField != null) {
			allField.addAll(Arrays.asList(clazz.getDeclaredFields()));
		} else {
			allField = Arrays.asList(clazz.getDeclaredFields());
		}
		for (Field f : allField) {
			if( f.isAnnotationPresent(NISPEntitySerialize.class) || f.isAnnotationPresent(NISPEntity.class) ){
				String field;
				if( f.isAnnotationPresent(NISPEntitySerialize.class) ){
					NISPEntitySerialize ann = (NISPEntitySerialize) f.getAnnotation(NISPEntitySerialize.class);
					field = ann.field();
				}else{
					NISPEntity ann = (NISPEntity) f.getAnnotation(NISPEntity.class);
					field = ann.columnName();
				}
				String fieldName = f.getName();				
				String setter = "set"+Character.toUpperCase(fieldName.charAt(0));
				if( fieldName.length() > 1 )
					setter+=fieldName.substring(1);									
				
				param[0] = f.getType();					
				Method method = clazz.getMethod(setter, param);
				Object value = row.get(field);
				if( value != null ){
					if( f.getType().getSimpleName().toLowerCase().startsWith("char") ){
						method.invoke(res, ((String) value).charAt(0));
					} else if( f.getType().getSimpleName().equalsIgnoreCase("double") ){
						double fValue = 0d;
						if( value instanceof BigDecimal ){
							fValue = ((BigDecimal) value).doubleValue();
						}else
							fValue = (double)value;
						method.invoke(res, fValue);
					}else if( f.getType().getSimpleName().equalsIgnoreCase("long") ){
						long fValue = 0L;
						if( value instanceof BigDecimal ){
							fValue = ((BigDecimal) value).longValue();
						}else
							fValue = (long)value;
						method.invoke(res, fValue);
					}else if( f.getType().getSimpleName().toLowerCase().startsWith("int") ){
						int fValue = 0;
						if( value instanceof BigDecimal ){
							fValue = ((BigDecimal) value).intValue();
						}else
							fValue = (int)value;
						method.invoke(res, fValue);
					}
					else
						method.invoke(res, value);
				}					
			}
		}
		return res;
	}
	
}
