package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.softwaresecurity.group5.dto.BillPayDTO;

public class BillPayExtractor implements ResultSetExtractor<BillPayDTO> {
		
		 public BillPayDTO extractData(ResultSet resultSet) throws SQLException,
		   DataAccessException {
			 
		  BillPayDTO billPayInfoDTO = new BillPayDTO();
		  
		  billPayInfoDTO.setId(resultSet.getInt(1));
		  billPayInfoDTO.setUsername(resultSet.getString(2));
		  billPayInfoDTO.setAmount(resultSet.getInt(3));
		  billPayInfoDTO.setAccountnumberfrom(resultSet.getInt(4));		  
		  
		  return billPayInfoDTO;
		 }
}
