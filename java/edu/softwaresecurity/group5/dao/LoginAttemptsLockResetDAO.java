package edu.softwaresecurity.group5.dao;

import edu.softwaresecurity.group5.model.AccountAttempts;

public interface LoginAttemptsLockResetDAO {
	void updateUserAccountAttempts(String username);
	void unlockAccount(String username);
	AccountAttempts getUserAttempts(String username);
}
