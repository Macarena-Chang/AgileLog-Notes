package com.macarenachang.authservice.controller;

import static com.macarenachang.authservice.util.Constants.CLAVE;
import static com.macarenachang.authservice.util.Constants.TIEMPO_VIDA;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;


import com.macarenachang.authservice.model.User;
import com.macarenachang.authservice.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController @RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    
    
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

        
    @GetMapping("test")
    public String helloAdmin() {
        return "Hello :)";
    }

    AuthenticationManager authManager; 
    //Indicarle a Spring que nos inyecte el AuthenticationManager en el constructor del controller.
	public AuthController(AuthenticationManager authManager) {
		this.authManager = authManager; 
	}

	@PostMapping("login")
	public String login(@RequestParam("email") String email , @RequestParam("password") String password) {
		try {
			Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			//si el usuario está autenticado, se genera el token
			if(authentication.isAuthenticated()) {
				return getToken(authentication); //generar token
			}else {
				return "no autenticado";
			}
		} catch(Exception e) {
			// Log the exception
			e.printStackTrace();
			return "Error during authentication: " + e.getMessage();
		}


	}
	
	private String getToken(Authentication autentication) {
		//en el body del token se incluye el usuario 
		//y los roles a los que pertenece, además
		//de la fecha de caducidad y los datos de la firma
		String token = Jwts.builder()
				.setIssuedAt(new Date()) //fecha creación
				.setSubject(autentication.getName()) //usuario
				.claim("authorities",autentication.getAuthorities().stream() //roles
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setExpiration(new Date(System.currentTimeMillis() + TIEMPO_VIDA)) //fecha caducidad
				.signWith(SignatureAlgorithm.HS512, CLAVE)//clave y algoritmo para firma
				.compact(); //generación del token
		return token;
	}



}

