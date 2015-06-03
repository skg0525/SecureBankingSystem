package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.softwaresecurity.group5.dto.CustomerInformationDTO;

public class UserWithSSNExtractor implements ResultSetExtractor<CustomerInformationDTO> {
	
	 public CustomerInformationDTO extractData(ResultSet resultSet) throws SQLException,
	   DataAccessException {
		 
	  CustomerInformationDTO custInfoDTO = new CustomerInformationDTO();
	  
	  custInfoDTO.setUsername(resultSet.getString(1));
	  custInfoDTO.setFirstname(resultSet.getString(2));
	  custInfoDTO.setLastname(resultSet.getString(3));
	  custInfoDTO.setSex(resultSet.getString(4));
	  custInfoDTO.setSelection(resultSet.getString(5));
	  custInfoDTO.setPhonenumber(resultSet.getString(6));
	  custInfoDTO.setEmail(resultSet.getString(7));
	  custInfoDTO.setAddress(resultSet.getString(8));
	  custInfoDTO.setSsn(resultSet.getString(9));
	  
	  return custInfoDTO;
	 }
}
