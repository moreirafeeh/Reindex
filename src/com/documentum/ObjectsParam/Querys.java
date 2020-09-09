package com.documentum.ObjectsParam;

public class Querys {
	
	//acha arquivos dentro de um folder
	// PastaparaArquivo(/Felipe Twitch/felipinho3)
	public static String PastaParaArquivo(String PathDeBusca){
		String query = "select object_name as resultado_query from dm_sysobject where FOLDER('" + PathDeBusca +  "',descend)";
		System.out.println(query);
		return query;
	}
	
	public static String ArquivosDeUmaPasta(String PathDeBusca){
		String query = "select dm_document.object_name as resultado_query from dm_document where FOLDER('"+ PathDeBusca +"') and dm_document.a_storage_type ='filestore_01';";
		System.out.println(query);
		return query;
	}
	
	public static String VerificaPasta(String NomePasta){
		String query = "select object_name as resultado_query from dm_folder where object_name='" + NomePasta + "' ";
		return query;
	}
	
	public static String UPDATE_LINK_ID(String PathSeraLinkado ,String id_arquivo){
		String query = "update dm_document object link '" + PathSeraLinkado +"' where r_object_id='" + id_arquivo +"'";
		return query;
		
	}
	
	public static String UPDATE_UNLINK_ID(String PathSeraLinkado ,String id_arquivo){
		String query = "update dm_document object unlink '" + PathSeraLinkado +"' where r_object_id='" + id_arquivo +"'";
		return query;
	}
	
	public static String UPDATE_LINK(String PathSeraLinkado ,String id_arquivo){
		String query = "update dm_document object link '" + PathSeraLinkado +"' where object_name LIKE '%" + id_arquivo +"%'";
		return query;
		
	}
	
	public static String UPDATE_UNLINK(String PathSeraLinkado ,String id_arquivo){
		String query = "update dm_document object unlink '" + PathSeraLinkado +"' where object_name LIKE '%" + id_arquivo +"%'";
		return query;
	}
	
	
	
	
	
	
	
}
