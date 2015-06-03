package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.softwaresecurity.group5.dto.TicketDetailDTO;

public class TicketDetailMapper implements RowMapper<TicketDetailDTO> {
	public TicketDetailDTO mapRow(ResultSet resultSet, int line) throws SQLException {
		  TicketDetailExtractor ticketExtractor = new TicketDetailExtractor();
		  return ticketExtractor.extractData(resultSet);
		 }
	}
