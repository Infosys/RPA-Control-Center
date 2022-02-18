 /*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */  

package com.infosys.aiauto.operationsportal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.infosys.aiauto.operationsportal.dto.RecentComponents;
import com.infosys.aiauto.operationsportal.dto.ReportComponents;
import com.infosys.aiauto.operationsportal.dto.ResourceType;
import com.infosys.aiauto.operationsportal.dto.ThresholdSummary;

@Repository
public class PortfolioDAO {

	@Autowired
	private DataSource dataSource;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void setNamedParameterJdbcTemplate(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;

	}

	@Value("${innerPortfolioQuery}")
	private String innerPortfolioQuery;
	
	@Value("${newPortReportCompQuery}")
	private String newPortReportCompQuery;
	
	@Value("${componentsCount}")
	private String componentsCount;

	@Value("${yettostart}")
	private String yetToStart;
	
	@Value("${critical}")
	private String critical;

	@Value("${warn}")
	private String warn;

	@Value("${healthy}")
	private String healthy;
	
	@Value("${recentComponentsQuery}")
	private String recentComponentsQuery;
	
	@Value("${dbservercriticalcount}")
	private String dbservercriticalcount;
	
	@Value("${dbserverwarncount}")
	private String dbserverwarncount;
	
	@Value("${servercriticalcount}")
	private String servercriticalcount;
	
	@Value("${serverwarncount}")
	private String serverwarncount;
	
	@Value("${othercriticalcount}")
	private String othercriticalcount;
	
	@Value("${otherwarncount}")
	private String otherwarncount;
	
	@Value("${resourceTypeQuery}")
	private String resourceTypeQuery;
	
	@Value("${countOfChecksNew}")
	private String countOfChecksNew;
	
	@Value("${serverStateNew}")
	private String serverStateNew;
	
	@Value("${componentStateQueryNew}")
	private String componentStateQueryNew;
	
	@Value("${configNameQuery}")
	private String configNameQuery;
	
	@Value("${portfolioIdQuery}")
	private String portfolioIdQuery;
	
	@Value("${observableNameQuery}")
	private String observableNameQuery;
	
	@Value("${resourceNameQuery}")
	private String resourceNameQuery;
	
	@Value("${resourceTypeNameQuery}")
	private String resourceTypeNameQuery;
	
	@Value("${resourceTypeIdQuery}")
	private String resourceTypeIdQuery;
	
	List<ThresholdSummary> incidentDetails = new ArrayList<ThresholdSummary>();

