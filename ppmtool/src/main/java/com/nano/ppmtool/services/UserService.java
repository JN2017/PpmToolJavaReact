package com.nano.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nano.ppmtool.domain.User;
import com.nano.ppmtool.exceptions.UserNameAlreadyExistsException;
import com.nano.ppmtool.repositories.UserRepository;

@Service
public class UserService {
	@Autowired 
	public UserRepository userRepository;
	
	@Autowired
	public BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public User saveUser(User newUser) {
		
		try {
			//encode password
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			//user name has to be unique(exception)
			newUser.setUsername(newUser.getUsername());
			//Make sure that the password and confirm password match
			
			//We don't persist or show to confirmed Password
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		
			
			
		} catch (Exception e) {
			throw new UserNameAlreadyExistsException("User name '" + newUser.getUsername() + "' already exists!");
		}
		
	}
}
