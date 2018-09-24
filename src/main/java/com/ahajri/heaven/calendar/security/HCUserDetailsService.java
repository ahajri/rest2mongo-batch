package com.ahajri.heaven.calendar.security;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("userDetailsService")
public class HCUserDetailsService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(HCUserDetailsService.class);

	private List<UserPrincipal> users;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// FIXME: recherche sur MLab

		List<UserPrincipal> foundUsers = users.stream().filter(u -> u.getUsername().equals(username))
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(foundUsers)) {
			throw new UsernameNotFoundException("Utilisater " + username + " non trouvé");
		}
		return foundUsers.get(0);

	}

	public boolean verifyCredentials(UserPrincipal principal) {
		return users.stream().anyMatch(e -> e.getUsername().equals(principal.getUsername())
				&& e.getPassword().equals(principal.getPassword()));
	}
}
