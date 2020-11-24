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
import com.documentum.fc.common.DfException;
import com.documentum.type.DocumentumReindexacao;

public class RepositoryDocumentum extends ConexaoDocumentum {

	public RepositoryDocumentum() {
		super();
	}

	public RepositoryDocumentum(String Usuario_, String Senha_,
			String Repositorio_) {
		super(Usuario_, Senha_, Repositorio_);
	}


	/**
	 * @since 29/09/2020
	 * @param Cabinet_name
	 *            : Nome do Cabinet que será criado
	 */
	public void createCabinet(String Cabinet_name) throws Exception {

		IDfFolder cabinet = (IDfFolder) getSessDctm().newObject("dm_cabinet");

		if (cabinet != null) {

			cabinet.setObjectName(Cabinet_name);
			cabinet.save();

			System.out.println("Cabinet criado com sucesso");

		}

	}

	/**
	 * @since 29/09/2020
	 * @param Cabinet_link
	 *            : Path do Documentum onde será criada a pasta.
	 * @param folder_name
	 *            : nome da pasta no documentum.
	 */
	public void createFolder(String Cabinet_link, String folder_name)
			throws Exception {

		IDfFolder folder = (IDfFolder) getSessDctm().newObject("dm_folder");
		ArrayList<String> pastas = this.ConsultarQuery(Querys
				.VerificaPasta(folder_name));
		if (folder != null && pastas.size() == 0) {

			folder.setObjectName(folder_name);

			folder.link("/" + Cabinet_link);

			folder.save();

			System.out.println("Pasta criada com sucesso no cabinet"
					+ Cabinet_link);

		} else {
			System.out.println("PASTA JA EXISTENTE");
		}
	}

	/**
	 * @since 29/09/2020
	 * @param Nome_doc
	 *            : nome do Documento.
	 * @param tipo_conteudo
	 *            : tipo do documento(txt,PDF etc..). tipo - crtext...
	 * @param path_conteudo
	 *            : path onde o documento está localizado em sua maquina.
	 * @param documentum_path
	 *            : path no documentum de onde será disponibilizado o arquivo.
	 * 
	 */
	public IDfDocument createDocument(String Nome_doc, String tipo_conteudo,
			String path_conteudo, String documentum_path) throws Exception {

		IDfDocument document = (IDfDocument) getSessDctm().newObject(
				"dm_document");

		if (document != null) {

			document.setObjectName(Nome_doc);

			document.setContentType(tipo_conteudo);

			document.setFile(path_conteudo);

			document.link(documentum_path);

			document.save();

		}

		return document;

	}

