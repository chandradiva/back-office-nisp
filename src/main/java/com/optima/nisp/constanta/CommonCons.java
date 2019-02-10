package com.optima.nisp.constanta;

import java.io.File;

public class CommonCons {

	public static String FILE_SEPARATOR = File.separator;
	public static final String DB_SCHEMA = "WEBSTATEMENT";
	public static final String FE_DB_SCHEMA = "ESTATEMENT";
//	public static final String DB_SCHEMA = "C##WEBSTATEMENT";
//	public static final String FE_DB_SCHEMA = "C##ESTATEMENT";
	public static final String SERVER_ID = "BACKOFFICE";
	public static final String WEBSTATEMENT_ID = "WEBSTATEMENT";
	public static final String LOGIN_URL = "session/login";
	public static final String LOGOUT_URL = "session/logout";
	public static final String RIWAYAT_TRANSAKSI_TAMPIL_URL = "riwayat-transaksi/riwayat_transaksi_tampil";
	public static final String RIWAYAT_TRANSAKSI_DOWNLOAD_URL = "riwayat-transaksi/riwayat_transaksi_download";
	
	public static final Long DEFAULT_MAX_ATTACHMENT_SIZE = 1536*1024L;
	
	public static final String SECURITY_GROUP = "Security Admin";
	public static final String SECURITY_FGROUP = "WEBSTMTSECADM";
	
	public static final int DEFAULT_BO_IDLE_TIME = 300;
	
	public static final String MANAGE_GROUP = "Manage Group";
	public static final String ADD_GROUP = "Add Group";
	public static final String EDIT_GROUP = "Edit Group";
	public static final String MANAGE_FGROUP = "Manage FGroup";
}
