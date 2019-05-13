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
		//String criarPastaPrincipal = "mkdir /codigos ; chmod 777 /codigos;";
		String irPasta = "cd /codigos ;";
		
		//execCommand("cd ../codigos");
		
		String diretorios = execCommand("ls -m /");
		
		if(containsPaste(diretorios.split(","), "codigos")) {
			
			comando.append(irPasta);
			diretorios = execCommand(comando.toString()+"ls -m;");
			if(containsPaste(diretorios.split(","), Integer.toString(idUsuario))) {
				return compilaCodigo(comando, idUsuario, codigo);
			}else {
				execCommand(comando.toString()+"mkdir "+idUsuario+";"); 
				return compilaCodigo(comando, idUsuario, codigo);
			}
		}else {
			return "Servidor não condigurado, entre em contato com o administrador.";
		}
	}
	
	private Boolean containsPaste(String[] diretorios, String nomePasta) {
		for(int i = 0 ; i < diretorios.length ; i++) {
			if(diretorios[i].trim().equals(nomePasta.trim())) {
				return true;
			}
		}
		return false;
	}
	
	private String compilaCodigo(StringBuilder comando, int idUsuario, String codigo) {
		String criarAquivo = "echo '"+codigo+"' >> tmp.cpp;";
		String gpp = "g++ tmp.cpp -o tmp;";
		String runcode = "./tmp;";
		String del = "rm tmp tmp.cpp -f;";

		comando.append("cd "+idUsuario+";");
		comando.append(criarAquivo);
		comando.append(gpp);
		comando.append(runcode);
		comando.append(del);
		return execCommand(comando.toString());
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
