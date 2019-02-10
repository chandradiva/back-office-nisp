package com.optima.nisp.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.optima.nisp.utility.CustomRequestWrapper;

public class LoggingFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		CustomRequestWrapper reqPut = null;
		try {
			if(request instanceof HttpServletRequest){
				
				HttpServletRequest wrapper = (HttpServletRequest) request;
				if(wrapper!=null && wrapper.getMethod().equals("PUT")){
					 reqPut = new CustomRequestWrapper(wrapper);
				}
			}
			if(reqPut!=null){
				chain.doFilter(reqPut, response);
			}else{
				chain.doFilter(request, response);	
			}
			
		} catch (Exception ex) {
			request.setAttribute("errorMessage", ex);
			request.getRequestDispatcher("/WEB-INF/views/jsp/error.jsp") .forward(request, response);
			ex.printStackTrace();
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
