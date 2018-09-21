package com.ahajri.heaven.calendar.security;

import java.util.Arrays;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class UserPrincipal implements UserDetails{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1761437650370626061L;

	@NotNull
    private String username;

    @NotNull
    private String password;

    private String role;
    
    
    


    public UserPrincipal(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(() -> role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
}
