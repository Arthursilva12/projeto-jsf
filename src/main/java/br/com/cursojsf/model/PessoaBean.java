package br.com.cursojsf.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.cursojsf.dao.DaoGeneric;
import br.com.cursojsf.entidades.Cidades;
import br.com.cursojsf.entidades.Estados;
import br.com.cursojsf.entidades.Pessoa;
import br.com.cursojsf.jpautil.JPAUtil;
import br.com.cursojsf.repository.IDaoPessoa;
import br.com.cursojsf.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	
	private List<SelectItem> estados;
	
	private List<SelectItem> cidades;
	
	private Part arquivofoto;
	
	public String salvar() throws IOException {
		// processar a imagem
		byte[] imagemByte = getByte(arquivofoto.getInputStream());
		pessoa.setFotoIconBase64original(imagemByte);// salva foto original
		
		// tranformar em bufferimagem
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
		
		// Pega o tipo da imgem
		int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		
		int largura = 200;
		int altura = 200;
		
		// criar a miniatura
		BufferedImage resizedImg = new BufferedImage(largura, altura, type);
		Graphics2D graphics2d = resizedImg.createGraphics();
		graphics2d.drawImage(bufferedImage, 0, 0, largura, altura, null);
		graphics2d.dispose();
		
		// escrever novamente a imagem em tamnho menor
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivofoto.getContentType().split("\\/")[1];// imagem/png
		ImageIO.write(resizedImg, extensao, baos);
		// micro imagem
		String miniImagem = "data:" + arquivofoto.getContentType()+";base64," 
							+ DatatypeConverter.printBase64Binary(baos.toByteArray());
		// processar a imagem
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		mostrarMsg("Cadastrado com sucesso!");
		return "";
	}
	
	public void editar() {
		if(pessoa != null) {
			Estados estado = pessoa.getCidades().getEstados();
			pessoa.setEstados(estado);
			
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = "+ estado.getId()).getResultList();
				
			List<SelectItem> selectItemsCidades = new ArrayList<SelectItem>();
				
			for (Cidades cidade : cidades) {
				selectItemsCidades.add(new SelectItem(cidade, cidade.getNome()));
			}
				
			setCidades(selectItemsCidades);
		}
	}
	
	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	public String limpar() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String delete() {
		daoGeneric.deletePorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		mostrarMsg("Removido com sucesso!");
		return "";
	}
	
	
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}
	
	//Metod executado depois do campo cep perde o foco.
	public void pesquisaCep(AjaxBehaviorEvent event) {
		
		try {
			//Consumindo API viaCep
			URL url = new URL("https://viacep.com.br/ws/"+pessoa.getCep()+"/json/");// monta a url
			URLConnection connection = url.openConnection();// abre a connection
			InputStream inputStream = connection.getInputStream();// faz consulta a retorna;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while ((cep = reader.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);
			
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setUnidade(gsonAux.getUnidade());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
//			pessoa.setEstado(gsonAux.getEstado());
			pessoa.setRegiao(gsonAux.getRegiao());
			pessoa.setIbge(gsonAux.getIbge());
			pessoa.setGia(gsonAux.getGia());
			pessoa.setDdd(gsonAux.getDdd());
			pessoa.setSiafi(gsonAux.getSiafi());
			
			System.out.println(pessoa.toString()); 
			System.out.println(jsonCep);
			
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao consultar o cep");
		}
		
	}
	
	
	public String deslogar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) 
				context.getCurrentInstance().getExternalContext().getContext();
		
		httpServletRequest.getSession().invalidate();
		
		return "index.jsf";
	}
	
	
	public String logar() {
		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		
		if(pessoaUser != null) {//Achou o usuário
			//adiciona o usuário na sessão usuarioLogado
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);
			
			return "primeirapagina.jsf";
		}
		return "index.jsf";
	}
	
	public boolean permitirAcesso(String acesso) {
		Pessoa pessoa = daoGeneric.getUserLogado();
		return pessoa.getPerfilUser().equals(acesso);
	}
	
	public List<SelectItem> getEstados(){
		estados = iDaoPessoa.listaEstados();
		return estados;
	}
	
	public void carregaCidades(AjaxBehaviorEvent event) {
		Estados estado = (Estados) ((HtmlSelectOneMenu)event.getSource()).getValue();
		
		if(estado != null) {
			pessoa.setEstados(estado);
				
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = "+ estado.getId()).getResultList();
				
			List<SelectItem> selectItemsCidades = new ArrayList<SelectItem>();
				
			for (Cidades cidade : cidades) {
				selectItemsCidades.add(new SelectItem(cidade, cidade.getNome()));
			}
				
			setCidades(selectItemsCidades);
		}
	}
	
	
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(Pessoa.class);
	}
	
	
	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public void setArquivofoto(Part arquivofoto) {
		this.arquivofoto = arquivofoto;
	}
	
	public Part getArquivofoto() {
		return arquivofoto;
	}
	// Metodo que coverte inputstream(entrada de dados) para um array de byte
	// isso porque as classes do projeto não aceita diretamente um inputstream
	private byte[] getByte(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf  = null;
		
		if(is instanceof ByteArrayInputStream){
			size = is.available();
			buf =  new byte[size];
			len = is.read(buf, 0, size);
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			
			while((len = is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, len);
			}
			 
			buf = bos.toByteArray();
		}
		
		return buf;
	}
	
}
