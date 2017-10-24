package br.com.casadocodigo.loja.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.casadocodigo.loja.models.Autor;

@FacesConverter("autorConverter") // "autorConverter" poderemos referenciá-lo em outros locais caso seja
									// necessário.
public class AutorConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String id) {

		if (id == null || id.trim().isEmpty())
			return null; // tratamento qdo recebe nulo (ao abrir o form por exemplo)

		Autor autor = new Autor();
		autor.setId(Integer.valueOf(id)); // atribuir o id vindo do form ao autor

		return autor;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object autorObject) {

		if (autorObject == null)
			return null;

		Autor autor = (Autor) autorObject;
		return autor.getId().toString();
	}

}
