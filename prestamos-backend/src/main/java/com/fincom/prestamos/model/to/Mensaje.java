package com.fincom.prestamos.model.to;

public class Mensaje {

	private String mensaje;
	private boolean flag;

	public Mensaje() {
		super();
	}

	public Mensaje(String mensaje, boolean flag) {
		super();
		this.mensaje = mensaje;
		this.flag = flag;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
