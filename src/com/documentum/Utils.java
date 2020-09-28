package com.documentum;

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
	
	
	
	
	
}
