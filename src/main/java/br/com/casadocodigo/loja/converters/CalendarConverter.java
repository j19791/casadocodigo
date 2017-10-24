package br.com.casadocodigo.loja.converters;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Calendar.class) // informar ao JSF que ela será nosso conversor do tipo de dado Calendar
public class CalendarConverter implements Converter {

	private DateTimeConverter converter = new DateTimeConverter();

	public CalendarConverter() {
		converter.setPattern("dd/MM/yyyy");
		converter.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	}

	@Override
	// Quando está no ManagedBean a data é um Objeto (Calendar) que queremos, desta
	// forma o JSF
	// chama o método getAsObject() p/ converter String / Objeto
	public Object getAsObject(FacesContext context, UIComponent component, String dataTexto) {
		Date data = (Date) converter.getAsObject(context, component, dataTexto); // converte String em Date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);// o Calendar recebe a data (Date) convertida
		return calendar;
	}

	@Override
	// Quando a informação está na tela, a data é uma String, nesse ponto o JSF
	// chama o método getAsString()
	public String getAsString(FacesContext context, UIComponent component, Object dataObject) {
		if (dataObject == null)
			return null;

		Calendar calendar = (Calendar) dataObject; // converte Object em Calendar
		return converter.getAsString(context, component, calendar.getTime()); // conversor para String recebe apenas
																				// Date, portanto deverá chamar
																				// calendar.getTime()
	}

}
