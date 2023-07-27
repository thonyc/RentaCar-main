package com.jwt_authenticator;


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
	
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	//method to perform the filter
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		String header = req.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(req, res);
			return;
		}
		
		// we get the user's data from the token
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		
		// user information is stored in security context
		// so that it can be used by Spring security during the
		// authorization process
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
		
	}
	
	// TO GET THE USER AUTHENTICATION
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		// the token comes in the request header
		String token = request.getHeader("Authorization");
		if (token != null) {

			// The token is processed and the user and roles are retrieved.
			Claims claims=Jwts.parser()
					.setSigningKey("TUC@S3cur1ty?GT")
					.parseClaimsJws(token.replace("Bearer ", ""))
					.getBody();
			String user = claims.getSubject();
			List<String> authorities=(List<String>) claims.get("authorities");
			if (user != null) {

				// we create the object with the user's information
				return new UsernamePasswordAuthenticationToken(user, null, authorities.stream()
													.map(SimpleGrantedAuthority::new)
													.collect(Collectors.toList()));
			}
			return null;
		}
		return null;
	}
}
