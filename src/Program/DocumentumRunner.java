package Program;

//import org.tempuri.CalculatorSoap_CalculatorSoap12_Client;

//------SOAP-----
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import com.documentum.RepositoryDocumentum;
import com.documentum.Utils;
import com.documentum.ObjectsParam.Querys;
import com.documentum.ReindexaPage;

//import org.tempuri.CalculatorSoap_CalculatorSoap12_Client;


public class DocumentumRunner {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//--------------------INSTANCIAS-----------------------
		RepositoryDocumentum documentumRepository = new RepositoryDocumentum();
		ReindexaPage reindexaPage = new ReindexaPage(documentumRepository);  
		Utils util = new Utils();
		//-----------------------------------------------------
		
		
		//-----JOGA O "object_name" DOS ARQUIVOS NO ARRAY------ 
		ArrayList<String> arquivosNaoIndexados = new ArrayList<String>();
		//-----------------------------------------------------
		
		//--------LIMPAR ARQUIVOS COM NOME NULO----------------
		documentumRepository.InvalidObjectNameZero();		
		ArrayList<String> arquivosNaoIndexadosFiltro = reindexaPage.BuscaPasta(); 
		//-----------------------------------------------------
		
		//MOVIMENTA OS ARQUIVOS PARA AS PASTA DE 30 E 60-------
		arquivosNaoIndexados = reindexaPage.filtrarArquivos(arquivosNaoIndexadosFiltro);
		reindexaPage.movimentarParaPastas(arquivosNaoIndexados);
		//-----------------------------------------------------
	}
}		