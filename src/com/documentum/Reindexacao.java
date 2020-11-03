package com.documentum;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.documentum.ObjectsParam.Querys;
import com.documentum.fc.common.DfException;
import com.documentum.RepositoryDocumentum;
import com.documentum.Utils;

public class Reindexacao {

	private RepositoryDocumentum documentumRepository;
	private Utils util;

	public Reindexacao(RepositoryDocumentum documentumRepository) {
		this.documentumRepository = documentumRepository;
		this.util = new Utils();
	}

	/**
	 * Este Método é responsavel por Buscar os nomes dos arquivos na Pasta
	 * "Não Indexados"
	 * 
	 * @since 29/09/2020
	 */
	@SuppressWarnings("finally")
	public ArrayList<String> BuscaPasta() {
		ArrayList<String> array = new ArrayList<String>();

		try {
			array = this.documentumRepository
					.BuscaArquivosPasta("/teste_pasta_reindex/Nao_Indexados_TESTE");

		} catch (Exception e) {
			System.out.println("Erro ao buscar arquivos.");
			e.printStackTrace();
		} finally {
			return array;
		}

	}

	/**
	 * Este Método é responsavel por filtrar os arquivos da pasta
	 * "Não Indexados" com parametros incorretos.
	 * 
	 * @since 29/09/2020
	 */
	public ArrayList<String> filtrarArquivos(
			ArrayList<String> arquivosNaoIndexadosFiltro) {

		ArrayList<String> arquivosNaoIndexados = new ArrayList<String>();

		int i = 0;
		while (i < arquivosNaoIndexadosFiltro.size()) {
			System.out.println(i);

			String[] params = arquivosNaoIndexadosFiltro.get(i).split("_");

			for (String param : params) {
				// Remove a extensão do arquivo.
				param = FilenameUtils.removeExtension(param);

				// Validação de Sinistro e Protocolo
				if (util.validaSinistro(param) || util.validaProtocolo(param)) {
					arquivosNaoIndexados.add(arquivosNaoIndexadosFiltro.get(i));
					break;
				}

			}

			// Caso o arquivo não seja adicionado na lista de arquivos valido
			// ele é enviado pra arquivos de parametros invalidos.
			if (arquivosNaoIndexados.size() > 0) {
				String ultimaPosicaoArray = arquivosNaoIndexados
						.get(arquivosNaoIndexados.size() - 1);
				if (ultimaPosicaoArray != arquivosNaoIndexadosFiltro.get(i)) {
					try {
						documentumRepository
								.ConsultarQueryUPDATE(Querys
										.UPDATE_LINK(
												"/teste_pasta_reindex/ParametrosIncorretos",
												arquivosNaoIndexadosFiltro
														.get(i),
												"/teste_pasta_reindex/Nao_Indexados_TESTE"));
						documentumRepository
								.ConsultarQueryUPDATE(Querys
										.UPDATE_UNLINK(
												"/teste_pasta_reindex/Nao_Indexados_TESTE",
												arquivosNaoIndexadosFiltro
														.get(i)));
					} catch (Exception e) {

						e.printStackTrace();
					}
				}

			} else {
				try {
					documentumRepository
							.ConsultarQueryUPDATE(Querys
									.UPDATE_LINK(
											"/teste_pasta_reindex/ParametrosIncorretos",
											arquivosNaoIndexadosFiltro.get(i),
											"/teste_pasta_reindex/Nao_Indexados_TESTE"));
					documentumRepository.ConsultarQueryUPDATE(Querys
							.UPDATE_UNLINK(
									"/teste_pasta_reindex/Nao_Indexados_TESTE",
									arquivosNaoIndexadosFiltro.get(i)));
				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			i = i + 1;
		}

		return arquivosNaoIndexados;

	}

	/**
	 * Este Método é responsavel por Decidir as movimentacoes realizadas por um
	 * arquivo.
	 * 
	 * @since 29/09/2020
	 * @param arquivosNaoIndexados
	 *            : array de arquivosFiltrados.
	 */
	public void movimentarParaPastas(ArrayList<String> arquivosNaoIndexados) {

		for (String params : arquivosNaoIndexados) {

			String[] DocumentoSplitado = params.split("_");

			boolean temPasta;

			Map<String, String> listaRetornoWM = returnoWM(DocumentoSplitado[1]);// params
			// para
			// bater
			// na WM
			// depois
			// !

			try {

				if (listaRetornoWM.get("reindexa").equals("sim")) {

					validaPasta(listaRetornoWM);

					documentumRepository
							.ConsultarQueryUPDATE(Querys.UPDATE_LINK(
									"/teste_pasta_reindex/"
											+ listaRetornoWM.get("sinistro") + "/"+listaRetornoWM.get("expediente") ,
									params,
									"/teste_pasta_reindex/Nao_Indexados_TESTE"));
					documentumRepository.ConsultarQueryUPDATE(Querys
							.UPDATE_UNLINK(
									"/teste_pasta_reindex/Nao_Indexados_TESTE",
									params));

				} else {

					this.controleDePastas(params);

				}

			} catch (Exception e) {
				System.err
						.println("Erro na movimentação do arquivos em pastas.");
				e.printStackTrace();
			}

		}
	}

	/**
	 * Este Método é responsavel por gerenciar arquivos sem pastas.
	 * 
	 * @since 29/09/2020
	 * @param param
	 *            : arquivo
	 */
	public void controleDePastas(String arquivosSemPasta) {
		// Registro JOAO depende do web service = false ----

		ArrayList<String> arquivosNaoIndexadosMenos30;
		ArrayList<String> arquivosNaoIndexadosMais60;

		/*
		 * Para cada arquivo que passou o filtro de arquivos fora do padrão e
		 * validado se o arquivo está registrado em uma das pastas. Caso não
		 * seja encontrado é criado o registro na pasta de
		 */
		try {
			arquivosNaoIndexadosMenos30 = documentumRepository
					.ConsultarQueryData(Querys
							.ArquivoNaoIndexado30(arquivosSemPasta));

			arquivosNaoIndexadosMais60 = documentumRepository
					.ConsultarQueryData(Querys
							.ArquivoNaoIndexado60(arquivosSemPasta));

			if (!arquivosNaoIndexadosMenos30.isEmpty()) {

				int diasProcessado = util
						.diasProcessados(arquivosNaoIndexadosMenos30.get(2)
								.split(" ")[0]);

				/*
				 * Caso o documento tenham sido processados por mais de 30 dias.
				 * Passa pelo processo de mudança de pasta caso atinga o
				 * trigésimo dia.
				 */
				if (diasProcessado >= 31) {
					documentumRepository.ConsultarQueryUPDATE(Querys
							.DELETE(arquivosNaoIndexadosMenos30.get(0)));
					documentumRepository.createPasta60(arquivosSemPasta,
							"date_nao_indexado_60", "", "",
							"/teste_pasta_reindex/Mais60",
							arquivosNaoIndexadosMenos30.get(2).split(" ")[0]);
				}
			}

			if (!arquivosNaoIndexadosMais60.isEmpty()) {

				int diasProcessado = util
						.diasProcessados(arquivosNaoIndexadosMais60.get(2)
								.split(" ")[0]);

				/*
				 * Caso o documento tenham sido processados por mais de 60 dias.
				 * Passa pelo processo de expurgo caso atinga o sexagésimo dia.
				 */
				if (diasProcessado >= 60) {
					documentumRepository.ConsultarQueryUPDATE(Querys
							.UPDATE_LINK("/teste_pasta_reindex/Expurgo", // Pasta
									// de
									// destino
									// do
									// arquivo
									// .
									arquivosSemPasta, // Nome do arquivo.
									"/teste_pasta_reindex/Nao_Indexados_TESTE" // Pasta
							// de
							// origem
							// do
							// arquivo
							// .
							));
					documentumRepository.ConsultarQueryUPDATE(Querys
							.UPDATE_UNLINK(
									"/teste_pasta_reindex/Nao_Indexados_TESTE", // Pasta
									// de
									// origem
									// do
									// arquivo
									// .
									arquivosSemPasta // Nome do arquivo.
							));
					documentumRepository.ConsultarQueryUPDATE(Querys // Apagando
							// o
							// objeto
							// de
							// cocntrole
							// da
							// pasta
							// de 60
							// dias.
							.DELETE(arquivosNaoIndexadosMais60.get(0)));
				}
			}

			/*
			 * Criação do objeto de controle, inicialmente na pasta de 30 dias.
			 */
			if (arquivosNaoIndexadosMenos30.isEmpty()
					&& arquivosNaoIndexadosMais60.isEmpty()) {
				documentumRepository.createPasta30(arquivosSemPasta,
						"date_nao_indexado_30", "", "",
						"/teste_pasta_reindex/Menos30");
			}
		} catch (Exception e) {
			System.err.println("Erro no controle do tempo de vida do arquivo.");
			e.printStackTrace();
		}

	}

	/**
	 * Metodo devolve o retorno da WM
	 * 
	 * 
	 */
	public Map<String, String> returnoWM(String sinistro) {
		// CHAMADA ENDPOINT PARA VALIDAÇÃO DE DADOS.
		// QName SERVICE_NAME = new
		// QName("http://tempuri.org/","Calculator");
		// CalculatorSoap_CalculatorSoap12_Client.SOAP_TESTE(args);// params

		// BATE NO WM

		Map<String, String> listRetornoWD = new HashMap<String, String>();

	
		if (sinistro.equals("301182000093177")) {
			// RETORNO DA WM A VARIAVEL "wsdl" FOR TRUE
			String sinsitro;
			String expediente;

			sinsitro = "S301182000093177";
			expediente = "TRC03";

			listRetornoWD.put("sinistro", new String(sinsitro));
			listRetornoWD.put("expediente", new String(expediente));
			listRetornoWD.put("reindexa", new String("sim"));

		} else if(sinistro.equals("2021720000331")) {
			
			// RETORNO DA WM A VARIAVEL "wsdl" FOR TRUE
			String sinsitro;
			String expediente;

			sinsitro = "S2021720000331";
			expediente = "TRC03";

			listRetornoWD.put("sinistro", new String(sinsitro));
			listRetornoWD.put("expediente", new String(expediente));
			listRetornoWD.put("reindexa", new String("sim"));
			
		}
		
		
		else {
			listRetornoWD.put("reindexa", new String("nao"));
		}

		return listRetornoWD;
	}

	/**
	 * Metodo faz uma validação de pasta de sinistro
	 * 
	 * @param String
	 *            numeroSinistro : numero do sinistro
	 */

	public boolean validaPasta(Map<String, String> retornoWMLista)
			throws DfException {

		for (String key : retornoWMLista.keySet()) {

			if (key.equals(("sinistro"))) {
				if (documentumRepository.validaPastaSinistro(retornoWMLista
						.get(key))) {

					if (documentumRepository
							.validaPastaExpediente(retornoWMLista.get(key))) {

					} else {

						String nomeFolder = retornoWMLista.get("expediente");
						String pathFolder = "/teste_pasta_reindex/"
								+ retornoWMLista.get("sinistro");
						documentumRepository.criarPasta(nomeFolder, pathFolder);

					}

				} else {

					String nomeFolderSinistro = retornoWMLista.get(key);
					String pathFolderCabinet = "/teste_pasta_reindex";

					documentumRepository.criarPasta(nomeFolderSinistro,
							pathFolderCabinet);

					String nomeFolder = retornoWMLista.get("expediente");
					String pathFolder = "/teste_pasta_reindex/"
							+ retornoWMLista.get("sinistro");

					documentumRepository.criarPasta(nomeFolder, pathFolder);

				}

			}
		}

		return true;

	}

}
