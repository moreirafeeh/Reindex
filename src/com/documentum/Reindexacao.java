package com.documentum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.documentum.ObjectsParam.Querys;
import com.documentum.fc.common.DfException;
import com.documentum.type.DocumentumReindexacao;

public class Reindexacao {

	private RepositoryDocumentum documentumRepository;

	public Reindexacao(RepositoryDocumentum documentumRepository) {
		this.documentumRepository = documentumRepository;
	}

	/**
	 * Este Método é responsavel por Buscar os nomes dos arquivos na Pasta "Não Indexados"
	 * 
	 * @since 29/09/2020
	 */
	@SuppressWarnings("finally")
	public ArrayList<DocumentumReindexacao> BuscaPasta(String path) {
		ArrayList<DocumentumReindexacao> array = new ArrayList<DocumentumReindexacao>();

		try {
			 array = this.documentumRepository.BuscaArquivosPasta(path);

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
	public ArrayList<DocumentumReindexacao> filtrarArquivos(ArrayList<DocumentumReindexacao> arquivosNaoIndexadosFiltro) {

		ArrayList<DocumentumReindexacao> arquivosNaoIndexados = new ArrayList<DocumentumReindexacao>();

		int i = 0;
		while (i < arquivosNaoIndexadosFiltro.size()) {

			String[] params = arquivosNaoIndexadosFiltro.get(i).getNome().split("_");

			for (String param : params) {
//				System.out.println(param + " " + (Utils.validaSinistro(param) || Utils.validaProtocolo(param)));
				// Remove a extensão do arquivo.
				param = FilenameUtils.removeExtension(param);

				// Validação de Sinistro e Protocolo
				if (Utils.validaSinistro(param) || Utils.validaProtocolo(param)) {
					arquivosNaoIndexados.add(arquivosNaoIndexadosFiltro.get(i));
					break;
				}

			}

			// Caso o arquivo não seja adicionado na lista de arquivos valido
			// ele é enviado pra arquivos de parametros invalidos.
			if (arquivosNaoIndexados.size() > 0) {
				DocumentumReindexacao ultimaPosicaoArray = arquivosNaoIndexados.get(arquivosNaoIndexados.size() - 1);
				if (!ultimaPosicaoArray.equals(arquivosNaoIndexadosFiltro.get(i))) {
					try {
						documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/ParametrosIncorretos",arquivosNaoIndexadosFiltro.get(i).getNome(),"/teste_pasta_reindex/Nao_Indexados_TESTE"));
						documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",arquivosNaoIndexadosFiltro.get(i).getNome()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else {
				try {
					documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK("/teste_pasta_reindex/ParametrosIncorretos",arquivosNaoIndexadosFiltro.get(i).getNome(),"/teste_pasta_reindex/Nao_Indexados_TESTE"));
					documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK("/teste_pasta_reindex/Nao_Indexados_TESTE",arquivosNaoIndexadosFiltro.get(i).getNome()));
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
	public void movimentarParaPastas(ArrayList<DocumentumReindexacao> arquivosNaoIndexados) {

		for (DocumentumReindexacao params : arquivosNaoIndexados) {

			String[] DocumentoSplitado = params.getNome().split("_");


			Map<String, String> listaRetornoWM = returnoWM(DocumentoSplitado[1]);

			try {
				if (listaRetornoWM.get("reindexa").equals("sim")) {

					validaPasta(listaRetornoWM);

					documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK_ID("/teste_pasta_reindex/" + listaRetornoWM.get("sinistro") + "/" + listaRetornoWM.get("expediente"),params.getId(),"/teste_pasta_reindex/Nao_Indexados_TESTE"));
					documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK_ID("/teste_pasta_reindex/Nao_Indexados_TESTE",params.getId()));
					documentumRepository.ConsultarQueryUPDATE(Querys.deleteValorTabelaRegistrada(params.getId()));

				} else {

					this.controleDeRegras(params);

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
	public void controleDeRegras(DocumentumReindexacao arquivosSemPasta) {
		// Registro JOAO depende do web service = false ----

		/*
		 * Para cada arquivo que passou o filtro de arquivos fora do padrão e
		 * validado se o arquivo está registrado em uma das pastas. Caso não
		 * seja encontrado é criado o registro na pasta de
		 */
		try {
			System.out.println("------ARQUIVo: " + arquivosSemPasta.getNome() + "------");
			ArrayList<String> arrayRegistroNome = this.documentumRepository.ConsultarQuery(Querys.selectTabelaRegistroNomeEspecifico(arquivosSemPasta.getNome()));
			System.out.println("------SELECT DO NOME: " + arrayRegistroNome.get(0)+ "------");
			int diasProcessado = Utils.diasProcessados(arrayRegistroNome.get(0).toString());
			System.out.println("------DIAS PROCESSADOS: " + diasProcessado + "------");
			
				
				if (diasProcessado >= 5) {
					documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK_ID("/teste_pasta_reindex/Regras" ,arquivosSemPasta.getNome(),"/teste_pasta_reindex/Nao_Indexados_TESTE"));
					documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK_ID("/teste_pasta_reindex/Nao_Indexados_TESTE",arquivosSemPasta.getNome()));
				}
				
				if (diasProcessado >= 10) {
					documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_LINK_ID("/teste_pasta_reindex/Expurgo" ,arquivosSemPasta.getId(),"/teste_pasta_reindex/Regras"));
					documentumRepository.ConsultarQueryUPDATE(Querys.UPDATE_UNLINK_ID("/teste_pasta_reindex/Regras",arquivosSemPasta.getId()));
					documentumRepository.ConsultarQueryUPDATE(Querys.deleteValorTabelaRegistrada(arquivosSemPasta.getId()));
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
		System.out.println(sinistro);

	
		if (sinistro.equals("3021720000331")) {
			// RETORNO DA WM A VARIAVEL "wsdl" FOR TRUE
			String sinsitro;
			String expediente;

			sinsitro = "S3021720000331";
			expediente = "TRC03";

			listRetornoWD.put("sinistro", new String(sinsitro));
			listRetornoWD.put("expediente", new String(expediente));
			listRetornoWD.put("reindexa", new String("sim"));

		} else if(sinistro.equals("3031720000333")) {
			
			// RETORNO DA WM A VARIAVEL "wsdl" FOR TRUE
			String sinsitro;
			String expediente;

			sinsitro = "S3031720000333";
			expediente = "TRC03";

			listRetornoWD.put("sinistro", new String(sinsitro));
			listRetornoWD.put("expediente", new String(expediente));
			listRetornoWD.put("reindexa", new String("sim"));
			
		}else if(sinistro.equals("3021730000344")) {
			
			// RETORNO DA WM A VARIAVEL "wsdl" FOR TRUE
			String sinsitro;
			String expediente;

			sinsitro = "S3021730000344";
			expediente = "TRC03";

			listRetornoWD.put("sinistro", new String(sinsitro));
			listRetornoWD.put("expediente", new String(expediente));
			listRetornoWD.put("reindexa", new String("sim"));
		} else {
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
				if (documentumRepository.validaPastaSinistro(retornoWMLista.get(key))) {

					if (documentumRepository.validaPastaExpediente(retornoWMLista.get(key))) {
						System.out.println();
					} else {

						String nomeFolder = retornoWMLista.get("expediente");
						String pathFolder = "/teste_pasta_reindex/"
								+ retornoWMLista.get("sinistro");
						documentumRepository.criarPasta(nomeFolder, pathFolder);

					}

				} else {

					String nomeFolderSinistro = retornoWMLista.get(key);
					String pathFolderCabinet = "/teste_pasta_reindex";

					documentumRepository.criarPasta(nomeFolderSinistro,pathFolderCabinet);

					String nomeFolder = retornoWMLista.get("expediente");
					String pathFolder = "/teste_pasta_reindex/"
							+ retornoWMLista.get("sinistro");

					documentumRepository.criarPasta(nomeFolder, pathFolder);
					
					if (documentumRepository.validaPastaSinistro(retornoWMLista.get("expediente"))) {

						if (documentumRepository.validaPastaExpediente(retornoWMLista.get("sinistro"))) {
							System.out.println("Pasta criada com sucesso.");
						}
					}

				}

			}
		}

		return true;

	}

}
