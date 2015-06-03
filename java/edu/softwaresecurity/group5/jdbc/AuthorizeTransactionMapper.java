package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.softwaresecurity.group5.dto.TicketDetailDTO;

public class AuthorizeTransactionMapper implements RowMapper<TicketDetailDTO> {
	public TicketDetailDTO mapRow(ResultSet resultSet, int line) throws SQLException {
		  AuthorizeTransactionExtractor ticketExtractor = new AuthorizeTransactionExtractor();
		  return ticketExtractor.extractData(resultSet);
		 }
	}
