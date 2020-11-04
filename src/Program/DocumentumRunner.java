package Program;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import com.documentum.Reindexacao;
import com.documentum.RepositoryDocumentum;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.common.DfException;
import com.documentum.ObjectsParam.Querys;



public class DocumentumRunner {

	public static void main(String[] args) throws Exception {
		// --------------------INSTANCIAS-----------------------
		RepositoryDocumentum documentumRepository = new RepositoryDocumentum();

		Reindexacao reindexacao = new Reindexacao(documentumRepository);  
		//-----------------------------------------------------
		
		documentumRepository.ConsultarQueryUPDATE(Querys.deleteValorTabelaRegistrada("2"));
//		//-----JOGA O "object_name" DOS ARQUIVOS NO ARRAY------ 
//		ArrayList<String> arquivosNaoIndexados = new ArrayList<String>();
//		//-----------------------------------------------------
//		
//		//--------LIMPAR ARQUIVOS COM NOME NULO----------------
//		documentumRepository.InvalidObjectNameZero();		
//		ArrayList<String> arquivosNaoIndexadosFiltro = reindexacao.BuscaPasta(); 
//		//-----------------------------------------------------
//		
//		//MOVIMENTA OS ARQUIVOS PARA AS PASTA DE 30 E 60-------
//		arquivosNaoIndexados = reindexacao.filtrarArquivos(arquivosNaoIndexadosFiltro);
//		reindexacao.movimentarParaPastas(arquivosNaoIndexados);
//		//-----------------------------------------------------
	}
	
	
	
}		

