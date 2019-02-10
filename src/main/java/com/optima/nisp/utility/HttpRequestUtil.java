package com.optima.nisp.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtil {

	public static InputStream requestStream(String url, String method, String urlParameters, int timeout) throws IOException{
		HttpURLConnection con = getConnection(url, method, timeout);
		if(method.equalsIgnoreCase("post")){
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
		}
		
		return con.getInputStream();
	}
	
	public static String requestWithJson(String url, String method, String json) throws IOException{
		return requestWithJson(url, method, json, 300000);		
	}
	
	public static String requestWithJson(String url, String method, String json, int timeout) throws IOException{
		HttpURLConnection con = getConnection(url, method, timeout);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		if( method.equalsIgnoreCase("post") ){
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(json);
			wr.flush();
			wr.close();
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
	
	public static String request(String url, String method, String urlParameters, int timeout) throws IOException{
		HttpURLConnection con = getConnection(url, method, timeout);
		if( method.equalsIgnoreCase("post") ){
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
	
	private static HttpURLConnection getConnection(String url, String method, int timeout) throws IOException{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod(method);
		con.setDoOutput(true);
		con.setConnectTimeout(timeout*1000);
		con.setReadTimeout(timeout*1000);
		return con;
	}
}
