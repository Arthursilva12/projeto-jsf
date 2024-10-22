package br.com.cursojsf.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cursojsf.entidades.Estados;
import br.com.cursojsf.jpautil.JPAUtil;

@FacesConverter(forClass = Estados.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable{
	private static final long serialVersionUID = 1L;

	@Override// Retorna objeto inteiro
	public Object getAsObject(FacesContext context, UIComponent component, String codEstado) {
		
		EntityManager manager = JPAUtil.getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		
		Estados estados = (Estados) manager.find(Estados.class, Long.parseLong(codEstado));
		
		return estados;
	}

	@Override// Retorna apenas o código em string
	public String getAsString(FacesContext context, UIComponent component, Object estado) {
			
		if(estado == null) {
			return null;
		}else if(estado instanceof Estados) {
			return ((Estados) estado).getId().toString();
		}else {
			return estado.toString();
		}
		
		
	}

}
