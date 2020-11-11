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
import com.documentum.type.DocumentumReindexacao;
import com.documentum.ObjectsParam.Querys;



public class DocumentumRunner {

	public static void main(String[] args) throws Exception {
		// --------------------INSTANCIAS-----------------------
		RepositoryDocumentum documentumRepository = new RepositoryDocumentum();

		Reindexacao reindexacao = new Reindexacao(documentumRepository);  
		//-----------------------------------------------------
		//documentumRepository.ConsultarQueryUPDATE(Querys.deleteValorTabelaRegistrada("2"));
		
		//-----JOGA O "object_name" DOS ARQUIVOS NO ARRAY------ 
		ArrayList<DocumentumReindexacao> arquivosNaoIndexados = new ArrayList<DocumentumReindexacao>();
		//--------LIMPAR ARQUIVOS COM NOME NULO----------------
		documentumRepository.InvalidObjectNameZero();
		//ARRAYS DE ARQUIVOS INDEXADOS  separado em array de ID E NOME
		ArrayList<DocumentumReindexacao> arquivosNaoIndexadosFiltro = reindexacao.BuscaPasta();
		
		//-----------------------------------------------------
		
		
		
		//-------------ARRAY DA TABELA DE REGISTRO SEPARADO EM ID, NOME, DATA DE ENTRADA
		//ArrayList<String> Registros = new ArrayList<String>();
		ArrayList<DocumentumReindexacao> arquivosRegistroID = reindexacao.BuscaRegistroID();
		
		//MOVIMENTA OS ARQUIVOS PARA AS PASTA DE 30 E 60-------
		arquivosNaoIndexados = reindexacao.filtrarArquivos(arquivosNaoIndexadosFiltro); 
		
		
		
		//------DATA DE HOJE-----
		Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    String dataDeEntradaDocumento = formatter.format(date);  
		System.out.println(dataDeEntradaDocumento);
		//-----------------------
		
		
		//----------------INSERCAO DE REGISTROS BASEADO NO arquivosNaoIndexadosFiltro--------
		for(int i = 0;  i < arquivosNaoIndexados.size(); i++ ){
			try{
				documentumRepository.ConsultarQueryUPDATE(Querys.inserirValoresTabelaRegistrada(arquivosNaoIndexados.get(i).getId(), arquivosNaoIndexados.get(i).getNome(), dataDeEntradaDocumento.toString()));
			}
			catch(Exception e){
				System.out.println("-----------" + arquivosNaoIndexadosFiltro.get(i).getId() + "------JÁ  PERTENCE AO REGISTRO");
			}
			//documentumRepository.ConsultarQueryUPDATE(Querys.deleteValorTabelaRegistrada(arquivosNaoIndexadosFiltroID.get(i)));
		}
		//------------------------------------------------------------------------------------
		
		
		
		System.out.println("DEPOIS DO FILTRO -----"+ arquivosNaoIndexados);
		reindexacao.movimentarParaPastas2(arquivosNaoIndexados,arquivosRegistroID, arquivosRegistroDataEntrada, arquivosRegistroNome); //FALTA FAZER A MANIPULACAO NOVA DE PASTA ANTES DOS 30 DIAS
		//-----------------------------------------------------
	}
	
	
	
	
	
	
	
}		

