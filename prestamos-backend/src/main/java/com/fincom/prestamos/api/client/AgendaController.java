package com.fincom.prestamos.api.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincom.prestamos.model.service.VisitaService;
import com.fincom.prestamos.model.to.Visita;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {
	@Autowired
	private VisitaService visitaService;

    
    @PostMapping("/agendar")
    public String agendarVisita(@RequestBody Visita nuevaVisita) {
        if (visitaService.agendarVisita(nuevaVisita)) {
            return "Visita agendada con éxito.";
        } else {
            return "Error al agendar la visita. Verifica las validaciones.";
        }
    }

    // Endpoint para obtener la agenda
    @GetMapping("/agenda")
    public List<Visita> obtenerAgenda() {
        return visitaService.obtenerAgenda();
    }
}
