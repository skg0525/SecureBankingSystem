package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.softwaresecurity.group5.dto.UserTransactionsDTO;

	public class UserTransactionsExtractor implements ResultSetExtractor<UserTransactionsDTO> {
		
		 public UserTransactionsDTO extractData(ResultSet resultSet) throws SQLException,
		   DataAccessException {
			 
			 UserTransactionsDTO userTransactionsDTO = new UserTransactionsDTO();
		  
			 userTransactionsDTO.setTransactionID(resultSet.getInt(1));
			 userTransactionsDTO.setUsernameFrom(resultSet.getString(2));
			 userTransactionsDTO.setUsernameTo(resultSet.getString(3));
			 userTransactionsDTO.setUsernameFromAccountNumber(resultSet.getString(4));
			 userTransactionsDTO.setUsernameToAccountNumber(resultSet.getString(5));
			 userTransactionsDTO.setTransactionType(resultSet.getString(6));
			 userTransactionsDTO.setUserDelete(resultSet.getBoolean(7));
			 userTransactionsDTO.setCurrentTimeStamp(resultSet.getTimestamp(8));	  
		  
		  return userTransactionsDTO;
		 }
}
