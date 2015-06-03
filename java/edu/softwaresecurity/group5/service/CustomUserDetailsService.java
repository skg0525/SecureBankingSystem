package edu.softwaresecurity.group5.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserDetailsService extends JdbcDaoImpl {

	@Override
	public void setUsersByUsernameQuery(String usersByUsernameQueryString) {
		super.setUsersByUsernameQuery(usersByUsernameQueryString);
	}

	@Override
	public void setAuthoritiesByUsernameQuery(String queryString) {
		super.setAuthoritiesByUsernameQuery(queryString);
	}
 
	@Override
	public List<UserDetails> loadUsersByUsername(String username) {
		return getJdbcTemplate().query(super.getUsersByUsernameQuery(), new String[] { username },
				new RowMapper<UserDetails>() {
					public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						String username = rs.getString("username");
						String password = rs.getString("password");
						boolean enabled = rs.getBoolean("enabled");
						boolean accountNonExpired = rs.getBoolean("userExpired");
						boolean credentialsNonExpired = rs.getBoolean("userDetailsExpired");
						boolean accountNonLocked = rs.getBoolean("userLocked");

						return new User(username, password, enabled, accountNonExpired, credentialsNonExpired,
								accountNonLocked, AuthorityUtils.NO_AUTHORITIES);
					}

				});
	}
	
	@Override
	public UserDetails createUserDetails(String username, UserDetails userFromUserQuery,
			List<GrantedAuthority> combinedAuthorities) {
		String returnUsername = userFromUserQuery.getUsername();

		if (super.isUsernameBasedPrimaryKey()) {
			returnUsername = username;
		}

		return new User(returnUsername, userFromUserQuery.getPassword(), userFromUserQuery.isEnabled(),
				userFromUserQuery.isAccountNonExpired(), userFromUserQuery.isCredentialsNonExpired(),
				userFromUserQuery.isAccountNonLocked(), combinedAuthorities);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

}