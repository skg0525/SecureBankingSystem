package edu.softwaresecurity.group5.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Repository;

import edu.softwaresecurity.group5.dao.LoginAttemptsLockResetDAO;
import edu.softwaresecurity.group5.model.AccountAttempts;
@Repository
public class LoginAttemptsLockResetDAOImpl extends JdbcDaoSupport implements LoginAttemptsLockResetDAO {

	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	private boolean isUserExists(String username) {
 
	  if (getJdbcTemplate().queryForObject(
			  "SELECT count(*) FROM users WHERE username = ?", new Object[] { username }, Integer.class) > 0) {
		  return true;
	  }
 
	  return false;
	}
	
	
	public void updateUserAccountAttempts(String username) {
		// TODO Auto-generated method stub
		AccountAttempts user = getUserAttempts(username);
		  if (user == null) {
			if (isUserExists(username)) {
				// insert record.
				getJdbcTemplate().update("INSERT INTO user_attempts (USERNAME, ATTEMPTS, LASTMODIFIED) VALUES(?,?,?)", new Object[] { username, 1, new Date() });
			}
		  } else {
	 
			if (isUserExists(username)) {
				// update records
				getJdbcTemplate().update("UPDATE user_attempts SET attempts = attempts + 1, lastmodified = ? WHERE username = ?", new Object[] { new Date(), username});
			}
	 
			if (user.getTries() + 1 >= 3) {
				// lock user
				getJdbcTemplate().update("UPDATE users SET userLocked = ? WHERE username = ?", new Object[] { false, username });
				// throw exception
				throw new LockedException("User Account is locked!");
			}
	 
		  }
	}

	public void unlockAccount(String username) {
		// TODO Auto-generated method stub
		 getJdbcTemplate().update(
				 "UPDATE user_attempts SET attempts = 0, lastmodified = ? WHERE username = ?", new Object[] { new Date(),username });
	 
	}

	public AccountAttempts getUserAttempts(String username) {
		// TODO Auto-generated method stub
		try {
			 
			AccountAttempts userAttempts = getJdbcTemplate().queryForObject("SELECT * FROM user_attempts WHERE username = ?",
				new Object[] { username }, new RowMapper<AccountAttempts>() {
				public AccountAttempts mapRow(ResultSet rs, int rowNum) throws SQLException {
	 
					AccountAttempts user = new AccountAttempts();
					user.setId(rs.getInt("id"));
					user.setUsername(rs.getString("username"));
					user.setTries(rs.getInt("attempts"));
					user.setLastAttempt(rs.getDate("lastModified"));
	 
					return user;
				}
	 
			});
			return userAttempts;
	 
		  } catch (EmptyResultDataAccessException e) {
			return null;
		  }
	}

}
