package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.softwaresecurity.group5.dto.EmployeeInformationDTO;

public class InternalUserExtractor implements ResultSetExtractor<EmployeeInformationDTO> {
	
	 public EmployeeInformationDTO extractData(ResultSet resultSet) throws SQLException,
	   DataAccessException {
		 
		 EmployeeInformationDTO employInfoDTO = new EmployeeInformationDTO();
	  
		 employInfoDTO.setUsername(resultSet.getString(1));
		 employInfoDTO.setFirstname(resultSet.getString(2));
		 employInfoDTO.setLastname(resultSet.getString(3));
		 employInfoDTO.setSex(resultSet.getString(4));
		 employInfoDTO.setPhonenumber(resultSet.getString(5));
		 employInfoDTO.setEmail(resultSet.getString(6));
		 employInfoDTO.setAddress(resultSet.getString(7));
	  
	  return employInfoDTO;
	 }
}
