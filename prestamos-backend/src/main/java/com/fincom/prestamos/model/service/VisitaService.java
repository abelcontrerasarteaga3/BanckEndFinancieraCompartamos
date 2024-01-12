package com.fincom.prestamos.model.service;

import java.util.List;

import com.fincom.prestamos.model.to.Visita;

public interface VisitaService {
	List<Visita> obtenerAgenda();

    boolean agendarVisita(Visita nuevaVisita);
}
