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
import org.springframework.stereotype.Component;

@Component
public class ObservationDAO {

	@Autowired
	private DataSource datasource;

	@Value("${obsResourceIdQuery}")
	private String obsResourceIdQuery;
	
	@Value("${obsObservableIdQuery}")
	private String obsObservableIdQuery;
	
	@Value("${obsAliasNameQuery}")
	private String obsAliasNameQuery;
	
	@Value("${obsPortfolioIdQuery}")
	private String obsPortfolioIdQuery;
	
	@Value("${obsConfigIdQuery}")
	private String obsConfigIdQuery;
	
	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public int saveObservations(String configid, String portfolioid, String resid, String obsid, String observationName,
			float metricValue, int sequence, String observationTime, String source) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		/*String insertQuery = "INSERT INTO observations (configid, portfolioid, resourceid, observableid, observablename, observationsequence, value, observationtime, source) values('"
				+ configid + "','" + portfolioid + "','" + resid + "','" + obsid + "','" + observationName + "',"
				+ sequence + "," + metricValue + ",'" + observationTime + "','" + source + "');";*/
		
		String insertQuery = "INSERT INTO observations (configid, portfolioid, resourceid, observableid, observablename, observationsequence, value, observationtime, source) values('"
				+ configid + "','" + portfolioid + "','" + resid + "','" + obsid + "','" + observationName + "',"
				+ sequence + "," + metricValue + ",LOCALTIMESTAMP(0),'" + source + "');";
		rows_updated = template.update(insertQuery);

		return rows_updated;
	}

	public String getResourceId(String resourceName) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(obsResourceIdQuery, new Object[] { resourceName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("resourceid");
				return resource;
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
	
	public String getSource(String resourceName) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(obsResourceIdQuery, new Object[] { resourceName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = Integer.toString(rs.getInt("resourcetypeid"));
				return resource;
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
	
	public String getResourceTypeId(String resourceName) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(obsResourceIdQuery, new Object[] { resourceName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = Integer.toString(rs.getInt("resourcetypeid"));
				return resource;
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
	
	public String getObservationId(String obsName) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(obsObservableIdQuery, new Object[] { obsName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("observableid");
				return resource;
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
	
	public String getAliasId(String resTypeId, String obsId) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(obsAliasNameQuery, new Object[] { resTypeId, obsId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("aliasname");
				return resource;
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
	
	public String getPortfolioId(String resId) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(obsPortfolioIdQuery, new Object[] { resId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("portfolioid");
				return resource;
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
	
	public String getConfigId(String resId, String aliasName) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(obsConfigIdQuery, new Object[] { resId, aliasName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("configid");
				return resource;
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
}
