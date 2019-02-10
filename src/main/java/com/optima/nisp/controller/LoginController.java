package com.optima.nisp.controller;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.tempuri.Authenticate3;
import org.tempuri.Authenticate3Response;
import org.tempuri.GetRSAPublicKey;
import org.tempuri.GetRSAPublicKeyResponse;
import org.tempuri.ServiceStub;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.ReturnCode;
import com.optima.nisp.constanta.UserSession;
import com.optima.nisp.model.Group;
import com.optima.nisp.model.SystemParameter;
import com.optima.nisp.response.Response;
import com.optima.nisp.service.BoLastLoginService;
import com.optima.nisp.service.MenuService;
import com.optima.nisp.service.SystemParameterService;
import com.optima.nisp.utility.CommonUtils;
import com.optima.nisp.utility.RsaUtil;

@Controller
@SessionAttributes("userSession")
public class LoginController {
	private static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Autowired
	RsaUtil rsaUtil;
	
	@Autowired
	MenuService menuService;
	
	@Resource(name="confProp")
	private Properties properties;
	
	@Autowired
	SystemParameterService sysParamService;
	
	@Autowired
	BoLastLoginService boLastLoginService;
	
	@RequestMapping(value = {"/", "/login"}, method=RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView mv = new ModelAndView("login");
		try{
			String domain = getDomain();
			mv.addObject("domain", domain);
		}catch( Exception e){
			logger.error("Error", e);
			mv.setViewName("redirect-page");
			mv.addObject("url","login");
		}
		
		return mv;
	}
		
	@RequestMapping(value = "/session/login", method= RequestMethod.POST)
	public @ResponseBody Response loginProcess(Model model, @RequestParam String username, @RequestParam String password){
		try{
			Response res = new Response();
//			res.setResultCode(-1);
//			String stubUrl = CommonUtils.getStubUrl(properties);
////			ServiceStub stub = new ServiceStub(strContextPath+"services/Service");
//			ServiceStub stub = new ServiceStub(stubUrl);
//			GetRSAPublicKey publicKey = new GetRSAPublicKey();			
//			GetRSAPublicKeyResponse pkResponse = stub.getRSAPublicKey(publicKey);
//			Authenticate3 auth = new Authenticate3();
//			auth.setAdDomain(getDomain());
//			auth.setAdUserName(username);
//			
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			InputSource is = new InputSource(new StringReader(pkResponse.getGetRSAPublicKeyResult()));
//			
//			Document doc = builder.parse(is);
//			doc.getDocumentElement().normalize();
//			
//			String modulus = doc.getElementsByTagName("Modulus").item(0).getTextContent();
//			String exponent = doc.getElementsByTagName("Exponent").item(0).getTextContent();
//			auth.setAdRSAEncyrptedPassword(rsaUtil.rsaEncrypt(password, modulus, exponent));
//			Authenticate3Response authResponse = stub.authenticate3(auth);
//			String resAuth = authResponse.getAuthenticate3Result();
//			logger.debug("Authenticate3 Result: "+resAuth);
			UserSession userSession = new UserSession();
//			Long[] groupIds = getGroupIds(resAuth, userSession);
			Long[] groupIds = {41L,61L};
			if( groupIds.length > 0 ){
				SystemParameter sysParam = sysParamService.getByKey("BOITP");
				int defaultIdleTime = CommonCons.DEFAULT_BO_IDLE_TIME;
				if(sysParam==null){
					userSession.setIdleTime(defaultIdleTime);
				} else {
					try {
						userSession.setIdleTime(Integer.parseInt(sysParam.getValue()));
					} catch(Exception e){
						userSession.setIdleTime(defaultIdleTime);
					}
				}
				
				userSession.setGroupIds(groupIds);
				userSession.setUsername(username.toUpperCase());
				SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy - HH:mm:ss");
				userSession.setLastLogin(sdf.format(boLastLoginService.getLastLogin(username.toUpperCase())));				
				model.addAttribute("userSession", userSession);
				res.setResultCode(ReturnCode.SUCCESS);
				boLastLoginService.save(username.toUpperCase());
			}else{
				res.setResultCode(ReturnCode.UNAUTHORIZED);
			}	
			return res;
		}catch( Exception e){
			logger.error("Error", e);
			return CommonUtils.responseException(e);
		}
		
	}
	
	private Long[] getGroupIds(String strAdAuth, UserSession userSession) throws ParserConfigurationException, SAXException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(strAdAuth));
		
		Document doc = builder.parse(is);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("memberOfCollections");
		ArrayList<Long>res = new ArrayList<Long>();
		String groupName = "";
		String fGroupName = "";
		for( int i=0; i<nodeList.getLength();i++){
			Element element = (Element)nodeList.item(i);
			NodeList members = element.getElementsByTagName("memberOf");
			for( int j=0;j<members.getLength();j++){
				boolean isAdd = false;
				String member = members.item(j).getTextContent();
				List<Group> groups = menuService.getGroupsByFGroup(member);
				for (Group group : groups) {
					Long groupId = group.getGroupId();
					if( !res.contains(groupId) ){
						res.add(groupId);
						if( groupName.length() > 0 )
							groupName+=",";
						groupName+=group.getGroupName();
						isAdd = true;						
					}
				}
				if( isAdd){
					if( fGroupName.length() > 0 )
						fGroupName+=",";
					fGroupName+=member;
				}
			}
			userSession.setfGroupName(fGroupName);
			userSession.setGroupName(groupName);
		}
		Long[] ids = new Long[res.size()];
		return res.toArray(ids);
	}
	
	@RequestMapping(value = "/session/logout", method = RequestMethod.GET)
	public String logout(SessionStatus status){
		status.setComplete();
		return "redirect:/login";
	}
	
	private String getDomain() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		SystemParameter domainSysParam = sysParamService.getByKey("BOADD");
		String domain;
		if( domainSysParam == null ){
			domain = "DEVTEAM.NET";
		}else{
			domain = domainSysParam.getValue();
		}
		return domain;
	}
}
