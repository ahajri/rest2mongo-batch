package com.ahajri.hc.security;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class LoginRequest {
    @NotBlank
    @Email(message="Bad email format !")
    private String email;

    @NotBlank
    private String password;

	public LoginRequest(@NotBlank String email, @NotBlank String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public LoginRequest() {
		super();
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    


}
