package com.fincom.prestamos.model.service;

import java.util.List;

import com.fincom.prestamos.model.to.Agenda;
import com.fincom.prestamos.model.to.Mensaje;
import com.fincom.prestamos.model.to.Response;
import com.fincom.prestamos.model.to.Visita;

public interface VisitaService {
	List<Visita> obtenerAgenda(String usuario);

	Response agendarVisita(Agenda nuevaVisita);
}
