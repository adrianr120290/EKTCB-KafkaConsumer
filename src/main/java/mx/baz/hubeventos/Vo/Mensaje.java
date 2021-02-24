package mx.baz.hubeventos.Vo;

public class Mensaje {

	private String contenidoMensaje;
	private String codRespuesta;
	private String desRespuesta;
	
	public Mensaje() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getContenidoMensaje() {
		return contenidoMensaje;
	}

	public void setContenidoMensaje(String contenidoMensaje) {
		this.contenidoMensaje = contenidoMensaje;
	}

	public String getCodRespuesta() {
		return codRespuesta;
	}

	public void setCodRespuesta(String codRespuesta) {
		this.codRespuesta = codRespuesta;
	}

	public String getDesRespuesta() {
		return desRespuesta;
	}

	public void setDesRespuesta(String desRespuesta) {
		this.desRespuesta = desRespuesta;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mensaje [contenidoMensaje=");
		builder.append(contenidoMensaje);
		builder.append(", codRespuesta=");
		builder.append(codRespuesta);
		builder.append(", desRespuesta=");
		builder.append(desRespuesta);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
