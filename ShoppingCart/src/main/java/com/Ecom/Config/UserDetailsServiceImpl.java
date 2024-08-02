package com.Ecom.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Ecom.Model.UserDtls;
import com.Ecom.Repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
	    @Autowired
	    private UserRepository userRepository;
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
		{
			UserDtls user=userRepository.findByEmail(username);
			if(user== null)
			{
				throw new UsernameNotFoundException("User Not Found"); 
			}
			return new CustomUser(user);		
		}
		
}
