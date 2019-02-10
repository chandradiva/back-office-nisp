package com.optima.nisp.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.dao.AuditTrailBOViewDao;
import com.optima.nisp.dao.AuditTrailWSViewDao;
import com.optima.nisp.model.AuditTrailBOView;
import com.optima.nisp.model.AuditTrailWSView;
import com.optima.nisp.response.AuditTrailResponse;
import com.optima.nisp.response.AuditTrailWSResponse;
import com.optima.nisp.utility.FileProcessing;

@Component
@Transactional
public class AuditTrailService {
	
	@Autowired
	private AuditTrailBOViewDao auditTrailViewDao;
	
	@Autowired
	private AuditTrailWSViewDao auditTrailWSViewDao;
	
	public Integer getTotalRow(String username, String groups, String atDateFrom, String atDateTo, String action, String information){
		return auditTrailViewDao.getTotalRow(username, groups, atDateFrom, atDateTo, action, information);
	}
	
	public Integer getWSTotalRow(String username, String atDateFrom, String atDateTo, String urlPath, String reqParam, String information, String cifKey, String activity){
		return auditTrailWSViewDao.getTotalRow(username, atDateFrom, atDateTo, urlPath, reqParam, information, cifKey, activity);
	}
	
	public List<AuditTrailWSResponse> getWebStatementViews(int start, int length, String username, String atDateFrom, String atDateTo, String urlPath, String reqParam, String information, String cifKey, String activity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		List<AuditTrailWSView> views = auditTrailWSViewDao.getList(start, length, username, atDateFrom, atDateTo, urlPath, reqParam, information, cifKey, activity);
		List<AuditTrailWSResponse> auditTrailsResponse = new ArrayList<AuditTrailWSResponse>();
		
		int number = start+1;
		for(AuditTrailWSView view : views){
			AuditTrailWSResponse atResp = new AuditTrailWSResponse();
			atResp.setUsername(view.getUsername());
			atResp.setNumber(number);
			atResp.setTime(view.getTime());
			atResp.setUrlPath(view.getUrlPath());
			atResp.setBrowser(view.getBrowser());
			atResp.setIpAddress(view.getIpAddress());
			atResp.setInformation(view.getInformation());
			atResp.setActivity(view.getActivity());
			atResp.setCifKey(view.getCifKey());
			
			if(view.getReqParam()!=null)
				atResp.setReqParam(view.getReqParam());
			else
				atResp.setReqParam("-");
			
			if(view.getUrlPath()!=null && view.getUrlPath().equalsIgnoreCase(CommonCons.LOGIN_URL))
				atResp.setReqParam("-");
			
			auditTrailsResponse.add(atResp);
			number++;
		}
		
		return auditTrailsResponse;
	}
	
	public List<AuditTrailResponse> getBackOfficeViews(int start, int length, String username, String groups, String atDateFrom, String atDateTo, String action, String information) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		List<AuditTrailBOView> views = auditTrailViewDao.getList(start, length, username, groups, atDateFrom, atDateTo, action, information);
		List<AuditTrailResponse> auditTrailsResponse = new ArrayList<AuditTrailResponse>();
		
		int number = 1;
		for(AuditTrailBOView view : views){
			AuditTrailResponse atResp = new AuditTrailResponse();
			atResp.setNumber(start+number);
			atResp.setUsername(view.getUsername());
			atResp.setGroups(view.getGroups());
			atResp.setTime(view.getTime());
			atResp.setInfo(view.getInformation());
			
			if(view.getMenuActivity()!=null || view.getButtonActivity()!=null){				
				if(view.getMenuActivity()!=null)
					atResp.setAction(view.getMenuActivity());
				else
					atResp.setAction(view.getButtonActivity());				
			}
			else {	//Button and Menu title is null
				if(view.getUrlPath().equals(CommonCons.LOGIN_URL)){
					atResp.setAction("Login");
				} else if (view.getUrlPath().equals(CommonCons.LOGOUT_URL)){
					atResp.setAction("Logout");
				} else if (view.getUrlPath().equals(CommonCons.RIWAYAT_TRANSAKSI_TAMPIL_URL)){
					atResp.setAction("Tampil Riwayat Transaksi");
				} else if (view.getUrlPath().equals(CommonCons.RIWAYAT_TRANSAKSI_DOWNLOAD_URL)){
					atResp.setAction("Download Riwayat Transaksi");
				}
			}
			
			auditTrailsResponse.add(atResp);
			
			number++;
		}
		
