package com.macarenachang.authservice.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.macarenachang.authservice.model.User;
import com.macarenachang.authservice.service.UserService;

@CrossOrigin(origins ="*")
@RestController
public class UserController {
    @Autowired
    UserService userService; 
    	
/* 	@GetMapping(value="users", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<User> recuperarUsers(){
		return userService.recuperarUsers(); 
	}  */

}
