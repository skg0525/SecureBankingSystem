package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.softwaresecurity.group5.dto.EmployeeInformationDTO;

public class InternalUserRowMapper implements RowMapper<EmployeeInformationDTO> {
	public EmployeeInformationDTO mapRow(ResultSet resultSet, int line)
			throws SQLException {
		InternalUserExtractor internalUserExtractor = new InternalUserExtractor();
		return internalUserExtractor.extractData(resultSet);
	}
}