		return auditTrailsResponse;
	}
	
	private List<Map<String, String>> getMaps(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.readValue(json, new TypeReference<List<Map<String, String>>>(){});		
	}
	
	private Map<String, String> getMap(String json) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.readValue(json, new TypeReference<Map<String, String>>(){});
	}
	
	private Map<String, String> getMapFromReqParam(String strParam){
		Map<String, String> map = new HashMap<String,String>();
		
		String[] pairs = strParam.split(",\\|");
		
		for(String pair : pairs){
			if(pair.contains("|") && pair.contains("=")){
				String key = pair.substring(pair.indexOf('|')+1, pair.indexOf('='));
				String value = pair.substring(pair.indexOf('=')+1);
				
				map.put(key, value);
			}
		}		
		
		return map;
	}
	
	public String getInformation(String reqParam){
		String info = "";
		
		if(reqParam!=null && !reqParam.isEmpty()){
			String newLine = "<br />";
			
			try { //Jika JSON Array 
				List<Map<String, String>> maps = getMaps(reqParam);
				
				if(maps!=null){
					for(Map<String, String> map : maps){
						if(map.containsKey("key")){		//(Perubahan data Sys Param atau App Param)
							String key = (map.get("key") != null) ? map.get("key") : "";
							String oldVal = (map.get("oldValue") != null) ? map.get("oldValue") : "";
							String newVal = (map.get("newValue") != null) ? map.get("newValue") : "";
							
							info += "<strong>Key : " + key + "</strong> -- Old Value : " + StringEscapeUtils.escapeHtml(oldVal) 
									+ " -- New Value : " + StringEscapeUtils.escapeHtml(newVal) + "<br />";	
						}
						else {
							info = reqParam;
						}
					}
				}						
				else {	//maps null-- kemungkinan bukan json
					Map<String, String> map = getMapFromReqParam(reqParam);
					
					if(map!=null && map.containsKey("header")){
						String header = (map.get("header")!=null) ? map.get("header"):"";
						String oldHeader = (map.get("oldHeader")!=null) ? map.get("oldHeader"):"";
						String pembuka1 = (map.get("pembuka1")!=null) ? map.get("pembuka1"):"";
						String oldPembuka1 = (map.get("oldPembuka1")!=null) ? map.get("oldPembuka1"):"";
						String pembuka2 = (map.get("pembuka2")!=null) ? map.get("pembuka2"):"";
						String oldPembuka2 = (map.get("oldPembuka2")!=null) ? map.get("oldPembuka2"):"";
						String body = (map.get("body")!=null) ? map.get("body"):"";
						String oldBody = (map.get("oldBody")!=null) ? map.get("oldBody"):"";
						String penutup = (map.get("penutup")!=null) ? map.get("penutup"):"";
						String oldPenutup = (map.get("oldPenutup")!=null) ? map.get("oldPenutup"):"";
						String footer = (map.get("footer")!=null) ? map.get("footer"):"";
						String oldFooter = (map.get("oldFooter")!=null) ? map.get("oldFooter"):"";
						String attachment1 = FileProcessing.getFileName((map.get("newAttachment1")!=null) ? map.get("newAttachment1"):"");
						String oldAttachment1 = FileProcessing.getFileName((map.get("oldAttachment1")!=null) ? map.get("oldAttachment1"):"");
						String attachment2 = FileProcessing.getFileName((map.get("newAttachment2")!=null) ? map.get("newAttachment2"):"");
						String oldAttachment2 = FileProcessing.getFileName((map.get("oldAttachment2")!=null) ? map.get("oldAttachment2"):"");
						String attachment3 = FileProcessing.getFileName((map.get("newAttachment3")!=null) ? map.get("newAttachment3"):"");
						String oldAttachment3 = FileProcessing.getFileName((map.get("oldAttachment3")!=null) ? map.get("oldAttachment3"):"");
						String attachment4 = FileProcessing.getFileName((map.get("newAttachment4")!=null) ? map.get("newAttachment4"):"");
						String oldAttachment4 = FileProcessing.getFileName((map.get("oldAttachment4")!=null) ? map.get("oldAttachment4"):"");
						String attachment5 = FileProcessing.getFileName((map.get("newAttachment5")!=null) ? map.get("newAttachment5"):"");
						String oldAttachment5 = FileProcessing.getFileName((map.get("oldAttachment5")!=null) ? map.get("oldAttachment5"):"");
						String attachment6 = FileProcessing.getFileName((map.get("newAttachment6")!=null) ? map.get("newAttachment6"):"");
						String oldAttachment6 = FileProcessing.getFileName((map.get("oldAttachment6")!=null) ? map.get("oldAttachment6"):"");
						String attachment7 = FileProcessing.getFileName((map.get("newAttachment7")!=null) ? map.get("newAttachment7"):"");
						String oldAttachment7 = FileProcessing.getFileName((map.get("oldAttachment7")!=null) ? map.get("oldAttachment7"):"");
						
						if(!header.equals(oldHeader)){
							oldHeader = oldHeader.replace("<", "&lt;");
							oldHeader = oldHeader.replace(">", "&gt");
							header = header.replace("<", "&lt;");
							header = header.replace(">", "&gt");
							info += "<strong>Old Header</strong> : " + oldHeader + newLine
									+"<strong>New Header</strong> : " + header + newLine + newLine;
						}
						if(!pembuka1.equals(oldPembuka1)){
							oldPembuka1 = oldPembuka1.replace("<", "&lt;");
							oldPembuka1 = oldPembuka1.replace(">", "&gt");
							pembuka1 = pembuka1.replace("<", "&lt;");
							pembuka1 = pembuka1.replace(">", "&gt");
							info += "<strong>Old Pembuka dengan Password</strong> : " + oldPembuka1 + newLine
									+"<strong>New Pembuka dengan Password</strong> : " + pembuka1 + newLine + newLine;
						}
						if(!pembuka2.equals(oldPembuka2)){
							oldPembuka2 = oldPembuka2.replace("<", "&lt;");
							oldPembuka2 = oldPembuka2.replace(">", "&gt");
							pembuka2 = pembuka2.replace("<", "&lt;");
							pembuka2 = pembuka2.replace(">", "&gt");
							info += "<strong>Old Pembuka tanpa Password</strong> : " + oldPembuka2 + newLine
									+"<strong>New Pembuka tanpa Password</strong> : " + pembuka2 + newLine + newLine;
						}
						if(!body.equals(oldBody)){
							oldBody = oldBody.replace("<", "&lt;");
							oldBody = oldBody.replace(">", "&gt");
							body = body.replace("<", "&lt;");
							body = body.replace(">", "&gt");
							info += "<strong>Old Body</strong> : " + oldBody + newLine
									+"<strong>New Body</strong> : " + body + newLine + newLine;
						}	
						if(!penutup.equals(oldPenutup)){
							oldPenutup = oldPenutup.replace("<", "&lt;");
							oldPenutup = oldPenutup.replace(">", "&gt");
							penutup = penutup.replace("<", "&lt;");
							penutup = penutup.replace(">", "&gt");
							info += "<strong>Old Penutup</strong> : " + oldPenutup + newLine
									+"<strong>New Penutup</strong> : " + penutup + newLine + newLine;
						}
						if(!footer.equals(oldFooter)){
							oldFooter = oldFooter.replace("<", "&lt;");
							oldFooter = oldFooter.replace(">", "&gt");
							footer = footer.replace("<", "&lt;");
							footer = footer.replace(">", "&gt");
							info += "<strong>Old Footer</strong> : " + oldFooter + newLine
									+"<strong>New Footer</strong> : " + footer + newLine + newLine;
						}
						if(!attachment1.equals(oldAttachment1)){
							info += "<strong>Old Attachment MultiCurrency</strong> : " + oldAttachment1 + newLine
									+"<strong>New Attachment MultiCurrency</strong> : " + attachment1 + newLine + newLine;
						}
						if(!attachment2.equals(oldAttachment2)){
							info += "<strong>Old Attachment Giro Non KRK</strong> : " + oldAttachment2 + newLine
									+"<strong>New Attachment Giro Non KRK</strong> : " + attachment2 + newLine + newLine;
						}
						if(!attachment3.equals(oldAttachment3)){
							info += "<strong>Old Attachment Giro KRK Tanpa Tunggakan</strong> : " + oldAttachment3 + newLine
									+"<strong>New Attachment Giro KRK Tanpa Tunggakan</strong> : " + attachment3 + newLine + newLine;
						}
						if(!attachment4.equals(oldAttachment4)){
							info += "<strong>Old Attachment Giro KRK Dengan Tunggakan</strong> : " + oldAttachment4 + newLine
									+"<strong>New Attachment Giro KRK Dengan Tunggakan</strong> : " + attachment4 + newLine + newLine;
						}
						if(!attachment5.equals(oldAttachment5)){
							info += "<strong>Old Attachment Tabungan Harian</strong> : " + oldAttachment5 + newLine
									+"<strong>New Attachment Tabungan Harian</strong> : " + attachment5 + newLine + newLine;
						}
						if(!attachment6.equals(oldAttachment6)){
							info += "<strong>Old Attachment Multi-currency Billingual</strong> : " + oldAttachment6 + newLine
									+"<strong>New Attachment Multi-currency Billingual</strong> : " + attachment6 + newLine + newLine;
						}
						if(!attachment7.equals(oldAttachment7)){
							info += "<strong>Old Attachment Credit Card</strong> : " + oldAttachment7 + newLine
									+"<strong>New Attachment Credit Card</strong> : " + attachment7 + newLine + newLine;
						}
					}
				}					
			} catch (JsonParseException e) {
				e.printStackTrace();
				info = reqParam;
			} catch (JsonMappingException e) { //ReqParam JSON Object
				try {
					Map<String, String> map = getMap(reqParam);
					
					if(map!=null && map.containsKey("header")){ //Ada key 'header' --> Perubahan Email Content
						String header = (map.get("header")!=null) ? map.get("header"):"";
						String oldHeader = (map.get("oldHeader")!=null) ? map.get("oldHeader"):"";
						String pembuka1 = (map.get("pembuka1")!=null) ? map.get("pembuka1"):"";
						String oldPembuka1 = (map.get("oldPembuka1")!=null) ? map.get("oldPembuka1"):"";
						String pembuka2 = (map.get("pembuka2")!=null) ? map.get("pembuka2"):"";
						String oldPembuka2 = (map.get("oldPembuka2")!=null) ? map.get("oldPembuka2"):"";
						String body = (map.get("body")!=null) ? map.get("body"):"";
						String oldBody = (map.get("oldBody")!=null) ? map.get("oldBody"):"";
						String penutup = (map.get("penutup")!=null) ? map.get("penutup"):"";
						String oldPenutup = (map.get("oldPenutup")!=null) ? map.get("oldPenutup"):"";
						String footer = (map.get("footer")!=null) ? map.get("footer"):"";
						String oldFooter = (map.get("oldFooter")!=null) ? map.get("oldFooter"):"";
						String attachment1 = (map.get("attachment1")!=null) ? map.get("attachment1"):"";
						String oldAttachment1 = (map.get("oldAttachment1")!=null) ? map.get("oldAttachment1"):"";
						String attachment2 = (map.get("attachment2")!=null) ? map.get("attachment2"):"";
						String oldAttachment2 = (map.get("oldAttachment2")!=null) ? map.get("oldAttachment2"):"";
						String attachment3 = (map.get("attachment3")!=null) ? map.get("attachment3"):"";
						String oldAttachment3 = (map.get("oldAttachment3")!=null) ? map.get("oldAttachment3"):"";
						String attachment4 = (map.get("attachment4")!=null) ? map.get("attachment4"):"";
						String oldAttachment4 = (map.get("oldAttachment4")!=null) ? map.get("oldAttachment4"):"";
						String attachment5 = (map.get("attachment5")!=null) ? map.get("attachment5"):"";
						String oldAttachment5 = (map.get("oldAttachment5")!=null) ? map.get("oldAttachment5"):"";
						String attachment6 = (map.get("attachment6")!=null) ? map.get("attachment6"):"";
						String oldAttachment6 = (map.get("oldAttachment6")!=null) ? map.get("oldAttachment6"):"";
						String attachment7 = (map.get("attachment7")!=null) ? map.get("attachment7"):"";
						String oldAttachment7 = (map.get("oldAttachment7")!=null) ? map.get("oldAttachment7"):"";
						
						if(!header.equals(oldHeader)){
							oldHeader = oldHeader.replace("<", "&lt;");
							oldHeader = oldHeader.replace(">", "&gt");
							header = header.replace("<", "&lt;");
							header = header.replace(">", "&gt");
							info += "<strong>Old Header</strong> : " + oldHeader + newLine
									+"<strong>New Header</strong> : " + header + newLine + newLine;
						}
						if(!pembuka1.equals(oldPembuka1)){
							oldPembuka1 = oldPembuka1.replace("<", "&lt;");
							oldPembuka1 = oldPembuka1.replace(">", "&gt");
							pembuka1 = pembuka1.replace("<", "&lt;");
							pembuka1 = pembuka1.replace(">", "&gt");
							info += "<strong>Old Pembuka dengan Password</strong> : " + oldPembuka1 + newLine
									+"<strong>New Pembuka dengan Password</strong> : " + pembuka1 + newLine + newLine;
						}
						if(!pembuka2.equals(oldPembuka2)){
							oldPembuka2 = oldPembuka2.replace("<", "&lt;");
							oldPembuka2 = oldPembuka2.replace(">", "&gt");
							pembuka2 = pembuka2.replace("<", "&lt;");
							pembuka2 = pembuka2.replace(">", "&gt");
							info += "<strong>Old Pembuka tanpa Password</strong> : " + oldPembuka2 + newLine
									+"<strong>New Pembuka tanpa Password</strong> : " + pembuka2 + newLine + newLine;
						}
						if(!body.equals(oldBody)){
							oldBody = oldBody.replace("<", "&lt;");
							oldBody = oldBody.replace(">", "&gt");
							body = body.replace("<", "&lt;");
							body = body.replace(">", "&gt");
							info += "<strong>Old Body</strong> : " + oldBody + newLine
									+"<strong>New Body</strong> : " + body + newLine + newLine;
						}	
						if(!penutup.equals(oldPenutup)){
							oldPenutup = oldPenutup.replace("<", "&lt;");
							oldPenutup = oldPenutup.replace(">", "&gt");
							penutup = penutup.replace("<", "&lt;");
							penutup = penutup.replace(">", "&gt");
							info += "<strong>Old Penutup</strong> : " + oldPenutup + newLine
									+"<strong>New Penutup</strong> : " + penutup + newLine + newLine;
						}
						if(!footer.equals(oldFooter)){
							oldFooter = oldFooter.replace("<", "&lt;");
							oldFooter = oldFooter.replace(">", "&gt");
							footer = footer.replace("<", "&lt;");
							footer = footer.replace(">", "&gt");
							info += "<strong>Old Footer</strong> : " + oldFooter + newLine
									+"<strong>New Footer</strong> : " + footer + newLine + newLine;
						}
						if(!attachment1.equals(oldAttachment1)){
							info += "<strong>Old Attachment MultiCurrency</strong> : " + oldAttachment1 + newLine
									+"<strong>New Attachment MultiCurrency</strong> : " + attachment1 + newLine + newLine;
						}
						if(!attachment2.equals(oldAttachment2)){
							info += "<strong>Old Attachment Giro Non KRK</strong> : " + oldAttachment2 + newLine
									+"<strong>New Attachment Giro Non KRK</strong> : " + attachment2 + newLine + newLine;
						}
						if(!attachment3.equals(oldAttachment3)){
							info += "<strong>Old Attachment Giro KRK Tanpa Tunggakan</strong> : " + oldAttachment3 + newLine
									+"<strong>New Attachment Giro KRK Tanpa Tunggakan</strong> : " + attachment3 + newLine + newLine;
						}
						if(!attachment4.equals(oldAttachment4)){
							info += "<strong>Old Attachment Giro KRK Dengan Tunggakan</strong> : " + oldAttachment4 + newLine
									+"<strong>New Attachment Giro KRK Dengan Tunggakan</strong> : " + attachment4 + newLine + newLine;
						}
						if(!attachment5.equals(oldAttachment5)){
							info += "<strong>Old Attachment Tabungan Harian</strong> : " + oldAttachment5 + newLine
									+"<strong>New Attachment Tabungan Harian</strong> : " + attachment5 + newLine + newLine;
						}
						if(!attachment6.equals(oldAttachment6)){
							info += "<strong>Old Attachment Multi-currency Billingual</strong> : " + oldAttachment6 + newLine
									+"<strong>New Attachment Multi-currency Billingual</strong> : " + attachment6 + newLine + newLine;
						}
						if(!attachment7.equals(oldAttachment7)){
							info += "<strong>Old Attachment Credit Card</strong> : " + oldAttachment7 + newLine
									+"<strong>New Attachment Credit Card</strong> : " + attachment7 + newLine + newLine;
						}
					} else {
						info = reqParam;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					info = reqParam;
				}						
			} catch (IOException e) {
				e.printStackTrace();
				info = reqParam;
			}
			
			if(info.isEmpty())
				info = "-";
		}
			
		else {	//Request Parameter is null or empty
			info = "-";
		}
		
		return info;
	}
}
