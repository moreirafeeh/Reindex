package src.Program;

import src.org.tempuri.CalculatorSoap_CalculatorSoap12_Client;

//------SOAP-----
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Document;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;

//--------------


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import src.com.documentum.UtilsDocumentum;
import src.com.documentum.ObjectsParam.Querys;

import src.org.tempuri.Calculator;


public class DocumentumRunner {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		

		UtilsDocumentum Utils = new UtilsDocumentum();

		
		
		//FLUXO REINDEXAÇÂO--------------
		
		
		/// limpa os arquivos com parametros zerados ==
		
		
		
		///--------------------------------------------------------------------
		
		/// joga o object_name dos arquivos no array=== 
		ArrayList<String> arquivosNaoIndexados;
		arquivosNaoIndexados = Utils.SrcClear(Querys.PastaParaArquivo("/Sinistros Autos/Não Indexados"));
		///--------------------------------------------------------------------
		
		
		
		
		/// consultando web service ==
		for(String params: arquivosNaoIndexados){

			
//			String param1=params.substring(4,19);
//			String param2=params.substring(20,37); 
//			String paramBoard = params.substring(38,45);
//			String param4 =  params.substring(46,60);

			
//			/// chamada do web service == 
//			QName SERVICE_NAME = new QName("http://tempuri.org/", "Calculator");
//			CalculatorSoap_CalculatorSoap12_Client.SOAP_TESTE(args);// params
			boolean temPasta = true;
			
			if(temPasta){
			
			//Registro JOAO depende do web service = false ----
			
			ArrayList<String> arquivosNaoIndexadosMenos30;
			ArrayList<String> arquivosNaoIndexadosMais60;
			
			boolean expurgo = false;
			/*
			 * Para cada arquivo que passou o filtro de arquivos fora do padrão
			 * e validado se o arquivo está registrado em uma das pastas.
			 * Caso não seja encontrado é criado o registro na pasta de 
			 */
			
			arquivosNaoIndexadosMenos30 = Utils.ConsultarQueryData(Querys.PastaParaArquivoData("/teste_pasta_reindex/Menos30",params));
			System.out.println("Menos 30");
			System.out.println(arquivosNaoIndexadosMenos30);
			
			arquivosNaoIndexadosMais60 = Utils.ConsultarQueryData(Querys.PastaParaArquivoData("/teste_pasta_reindex/Mais60",params));
			System.out.println("Mais 60");
			System.out.println(arquivosNaoIndexadosMais60);
			
			if(!arquivosNaoIndexadosMenos30.isEmpty()){
				
				String dataDocumento = arquivosNaoIndexadosMenos30.get(1).split(" ")[0];
			    Date dataDeEntradaDocumento = new SimpleDateFormat("dd/MM/yyyy").parse(dataDocumento);  
			    
			    long diffInMillies = Math.abs(new Date().getTime() - dataDeEntradaDocumento.getTime());
			    int diasProcessado = (int) (diffInMillies / (1000*60*60*24));
		    
			    /*
			     * Caso o documento tenham sido processados por mais de 30 dias.
			     * Passa pelo processo de mudança de pasta caso atinga o trigésimo dia.
			    */
			    if(diasProcessado >= 31){
					Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Menos30", params));
					Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/Mais60", params));
			    }  
			}
			
			if(!arquivosNaoIndexadosMais60.isEmpty()){
				
				String dataDocumento = arquivosNaoIndexadosMais60.get(1).split(" ")[0];
			    Date dataDeEntradaDocumento = new SimpleDateFormat("dd/MM/yyyy").parse(dataDocumento);  
			    
			    long diffInMillies = Math.abs(new Date().getTime() - dataDeEntradaDocumento.getTime());
			    int diasProcessado = (int) (diffInMillies / (1000*60*60*24));
			    
			    /*
			     * Caso o documento tenham sido processados por mais de 60 dias.
			     * Passa pelo processo de expurgo caso atinga o sexagésimo dia.
			    */
			    if(diasProcessado >= 60){
			    	expurgo = false;
			    }
			}
			
			if(arquivosNaoIndexadosMenos30.isEmpty() && arquivosNaoIndexadosMais60.isEmpty()){
				Utils.createObject(params, "dm_date_nao_indexado", "", "", "/teste_pasta_reindex/Menos30");
			}
			
			//-----------------------------
			
			// movimentacao do lucas ------
			
			//-----------------------------
			
			
			}
			
			
			// movimentacao do felipe == true -----
			
			//Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK_ID("/Sinistros Autos/Não Indexados",params));
			//Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK_ID("/Lucas Vidotti/ParametrosIncorretos",params));
			
			
			//-----------------------------
		}
		//------------------------------------------------------------------
		
		
		
		
		//for(int i = 0; i <= arquivosNaoIndexados.size(); i++){
		//QName SERVICE_NAME = new QName("http://tempuri.org/", "Calculator");

			try{
			//	CalculatorSoap_CalculatorSoap12_Client.SOAP_TESTE(args);
			}
		catch (Exception e){
				//Utils.createFolder("Felipe Twitch", "parametros incorretos");
				//Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/Felipe Twitch/Felipe Teste", "0900069f800b99ba"));
				//Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/Felipe Twitch/parametros incorretos", "0900069f800b99ba"));
				//break;
			}
			/*
			try{
				validarParametrosDaReq(retornoReq);
			}
			catch (Exception e){
				//Utils.createFolder("Felipe Twitch", "Formatos incorretos");
				//Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/Felipe Twitch/Felipe Teste", "0900069f800b99ba"));
				//Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/Felipe Twitch/Formatos incorretos", "0900069f800b99ba"));
				break;
			}
			try{
				Utils.createFolder("Felipe Twitch", "PASTA COM NOME DO ARQUIVO FORMATADO");
				Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/Felipe Twitch/Felipe Teste", "0900069f800b99ba"));
				Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/Felipe Twitch/Formatos incorretos", "0900069f800b99ba"));
			}
			catch (Exception e){
				continue;
			}
			
			lib.requisicao(CriarAgenda);
				
	}
	*/

	}
}		