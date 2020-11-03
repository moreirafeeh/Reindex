package Program;

import java.util.ArrayList;
import java.util.Map;

import com.documentum.Reindexacao;
import com.documentum.RepositoryDocumentum;



public class DocumentumRunner {

	public static void main(String[] args) throws Exception {
		// --------------------INSTANCIAS-----------------------
		RepositoryDocumentum documentumRepository = new RepositoryDocumentum();
		Reindexacao reindexacao = new Reindexacao(documentumRepository);
		// -----------------------------------------------------

		// -----JOGA O "object_name" DOS ARQUIVOS NO ARRAY------
		ArrayList<String> arquivosNaoIndexados = new ArrayList<String>();
		// -----------------------------------------------------

		// --------LIMPAR ARQUIVOS COM NOME NULO----------------
		documentumRepository.InvalidObjectNameZero();
		ArrayList<String> arquivosNaoIndexadosFiltro = reindexacao.BuscaPasta();
		// -----------------------------------------------------
		
		// MOVIMENTA OS ARQUIVOS PARA AS PASTA DE 30 E 60-------
		arquivosNaoIndexados = reindexacao
				.filtrarArquivos(arquivosNaoIndexadosFiltro);
		reindexacao.movimentarParaPastas(arquivosNaoIndexados);
		// -----------------------------------------------------
	}
}