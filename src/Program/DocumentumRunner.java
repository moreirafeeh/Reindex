package Program;

import java.util.ArrayList;

import com.documentum.Reindexacao;
import com.documentum.RepositoryDocumentum;
import com.documentum.Utils;
import com.documentum.ObjectsParam.Querys;
import com.documentum.type.DocumentumReindexacao;

public class DocumentumRunner {

	public static void main(String[] args) throws Exception {
		//------DATA DE HOJE-----
		   
	    	System.out.println("Execução dia: " + Utils.getToday());

		//--------------------INSTANCIÇÃO-----------------------
		
			RepositoryDocumentum documentumRepository = new RepositoryDocumentum();
			
			Reindexacao reindexacao = new Reindexacao(documentumRepository);  
			
			System.out.println("Instancias criadas.");
			
			documentumRepository.ImportarDocumentum();
		//-----------------------------------------------------
		
			//documentumRepository.ConsultarQueryUPDATE(Querys.deleteValorTabelaRegistrada("2"));
		 
		//--------LIMPAR ARQUIVOS COM NOME NULO----------------
			
			documentumRepository.InvalidObjectNameZero();
			
			System.out.println("Limpeza de arquivos com nome vazio.");

		//--------ARRAY DE ARQUIVOS INDEXADOS------------------
			ArrayList<DocumentumReindexacao> arquivosNaoIndexadosFiltro = new ArrayList<DocumentumReindexacao>();
			switch (Integer.parseInt(args[0])) {
			
			case 1:
				arquivosNaoIndexadosFiltro = reindexacao.BuscaPasta("/teste_pasta_reindex/Nao_Indexados_TESTE");
				System.out.println("Arquivos recuperados da pasta de Não Indexados.");
				break;
				
			case 2:
				arquivosNaoIndexadosFiltro = reindexacao.BuscaPasta("/teste_pasta_reindex/Regras");
				System.out.println("Arquivos recuperados da pasta de Regras.");
				break;


			default:
				break;
			}
		
		
		//-------------ARRAY DA TABELA DE REGISTRO CONTENDO EM ID, NOME, DATA DE ENTRADA
			
			ArrayList<DocumentumReindexacao> arquivosNaoIndexados = new ArrayList<DocumentumReindexacao>();
			
			arquivosNaoIndexados = reindexacao.filtrarArquivos(arquivosNaoIndexadosFiltro);
			
			System.out.println("Arquivos com parametros incorretos filtrados.");
		
		
		//----------------INSERCAO DE REGISTROS BASEADO NO arquivosNaoIndexadosFiltro--------
			for(int i = 0;  i < arquivosNaoIndexados.size(); i++ ){
				try {
					
					documentumRepository.ConsultarQueryUPDATE(Querys.inserirValoresTabelaRegistrada(arquivosNaoIndexados.get(i).getId(), arquivosNaoIndexados.get(i).getNome(), Utils.getToday()));
				
				}
				catch(Exception e){
					
					System.out.println(arquivosNaoIndexadosFiltro.get(i).getId() + " JÁ  PERTENCE AO REGISTRO");
				
				}
			}
			
			reindexacao.movimentarParaPastas(arquivosNaoIndexados); 
	}
}		

