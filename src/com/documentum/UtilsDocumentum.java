package com.documentum;
import java.io.IOException;

import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import com.documentum.ObjectsParam.Querys;

import com.documentum.conexao_documentum;
import com.documentum.ObjectsParam.Querys;
import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfActivity;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfDocbaseMap;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfProcess;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.client.IDfVirtualDocument;
import com.documentum.fc.client.IDfVirtualDocumentNode;
import com.documentum.fc.client.IDfWorkflowBuilder;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfList;
import com.documentum.fc.common.IDfAttr;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfList;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;

import org.apache.commons.io.FilenameUtils;


public class UtilsDocumentum extends conexao_documentum {
	
	public UtilsDocumentum(){
		super();
	}

	public UtilsDocumentum(String Usuario_, String Senha_, String Repositorio_) {
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
	
		//-------------------CONSULTAS DQL--------------------
		public ArrayList<String> ConsultarQuery(String queryString) throws Exception {
			
			System.out.println(getRepositorioDctm());
			System.out.println(getSessDctm());
			System.out.println(getUsuarioDctm());
			
			ArrayList<String> arquivo = new ArrayList<String>();
			
			IDfQuery query = new DfQuery();
			
			query.setDQL(queryString);
			
			IDfCollection coll = query.execute(getSessDctm(), 0);
			
			while (coll.next()) {
			
				IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();	
				
				System.out.println("----------------------------------------------------");
			    System.out.println("resultado: "+ typeObject.getString("resultado_query"));
			    //System.out.println("creation date "+ typeObject.getString("r_object_id"));
			    System.out.println("----------------------------------------------------");
			    
			    arquivo.add(typeObject.getString("resultado_query"));
			}
			
			if (coll != null)
			
				coll.close();
			
			return arquivo; 
			
			}
		//------------------------------------------------------
		
		//UPDATE QUERYS----
		public void ConsultarQueryUPDATE(String queryString) throws Exception {
			
			System.out.println(getRepositorioDctm());
			System.out.println(getSessDctm());
			System.out.println(getUsuarioDctm());
			
			IDfQuery query = new DfQuery();
			
			query.setDQL(queryString);
			
			query.execute(getSessDctm(), 0);
			 
			
			}
		
		//-------------------------------------------------------------------------
		
		public ArrayList<String> SrcClear(String queryString) throws Exception {

			System.out.println(getRepositorioDctm());
			System.out.println(getSessDctm());
			System.out.println(getUsuarioDctm());

			ArrayList<String> arquivo = new ArrayList<String>();

			IDfQuery query = new DfQuery();

			query.setDQL(queryString);

			IDfCollection coll = query.execute(getSessDctm(), 0);

			int cont =0;
			while (coll.next()) {

				IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();

				String objectNameFile = typeObject.getString("resultado_query");

				// tirar extenção do aquivo

				String[] params = objectNameFile.split("_");
				
				for (String param : params) {

					param = FilenameUtils.removeExtension(param);
					// valida sinistro ==
					if (param.matches("[0-9]*") && param.length() >= 13) {
						if (Double.parseDouble(param) > 0 ) {
							arquivo.add(objectNameFile);
							break;
						}
					}
					// valida protocolo ==
					if (param.length() == 17 && Character.toString(param.charAt(14)).matches("[A-Z]*")) {
						arquivo.add(objectNameFile);
						break;
					}
						
					// valida placa ==
					 if (param.length() == 7 && param.substring(0, 3).matches("[A-Z]*")) {
						 arquivo.add(objectNameFile);
						 break;
					}
					// valida expediente ==
					 if (param.length() == 5 && param.substring(0, 3).matches("[A-Z]*")) {
							 arquivo.add(objectNameFile);
							  break;
						}
				
					}
				
				
				/// se não for add no array ele eh expurgado ==
				if(objectNameFile.equals("")){
					System.out.println("vazio");
				}
					
//					if(arquivo.get(arquivo.size()) != objectNameFile || objectNameFile.equals("")){
//						ConsultarQueryUPDATE(Querys.UPDATE_LINK("/Lucas Vidotti/ParametrosIncorretos",objectNameFile));
//						ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/Sinistros Autos/Não Indexados",objectNameFile));
//					}
				}

			

			if (coll != null)

				coll.close();

			return arquivo;

		}
		
		
}
