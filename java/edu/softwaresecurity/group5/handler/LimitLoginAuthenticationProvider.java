package edu.softwaresecurity.group5.handler;

import java.util.Date;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import edu.softwaresecurity.group5.dao.LoginAttemptsLockResetDAO;
import edu.softwaresecurity.group5.model.AccountAttempts;

public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

	LoginAttemptsLockResetDAO userDetailsDao;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		try {

			Authentication auth = super.authenticate(authentication);

			// if reach here, means login success, else exception will be thrown
			// reset the user_attempts
			userDetailsDao.unlockAccount(authentication.getName());

			return auth;

		} catch (BadCredentialsException e) {

			userDetailsDao.updateUserAccountAttempts(authentication.getName());
			throw e;

		} catch (LockedException e) {

			String error = "";
			AccountAttempts userAttempts = userDetailsDao.getUserAttempts(authentication.getName());
			if (userAttempts != null) {
				Date lastAttempts = userAttempts.getLastAttempt();
				error = "User account is locked! <br><br>Username : " + authentication.getName()
						+ "<br>Last Attempts : " + lastAttempts;
			} else {
				error = e.getMessage();
			}

			throw new LockedException(error);
		}

	}

	public LoginAttemptsLockResetDAO getUserDetailsDao() {
		return userDetailsDao;
	}

	public void setUserDetailsDao(LoginAttemptsLockResetDAO userDetailsDao) {
		this.userDetailsDao = userDetailsDao;
	}

}