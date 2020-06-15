package com.nano.ppmtool.payload;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "User name can't be blank")
	public String username;
	@NotBlank(message = "Password can't be blank")
	public String password;
	
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
}
