package com.optima.nisp.utility;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.optima.nisp.constanta.ParameterCons;
import com.optima.nisp.constanta.ReturnCode;
import com.optima.nisp.model.ApplicationParameter;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.ApplicationParameterService;

public class CommonUtils {

	public static boolean isPositiveInteger(String s){
		return isPositiveInteger(s, 10);
	}
	
	public static boolean isPositiveInteger(String s, int radix){
		if(s==null || s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	public static boolean isNumber(String s){
		if(s==null || s.isEmpty()) return false;
		boolean dotFound = false;
		if( s.length() == 1 && (s.charAt(0) == '-' || s.charAt(0) == '.'))
			return false;
		for(int i=0; i< s.length(); i++ ){
			char c = s.charAt(i);
			if( i == 0 && c == '.' )
				return false;
			
			if( c == '-' ){
				if( i != 0 )
					return false;		
			}else if( c == '.' ){
				if( dotFound )
					return false;
				dotFound = true;
			}else if(Character.digit(s.charAt(i),10) < 0)
				return false;
		}
		return true;
	}
	
	public static boolean isNumber(String s, int precious, int scale){
		if(s==null || s.isEmpty()) return false;
		boolean dotFound = false;
		if( s.length() == 1 && (s.charAt(0) == '-' || s.charAt(0) == '.'))
			return false;
		for(int i=0; i< s.length(); i++ ){
			char c = s.charAt(i);
			if( i == 0 && c == '.' )
				return false;
			
			if( c == '-' ){
				if( i != 0 )
					return false;		
			}else if( c == '.' ){
				if( dotFound )
					return false;
				dotFound = true;
				if( i > precious - scale )
					return false;
				
				if( s.substring(i).length() > scale+1 )
					return false;
			}else if(Character.digit(s.charAt(i),10) < 0)
				return false;
		}
		return true;
	}
	
	public static boolean validateInput(BindingResult bindingResult, Hashtable<String, String>messages){
		if( bindingResult.hasErrors() ){
			for (FieldError fe : bindingResult.getFieldErrors()) {
				String field = fe.getField();
				messages.put(field, fe.getDefaultMessage());
			}
			return false;
		}
		return true;
	}
	
	public static boolean checkPositiveInteger(String fieldName, String s, Hashtable<String, String> messages){
		if( !isPositiveInteger(s) ){
			messages.put(fieldName, "must be a positive integer");
			return false;
		}
		return true;
	}

	public static int getStubTimeout(Properties properties) {
		
		String strStubTimeout = properties.getProperty("stub_timeout");
		if( strStubTimeout == null )
			return 300000;
		return Integer.parseInt(strStubTimeout);		
	}
	
	public static String getStubUrl(Properties properties){
		return properties.getProperty("stub_url");
	}
	
	/*
	 * Get thousand separator
	 * Using default jvm locale
	 * 
	 * @return default thousand separator character
	 * 
	 */
	public static char getThousandSeparator(){
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		return symbols.getGroupingSeparator();
	}
	
	/*
	 * Return thousand separtor
	 * 
	 * @param locale desired locale
	 * 
	 * @return thousand separator in locale <tt>locale</tt>
	 * 
	 */
	
	public static char getThousandSeparator(Locale locale){
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
		return symbols.getGroupingSeparator();
	}
	
	public static String getElixirHostname(Properties prop){
		String strHostname = prop.getProperty("elixir_hostname");
		
		return strHostname;
	}
	
	public static int getElixirPort(Properties prop){
		int port =  Integer.parseInt(prop.get("elixir_port").toString());
		
		return port;
	}
	
	public static String getElixirUsername(Properties prop){
		String username = prop.getProperty("elixir_username");
		
		return username;
	}
	
	public static String getElixirPassword(Properties prop){
		String password = prop.getProperty("elixir_password");
		
		return password;
	}
	
	public static String getElixirWorkspace(Properties prop){
		String workspace = prop.getProperty("elixir_workspace");
		
		return workspace;
	}
	
	public static String getElixirResources(Properties prop){
		String resources = prop.getProperty("elixir_resources");
		
		return resources;
	}
	
	public static String getPeriodeBefore() {
		String result = "";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		month++;
		String strMonth = "";
		if (month < 10) {
			strMonth = "0" + month;
		} else {
			strMonth = "" + month;
		}
		
		result = year + strMonth;
		
		return result;
	}
	
	public static String getLongPeriode(String periode){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		try {
			Date date = sdf.parse(periode);
			SimpleDateFormat newSdf = new SimpleDateFormat("MMMM yyyy");
			return newSdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getStrDate(Date date, String pattern, Locale local) {
		String strDate = "";
		SimpleDateFormat formatter = null;
		if (local == null) {
			formatter = new SimpleDateFormat(pattern);
		} else {
			formatter = new SimpleDateFormat(pattern, local);
		}
 
		strDate = formatter.format(date);
		return strDate;
    }
	
	public static Response responseException(Exception e){
		Response res = new Response();
		res.setResultCode(ReturnCode.INTERNAL_SERVER_ERROR);
		res.setData(ExceptionUtils.getStackTrace(e));		
		return res;
	}
	
	public static String getDefaultRowCount( ApplicationParameterService applicationParameterService) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ApplicationParameter appParam = applicationParameterService.getByKey(ParameterCons.DEFAULT_ROW_DATA_PER_PAGE);
		String[] arrDefCount = appParam.getValue().split(",");
		Arrays.sort(arrDefCount, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return Integer.parseInt(o1)-Integer.parseInt(o2);
			}
		});
		String strDefCount = "";
		for (String count : arrDefCount) {
			if( strDefCount.length() > 0 )
				strDefCount+=",";
			strDefCount += count;
		}		
		return strDefCount;
	}
	
	public static String getDefaultRowCountOptions( ApplicationParameterService applicationParameterService) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ApplicationParameter appParam = applicationParameterService.getByKey(ParameterCons.DEFAULT_ROW_DATA_PER_PAGE);
		String[] arrDefCount = appParam.getValue().split(",");
		Arrays.sort(arrDefCount, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return Integer.parseInt(o1)-Integer.parseInt(o2);
			}
		});
		String strDefCount = "";
		for (String count : arrDefCount) {
			strDefCount += "<option value='"+count+"'>"+count+"</option>";
		}		
		return strDefCount;
	}
}
