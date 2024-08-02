package com.Ecom.Service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Ecom.Model.UserDtls;
import com.Ecom.Repository.UserRepository;
import com.Ecom.Service.UserService;

@Service
public class UserServiceImpl implements UserService
{
	
		@Autowired
	    private UserRepository userRepository;
	    
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		@Override
		public UserDtls saveUser(UserDtls user) 
		{
			user.setRole("ROLE_USER");
			String encodePassword= passwordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);
		    UserDtls saveUser=userRepository.save(user);
		    
			return saveUser;
		}

}
