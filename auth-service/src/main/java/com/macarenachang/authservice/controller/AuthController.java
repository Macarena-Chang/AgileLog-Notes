package com.macarenachang.authservice.controller;

import static com.macarenachang.authservice.util.Constants.CLAVE;
import static com.macarenachang.authservice.util.Constants.TIEMPO_VIDA;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.macarenachang.authservice.model.User;
import com.macarenachang.authservice.model.request.RegistrationRequest;
import com.macarenachang.authservice.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest regreq) {
		if (userService.existsByEmail(regreq.email)) {
			return new ResponseEntity<>("Error: Email is already in use", HttpStatus.BAD_REQUEST);
		}

		userService.registerUser(regreq.email, regreq.password);

		return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
	}

	@PostMapping("/refresh")
	public ResponseEntity<String> refreshToken() {
		// Get the current authenticated user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Generate a new token
		String newToken = getToken(authentication);

		// Return the new token
		return new ResponseEntity<>(newToken, HttpStatus.OK);
	}



	// Indicarle a Spring que nos inyecte el AuthenticationManager en el constructor
	// del controller.
	public AuthController(AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	@PostMapping("login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password) {
		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			// si el usuario está autenticado, se genera el token
			if (authentication.isAuthenticated()) {
				return getToken(authentication); // generar token
			} else {
				return "no autenticado";
			}
		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();
			return "Error during authentication: " + e.getMessage();
		}

	}

	private String getToken(Authentication autentication) {
		// en el body del token se incluye el usuario
		// y los roles a los que pertenece, además
		// de la fecha de caducidad y los datos de la firma
		String token = Jwts.builder()
				.setIssuedAt(new Date()) // fecha creación
				.setSubject(autentication.getName()) // usuario
				.claim("authorities", autentication.getAuthorities().stream() // roles
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()))
				.setExpiration(new Date(System.currentTimeMillis() + TIEMPO_VIDA)) // fecha caducidad
				.signWith(SignatureAlgorithm.HS512, CLAVE)// clave y algoritmo para firma
				.compact(); // generación del token
		return token;
	}

	@GetMapping("test")
	public String helloAdmin() {
		return "Hello :)";
	}

}
