package com.documentum;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;


public abstract class ConexaoDocumentum {
	
	public IDfSession sessDctm = null;
	private IDfSessionManager msessDctm = null;
	private IDfClient clientDctm;
	private String repositorioDctm;
	private String usuarioDctm;
	private String senhaDctm;
	private String erroDctm = null;
	private static Properties props;
	
	public ConexaoDocumentum() {
		
		getProp();
		try {
			clientDctm = DfClient.getLocalClient();
			msessDctm = clientDctm.newSessionManager();
			IDfLoginInfo li = new DfLoginInfo();
			li.setUser(props.getProperty("usuario"));
			li.setPassword(props.getProperty("senha"));
			msessDctm.setIdentity(props.getProperty("banco"), li);
			this.usuarioDctm = props.getProperty("usuario");
			this.repositorioDctm = props.getProperty("banco");
			this.senhaDctm = props.getProperty("senha");
			this.ConectarDocumentum();

		} catch (DfException dfe) {
			if (dfe instanceof DfException) {
				String MsgErro = ((DfException) dfe).getStackTraceAsString();
				erroDctm = MsgErro;
				
			} else {
				
				dfe.printStackTrace();
			}
		}

		
	}
	
	public ConexaoDocumentum(String Usuario_, String Senha_, String Repositorio_) {
		
		try {
			clientDctm = DfClient.getLocalClient();
			msessDctm = clientDctm.newSessionManager();
			IDfLoginInfo li = new DfLoginInfo();
			li.setUser(Usuario_);
			li.setPassword(Senha_);
			msessDctm.setIdentity(Repositorio_, li);
			this.usuarioDctm = Usuario_;
			this.repositorioDctm = Repositorio_;
			this.senhaDctm = Senha_;
			this.ConectarDocumentum();

		} catch (DfException dfe) {
			if (dfe instanceof DfException) {
				String MsgErro = ((DfException) dfe).getStackTraceAsString();
				erroDctm = MsgErro;
				
			} else {
				
				dfe.printStackTrace();
			}
		}
	}
	
	private static Properties getProp() {
		if (props == null) {
			props = new Properties();
			try {
				props.load(new FileInputStream(".\\src\\com\\documentum\\DatasInfo.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}
	
	
	private IDfSession ConectarDocumentum() throws DfException {
		try {

			sessDctm = msessDctm.getSession(repositorioDctm);

		} catch (DfException dfe) {
			if (dfe instanceof DfException) {
				
				String MsgErro = ((DfException) dfe).getStackTraceAsString();
				erroDctm = MsgErro;
			} else {
				dfe.printStackTrace();
			}
		}
		return sessDctm;
	}
	
	public boolean DesconectarDocumentum() throws Exception {
		boolean bolRet = false;
		try {
			msessDctm.release(sessDctm);
			bolRet = true;
		} catch (Exception ex) {
			String MsgErro = ((Exception) ex).getMessage();
			erroDctm = MsgErro;
		
		}
	
		return bolRet;
}
	

	public IDfSession getSessDctm() {
		return sessDctm;
	}
	public void setSessDctm(IDfSession sessDctm) {
		this.sessDctm = sessDctm;
	}
	public IDfSessionManager getMsessDctm() {
		return msessDctm;
	}
	public void setMsessDctm(IDfSessionManager msessDctm) {
		this.msessDctm = msessDctm;
	}
	public IDfClient getClientDctm() {
		return clientDctm;
	}
	public void setClientDctm(IDfClient clientDctm) {
		this.clientDctm = clientDctm;
	}
	public String getRepositorioDctm() {
		return repositorioDctm;
	}
	public void setRepositorioDctm(String repositorioDctm) {
		this.repositorioDctm = repositorioDctm;
	}
	public String getUsuarioDctm() {
		return usuarioDctm;
	}
	public void setUsuarioDctm(String usuarioDctm) {
		this.usuarioDctm = usuarioDctm;
	}
	public String getSenhaDctm() {
		return senhaDctm;
	}
	public void setSenhaDctm(String senhaDctm) {
		this.senhaDctm = senhaDctm;
	}
	public String getErroDctm() {
		return erroDctm;
	}
	public void setErroDctm(String erroDctm) {
		this.erroDctm = erroDctm;
	}
}
