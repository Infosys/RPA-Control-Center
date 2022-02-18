 /*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */  

package com.infosys.aiauto.operationsportal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.infosys.aiauto.operationsportal.dto.Expert;

/**
 * @author Devesh_Mankar
 *
 */
@Repository
public class ExpertsDao {

	@Autowired
	private DataSource dataSource;
	
	@Value("${fetchAllExpertsQuery}")
	private String fetchAllExpertsQuery;

	public List<Expert> getAllExperts(String obsId, String restypeId) {
		// get list of all platforms from database
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<Expert> experts = template.query(fetchAllExpertsQuery, new Object[] { restypeId, obsId },
				new RowMapper<Expert>() {
					@Override
					public Expert mapRow(ResultSet rs, int rowNum) throws SQLException {
						// Map Rows from DB to object
						Expert dto = new Expert();
						dto.setExpertId(rs.getString("expertid"));
						dto.setExpertName(rs.getString("expertname"));
						dto.setExpertChatId(rs.getString("expertchatid"));
						return dto;
					}
				});
		return experts;
	}

}
