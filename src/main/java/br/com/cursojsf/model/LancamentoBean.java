package br.com.cursojsf.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.cursojsf.dao.DaoGeneric;
import br.com.cursojsf.entidades.Lancamento;
import br.com.cursojsf.entidades.Pessoa;
import br.com.cursojsf.repository.IDaoLancamento;
import br.com.cursojsf.repository.IDaoLancamentoImpl;

@ViewScoped
@ManagedBean(name = "lancamentoBean")
public class LancamentoBean {

	private Lancamento lancamento = new Lancamento();
	private DaoGeneric<Lancamento> daoGeneric = new DaoGeneric<Lancamento>();
	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();
	private IDaoLancamento daoLancamento = new IDaoLancamentoImpl();

	
	public String salvar () {
		Pessoa pessoaUser = daoGeneric.getUserLogado();
		lancamento.setUsuario(pessoaUser);
		lancamento = daoGeneric.merge(lancamento);
		
		carregarLancamentos();
		
		return "";
	}
	
	@PostConstruct
	public void carregarLancamentos() {
		Pessoa pessoaUser = daoGeneric.getUserLogado();
		lancamentos = daoLancamento.consultar(pessoaUser.getId());
		
	}
	
	public String delete() {
		daoGeneric.deletePorId(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		return "";
	}
	
	public String novo() {
		lancamento = new Lancamento();
		return "";
	}
	
	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public DaoGeneric<Lancamento> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Lancamento> getListLancamentos() {
		return lancamentos;
	}

	public void setListLancamentos(List<Lancamento> listLancamentos) {
		this.lancamentos = listLancamentos;
	}

}
