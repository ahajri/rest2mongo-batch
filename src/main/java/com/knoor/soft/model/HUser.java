package com.knoor.soft.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;

/**
 * 
 * @author ahajri
 *
 */
public class HUser {

	private ObjectId _id;
	
	@Email(message="Email not valid")
	private String email;
	
	@NotNull(message="Password required")
	private String password;
	
	private Set<HRole> roles= new HashSet<HRole>();
	
	private boolean actif = false;
	
	
	private String token;
	
	
	public HUser() {
		
	}

	public HUser(String email, String password) {
		super();
		this.email = email;
		this.password = password;
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

	public Set<HRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<HRole> roles) {
		this.roles.addAll(roles);
	}

	public boolean isActif() {
		return actif;
	}
	
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public ObjectId get_id() {
		return _id;
	}
	
	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	
}
