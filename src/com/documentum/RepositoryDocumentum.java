 package com.documentum;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.documentum.ObjectsParam.Querys;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfTypedObject;


public class RepositoryDocumentum extends ConexaoDocumentum {
	
	public RepositoryDocumentum(){
		super();
	}

	public RepositoryDocumentum(String Usuario_, String Senha_, String Repositorio_) {
		super(Usuario_, Senha_, Repositorio_);	
	}
	
	
	//------------------Criar Cabinet-------------------------
	public void createCabinet(String Cabinet_name) throws Exception {
		 
		IDfFolder cabinet = (IDfFolder) getSessDctm().newObject("dm_cabinet");
		
		if (cabinet != null) {
		
			cabinet.setObjectName(Cabinet_name);
			cabinet.save();
			
			System.out.println("Cabinet criado com sucesso");
		
		    }
		
	}
	//---------------------------------------------------------
	//----------------Criar Pasta (necessario passar cabinet onde a pasta ficará)------------------------------
	public void createFolder(String Cabinet_link, String folder_name) throws Exception {
		
		  IDfFolder folder = (IDfFolder) getSessDctm().newObject("dm_folder");
		  ArrayList<String> pastas = this.ConsultarQuery(Querys.VerificaPasta(folder_name));
		  if (folder != null && pastas.size() == 0) {
		
		      folder.setObjectName(folder_name);
		
		      folder.link("/"+Cabinet_link);
		
		      folder.save();
		      
		      System.out.println("Pasta criada com sucesso no cabinet"+ Cabinet_link);
		
		   }
		  else{
			  System.out.println("PASTA JA EXISTENTE");
		  }
	}
    //---------------------------------------------------------
	//----------------Criar(Importar) um Documento-------------
	//EXEMPLOS                                                                                            
	//createDocument("Felipe_teste_func", "crtext", "C:\\Documentum\\export\\testandoTwitch.txt","/Felipe Twitch/felipinho3");
	//-------
	public IDfDocument createDocument(String Nome_doc, String tipo_conteudo, String path_conteudo, String documentum_path) throws Exception {
		
		IDfDocument document = (IDfDocument) getSessDctm().newObject("dm_document");
		
		if (document != null) {
		
			document.setObjectName(Nome_doc);
		
		    document.setContentType(tipo_conteudo);
		
		    document.setFile(path_conteudo);
		
		    document.link(documentum_path);
		
		    document.save();
		
		    }
		
		return document;
		
	}
	
	public IDfDocument createObject(String Nome_doc,String doc_type, String tipo_conteudo, String path_conteudo, String documentum_path) throws Exception {
		
		IDfDocument document = (IDfDocument) getSessDctm().newObject(doc_type);
		
		if (document != null) {
		
			document.setObjectName(Nome_doc);
		
		    document.setContentType(tipo_conteudo);
		
		    document.link(documentum_path);
			
		    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
			String strDate = dateFormat.format(new Date()); 
			
		    document.setString("date_controler", strDate);
		
		    document.save();
		
		    }
		System.out.println(document.getString("date_controler"));
		return document;
		
	}
	
	public IDfDocument createObject(String Nome_doc,String doc_type, String tipo_conteudo, String path_conteudo, String documentum_path,String param) throws Exception {
		
		IDfDocument document = (IDfDocument) getSessDctm().newObject(doc_type);
		
		if (document != null) {
		
			document.setObjectName(Nome_doc);
		
		    document.setContentType(tipo_conteudo);
		
		    document.link(documentum_path);
			
		    document.setString("date_controler", param);
		
		    document.save();
		
		    }
		System.out.println(document.getString("date_controler"));
		return document;
		
	}
	
	//-------------------CONSULTAS DQL--------------------
	public ArrayList<String> ConsultarQuery(String queryString) throws Exception {
		System.out.println("PRINT CONSULTAR QUERY");
		System.out.println(queryString);
		ArrayList<String> arquivo = new ArrayList<String>();
		
		IDfQuery query = new DfQuery();
		
		query.setDQL(queryString);
		
		IDfCollection coll = query.execute(getSessDctm(), 0);
		
		while (coll.next()) {
		
			IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();	
			
			System.out.println("----------------------------------------------------");
		    System.out.println("resultado: "+ typeObject.getString("resultado_query"));
		    System.out.println("creation date "+ typeObject.getString("r_object_id"));
		    System.out.println("----------------------------------------------------");
		    
		    arquivo.add(typeObject.getString("resultado_query"));
		}
		
		if (coll != null)
		
			coll.close();
		
		return arquivo; 
		
		}
	
	
public boolean ConsultarPasta(String queryString) throws Exception {
	System.out.println("PRINT CONSULTAR PASTA");
		IDfQuery query = new DfQuery();
		
		query.setDQL(queryString);
		
		IDfCollection coll = query.execute(getSessDctm(), 0);
		
		boolean pastaExiste = false;
		
		
		while (coll.next()) {
		
			IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();	
			
			System.out.println("----------------------------------------------------");
		    System.out.println("resultado: "+ typeObject.getString("resultado_query"));
		    //System.out.println("creation date "+ typeObject.getString("r_object_id"));
		    System.out.println("----------------------------------------------------");
		    
		    String arquivo = typeObject.getString("resultado_query");
		    pastaExiste = Integer.parseInt(arquivo) > 0;
		    
		}
		
		if (coll != null)
		
			coll.close();
		
		return pastaExiste; 
		
		}
	
	
	
