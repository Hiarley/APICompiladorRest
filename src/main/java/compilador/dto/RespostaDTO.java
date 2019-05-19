package compilador.dto;

public class RespostaDTO {
	String codigo;
	String resultadoResposta;
	String resultadoTestes;
	String mensagem;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getResultadoResposta() {
		return resultadoResposta;
	}

	public void setResultadoResposta(String resultadoResposta) {
		this.resultadoResposta = resultadoResposta;
	}

	public String getResultadoTestes() {
		return resultadoTestes;
	}

	public void setResultadoTestes(String resultadoTestes) {
		this.resultadoTestes = resultadoTestes;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
