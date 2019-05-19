package compilador.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import compilador.dto.MensagemDTO;
import compilador.dto.RespostaDTO;

@Stateless
@Path("/comando")
public class CompiladorService {
	private Runtime run = Runtime.getRuntime();
	private Process pro;
	private String cMaker = "cmake_minimum_required(VERSION 2.6)\r\n" + 
			" \r\n" + 
			"# Locate GTest\r\n" + 
			"find_package(GTest REQUIRED)\r\n" + 
			"include_directories(${GTEST_INCLUDE_DIRS})\r\n" + 
			" \r\n" + 
			"# Link runTests with what we want to test and the GTest and pthread library\r\n" + 
			"add_executable(runTests teste.cpp)\r\n" + 
			"target_link_libraries(runTests ${GTEST_LIBRARIES} pthread)";
	private BufferedReader read;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/compilar")
	public RespostaDTO compilar(MensagemDTO dto) {
		StringBuilder comando = new StringBuilder();
		String irPasta = "cd /codigos ;";

		String diretorios = execCommand("ls -m /");
		RespostaDTO respostaDTO = new RespostaDTO();

		
		if (containsPaste(diretorios.split(","), "codigos")) {

			comando.append(irPasta);
			diretorios = execCommand(comando.toString() + "ls -m;");
			if (!containsPaste(diretorios.split(","), Integer.toString(dto.getIdUsuario()))) {
				criarPasta(comando.toString(), String.valueOf(dto.getIdUsuario()));
			}
			comando.append("cd " + dto.getIdUsuario() + ";");
			salvaArquivo(comando.toString(), "tmp.cpp", dto.getCodigoResposta());
			salvaArquivo(comando.toString(), "teste.cpp", montarTeste(dto));
			diretorios = execCommand(comando.toString());
			if(!containsPaste(diretorios.split(","), "CMakeLists.txt")) {
				salvaArquivo(comando.toString(), "CMakeLists.txt", cMaker);
			}
			respostaDTO.setResultadoTestes(execCommand(comando.toString()+"cmake CMakeLists.txt; make; ./runTests;"));	
			execCommand(comando.toString()+"cd ..; rm -r -f "+dto.getIdUsuario()+";");
			


		} else {
			respostaDTO.setMensagem("Servidor não condigurado, entre em contato com o administrador.");
		}
		return respostaDTO;
	}
	
	private String montarTeste(MensagemDTO dto) {
		StringBuilder codigoTeste = new StringBuilder();
		codigoTeste.append("#include \"tmp.cpp\"\n");
		codigoTeste.append("#include <gtest/gtest.h>\n\n");
		if(dto.getEntradasPositiva() != null && dto.getSaidasPositiva() != null) {
			codigoTeste.append(construtorTeste(dto.getNomeMetodo(), dto.getEntradasPositiva(), dto.getSaidasPositiva(), "PositiveNos"));
			codigoTeste.append("\n\n");
		}
		if(dto.getEntradasNegativa() != null && dto.getSaidasNegativa() != null) {
			codigoTeste.append(construtorTeste(dto.getNomeMetodo(), dto.getEntradasNegativa(), dto.getSaidasNegativa(), "NegativeNos"));
			codigoTeste.append("\n\n");
		}
		codigoTeste.append("int main(int argc, char **argv) {\r\n" + 
				"    testing::InitGoogleTest(&argc, argv);\r\n" + 
				"    return RUN_ALL_TESTS();\r\n" + 
				"}");
		
		return codigoTeste.toString();

	}

	private String construtorTeste(String nomeMetodo,String[] todasEntradas, String[] todasSaidas, String typeNos) {
		StringBuilder codigoTeste = new StringBuilder();
		codigoTeste.append("TEST(SquareRootTest, "+typeNos+") { \n");
		String[] parametrosMetodo;
		int inicio = nomeMetodo.lastIndexOf("(") + 1;
		int fim = nomeMetodo.lastIndexOf(")");
		parametrosMetodo = nomeMetodo.substring(inicio, fim).split(",");
		int numEntradas = todasEntradas.length;
		for (int i = 0; i < numEntradas; i++) {
			codigoTeste.append("ASSERT_EQ(" + todasSaidas[i] + "," + nomeMetodo.substring(0, inicio));
			String[] entradas = todasEntradas[i].split(",");
 			for (int j = 0 ; j < entradas.length ; j++) {
				
				if (parametrosMetodo[j].trim().contains("int") || parametrosMetodo[j].trim().contains("short")
						|| parametrosMetodo[j].trim().contains("double") || parametrosMetodo[j].trim().contains("float")
						|| parametrosMetodo[j].trim().contains("bool")) {
					codigoTeste.append(entradas[j]);
				} else if(parametrosMetodo[j].trim().contains("char")){
					codigoTeste.append("'"+entradas[j]+"'");					
				}else {
					codigoTeste.append("\""+entradas[j]+"\"");
				}
				if(j == entradas.length-1) {
					codigoTeste.append("));\n");
				}else {
					codigoTeste.append(",");
				}
			}
		}
		codigoTeste.append("}\n\n");

		return codigoTeste.toString();
	}

	private Boolean containsPaste(String[] diretorios, String nomePasta) {
		for (int i = 0; i < diretorios.length; i++) {
			if (diretorios[i].trim().equals(nomePasta.trim())) {
				return true;
			}
		}
		return false;
	}
	private String criarPasta(String comando, String nomePasta) {
		return execCommand(comando+"mkdir "+nomePasta+";");
	}
	
	private String salvaArquivo(String comando, String nomeArquivo, String conteudo) {
		String criarAquivo = "echo '" + conteudo + "' >> "+nomeArquivo+";";
		return execCommand(comando+criarAquivo);
	}

	private String compilaCodigo(String comando) {
		String gpp = "g++ tmp.cpp -o tmp;";
		String runcode = "./tmp;";
		return execCommand(comando.toString()+gpp+runcode);
	}

	private String execCommand(String command) {
		// String[] array = {"cmd", "/c", String.join("& ", command)}; // Windows
		String[] array = { "/bin/bash", "-c", "cd /codigos;" + command };

		try {
			ProcessBuilder builder = new ProcessBuilder(array);
			builder.redirectErrorStream(true);

			Process p = builder.start();

			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			StringBuilder res = new StringBuilder();
			while (true) {
				line = r.readLine();
				if (line == null) {
					break;
				}
				res.append(line);
			}
			return res.toString();
		} catch (Exception e) {
			System.err.println(e);
			return e.getMessage();
		}
	}
	
	

}
