package com.fincom.prestamos.model.to;

public class Visita {
	private String id;
	private String documento;
	private String fechaVisita;
	private String horaVisita;
	private String fechaHoraVisita;
	private String telefono;
	private String usuario;
	private String fechaModificacion;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getFechaHoraVisita() {
		return fechaHoraVisita;
	}
	public void setFechaHoraVisita(String fechaHoraVisita) {
		this.fechaHoraVisita = fechaHoraVisita;
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
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	@Override
	public String toString() {
		return "Visita [id=" + id + ", documento=" + documento + ", fechaVisita=" + fechaVisita + ", horaVisita="
				+ horaVisita + ", fechaHoraVisita=" + fechaHoraVisita + ", telefono=" + telefono + ", usuario="
				+ usuario + ", fechaModificacion=" + fechaModificacion + "]";
	}
	
	
}
