package edu.softwaresecurity.group5.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.softwaresecurity.group5.dto.BillPayDTO;

public class BillPayMapper implements RowMapper<BillPayDTO>{
		public BillPayDTO mapRow(ResultSet resultSet, int line) throws SQLException {
			BillPayExtractor billPayExtractor = new BillPayExtractor();
		  return billPayExtractor.extractData(resultSet);
	}
}
