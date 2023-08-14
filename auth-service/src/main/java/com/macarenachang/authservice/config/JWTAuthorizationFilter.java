package com.macarenachang.authservice.config;

import static com.macarenachang.authservice.util.Constants.CLAVE;
import static com.macarenachang.authservice.util.Constants.ENCABEZADO;
import static com.macarenachang.authservice.util.Constants.PREFIJO_TOKEN;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    // constructor
	// Le decimos a Spring que nos INYECTE el AuthenticationManager
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(ENCABEZADO); // extraer encabezado donde viene el token
		if (header == null || !header.startsWith(PREFIJO_TOKEN)) { // si falta el encabezado entero o no tiene el
																	// prefijo Bearer
			chain.doFilter(req, res); // pasa al siguiente filtro de la cadena o final
			return;
		}
		// obtenemos los datos del usuario a partir del token
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		// La información del usuario se guarda en el contexto para uso de Spring Security.
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		// el token viene en la cabecera de la petición/request
		String token = request.getHeader(ENCABEZADO);
		if (token != null) {
			// Se procesa el token (si no es null) y se recupera el usuario y los roles.
			Claims claims = Jwts.parser() 
					.setSigningKey(CLAVE).parseClaimsJws(token.replace(PREFIJO_TOKEN, "")).getBody();
			String user = claims.getSubject();
			List<String> authorities = (List<String>) claims.get("authorities");
			if (user != null) {
				// creamos el objeto con la información del usuario
				return new UsernamePasswordAuthenticationToken(user, null,
						authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
			}
			return null;
		}
		return null;
	}

}
