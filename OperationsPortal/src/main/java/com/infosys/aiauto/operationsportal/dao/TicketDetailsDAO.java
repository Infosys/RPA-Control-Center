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

import com.infosys.aiauto.operationsportal.dto.TicketDetails;

@Component
public class TicketDetailsDAO {

	@Autowired
	private DataSource datasource;
	
	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	@Value("${ticketCountQuery}")
	private String ticketCountQuery;
	
	public TicketDetails getTicketDetails() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<TicketDetails> list = template.query(ticketCountQuery, new Object[] {  }, new RowMapper<TicketDetails>() {
			public TicketDetails mapRow(ResultSet rs, int rownumber) throws SQLException {
				TicketDetails resource = new TicketDetails();
				resource.setAutomationTickets(rs.getInt("automationcount"));
				resource.setManualTickets(rs.getInt("manualcount"));
				resource.setTotalTickets(rs.getInt("totalCount"));
				return resource;
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return null;
		}
	}
}
