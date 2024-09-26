package br.com.cursojsf.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.cursojsf.entidades.Pessoa;
import br.com.cursojsf.jpautil.JPAUtil;

@WebFilter(urlPatterns = "/*")
public class filterAltenticacao implements Filter {

	public void destroy() {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, 
					FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		Pessoa userLogin = (Pessoa) session.getAttribute("usuarioLogado");
		String url = req.getServletPath();
		
		if(!url.equalsIgnoreCase("index.jsp") && userLogin == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsf");
			dispatcher.forward(request, response);
			return;
		}else {
			// Executa ações do request e do response
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		JPAUtil.getEntityManager();
	}
	
}
