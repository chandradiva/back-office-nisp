package com.optima.nisp.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.optima.nisp.constanta.ParameterCons;
import com.optima.nisp.model.ApplicationParameter;
import com.optima.nisp.service.ApplicationParameterService;
import com.optima.nisp.utility.CommonUtils;
import com.optima.nisp.utility.HttpRequestUtil;

@Controller
@RequestMapping(value="api")
@SessionAttributes("userSession")
public class ApiController {

	private static final Logger logger = Logger.getLogger(ApiController.class);
	@Autowired
	private ApplicationParameterService applicationParameterService;
	
	@Resource(name="confProp")
	private Properties properties;
	
	@RequestMapping(value = "downloadfile/{filename:.+}", method = RequestMethod.GET)
	public @ResponseBody void downloadFile(Model model, @PathVariable String filename,
			@RequestParam(defaultValue="") String dlName,
		@RequestParam String contentType, @RequestParam(defaultValue = "0") int isPrint, HttpServletResponse servletResponse) {
		try {
			if( model.containsAttribute("userSession") ){
				ApplicationParameter appParam = applicationParameterService.getByKey(ParameterCons.AJAX_REQUEST_TIMEOUT);
				
				String strContextPath = (String) properties.get("api_protocol") + "://"
						+ (String) properties.get("api_server");
				String strPort = (String) properties.get("api_port");
				if(!strPort.equals("80")){
					strContextPath += ":"+strPort;
				}
				strContextPath += (String) properties.get("api_context_path");
				
				InputStream res = HttpRequestUtil.requestStream(strContextPath+"api/downloadfile/"+filename, "GET", null, Integer.parseInt(appParam.getValue()));
				
				ServletOutputStream outServlet = servletResponse.getOutputStream();
				
				byte [] buffer = new byte[10240];
				int bytesRead = 0;
				while((bytesRead = res.read(buffer)) != -1) {
				    outServlet.write(buffer, 0, bytesRead);
				}
				
				if( !filename.endsWith(".pdf") )
					filename += ".pdf";
				servletResponse.setContentType(contentType);
				
				String downloadName = dlName;
				if( downloadName.length() == 0 )
					downloadName = filename;
				String cd = "attachment";
				if (isPrint == 1)
					cd = "inline";
				servletResponse.setHeader("Content-disposition", cd + "; filename=" + downloadName);
				
			}
			
			
		} catch (Exception e) {
			logger.error("Error", e);
			try{
				ObjectMapper mapper = new ObjectMapper();
				PrintWriter out = servletResponse.getWriter();				
				out.print(mapper.writeValueAsString(CommonUtils.responseException(e)));
			}catch( Exception e1){
				logger.error("Error", e1);
			}
		}				
	}
}
