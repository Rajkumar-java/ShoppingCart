package com.Ecom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecom.Model.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls, Integer>
{ 
	public UserDtls findByEmail(String username);
}
