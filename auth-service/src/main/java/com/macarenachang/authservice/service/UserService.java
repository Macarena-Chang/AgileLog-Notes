package com.macarenachang.authservice.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.macarenachang.authservice.dao.UserRepository;
import com.macarenachang.authservice.model.User;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        // Check if email is already registered
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        // Hash the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    } 

/*     public List<User> recuperarUsers() {
		/*Para simular que tarda mucho tiempo en microservicio-users 
		 *  ServiceImpl agregar un Thread.sleep(10000)
		 * y ver cómo funcionan los métodos asincronicos (28)
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return userRepository.devolverUsers();
		
	} */

    
}

