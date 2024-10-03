package br.com.cursojsf.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Pessoa implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String Nome;
	private String sobrenome;
	private Integer idade;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date dataNascimento = new Date();
	
	private String sexo;
	
	private String[] frameworks;
	
	private Boolean ativo;
	
	private String login;
	private String senha;
	
	private String perfilUser;
	
	private String nivelProgramador;

	private Integer[] linguagens; 
	
	public Integer[] getLinguagens() {
		return linguagens;
	}

	public void setLinguagens(Integer[] linguagens) {
		this.linguagens = linguagens;
	}

	public String getNivelProgramador() {
		return nivelProgramador;
	}

	public void setNivelProgramador(String nivelProgramador) {
		this.nivelProgramador = nivelProgramador;
	}

	public String getPerfilUser() {
		return perfilUser;
	}

	public void setPerfilUser(String perfilUser) {
		this.perfilUser = perfilUser;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getAtivo() {
		return ativo;
	}
	
	public void setFrameworks(String[] frameworks) {
		this.frameworks = frameworks;
	}
	
	public String[] getFrameworks() {
		return frameworks;
	}
	
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Pessoa() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public java.util.Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(java.util.Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Pessoa [id=" + id + "]";
	}
	
	
	
}
