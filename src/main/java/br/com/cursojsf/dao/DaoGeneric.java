package br.com.cursojsf.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
}
