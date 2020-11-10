package com.documentum.ObjectsParam;

public class Querys {
	
	//acha arquivos dentro de um folder
	// PastaparaArquivo(/Felipe Twitch/felipinho3)
	public static String PastaParaArquivo(String PathDeBusca){
		String query = "select object_name as resultado_query from dm_sysobject where FOLDER('" + PathDeBusca +  "',descend)";
		System.out.println(query);
		return query;
	}
	
	public static String PastaParaArquivoID(String PathDeBusca){
		String query = "select r_object_id as resultado_query from dm_sysobject where FOLDER('" + PathDeBusca +  "',descend)";
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
	
	public static String UPDATE_LINK(String PathSeraLinkado ,String id_arquivo, String PathBusca){
		String query = "update dm_document object link '" + PathSeraLinkado +"' where object_name  ='" + id_arquivo +"'" + "and FOLDER('" + PathBusca + "')" ;
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
	
	
	public static String PastaExiste(String Pasta){
		String query = "select count(object_name) as resultado_query from dm_folder where object_name = '"+ Pasta +"'";
		
		return query;
	}
	
	public static String CriarTabelaBanco(String NomeTabela){
		String query = "EXECUTE exec_sql with query='create table" + NomeTabela + "(id_arquivo varchar(200) UNIQUE,nome_arquivo varchar(200) , data_entrada varchar(20))'";
		System.out.println(query);
		return query;
	}
	//TabelaReindexacaoUnica
	//Quando se cria uma tabela registrada é necessário primeiro executar o método "CriarTabelaBanco"
	//Em seguida criarTabelaRegistrada com o mesmo no da tabela no banco
	public static String CriarTabelaRegistrada(String NomeTabela){
		String query = "REGISTER TABLE dm_dbo." + NomeTabela + "(id_arquivo STRING(200), nome_arquivo STRING(200), data_entrada STRING(20))  KEY (id_arquivo)SYNONYM 'tabela controle ids'";
		System.out.println(query);
		return query;
	}
	
	public static String inserirValoresTabelaRegistrada(String id_arquivo,String nome_arquivo, String data_entrada){
		String query = "INSERT INTO dm_dbo.TabelaReindexacaoUnica  (id_arquivo, nome_arquivo, data_entrada) VALUES ( '"+ id_arquivo +"' ,' " + nome_arquivo + "', '" + data_entrada + " ')";
		System.out.println(query);
		return query;
	}
	
	public static String updateTabelaRegistrada(String atributoDesejaMudar,String valorDaAlteracao, String id_arquivoWHERE){
		String query = "UPDATE dm_dbo.TabelaReindexacaoUnica  SET " + atributoDesejaMudar + "= '" + valorDaAlteracao +"' WHERE id_arquivo = '" + id_arquivoWHERE + "';";
		System.out.println(query);
		return query;
	}
	
	public static String deleteValorTabelaRegistrada(String idArquivoSeraDeletado){
		String query = "DELETE from dm_dbo.TabelaReindexacaoUnica  Where id_arquivo = '" + idArquivoSeraDeletado +"'";
		System.out.println(query);
		return query;
	}
	
	public static String selectTabelaRegistroID(){
		String query = "select id_arquivo as resultado_query from dm_dbo.TabelaReindexacaoUnica";
		return query;
	}
	
	public static String selectTabelaRegistroDataEntrada(){
		String query = "select data_entrada as resultado_query from dm_dbo.TabelaReindexacaoUnica";
		return query;
	}
	
	public static String selectTabelaRegistroNome(){
		String query = "select nome_arquivo as resultado_query from dm_dbo.TabelaReindexacaoUnica";
		return query;
	}
	
	public static String selectTabelaRegistroNomeEspecifico(String nome_arquivo){
		String query = "select data_entrada as resultado_query from dm_dbo.TabelaReindexacaoUnica where nome_arquivo LIKE'%" + nome_arquivo + "%'";
		return query;
	}
	
	
	
	public static String PastaExisteExpediente(String sinistro){
		String query = "select count(object_name) as resultado_query  from  dm_folder where FOLDER('/teste_pasta_reindex/"+sinistro+"')";
		return query;
	}

	
	
	
	
}
