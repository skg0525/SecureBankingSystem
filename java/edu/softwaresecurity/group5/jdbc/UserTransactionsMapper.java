package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.softwaresecurity.group5.dto.UserTransactionsDTO;

public class UserTransactionsMapper implements RowMapper<UserTransactionsDTO>{
		public UserTransactionsDTO mapRow(ResultSet resultSet, int line) throws SQLException {
		 UserTransactionsExtractor userTransactionsExtractor = new UserTransactionsExtractor();
		  return userTransactionsExtractor.extractData(resultSet);
	}
}
