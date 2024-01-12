package com.fincom.prestamos.model.to;

public class Agenda {
	
	private String documento;
	private String fechaVisita;
	private String horaVisita;
	private String telefono;
	private String usuario;
	
	
	
	public Agenda(String documento, String fechaVisita, String horaVisita, String telefono, String usuario) {
		super();
		this.documento = documento;
		this.fechaVisita = fechaVisita;
		this.horaVisita = horaVisita;
		this.telefono = telefono;
		this.usuario = usuario;
	}
	public Agenda() {
		super();
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getFechaVisita() {
		return fechaVisita;
	}
	public void setFechaVisita(String fechaVisita) {
		this.fechaVisita = fechaVisita;
	}
	public String getHoraVisita() {
		return horaVisita;
	}
	public void setHoraVisita(String horaVisita) {
		this.horaVisita = horaVisita;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
}