	public List<String> getPortfolioChilds(String resId) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> list = template.query(innerPortfolioQuery, new Object[] { resId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				return rs.getString("resourceid");
			}
		});
		return list;
	}
	
	public List<ThresholdSummary> getIncidentDetails(String resId, int count) {

		JdbcTemplate template = new JdbcTemplate(dataSource);
//		if(count <= 1) {
			List<ThresholdSummary> list = template.query(configNameQuery, new Object[] { resId},
					new RowMapper<ThresholdSummary>() {
						public ThresholdSummary mapRow(ResultSet rs, int rownumber) throws SQLException {
							ThresholdSummary dto = new ThresholdSummary();
							dto.setIncidentId(" ");
							dto.setPortfolioId(rs.getString("resourceid"));
							dto.setConfigID(rs.getString("configid"));
							return dto;
						}
					});
			return list;
	}
	
	public int getComponentsCount(List<String> innerPortfolio) {
		// calling function to get JDBC connection
		setNamedParameterJdbcTemplate(dataSource);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", innerPortfolio);
		int Count = namedParameterJdbcTemplate.queryForObject(componentsCount, params, Integer.class);
		return Count;
	}

	public int getCountOfChecks(String portfolioId) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<Integer> list = template.query(countOfChecksNew, new Object[] { portfolioId }, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rownumber) throws SQLException {
				return rs.getInt("checkcount");
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}else {
			return 0;
		}
		
	}
	
	public Map<String, Integer> getServerState(String portfolioId) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Map<String, Integer> stateCount = new HashMap<String, Integer>();
		@SuppressWarnings("unused")
		List<Map<String, Integer>> stateCountmap = template.query(serverStateNew, new Object[] { portfolioId },
				new RowMapper<Map<String, Integer>>() {
					public Map<String, Integer> mapRow(ResultSet rs, int rownumber) throws SQLException {
						stateCount.put(critical, rs.getInt("critical"));
						stateCount.put(warn, rs.getInt("warning"));
						stateCount.put(healthy, rs.getInt("healthy"));
						stateCount.put(dbservercriticalcount, rs.getInt("dbservercriticalcount"));
						stateCount.put(dbserverwarncount, rs.getInt("dbserverwarncount"));
						stateCount.put(servercriticalcount, rs.getInt("servercriticalcount"));
						stateCount.put(serverwarncount, rs.getInt("serverwarncount"));
						stateCount.put(othercriticalcount, rs.getInt("othercriticalcount"));
						stateCount.put(otherwarncount, rs.getInt("otherwarncount"));
						return stateCount;
					}
				});
		return stateCount;
	}
	
	public Map<String, String> getComponentState(String portfolioId) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		Map<String, String> stateCount = new HashMap<String, String>();
		List<Map<String, String>> stateCountmap = template.query(componentStateQueryNew, new Object[] { portfolioId },
				new RowMapper<Map<String, String>>() {
					public Map<String, String> mapRow(ResultSet rs, int rownumber) throws SQLException {
						stateCount.put(rs.getString("resourceid"),getStateComponent(rs.getInt("criticalcount"), rs.getInt("warncount"), rs.getInt("healthycount")));
						return stateCount;
					}
				});
		if(stateCountmap != null && !stateCountmap.isEmpty()){
			return stateCountmap.get(0);
		}else {
			return stateCount;
		}
		
	}
	
	public String getStateComponent(int criticalcount, int warncount, int healthycount) {
		// TODO Auto-generated method stub
		String status = "healthy";
		if(criticalcount > 0) {
			status = "critical";
		}else if (warncount > 0) {
			status = "warning";
		}else {
			status = "healthy";
		}
		
		return status;
	}
	
	public List<ReportComponents> getPortReportCompDetails(List<String> innerPortfolio)
			throws SQLException {
		String portfolioId = innerPortfolio.get(0);
		JdbcTemplate template = new JdbcTemplate(dataSource);
		
		List<ReportComponents> list = template.query(newPortReportCompQuery, new Object[] { portfolioId }, new RowMapper<ReportComponents>() {
			public ReportComponents mapRow(ResultSet rs, int rownumber) throws SQLException {
				ReportComponents reportComp = new ReportComponents();
				Map<String, Integer> serverState = new HashMap<String, Integer>();
				int resourceTypeId=rs.getInt("resourcetypeid");
				String restypeid = Integer.toString(resourceTypeId);
//				reportComp.setResourcetypeId(getResourceTypeName(resourceTypeId));
				reportComp.setResourcetypeId(rs.getString("resourcetypename"));
				reportComp.setResourceTypeID(restypeid);
				reportComp.setResourceId(rs.getString("resourceid"));
				reportComp.setResourceName(rs.getString("resourcename"));
				reportComp.setObservableName(rs.getString("observablename"));
				//reportComp.setObservableName(getObservableName(rs.getString("observableid")));
				reportComp.setValue(rs.getString("value") == null ? "NA" : rs.getString("value"));
				reportComp.setThreshold(rs.getString("threshold"));				
				reportComp.setObservationTime(rs.getTimestamp("observationtime").toString());
				reportComp.setObservableId(rs.getString("observableid"));
				serverState.put(yetToStart, rs.getInt("yettostart"));
				serverState.put(critical, rs.getInt("critical"));
				serverState.put(warn, rs.getInt("warn"));
				serverState.put(healthy, rs.getInt("healthy"));
				reportComp.setServerStateCount(serverState);
				
				return reportComp;
			}
		});
		return list;
	}
	
	public List<ReportComponents> getPortReportDetails(List<String> innerPortfolio) throws SQLException {
		List<ReportComponents> list = new ArrayList<ReportComponents>();
		list = getPortReportCompDetails(innerPortfolio);
		return list;
	}
	
	public String getResourceName(String observableId) {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> list = template.query(resourceNameQuery, new Object[] { observableId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = new String();
				resource = rs.getString("resourcename");
				return resource;
			}
		});
		
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}

	public String getObservableName(String observableId) {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> list = template.query(observableNameQuery, new Object[] { observableId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = new String();
				resource = rs.getString("observablename");
				return resource;
			}
		});
		
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}

	public List<RecentComponents> getRecentComponentList(String portfolioId) {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> resourceList = template.query(recentComponentsQuery, new Object[] { portfolioId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resourceid;
				resourceid = rs.getString("resourceid");				
				return resourceid;
			}
		});
		/*
		 * Sql server does not support order by in inner query, so we are implementing sorting logic in java
		 */
		Set<String> resourceSet = new LinkedHashSet<String>(resourceList);
		List<RecentComponents> recentComponents = new ArrayList<RecentComponents>();
		int i = 0;
		for(String resourceid : resourceSet){			
			RecentComponents resource = new RecentComponents();
			resource.setResourceId(resourceid);
			resource.setResourceName(getResourceName(resourceid));
			recentComponents.add(resource);			
			i++;
			if(i == 5){
				break;
			}			
		}
		return recentComponents;
	}
	
	public List<ResourceType> getResourceType() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<ResourceType> list = template.query(resourceTypeQuery, new Object[] {}, new RowMapper<ResourceType>() {
			public ResourceType mapRow(ResultSet rs, int rownumber) throws SQLException {
				ResourceType dto = new ResourceType();
				dto.setResourceTypeID(rs.getString("resourcetypename"));
				return dto;
			}
		});
		return list;
	}
	
	public String getPortfolioId(String portfolioName) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> list = template.query(portfolioIdQuery, new Object[] { portfolioName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				return rs.getString("resourceid");
			}
		});	
		
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
	
	public String getResourceTypeName(int resourcetypeId) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> list = template.query(resourceTypeNameQuery, new Object[] { resourcetypeId }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				return rs.getString("resourcetypename");
			}
		});
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
	
	public String getResourceTypeId(String resourcetypeName) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		List<String> list = template.query(resourceTypeIdQuery, new Object[] { resourcetypeName }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				return Integer.toString(rs.getInt("resourcetypeid"));				
			}
		});
		
		if(list != null && !list.isEmpty()){
			return list.get(0);		
		}else {
			return " ";
		}
	}
		
}
