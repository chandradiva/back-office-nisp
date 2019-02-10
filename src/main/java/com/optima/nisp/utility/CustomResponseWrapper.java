package com.optima.nisp.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CustomResponseWrapper extends HttpServletResponseWrapper{

	private ByteArrayOutputStream baos;
	CustomResponseOutputStream cros;
	public CustomResponseWrapper(HttpServletResponse response) {
		super(response);
		baos = new ByteArrayOutputStream();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if( cros == null )
			cros = new CustomResponseOutputStream(baos);
		return cros;
	}
	
	public byte[] getDataStream(){
		return baos.toByteArray();
	}
}
