package br.com.cursojsf.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cursojsf.entidades.Lancamento;
import br.com.cursojsf.jpautil.JPAUtil;

public class IDaoLancamentoImpl implements IDaoLancamento{

	@Override
	public List<Lancamento> consultar(Long codUser) {
		List<Lancamento> lista = null;
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		lista = entityManager.createQuery("from Lancamento where usuario.id = " + codUser).getResultList();
		transaction.commit();
		
		return lista;
	}

}
