package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.softwaresecurity.group5.dto.TicketInformationDTO;

public class TicketExtractor implements ResultSetExtractor<TicketInformationDTO> {
	
	 public TicketInformationDTO extractData(ResultSet resultSet) throws SQLException,
	   DataAccessException {
		 
	  TicketInformationDTO ticketInfoDTO = new TicketInformationDTO();
	  
	  ticketInfoDTO.setId(resultSet.getInt(1));
	  ticketInfoDTO.setUsername(resultSet.getString(2));
	  ticketInfoDTO.setRequestcompleted(resultSet.getBoolean(3));
	  ticketInfoDTO.setRequestapproved(resultSet.getBoolean(4));
	  ticketInfoDTO.setRequestrejected(resultSet.getBoolean(5));
	  ticketInfoDTO.setRequesttype(resultSet.getString(6));
	  
	  return ticketInfoDTO;
	 }
}
