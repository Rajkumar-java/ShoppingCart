package com.Ecom.Config;


import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Ecom.Model.UserDtls;

public class CustomUser implements UserDetails
{

		public CustomUser() 
		{
			
		}
				
	    public CustomUser(UserDtls user) 
	    {
		
			this.user = user;
		}

		private UserDtls user;

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() 
		{    
		   SimpleGrantedAuthority authority = new SimpleGrantedAuthority(getPassword());	
			return Arrays.asList(authority);
		}

		@Override
		public String getPassword() 
		{

			return user.getPassword();
		}

		@Override
		public String getUsername() {
		
			return user.getEmail();
		}

		
}
