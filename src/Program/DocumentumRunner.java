package Program;

//import org.tempuri.CalculatorSoap_CalculatorSoap12_Client;

//------SOAP-----
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import com.documentum.RepositoryDocumentum;
import com.documentum.ObjectsParam.Querys;

//import org.tempuri.CalculatorSoap_CalculatorSoap12_Client;


public class DocumentumRunner {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		

		RepositoryDocumentum Utils = new RepositoryDocumentum();

		
		
		//FLUXO REINDEXAÇÂO--------------
		
		
		/// limpa os arquivos com parametros zerados ==
		
		
		
		///--------------------------------------------------------------------
		
		/// joga o object_name dos arquivos no array=== 
		ArrayList<String> arquivosNaoIndexados = new ArrayList<String>();
		//LIMPAR ARQUIVOS COM NOME NULO-----
		Utils.InvalidObjectNameZero();
				
		ArrayList<String> arquivosNaoIndexadosFiltro = Utils.BuscaArquivosPasta("/teste_pasta_reindex/Nao_Indexados_TESTE");
		
		
		//-------------------------------
		int i = 0;
		System.out.println(arquivosNaoIndexadosFiltro.size());
		while ( i < arquivosNaoIndexadosFiltro.size()) {
		System.out.println(i);
		
		String[] params = arquivosNaoIndexadosFiltro.get(i).split("_");
		boolean estaIncorreto = false;
		
			for (String param : params) {
	            // remove a extensão do arquivo
					param = FilenameUtils.removeExtension(param);
					
					// valida sinistro ==
					if (validaSinistro(param)) {
						if (Double.parseDouble(param) > 0 ) {
							arquivosNaoIndexados.add(arquivosNaoIndexadosFiltro.get(i));
							break;
						}
					}
					// valida protocolo ==
					if (validaProtocolo(param)) {
						arquivosNaoIndexados.add(arquivosNaoIndexadosFiltro.get(i));
						break;
					}
				}
			
			
			int tamanhoArray = arquivosNaoIndexados.size();
			String ultimaPosicaoArray = arquivosNaoIndexados.get(arquivosNaoIndexados.size()-1);
			
			
			if(tamanhoArray==0|| ultimaPosicaoArray != arquivosNaoIndexadosFiltro.get(i)){
				Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/ParametrosIncorretos",arquivosNaoIndexadosFiltro.get(i),"/teste_pasta_reindex/Nao_Indexados_TESTE"));
				Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",arquivosNaoIndexadosFiltro.get(i)));
			}
			i = i + 1;
		}
		System.out.println(arquivosNaoIndexados);
//--------------------------------------------
		
		///--------------------------------------------------------------------
		
		
//		/// consultando web service ==
//		for(String params: arquivosNaoIndexados){
//
//		 	String[] DocumentoSplitado =  params.split("_");
//		 	 
////			chamada do web service == 
//			//QName SERVICE_NAME = new QName("http://tempuri.org/", "Calculator");
//			//CalculatorSoap_CalculatorSoap12_Client.SOAP_TESTE(args);// params
//		 	boolean temPasta  = Utils.ConsultarPasta(Querys.PastaExiste("S"+DocumentoSplitado[1]));
//			
//			if(!temPasta){
//				//Registro JOAO depende do web service = false ----
//			
//				ArrayList<String> arquivosNaoIndexadosMenos30;
//				ArrayList<String> arquivosNaoIndexadosMais60;
//			
//				boolean expurgo = false;
//				/*
//				 * Para cada arquivo que passou o filtro de arquivos fora do padrão
//				 * e validado se o arquivo está registrado em uma das pastas.
//				 * Caso não seja encontrado é criado o registro na pasta de 
//				 */
//				arquivosNaoIndexadosMenos30 = Utils.ConsultarQueryData(Querys.ArquivoNaoIndexado30(params));
//				//System.out.println("Menos 30");
//				//System.out.println(arquivosNaoIndexadosMenos30);
//				
//				arquivosNaoIndexadosMais60 = Utils.ConsultarQueryData(Querys.ArquivoNaoIndexado60(params));
//				//System.out.println("Mais 60");
//				//System.out.println(arquivosNaoIndexadosMais60);
//			
//				if(!arquivosNaoIndexadosMenos30.isEmpty()){
//					
//					String dataDocumento = arquivosNaoIndexadosMenos30.get(2).split(" ")[0];
//				    Date dataDeEntradaDocumento = new SimpleDateFormat("dd/MM/yyyy").parse(dataDocumento);  
//				    
//				    long diffInMillies = Math.abs(new Date().getTime() - dataDeEntradaDocumento.getTime());
//				    int diasProcessado = (int) (diffInMillies / (1000*60*60*24));
//				    //System.out.println(diasProcessado);
//			    
//				    /*
//				     * Caso o documento tenham sido processados por mais de 30 dias.
//				     * Passa pelo processo de mudança de pasta caso atinga o trigésimo dia.
//				    */
//				    if(diasProcessado >= 31){
//						Utils.ConsultarQueryUPDATE(Querys.DELETE(arquivosNaoIndexadosMenos30.get(0)));
//						Utils.createObject(params, "date_nao_indexado_60", "", "", "/teste_pasta_reindex/Mais60",dataDocumento);
//				    }  
//				}
//			
//				if(!arquivosNaoIndexadosMais60.isEmpty()){
//					
//					String dataDocumento = arquivosNaoIndexadosMais60.get(2).split(" ")[0];
//				    Date dataDeEntradaDocumento = new SimpleDateFormat("dd/MM/yyyy").parse(dataDocumento);  
//				    
//				    long diffInMillies = Math.abs(new Date().getTime() - dataDeEntradaDocumento.getTime());
//				    int diasProcessado = (int) (diffInMillies / (1000*60*60*24));
//				    
//				    /*
//				     * Caso o documento tenham sido processados por mais de 60 dias.
//				     * Passa pelo processo de expurgo caso atinga o sexagésimo dia.
//				    */
//				    if(diasProcessado >= 60){
//				    	Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/Expurgo",params, "/teste_pasta_reindex/Nao_Indexados_TESTE"));
//				    	Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",params));
//				    	Utils.ConsultarQueryUPDATE(Querys.DELETE(arquivosNaoIndexadosMais60.get(0)));
//				    }
//				}
//			
//				if(arquivosNaoIndexadosMenos30.isEmpty() && arquivosNaoIndexadosMais60.isEmpty()){
//					Utils.createObject(params, "date_nao_indexado_30", "", "", "/teste_pasta_reindex/Menos30");
//				}
//			
//				
//			//-----------------------------
//			
//			// movimentacao do lucas ------
//			
//			//-----------------------------
//			
//			
//			}
//			
//			else{
//				Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/S"+ DocumentoSplitado[1] + "/TRC03",params, "/teste_pasta_reindex/Nao_Indexados_TESTE"));
//				Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",params));
//			}
//			
//		}
//		//------------------------------------------------------------------
//		

	}
}		