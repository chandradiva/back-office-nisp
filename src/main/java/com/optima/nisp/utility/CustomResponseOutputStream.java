package com.optima.nisp.utility;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class CustomResponseOutputStream extends ServletOutputStream{
	DataOutputStream output;
	
	public CustomResponseOutputStream(OutputStream os){
		output = new DataOutputStream(os);
	}
	
	@Override
	public void write(int b) throws IOException {
		output.write(b);
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		output.write(b);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		output.write(b, off, len);
	}
}
