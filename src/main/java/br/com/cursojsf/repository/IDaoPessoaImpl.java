package br.com.cursojsf.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cursojsf.entidades.Pessoa;
import br.com.cursojsf.jpautil.JPAUtil;

public class IDaoPessoaImpl implements IDaoPessoa, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Pessoa consultarUsuario(String login, String senha) {
		Pessoa pessoa = null;
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		try {
			pessoa = (Pessoa) entityManager.createQuery(
					"select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "'").getSingleResult();
		} catch (javax.persistence.NoResultException e) {}
		
		entityTransaction.commit();
		
		return pessoa;
	}

}
