package com.optima.nisp.constanta;

public class SendEmailStatus {

	public static int ALL_STATUS = -1;
	public static int NOT_SENT = 0;		//Belum terkirim
	public static int SENT = 1;			//Terkirim
	public static int FAILED = 2;		//Gagal Kirim
	public static int EXPIRED = 3;		//Kadaluarsa
	public static int QUEUE = 4;		// antrian
	public static int PROCESSING = 5;	// Sedang diproses
	public static int READ = 6;	// Sudah dibaca
}
