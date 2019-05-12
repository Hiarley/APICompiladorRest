package compilador.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
	@Path("/compilar/")
	public String compilar( String codigo) {
		String gpp = "g++ hello.cpp -o hello";
		String runcode = "./hello";
		String del = "rm hello";
		
		
		criarArquivo(codigo);
		
		//execCommand("dir");
		
		
		
		
		return execCommand("dir");
	}
	
	public String criarArquivo(String codigo) {
		return "";
	}

		
	
	public static String execCommand(String command) {
		//String[] array = {"cmd", "/c",  String.join("& ", command)}; // Windows
		String[] array = {"/bin/bash", "-c", command};

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
