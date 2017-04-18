package edu.ssdut.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import edu.ssdut.util.Tokens;

public class TokenVerifyFilter extends OncePerRequestFilter {

	private String userType;
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chains)
			throws ServletException, IOException {
		
		System.out.println("Token Verify Filter " + userType);
		
		String jwt = request.getHeader("auth_token");
		
		if (jwt == null) {
			request.removeAttribute("username");
		} else {
			String username = Tokens.verityJWT(jwt, userType);
			System.out.println("username : " + username);
			if (username == null) {
				request.removeAttribute("username");
				System.out.println("verification failed !");
			}
			else {
				request.setAttribute("username", username);
				System.out.println("verification passed ~");
			}
		}
		chains.doFilter(request, response);
	}
}
