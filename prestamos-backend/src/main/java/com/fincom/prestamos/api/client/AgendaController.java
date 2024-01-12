package com.fincom.prestamos.api.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.fincom.prestamos.model.service.VisitaService;
import com.fincom.prestamos.model.to.Agenda;
import com.fincom.prestamos.model.to.Mensaje;
import com.fincom.prestamos.model.to.Response;
import com.fincom.prestamos.model.to.Visita;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {
	@Autowired
	private VisitaService visitaService;

	@PostMapping("/agendarNuevaVisita")
	public ResponseEntity<?> agendarVisita(@RequestBody Agenda request) {
		Response mensaje = new Response();
		try {
			 mensaje = visitaService.agendarVisita(request);
			return new ResponseEntity<>(mensaje, HttpStatus.OK);
		} catch (Exception e) {
			mensaje.setMessage(e.getMessage());
			mensaje.setSuccess(false);
			return new ResponseEntity<>(mensaje, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/ObtenerAgenda/{usuario}")
	public ResponseEntity<List<Visita>> listarCentro(@PathVariable String usuario) {
		List<Visita> listaResultado = null;
		try {
			listaResultado = visitaService.obtenerAgenda(usuario);
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return new ResponseEntity<>(listaResultado, HttpStatus.OK);
	}
}
