package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.softwaresecurity.group5.dto.TicketInformationDTO;

public class TicketRowMapper implements RowMapper<TicketInformationDTO> {
	public TicketInformationDTO mapRow(ResultSet resultSet, int line) throws SQLException {
		  TicketExtractor ticketExtractor = new TicketExtractor();
		  return ticketExtractor.extractData(resultSet);
		 }
	}