	/**
	 * Este Método permite qualquer criacao de Objeto
	 * 
	 * @since 29/09/2020
	 * @param Nome_doc
	 *            : nome do Documento.
	 * @param doc_type
	 *            : tipo do documento(Objeto documentum)
	 * @param tipo_conteudo
	 *            : tipo do conteudo(crtext e etc).
	 * @param path_conteudo
	 *            : path no documentum de onde será disponibilizado o arquivo.
	 * @param documentum_path
	 *            : path no documentum de onde será disponibilizado o arquivo.
	 */
	public IDfDocument createPasta30(String Nome_doc, String doc_type,
			String tipo_conteudo, String path_conteudo, String documentum_path)
			throws Exception {

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

	/**
	 * Este Método permite qualquer criacao de Objeto
	 * 
	 * @since 29/09/2020
	 * @param Nome_doc
	 *            : nome do Documento.
	 * @param doc_type
	 *            : tipo do documento(Objeto documentum)
	 * @param tipo_conteudo
	 *            : tipo do conteudo(crtext e etc).
	 * @param path_conteudo
	 *            : path no documentum de onde será disponibilizado o arquivo.
	 * @param documentum_path
	 *            : path no documentum de onde será disponibilizado o arquivo.
	 */
	public IDfDocument createPasta60(String Nome_doc, String doc_type,
			String tipo_conteudo, String path_conteudo, String documentum_path,
			String data) throws Exception {

		IDfDocument document = (IDfDocument) getSessDctm().newObject(doc_type);

		if (document != null) {

			document.setObjectName(Nome_doc);

			document.setContentType(tipo_conteudo);

			document.link(documentum_path);

			document.setString("date_controler", data);

			document.save();

		}
		System.out.println(document.getString("date_controler"));
		return document;

	}

	/**
	 * Este Método faz uma consulta DQL no Documentum
	 * 
	 * @since 29/09/2020
	 * @param queryString
	 *            : recebe uma Query DQL.
	 */
	// -------------------CONSULTAS DQL--------------------
	public ArrayList<String> ConsultarQuery(String queryString)
			throws Exception {
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

	/**
	 * Este Método faz uma consulta DQL no Documentum
	 * 
	 * @since 29/09/2020
	 * @param queryString
	 *            : recebe uma Query DQL pré-selecionada.
	 */
	public boolean ConsultarPasta(String queryString) throws Exception {
		System.out.println("PRINT CONSULTAR PASTA");
		IDfQuery query = new DfQuery();

		query.setDQL(queryString);

		IDfCollection coll = query.execute(getSessDctm(), 0);

		boolean pastaExiste = false;

		while (coll.next()) {

			IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();

			System.out
					.println("----------------------------------------------------");
			System.out.println("resultado: "
					+ typeObject.getString("resultado_query"));
			// System.out.println("creation date "+
			// typeObject.getString("r_object_id"));
			System.out
					.println("----------------------------------------------------");

			String arquivo = typeObject.getString("resultado_query");
			pastaExiste = Integer.parseInt(arquivo) > 0;

		}

		if (coll != null)

			coll.close();

		return pastaExiste;

	}

	/**
	 * Este Método faz uma consulta DQL no Documentum
	 * 
	 * @since 29/09/2020
	 * @param queryString
	 *            : recebe uma Query DQL pré-selecionada.
	 */
	public ArrayList<String> ConsultarQueryData(String queryString)
			throws Exception {
		System.out.println("PRINT CONSULTAR QUERY DATA");
		System.out.println(queryString);
		ArrayList<String> arquivo = new ArrayList<String>();

		IDfQuery query = new DfQuery();

		query.setDQL(queryString);

		IDfCollection coll = query.execute(getSessDctm(), 0);

		while (coll.next()) {

			IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();

			System.out
					.println("----------------------------------------------------");
			System.out.println("resultado: "
					+ typeObject.getString("r_object_id"));
			System.out.println("resultado: "
					+ typeObject.getString("object_name"));
			System.out.println("creation date "
					+ typeObject.getString("r_creation_date"));
			System.out
					.println("----------------------------------------------------");

			arquivo.add(typeObject.getString("r_object_id"));
			arquivo.add(typeObject.getString("object_name"));
			arquivo.add(typeObject.getString("r_creation_date"));
		}

		if (coll != null)

			coll.close();

		return arquivo;

	}
	
	public ArrayList<String> ConsultarQueryData2(String queryString)throws Exception {
	System.out.println("PRINT CONSULTAR QUERY DATA");
	System.out.println(queryString);
	ArrayList<String> arquivo = new ArrayList<String>();
	
	IDfQuery query = new DfQuery();
	
	query.setDQL(queryString);
	
	IDfCollection coll = query.execute(getSessDctm(), 0);
	
	while (coll.next()) {
	
		IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();
	
		System.out
				.println("----------------------------------------------------");
		System.out.println("resultado: "
				+ typeObject.getString("data_entrada"));
	
		arquivo.add(typeObject.getString("data_entrada"));
}

if (coll != null)

	coll.close();

return arquivo;

}

	/**
	 * Este Método faz um UPDATE DQL no Documentum
	 * 
	 * @since 29/09/2020
	 * @param queryString
	 *            : recebe uma Query DQL pré-selecionada(classe Queryes).
	 */
	public void ConsultarQueryUPDATE(String queryString) throws Exception {

		//		System.out.println(queryString);
		IDfQuery query = new DfQuery();

		query.setDQL(queryString);

		query.execute(getSessDctm(), 0);

	}

	/**
	 * Este Método verifica se existem nomes Nulos nos arquivos. Se tiver nomes
	 * nulos, move para pasta "Parametros Incorretos".
	 * 
	 * @since 29/09/2020
	 */
	public void InvalidObjectNameZero() throws Exception {
		
		System.out.println("Limpando arquivos com nome vazio.");
		
		IDfQuery query = new DfQuery();

		query.setDQL(Querys.MoveFileNameNull("Nao_Indexados_TESTE"));

		IDfCollection collNotName = query.execute(getSessDctm(), 0);

		while (collNotName.next()) {

			IDfTypedObject typeObject = (IDfTypedObject) collNotName.getTypedObject();

			System.out.println("Arquivo invalido: " + typeObject.getString("r_object_id"));

			ConsultarQueryUPDATE(Querys.UPDATE_LINK_NAME_NULL("/teste_pasta_reindex/ParametrosIncorretos", typeObject.getString("r_object_id")));
			ConsultarQueryUPDATE(Querys.UPDATE_UNLINK_NAME_NULL("/teste_pasta_reindex/Nao_Indexados_TESTE", typeObject.getString("r_object_id")));

		}

	}

	/**
	 * Metodo faz uma query e Atribui o object_name ao Array
	 * 
	 * @param String
	 *            pasta: Path da pasta onde será buscado os arquivos no
	 *            Documentum
	 */
	public ArrayList<DocumentumReindexacao> BuscaArquivosPasta(String pasta) throws Exception {
		String queryString = Querys.PastaParaArquivo(pasta);

		ArrayList<DocumentumReindexacao> arquivo = new ArrayList<DocumentumReindexacao>();

		IDfQuery query = new DfQuery();

		query.setDQL(queryString);

		IDfCollection coll = query.execute(getSessDctm(), 0);

		while (coll.next()) {

			IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();
			
			DocumentumReindexacao doc = new DocumentumReindexacao();
			
			doc.setId(typeObject.getString("r_object_id"));
			doc.setNome(typeObject.getString("object_name"));
			
			arquivo.add(doc);

		}

		if (coll != null)

			coll.close();

		return arquivo;

	}
	
	public ArrayList<String> BuscaArquivosPastaID(String pasta) throws Exception {
		String queryString = Querys.PastaParaArquivoID(pasta);

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
	
	public ArrayList<String> BuscaArquivosPastaID() throws Exception {
		String queryString = Querys.selectTabelaRegistroID();

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
	
	public ArrayList<String> BuscaArquivosPastaDataEntrada() throws Exception {
		String queryString = Querys.selectTabelaRegistroDataEntrada();

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
	
	public ArrayList<String> BuscaArquivosPastaNome() throws Exception {
		String queryString = Querys.selectTabelaRegistroNome();

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
	
	

	/**
	 * Este metodo é responsavel por fazer uma consulta qdl.
	 * 
	 * 
	 * @param String
	 *            recebe numero de sinistro do WSDL
	 * 
	 */

	public int pastaSinistro(String Sinistro) throws DfException {
		String queryString = Querys.PastaExiste(Sinistro);
		int folderExiste = 0;
		IDfQuery query = new DfQuery();

		query.setDQL(queryString);

		IDfCollection coll = query.execute(getSessDctm(), 0);

		while (coll.next()) {

			IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();

			folderExiste = typeObject.getInt("resultado_query");

		}

		if (coll != null)

			coll.close();

		return folderExiste;

	}

	/**
	 * Este metodo é responsavel por fazer uma consulta qdl.
	 * 
	 * 
	 * @param String
	 *            recebe numero de expediente do WSDL
	 * 
	 * 
	 */

	public int pastaExpediente(String sinistro) throws DfException {
		String queryString = Querys.PastaExisteExpediente(sinistro);
		int folderExiste = 0;
		IDfQuery query = new DfQuery();

		query.setDQL(queryString);

		IDfCollection coll = query.execute(getSessDctm(), 0);

		while (coll.next()) {

			IDfTypedObject typeObject = (IDfTypedObject) coll.getTypedObject();

			folderExiste = typeObject.getInt("resultado_query");

		}

		if (coll != null)

			coll.close();

		return folderExiste;

	}

	/**
	 * Este metodo é responsavel por validar a existencia da pasta sinistro
	 * 
	 * 
	 * @param String
	 *            recebe numero de sinistro do WSDL
	 * 
	 * 
	 */

	public boolean validaPastaSinistro(String sinistro) throws DfException {

		boolean valido = false;
		if (pastaSinistro(sinistro) == 1) {

			valido = true;
		}

		return valido;
	}

	/**
	 * Este metodo é responsavel por validar a existencia da pasta expediente
	 * 
	 * 
	 * @param String
	 *            recebe numero de sinistro do WSDL
	 * 
	 * 
	 */

	public boolean validaPastaExpediente(String sinistro) throws DfException {

		boolean valido = false;
		if (pastaExpediente(sinistro) == 1) {

			valido = true;
		}

		return valido;

	}

	/**
	 * Este metodo é responsavel por criar pasta
	 * 
	 * 
	 * @param String
	 *            : nome da pasta, path da pasta
	 * 
	 * 
	 * 
	 */
	public void criarPasta(String nomeFolder, String pathFolderCabinet)
			throws DfException {

		IDfFolder folder = (IDfFolder) getSessDctm().newObject("dm_folder");

		folder.setObjectName(nomeFolder);

		folder.link(pathFolderCabinet);

		folder.save();

	}
	
	public void ImportarDocumentum() {

		 IDfDocument sysObj = null;

		 try {
			 
			 String[] array = {
					 "NFO_3021720000331_00012020050307T02_TRC03_AFJ8654.pdf",
					 "NFO_0000000000000_00000000000000000_00000_0000000.pdf",
					 "NFO_0000000000000_00000000000000000_TRC03_AFJ8654.pdf",
					 "NFO_0000000000000_00012020050307T02_TRC03_AFJ8654.pdf",
					 "NFO_3031720000333_00012020076357T03_TRC03_AFJ8654.pdf",
					 "NFO_3021730000344_00000000000000000_TRC03_AFJ8654.pdf",
					 ".pdf",
					 "TEST.pdf",
					 "",					 
					 };
			 for (String string : array) {
					sysObj = (IDfDocument) getSessDctm().newObject("dm_document");
					sysObj.setObjectName(string);
					sysObj.setContentType("pdf");
					sysObj.setFile("C:\\document.pdf");
					sysObj.link("/teste_pasta_reindex/Nao_Indexados_TESTE");
					sysObj.save();
			}
			 System.out.println("Arquivos Inseridos PARA TESTE");

		 } catch (DfException e) {
			 e.printStackTrace();
		}

		 }

}
