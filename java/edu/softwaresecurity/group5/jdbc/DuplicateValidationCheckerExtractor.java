package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.softwaresecurity.group5.dto.DuplicateValidationCheckerDTO;

public class DuplicateValidationCheckerExtractor implements ResultSetExtractor<DuplicateValidationCheckerDTO> {
		
		 public DuplicateValidationCheckerDTO extractData(ResultSet resultSet) throws SQLException,
		   DataAccessException {
			 
		  DuplicateValidationCheckerDTO duplicateValidateDTO = new DuplicateValidationCheckerDTO();
		  
		  duplicateValidateDTO.setUsername(resultSet.getString(1));
		  duplicateValidateDTO.setEmail(resultSet.getString(2));
		  duplicateValidateDTO.setSsn(resultSet.getString(3));
		  
		  return duplicateValidateDTO;
		 }
}
