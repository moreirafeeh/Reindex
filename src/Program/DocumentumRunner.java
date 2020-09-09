package Program;

import org.tempuri.CalculatorSoap_CalculatorSoap12_Client;

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
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.documentum.UtilsDocumentum;
import com.documentum.ObjectsParam.Querys;

import org.tempuri.Calculator;
import org.tempuri.CalculatorSoap;

public class DocumentumRunner {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		

		UtilsDocumentum Utils = new UtilsDocumentum();

		
		
		//FLUXO REINDEXAÇÂO--------------
		
		
		/// limpa os arquivos com parametros zerados ==
		
		String nameinvalid = "000000000000000_00000000000000000_0000000_00000000000000";
		Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/Lucas Vidotti/ParametrosIncorretos",nameinvalid));
		Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/Sinistros Autos/Não Indexados",nameinvalid));
		
		///--------------------------------------------------------------------
		
		/// joga o object_name dos arquivos no array=== 
		ArrayList<String> arquivosNaoIndexados;
		arquivosNaoIndexados = Utils.ConsultarQuery(Querys.PastaParaArquivo("/Sinistros Autos/Não Indexados"));
		///--------------------------------------------------------------------
		
		/// consultando web service ==
		for(String params: arquivosNaoIndexados){
			String param1=params.substring(4,19);
			String param2=params.substring(20,37); 
			String paramBoard = params.substring(38,45);
			String param4 =  params.substring(46,60);
			
			
//			/// chamada do web service == 
//			QName SERVICE_NAME = new QName("http://tempuri.org/", "Calculator");
//			CalculatorSoap_CalculatorSoap12_Client.SOAP_TESTE(args);// params
			
			//Registro JOAO depende do web service = false ----
			
			//-----------------------------
			
			// movimentacao do lucas ------
			
			//-----------------------------
			
			// movimentacao do felipe == true -----
			
			Utils.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK_ID("/Sinistros Autos/Não Indexados",nameinvalid));
			Utils.ConsultarQueryUPDATE(Querys.UPDATE_LINK_ID("/Lucas Vidotti/ParametrosIncorretos",nameinvalid));
			
			
			//-----------------------------
		}
		//------------------------------------------------------------------
		
		
		
		
		//for(int i = 0; i <= arquivosNaoIndexados.size(); i++){
		//QName SERVICE_NAME = new QName("http://tempuri.org/", "Calculator");

			try{
				CalculatorSoap_CalculatorSoap12_Client.SOAP_TESTE(args);
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
		
		
		
		//-------------------------------

		

