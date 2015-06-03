package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import edu.softwaresecurity.group5.dto.DuplicateValidationCheckerDTO;

public class DuplicateValidationCheckerMapper implements RowMapper<DuplicateValidationCheckerDTO>{
		public DuplicateValidationCheckerDTO mapRow(ResultSet resultSet, int line) throws SQLException {
			DuplicateValidationCheckerExtractor duplicateExtractor = new DuplicateValidationCheckerExtractor();
		  return duplicateExtractor.extractData(resultSet);
	}
}
