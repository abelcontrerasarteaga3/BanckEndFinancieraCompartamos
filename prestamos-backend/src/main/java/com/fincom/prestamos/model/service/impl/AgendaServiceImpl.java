package com.fincom.prestamos.model.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fincom.prestamos.model.service.VisitaService;
import com.fincom.prestamos.model.to.Agenda;
import com.fincom.prestamos.model.to.Mensaje;
import com.fincom.prestamos.model.to.Response;
import com.fincom.prestamos.model.to.Visita;

@Service
public class AgendaServiceImpl implements VisitaService {

	@Override
	public List<Visita> obtenerAgenda(String usuario) {
		List<Visita> visitas = null;
		ObjectMapper objectMapper = new ObjectMapper();
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			String url = "https://webapicalendar.azurewebsites.net/api/agenda/" + usuario;
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Content-Type", "application/json");
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity responseEntity = response.getEntity();
			String responseBody = EntityUtils.toString(responseEntity);
			System.out.println(responseBody);
			// return responseBody;
			visitas = objectMapper.readValue(responseBody, new TypeReference<List<Visita>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return visitas;

	}

	@Override
	public Response agendarVisita(Agenda nuevaVisita) {
		Response mensaje=new Response();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://webapicalendar.azurewebsites.net/api/agenda/registrar");
		try {
			List<Visita> visitas = obtenerAgendas(nuevaVisita.getUsuario());
			mensaje=validarHorarioAgendas(nuevaVisita);
			if(!mensaje.isSuccess()) {
				return mensaje;
			}
			mensaje = validarConflictosAgendas(nuevaVisita);
			if(!mensaje.isSuccess()) {
				return mensaje;
			}
			httpPost.setHeader("Content-Type", "application/json");
			String json = objectMapper.writeValueAsString(nuevaVisita);
			httpPost.setEntity(new StringEntity(json));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				String responseString = EntityUtils.toString(entity);
				System.out.print(responseString);
				mensaje = objectMapper.readValue(responseString, Response.class);
				if(mensaje.isSuccess()) {
					mensaje.setMessage("Agendado correctemente");
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mensaje;
	}

	private Response validarConflictosAgendas(Agenda nuevaVisit) {
		
		List<Visita> visitas = obtenerAgendas(nuevaVisit.getUsuario());
		
		Response mensaje=new Response();
		mensaje.setSuccess(true);
		for (int i = 0; i < visitas.size(); i++) {
				Visita visita1 = visitas.get(i);
				if (visita1.getUsuario().equals(nuevaVisit.getUsuario())) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						Date inicioVisita1 = sdf.parse(visita1.getFechaHoraVisita());
						Date finVisita1 = sumarMinutos(inicioVisita1, 30);

						SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						Date inicioVisita2 = sdfx.parse(nuevaVisit.getHoraVisita());
						Date finVisita2 = sumarMinutos(inicioVisita2, 30);

						// Verificar conflicto en el rango de tiempo
						if ((inicioVisita1.before(finVisita2) && finVisita1.after(inicioVisita2))
								|| (inicioVisita2.before(finVisita1) && finVisita2.after(inicioVisita1))) {
							mensaje.setMessage("Hay cruce de horarios");
							mensaje.setSuccess(false);
						}
					} catch (ParseException e) {
						e.printStackTrace();
						mensaje.setMessage(e.getMessage());
						mensaje.setSuccess(false);
					}
				}
			
		}
		return mensaje;
	}

	private Response validarHorarioAgendas(Agenda nuevaVisit) {
		Response mensaje=new Response();
		mensaje.setSuccess(true);
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date fechaHoraVisita = sdf.parse(nuevaVisit.getHoraVisita());
				if (fechaHoraVisita.getHours() < 8 || fechaHoraVisita.getHours() >= 18) {
					mensaje.setMessage("No se puede registrar, se encuentra fuera de horario");
					mensaje.setSuccess(false);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				mensaje.setMessage(e.getMessage());
				mensaje.setSuccess(false);
			}
		
		return mensaje;
	}

	private Date sumarMinutos(Date fecha, int minutos) {
		long tiempo = fecha.getTime();
		tiempo += minutos * 60 * 1000;
		return new Date(tiempo);
	}

	private List<Visita> obtenerAgendas(String usuario) {
		List<Visita> visitas = null;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			String url = "https://webapicalendar.azurewebsites.net/api/agenda/" + usuario;
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Content-Type", "application/json");
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity responseEntity = response.getEntity();
			String responseBody = EntityUtils.toString(responseEntity);
			System.out.println(responseBody);
			// return responseBody;
			visitas = objectMapper.readValue(responseBody, new TypeReference<List<Visita>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return visitas;
	}

	private String listarPersonas() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://webapicalendar.azurewebsites.net/api/persona/lista");
		try {
			httpPost.setHeader("Content-Type", "application/json");
			String json = "[{\"param1\":\"value1\",\"param2\":\"value2\"}]";
			httpPost.setEntity(new StringEntity(json));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String responseString = EntityUtils.toString(entity);
				System.out.println("Response: " + responseString);
				return responseString;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String registrarAgenda(Agenda agenda) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("https://webapicalendar.azurewebsites.net/api/agenda/registrar");
		try {
			httpPost.setHeader("Content-Type", "application/json");

			String json = objectMapper.writeValueAsString(agenda);
			httpPost.setEntity(new StringEntity(json));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String responseString = EntityUtils.toString(entity);
				System.out.println("Response: " + responseString);
				return responseString;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
/*
	  public static void main(String arg[]) 
	 { Agenda agenda = new Agenda();
	 agenda.setDocumento("document"); 
	 agenda.setFechaVisita("12/01/2024");
	 agenda.setHoraVisita("09:00:00"); 
	 agenda.setTelefono("978282450");
	 agenda.setUsuario("abel"); 
	 AgendaServiceImpl impl = new AgendaServiceImpl();
	  impl.agendarVisita(agenda);
	
	 //System.out.println(impl.obtenerAgendas("abel")); 
	  }*/
	
}
