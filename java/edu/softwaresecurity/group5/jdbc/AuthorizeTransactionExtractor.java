package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.softwaresecurity.group5.dto.TicketDetailDTO;

public class AuthorizeTransactionExtractor implements ResultSetExtractor<TicketDetailDTO> {
	
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
	  ticketInfoDTO.setAccountNumber(resultSet.getInt(9));
	  ticketInfoDTO.setAccountBalance(resultSet.getFloat(10));
	  ticketInfoDTO.setTransactionAmount(resultSet.getString(11));
	  ticketInfoDTO.setToAccountNumber(resultSet.getString(12));
	  ticketInfoDTO.setBillpay(resultSet.getBoolean(13));
	  ticketInfoDTO.setPendingid(resultSet.getInt(14));
	  ticketInfoDTO.setTransactionamountInfloat(resultSet.getFloat(11));
	  
	  return ticketInfoDTO;
	 }
}
