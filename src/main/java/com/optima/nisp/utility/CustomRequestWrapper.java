package com.optima.nisp.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.input.TeeInputStream;
import org.springframework.http.HttpMethod;

public class CustomRequestWrapper extends HttpServletRequestWrapper  {
	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
	String body = null;

	public CustomRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		// StringBuilder stringBuilder = new StringBuilder();
		// BufferedReader bufferedReader = null;
		// try {
		// InputStream inputStream = request.getInputStream();
		// if (inputStream != null) {
		// bufferedReader = new BufferedReader(new
		// InputStreamReader(inputStream));
		// char[] charBuffer = new char[128];
		// int bytesRead = -1;
		// while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		// stringBuilder.append(charBuffer, 0, bytesRead);
		// }
		// } else {
		// stringBuilder.append("");
		// }
		// } catch (IOException ex) {
		// throw ex;
		// } finally {
		// if (bufferedReader != null) {
		// try {
		// bufferedReader.close();
		// } catch (IOException ex) {
		// throw ex;
		// }
		// }
		// }
		// body = stringBuilder.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ServletInputStream servletInputStream = super.getInputStream();
		return new ServletInputStream() {
			private TeeInputStream tee = new TeeInputStream(servletInputStream, bos);

			@Override
			public int read() throws IOException {

				return tee.read();
			}
			
			@Override
			public void close() throws IOException {
			
				super.close();
				
				copyToString();
			}

			
		};
	}

	//
	// @Override
	// public BufferedReader getReader() throws IOException {
	// return new BufferedReader(new InputStreamReader(this.getInputStream()));
	// }
	//
	// public String getBody() {
	// return this.body;
	// }

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return HttpMethod.PUT.toString();
	}
	
	private void copyToString() {
		body = new String(bos.toByteArray());
		
	}

	public String getBody() {
		return body;
	}
}
