package com.documentum;

import java.awt.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.documentum.fc.client.IDfDocument;

public class Utils {

	
	
	
	
	
	
	
	public boolean validaSinistro(String param){
		if (param.matches("[0-9]*") && param.length() >= 13) {
			if (Double.parseDouble(param) > 0 ) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean validaProtocolo(String param){
		if (param.length() == 17 && Character.toString(param.charAt(14)).matches("[A-Z]*")) {
				return true; 
		}
		return false;
	}
	
	
	public int diasProcessados(String dataDocumento) {
		int diasProcessado = 0;
		try {
			Date dataDeEntradaDocumento = new SimpleDateFormat("dd/MM/yyyy").parse(dataDocumento);
			long diffInMillies = Math.abs(new Date().getTime() - dataDeEntradaDocumento.getTime());
			diasProcessado = (int) (diffInMillies / (1000*60*60*24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return diasProcessado;      
	}
	
	
}
