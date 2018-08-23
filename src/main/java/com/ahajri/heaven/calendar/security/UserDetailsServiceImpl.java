package com.ahajri.heaven.calendar.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ahajri.heaven.calendar.enums.ErrorMessageEnum;
import com.ahajri.heaven.calendar.exception.BusinessException;
import com.ahajri.heaven.calendar.model.HRole;
import com.ahajri.heaven.calendar.model.HUser;
import com.ahajri.heaven.calendar.mongo.cloud.CloudMongoService;
import com.ahajri.heaven.calendar.utils.JsonUtils;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private CloudMongoService cloudMongoService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		List<Document> documents = null;
		try {
			documents = cloudMongoService.findByExample("users", new Document("email", email));
			if (CollectionUtils.isEmpty(documents)) {
				throw new UsernameNotFoundException(ErrorMessageEnum.USER_NOT_FOUND_FOR_EMAIL.getMessage(email));
			} else if (documents.size() > 1) {
				throw new UsernameNotFoundException(ErrorMessageEnum.MORE_THAN_ONE_USER_FOR_EMAIL.getMessage(email));
			} else if (documents.get(0).getBoolean("actif")) {
				throw new UsernameNotFoundException(ErrorMessageEnum.USER_STILL_NOT_ACTIF.getMessage(email));
			}

		} catch (BusinessException e) {
			throw new UsernameNotFoundException(e.getMessage());
		}

		Document foundDocument = documents.get(0);

		HUser user = new HUser(email, StringUtils.EMPTY);
		user.setActif(foundDocument.getBoolean("actif"));
		user.setTrelloUsername(foundDocument.getString("trelloUsername"));

		Set<HRole> roles = null;
		try {
			roles = JsonUtils.jsonArrayToObjectSet(foundDocument.getString("roles"), HRole.class);
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		user.setRoles(roles);
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		user.getRoles().stream().forEach(r -> {
			grantedAuthorities.add(new SimpleGrantedAuthority(r.getName()));
		});
		

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				grantedAuthorities);
	}

}
