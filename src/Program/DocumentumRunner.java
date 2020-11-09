package Program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Date;
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
		//documentumRepository.ConsultarQueryUPDATE(Querys.deleteValorTabelaRegistrada("2"));
		
		//-----JOGA O "object_name" DOS ARQUIVOS NO ARRAY------ 
		ArrayList<String> arquivosNaoIndexados = new ArrayList<String>();
		//--------LIMPAR ARQUIVOS COM NOME NULO----------------
		documentumRepository.InvalidObjectNameZero();	
		ArrayList<String> arquivosNaoIndexadosFiltro = reindexacao.BuscaPasta();
		ArrayList<String> arquivosNaoIndexadosFiltroID = reindexacao.BuscaPastaID();
		
		//-----------------------------------------------------
		
		Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    String dataDeEntradaDocumento = formatter.format(date);  
		
		System.out.println(dataDeEntradaDocumento);
		for(int i = 0; i <  arquivosNaoIndexadosFiltroID.size() && i < arquivosNaoIndexadosFiltro.size(); i++ ){
			try{
				documentumRepository.ConsultarQueryUPDATE(Querys.inserirValoresTabelaRegistrada(arquivosNaoIndexadosFiltroID.get(i), arquivosNaoIndexadosFiltro.get(i), dataDeEntradaDocumento.toString()));
			}
			catch(Exception e){
				System.out.println("-----------" + arquivosNaoIndexadosFiltroID.get(i) + "------JÁ  PERTENCE AO REGISTRO");
			}
			//documentumRepository.ConsultarQueryUPDATE(Querys.deleteValorTabelaRegistrada(arquivosNaoIndexadosFiltroID.get(i)));
		}
		
		
		ArrayList<String> Registros = new ArrayList<String>();
		ArrayList<String> arquivosRegistroID = reindexacao.BuscaRegistroID();
		System.out.println(arquivosRegistroID);
		//ArrayList<String> arquivosRegistroID = reindexacao.BuscaRegistroDataEntrega();
		
//		for(int i = 0; i <  arquivosNaoIndexadosFiltroID.size() && i < arquivosNaoIndexadosFiltro.size(); i++ ){
//			if(arquivosNaoIndexadosFiltroID.get(i) < ){
//		}
//		
		//-----------------------------------------------------
		
		

		//-----------------------------------------------------
		
		//MOVIMENTA OS ARQUIVOS PARA AS PASTA DE 30 E 60-------
		arquivosNaoIndexados = reindexacao.filtrarArquivos(arquivosNaoIndexadosFiltro); 
		reindexacao.movimentarParaPastas(arquivosNaoIndexados); //FALTA FAZER A MANIPULACAO NOVA DE PASTA ANTES DOS 30 DIAS
		//-----------------------------------------------------
	}
	
	
	
	
	
	
	
}		

