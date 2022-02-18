 /*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */  

package com.infosys.aiauto.operationsportal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.infosys.aiauto.operationsportal.dto.NiaPlatform;

/**
 * @author Devesh_Mankar
 *
 */
@Repository
public class PlatformDao {

	@Autowired
	private DataSource dataSource;
	
	@Value("${fetchAllPlatformsQuery}")
	private String fetchAllPlatformsQuery;
	
	@Value("${executionModeList}")
	private String executionModeList;

	public List<NiaPlatform> getAllPlatforms() {
		// get list of all platforms from database
		JdbcTemplate template = new JdbcTemplate(dataSource);
		String[] executionModes = executionModeList.split(",");
		HashMap<String, Integer> executionModeMap = new HashMap<String, Integer>();
		for(int i=0 ; i<executionModes.length ; i++) {
			String[] executions = executionModes[i].split(":");
			executionModeMap.put(executions[0], Integer.parseInt(executions[1]));
		}
		List<NiaPlatform> platforms = template.query(fetchAllPlatformsQuery, new RowMapper<NiaPlatform>() {
			@Override
			public NiaPlatform mapRow(ResultSet rs, int rowNum) throws SQLException {
				// Map Rows from DB to object
				NiaPlatform dto = new NiaPlatform();
				dto.setExecutionMode(executionModeMap.get(rs.getString("platformname")));
				dto.setPlatformId(rs.getString("platformid"));
				dto.setPlatformName(rs.getString("platformname"));
				return dto;
			}
		});
		return platforms;
	}

}
