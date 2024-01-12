package com.fincom.prestamos.model.to;

import java.sql.Date;

public class Visita {
	private Date fecha;
	private String hora;
	private String telefono;
	private Usuario usuario;

	public Visita(Date fecha, String hora, String telefono, Usuario usuario) {

		this.fecha = fecha;
		this.hora = hora;
		this.telefono = telefono;
		this.usuario = usuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
