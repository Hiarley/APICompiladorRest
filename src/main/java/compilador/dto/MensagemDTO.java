package compilador.dto;

public class MensagemDTO {
	int idUsuario;
	String nomeMetodo;
	String codigoResposta;
	String[] entradasPositiva;
	String[] saidasPositiva;
	String[] entradasNegativa;
	String[] saidasNegativa;

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNomeMetodo() {
		return nomeMetodo;
	}

	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}

	public String getCodigoResposta() {
		return codigoResposta;
	}

	public void setCodigoResposta(String codigoResposta) {
		this.codigoResposta = codigoResposta;
	}

	public String[] getEntradasPositiva() {
		return entradasPositiva;
	}

	public void setEntradasPositiva(String[] entradasPositiva) {
		this.entradasPositiva = entradasPositiva;
	}

	public String[] getSaidasPositiva() {
		return saidasPositiva;
	}

	public void setSaidasPositiva(String[] saidasPositiva) {
		this.saidasPositiva = saidasPositiva;
	}

	public String[] getEntradasNegativa() {
		return entradasNegativa;
	}

	public void setEntradasNegativa(String[] entradasNegativa) {
		this.entradasNegativa = entradasNegativa;
	}

	public String[] getSaidasNegativa() {
		return saidasNegativa;
	}

	public void setSaidasNegativa(String[] saidasNegativa) {
		this.saidasNegativa = saidasNegativa;
	}

}