	public ArrayList<String> ConsultarQueryData(String queryString) throws Exception {
		System.out.println("PRINT CONSULTAR QUERY DATA");
		System.out.println(queryString);
		ArrayList<String> arquivo = new ArrayList<String>();
		
		IDfQuery query = new DfQuery();
		
		query.setDQL(queryString);
		
		IDfCollection coll = query.execute(getSessDctm(), 0);
		
		while (coll.next()) {
		
			IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();	
			
			System.out.println("----------------------------------------------------");
			System.out.println("resultado: "+ typeObject.getString("r_object_id"));
		    System.out.println("resultado: "+ typeObject.getString("object_name"));
		    System.out.println("creation date "+ typeObject.getString("r_creation_date"));
		    System.out.println("----------------------------------------------------");
		    
		    arquivo.add(typeObject.getString("r_object_id"));
		    arquivo.add(typeObject.getString("object_name"));
		    arquivo.add(typeObject.getString("r_creation_date"));
		}
		
		if (coll != null)
		
			coll.close();
		
		return arquivo; 
		
		}
	//------------------------------------------------------
	
	//UPDATE QUERYS----
	public void ConsultarQueryUPDATE(String queryString) throws Exception {
			
			System.out.println(queryString);
			IDfQuery query = new DfQuery();
			
			query.setDQL(queryString);
			
			query.execute(getSessDctm(), 0);
			 
			
			}
		
		//-------------------------------------------------------------------------
	
	
	    /*
	     * Metodo InvalidObjectNameZero() 
	     * pega o r_object_name dos arquivos com object_name vazio por meio de uma query(MoveFileNameNull) 
	     * faz a movimentação desse arquivo para outra pasta com o UPDATE_LINK_NAME_NULL e UPDATE_UNLINK_NAME_NULL
	     */
		public void InvalidObjectNameZero() throws Exception{
			
			IDfQuery query = new DfQuery();
			
			query.setDQL(Querys.MoveFileNameNull("Nao_Indexados_TESTE"));
			
			IDfCollection collNotName = query.execute(getSessDctm(), 0);

			while (collNotName.next()) {
				
				System.out.println("ENTREI NA FUNCAO 0");
				IDfTypedObject typeObject = (IDfTypedObject) collNotName.getTypedObject();
				
				System.out.println("========"+typeObject.getString("r_object_id"));
				
				ConsultarQueryUPDATE(Querys.UPDATE_LINK_NAME_NULL("/teste_pasta_reindex/ParametrosIncorretos",typeObject.getString("r_object_id")));
				ConsultarQueryUPDATE(Querys.UPDATE_UNLINK_NAME_NULL("/teste_pasta_reindex/Nao_Indexados_TESTE",typeObject.getString("r_object_id")));
			
			}
			
		}
		

		/*
		 * Metodo SrcClear()
		 * faz uma query String queryString = PastaParaArquivo pegando o object_name 
		 * Quebra o object_name com "Split(_)" e faz a validação
		 * faz a movimentação dos arquivos que não foram validados com UPDATE_LINK e UPDATE_UNLINK
		 */
		public ArrayList<String> BuscaArquivosPasta(String pasta) throws Exception {
			String queryString = Querys.PastaParaArquivo(pasta);
			// metodo que faz a limpeza de arquivos com object_name vazio 
			
			ArrayList<String> arquivo = new ArrayList<String>();

			IDfQuery query = new DfQuery();

			query.setDQL(queryString);

			IDfCollection coll = query.execute(getSessDctm(), 0);

			while (coll.next()) {
				
				IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();

				String objectNameFile = typeObject.getString("resultado_query");
				
				arquivo.add(objectNameFile);
				
			}

			if (coll != null)

				coll.close();

			return arquivo;

		}
		
		
		
	
		
		
}
