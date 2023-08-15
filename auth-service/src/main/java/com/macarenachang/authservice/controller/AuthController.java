package com.macarenachang.authservice.controller;

import static com.macarenachang.authservice.util.Constants.CLAVE;
import static com.macarenachang.authservice.util.Constants.TIEMPO_VIDA;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.macarenachang.authservice.model.request.UserAuthDTO;
import com.macarenachang.authservice.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authManager;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserAuthDTO regreq) {
		if (userService.existsByEmail(regreq.email)) {
			return new ResponseEntity<>("Error: Email is already in use", HttpStatus.BAD_REQUEST);
		}

		userService.registerUser(regreq.email, regreq.password);

		return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
	}

	@PostMapping("login")
	@Operation(summary = "Use your credentials to login",
            responses = {
		@ApiResponse(responseCode = "200", description = "Successfully authenticated"),
		@ApiResponse(responseCode = "401", description = "Invalid Credentials")})
	public ResponseEntity<?> login(@RequestBody UserAuthDTO regreq) {
		try {
			Authentication authentication = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(regreq.email, regreq.password));

			// if the user is authenticated, generate the token
			if (authentication.isAuthenticated()) {
				String token = getToken(authentication);
				Map<String, String> tokenResponse = new HashMap<>();
				tokenResponse.put("token", token);
				return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Unauthenticated", HttpStatus.UNAUTHORIZED);
			}
		} catch (BadCredentialsException e) {
			// TODO:  Handle the case where authentication request is invalid
			return new ResponseEntity<>("Invalid email or password.", HttpStatus.UNAUTHORIZED);

		} catch (DisabledException e) {
			// TODO:  Handle the case where the user is disabled 
			return new ResponseEntity<>("Account is disabled.", HttpStatus.UNAUTHORIZED);

		} catch (Exception e) {
			// Log the exception
			e.printStackTrace();

			// Return a generic error response to the client
			return new ResponseEntity<>("Error during authentication: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
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


	
	private String getToken(Authentication autentication) {
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
