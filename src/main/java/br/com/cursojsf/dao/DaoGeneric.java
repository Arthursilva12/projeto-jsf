package br.com.cursojsf.dao;

import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cursojsf.entidades.Pessoa;
import br.com.cursojsf.jpautil.JPAUtil;

public class DaoGeneric<E> {
	
	private EntityManager entityManager = JPAUtil.getEntityManager();
	
	public void salvar(E entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();//inicia o processo salvar, excluir etc..
		entityTransaction.begin();
		entityManager.persist(entidade);// salva
		entityTransaction.commit();
	}
	
	public E merge(E entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();//inicia o processo salvar, excluir etc..
		entityTransaction.begin();
		
		E retorno = entityManager.merge(entidade);// salva
		entityTransaction.commit();
		return retorno;
	}
	
	public void delete(E entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.remove(entidade);// salva
		entityTransaction.commit();
	}
	
	public void deletePorId(E entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		Object id = JPAUtil.getPrimaryKey(entidade);
		entityManager.createQuery(
				"delete from " + entidade.getClass().getCanonicalName()+" where id = "+id).executeUpdate();
		entityTransaction.commit();
	}
	
	public List<E> getListEntity(Class<E> entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> retorno = entityManager.createQuery("from "+entidade.getName()).getResultList();	
		
		entityTransaction.commit();
		return retorno;
	}
	
	public Pessoa getUserLogado() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		return pessoaUser;
	}
	
	public E consultar(Class<E> entidade, String codigo) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		E objeto = (E) entityManager.find(entidade, Long.parseLong(codigo));
		entityTransaction.commit();
		
		return objeto;
	}
}
