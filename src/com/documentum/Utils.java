package com.documentum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static boolean validaSinistro(String param){
		if (param.matches("[0-9]*") && param.length() >= 13) {
			if (Double.parseDouble(param) > 0 ) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean validaProtocolo(String param){
		if (param.length() == 17 && Character.toString(param.charAt(14)).matches("[A-Z]*")) {
				return true; 
		}
		return false;
	}
	
	
	public static int diasProcessados(String dataDocumento) {
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
	
	public static String getToday() {
		
		Date date = new Date();  
	    
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    
	    return formatter.format(date);  

		
	}
}
