package com.ahajri.heaven.calendar.collection;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection="UserAuth")
public class UserAuth implements Serializable {

	private static final long serialVersionUID = 182736296856694981L;

	@Id
	private String id;

	@Indexed(unique = true)
	private String username;

	private String password;

	@Transient
	private boolean isPasswordEncrypted = false;

	public UserAuth() {
		super();
	}

	public UserAuth(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public boolean isPasswordEncrypted() {
		return isPasswordEncrypted;
	}

	public void setPasswordEncrypted(boolean isPasswordEncrypted) {
		this.isPasswordEncrypted = isPasswordEncrypted;
	}

}
