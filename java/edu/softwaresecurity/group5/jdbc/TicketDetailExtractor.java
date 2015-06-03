package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.softwaresecurity.group5.dto.TicketDetailDTO;

public class TicketDetailExtractor implements ResultSetExtractor<TicketDetailDTO> {
	
	 public TicketDetailDTO extractData(ResultSet resultSet) throws SQLException,
	   DataAccessException {
		 
		 TicketDetailDTO ticketInfoDTO = new TicketDetailDTO();
	  
	  ticketInfoDTO.setId(resultSet.getInt(1));
	  ticketInfoDTO.setUsername(resultSet.getString(2));
	  ticketInfoDTO.setRequestcompleted(resultSet.getBoolean(3));
	  ticketInfoDTO.setRequestapproved(resultSet.getBoolean(4));
	  ticketInfoDTO.setRequestrejected(resultSet.getBoolean(5));
	  ticketInfoDTO.setRequesttype(resultSet.getString(6));
	  ticketInfoDTO.setFirstname(resultSet.getString(7));
	  ticketInfoDTO.setLastname(resultSet.getString(8));
	  ticketInfoDTO.setSex(resultSet.getString(9));
	  ticketInfoDTO.setSelection(resultSet.getString(10));
	  ticketInfoDTO.setPhonenumber(resultSet.getString(11));
	  ticketInfoDTO.setEmail(resultSet.getString(12));
	  ticketInfoDTO.setAddress(resultSet.getString(13));
	  ticketInfoDTO.setAccountNumber(resultSet.getInt(14));
	  ticketInfoDTO.setAccountBalance(resultSet.getFloat(15));
	  ticketInfoDTO.setDeleteaccount(resultSet.getBoolean(16));
	  return ticketInfoDTO;
	 }
}