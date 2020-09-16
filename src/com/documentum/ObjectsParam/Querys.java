package src.com.documentum.ObjectsParam;

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
	
	public static String PastaParaArquivoData(String PathDeBusca, String id_arquivo){
		String query = "select object_name,r_creation_date from dm_sysobject where FOLDER('" + PathDeBusca +  "',descend) and object_name LIKE '%" + id_arquivo +"%'";
		System.out.println(query);
		return query;
	}
	
	public static String ArquivoNaoIndexado30(String id_arquivo){
		String query = "select r_object_id,object_name,date_controler as r_creation_date from  date_nao_indexado_30 doc, dm_folder fb where any doc.i_folder_id = fb.r_object_id and doc.object_name = '"+id_arquivo+"'";
//		System.out.println(query);
		return query;
	}
	
	
	public static String ArquivoNaoIndexado60(String id_arquivo){
		String query = "select r_object_id,object_name,date_controler as r_creation_date from  date_nao_indexado_60 doc, dm_folder fb where any doc.i_folder_id = fb.r_object_id and doc.object_name = '"+id_arquivo+"'";
//		System.out.println(query);
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
		String query = "update dm_document object link '" + PathSeraLinkado +"' where object_name  ='" + id_arquivo +"'";
		return query;
		
	}
	
	public static String UPDATE_UNLINK(String PathSeraLinkado ,String id_arquivo){
		String query = "update dm_document object unlink '" + PathSeraLinkado +"' where object_name ='" + id_arquivo +"'";
		return query;
	}
	
	public static String DELETE(String object_id){
		String query = "DELETE dm_document objects where r_object_id = '"+object_id+"' enable (RETURN_TOP 1);";
	return query;
	}
	public static String MoveFileNameNull(String path ){
		
		String query = "select r_object_id from  dm_document doc,dm_folder fb where any doc.i_folder_id = fb.r_object_id  and fb.object_name = '"+path+"'  and doc.object_name=' '";
		
		return query;
	}
	
	
	
	public static String UPDATE_LINK_NAME_NULL(String PathSeraLinkado ,String id_arquivo){
		String query = "update dm_document object link '" + PathSeraLinkado +"' where r_object_id ='" + id_arquivo +"'";
		return query;
		
	}
	
	
	
	public static String UPDATE_UNLINK_NAME_NULL(String PathSeraLinkado ,String id_arquivo){
		String query = "update dm_document object unlink '" + PathSeraLinkado +"' where r_object_id = '" + id_arquivo +"'";
		return query;
	}
	
	
	
	
	
	
}
