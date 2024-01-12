package com.fincom.prestamos.model.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.fincom.prestamos.model.service.VisitaService;
import com.fincom.prestamos.model.to.Visita;

@Service
public class VisitaServiceImpl implements VisitaService {
	/*
	String listar = "https://webapicalendar.azurewebsites.net/api/persona/lista";
	String obtenerPersona = "https://webapicalendar.azurewebsites.net/api/persona/{documento}";
	String buscarAgenda = "https://webapicalendar.azurewebsites.net/api/agenda/{codigoUsuario}";
	String registrarAgenda = "https://webapicalendar.azurewebsites.net/api/agenda/registrar";
	HttpClient httpClient = HttpClients.createDefault();
	
	Los servicios a utilizar en los metodos.
	
	*/
	private List<Visita> agenda = new ArrayList<>();
	
	@Override
	public List<Visita> obtenerAgenda() {
		return agenda;
	}

	@Override
	public boolean agendarVisita(Visita nuevaVisita) {
		// Validar horario permitido
        if (!validarHorario(nuevaVisita.getHora())) {
            return false;
        }

        // Validar conflicto con otras visitas del mismo usuario
        if (!validarConflicto(nuevaVisita)) {
            return false;
        }

        // agregar la nueva visita
        agenda.add(nuevaVisita);
        return true;
	}
	private boolean validarHorario(String hora) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date horaVisita = sdf.parse(hora);
            Date horaInicio = sdf.parse("08:00");
            Date horaFin = sdf.parse("18:00");

            return horaVisita.after(horaInicio) && horaVisita.before(horaFin);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validar conflicto con otras visitas del mismo usuario
    private boolean validarConflicto(Visita nuevaVisita) {
        for (Visita visita : agenda) {
            if (nuevaVisita.getUsuario().equals(visita.getUsuario())) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date horaNuevaVisita = sdf.parse(nuevaVisita.getHora());
                    Date horaExistente = sdf.parse(visita.getHora());
                    Date horaFinExistente = sumarMinutos(horaExistente, 30);

                    if ((horaNuevaVisita.after(horaExistente) && horaNuevaVisita.before(horaFinExistente)) ||
                            (horaExistente.after(horaNuevaVisita) && horaExistente.before(sumarMinutos(horaNuevaVisita, 30)))) {
                        return false; // Conflicto de horario
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true; 

        
    }

    private Date sumarMinutos(Date fecha, int minutos) {
        long tiempo = fecha.getTime();
        tiempo += minutos * 60 * 1000;
        return new Date(tiempo);
    }
}
