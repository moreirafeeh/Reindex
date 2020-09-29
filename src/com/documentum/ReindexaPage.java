package com.documentum;

import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

import com.documentum.ObjectsParam.Querys;
import com.documentum.RepositoryDocumentum;
import com.documentum.Utils;

public class ReindexaPage {
	
	private RepositoryDocumentum documentumRepository;
	private Utils util;
	
	public ReindexaPage(RepositoryDocumentum documentumRepository){
		this.documentumRepository = documentumRepository;
		this.util = new Utils() ;
	}
	
		public ArrayList<String> filtrarArquivos(ArrayList<String> arquivosNaoIndexadosFiltro){
			
			ArrayList<String> arquivosNaoIndexados = new ArrayList<String>();
			
			int i = 0;
			//System.out.println(arquivosNaoIndexadosFiltro.size());
			while ( i < arquivosNaoIndexadosFiltro.size()) {
			System.out.println(i);
			
			String[] params = arquivosNaoIndexadosFiltro.get(i).split("_");
			
			boolean estaIncorreto = false;
			
				for (String param : params) {
		            // remove a extensão do arquivo
						param = FilenameUtils.removeExtension(param);
						
						// valida sinistro ==
						if (util.validaSinistro(param)||util.validaProtocolo(param)) {
								arquivosNaoIndexados.add(arquivosNaoIndexadosFiltro.get(i));
								break;
						}
						
				}
				
				if(arquivosNaoIndexados.size() > 0){
					String ultimaPosicaoArray = arquivosNaoIndexados.get(arquivosNaoIndexados.size()-1);
					if(ultimaPosicaoArray != arquivosNaoIndexadosFiltro.get(i)){
						try {
							documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/ParametrosIncorretos",arquivosNaoIndexadosFiltro.get(i),"/teste_pasta_reindex/Nao_Indexados_TESTE"));
							documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",arquivosNaoIndexadosFiltro.get(i)));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				else{
					try {
						documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/ParametrosIncorretos",arquivosNaoIndexadosFiltro.get(i),"/teste_pasta_reindex/Nao_Indexados_TESTE"));
						documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",arquivosNaoIndexadosFiltro.get(i)));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				i = i + 1;
			}
			
			return arquivosNaoIndexados;
			
			
		}
		
		public ArrayList<String> BuscaPasta(){
			ArrayList<String> array = new ArrayList<String>();
			
		 try { 
			array = this.documentumRepository.BuscaArquivosPasta("/teste_pasta_reindex/Nao_Indexados_TESTE");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Erro ao buscar arquivos");
		}
		finally{
			return array;
		}
		
	}
		
	public void movimentarParaPastas(ArrayList<String> arquivosNaoIndexados){
		
		for(String params: arquivosNaoIndexados){

		 	String[] DocumentoSplitado =  params.split("_");
		 	 
			//QName SERVICE_NAME = new QName("http://tempuri.org/", "Calculator");
			//CalculatorSoap_CalculatorSoap12_Client.SOAP_TESTE(args);// params
		 	boolean temPasta;
			try {
				temPasta = documentumRepository.ConsultarPasta(Querys.PastaExiste("S"+DocumentoSplitado[1]));
			
			
			if(!temPasta){
				
			}
			
			else{
				documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/S"+ DocumentoSplitado[1] + "/TRC03",params, "/teste_pasta_reindex/Nao_Indexados_TESTE"));
				documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",params));
			}
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}	
	}
	
	public void controleDePastas(String params){
		//Registro JOAO depende do web service = false ----
		
		ArrayList<String> arquivosNaoIndexadosMenos30;
		ArrayList<String> arquivosNaoIndexadosMais60;
		
		/*
		 * Para cada arquivo que passou o filtro de arquivos fora do padrão
		 * e validado se o arquivo está registrado em uma das pastas.
		 * Caso não seja encontrado é criado o registro na pasta de 
		 */
		try {
			arquivosNaoIndexadosMenos30 = documentumRepository.ConsultarQueryData(Querys.ArquivoNaoIndexado30(params));
		 
		arquivosNaoIndexadosMais60 = documentumRepository.ConsultarQueryData(Querys.ArquivoNaoIndexado60(params));
	
		if(!arquivosNaoIndexadosMenos30.isEmpty()){
			
			int diasProcessado  = util.diasProcessados(arquivosNaoIndexadosMenos30.get(2).split(" ")[0]);
	    
		    /*
		     * Caso o documento tenham sido processados por mais de 30 dias.
		     * Passa pelo processo de mudança de pasta caso atinga o trigésimo dia.
		    */
		    if(diasProcessado >= 31){
		    	documentumRepository.ConsultarQueryUPDATE(Querys.DELETE(arquivosNaoIndexadosMenos30.get(0)));
		    	documentumRepository.createObject(params, "date_nao_indexado_60", "", "", "/teste_pasta_reindex/Mais60",arquivosNaoIndexadosMenos30.get(2).split(" ")[0]);
		    }  
		}
	
		if(!arquivosNaoIndexadosMais60.isEmpty()){
			
			int diasProcessado  = util.diasProcessados(arquivosNaoIndexadosMais60.get(2).split(" ")[0]);
		    
		    /*
		     * Caso o documento tenham sido processados por mais de 60 dias.
		     * Passa pelo processo de expurgo caso atinga o sexagésimo dia.
		    */
		    if(diasProcessado >= 60){
		    	documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/Expurgo",params, "/teste_pasta_reindex/Nao_Indexados_TESTE"));
		    	documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",params));
		    	documentumRepository.ConsultarQueryUPDATE(Querys.DELETE(arquivosNaoIndexadosMais60.get(0)));
		    }
		}
	
		if(arquivosNaoIndexadosMenos30.isEmpty() && arquivosNaoIndexadosMais60.isEmpty()){
			documentumRepository.createObject(params, "date_nao_indexado_30", "", "", "/teste_pasta_reindex/Menos30");
		}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
		
}
