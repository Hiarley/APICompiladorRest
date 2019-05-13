package compilador.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/comando")
public class CompiladorService {
	private	Runtime run = Runtime.getRuntime();
    private Process pro;
    private BufferedReader read;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/compilar/{idUsuario}")
	public String compilar(@PathParam("idUsuario") int idUsuario ,String codigo) {
		StringBuilder comando = new StringBuilder();
		String criarPastaPrincipal = "mkdir /codigos ; chmod 777 /codigos;";
		String irPasta = "cd /codigos ;";
		String nomeArquivo = "";
		String criarAquivo = "echo '"+codigo+"' >> "+nomeArquivo+";";
		String gpp = "g++ hello.cpp -o hello";
		String runcode = "./hello";
		String del = "rm hello";
		execCommand("cd ../codigos");
		
		String diretorios = execCommand("ls -m /");
		
		if(containsPaste(diretorios.split(","), "codigos")) {
			
			comando.append(irPasta);
			diretorios = execCommand(irPasta+"ls -m;");
			if(containsPaste(diretorios.split(","), Integer.toString(idUsuario))) {
				
			}else {
				String teste = execCommand(comando.toString()+"mkdir "+idUsuario+";"); 
				comando.append("cd "+idUsuario+";");
			}
		}else {
			String teste = execCommand(criarPastaPrincipal);
			System.err.println(teste);
		}
		return execCommand("pwd");
	}
	
	private Boolean containsPaste(String[] diretorios, String nomePasta) {
		for(int i = 0 ; i < diretorios.length ; i++) {
			if(diretorios[i].trim().equals(nomePasta.trim())) {
				return true;
			}
		}
		return false;
	}

		
	
	private String execCommand(String command) {
		//String[] array = {"cmd", "/c",  String.join("& ", command)}; // Windows
		String[] array = {"/bin/bash", "-c","cd /codigos;"+command};

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
		} catch(Exception e) {
			System.err.println(e);
			return "";
		}
	}

}
