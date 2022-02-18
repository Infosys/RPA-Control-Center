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
import java.util.Map;

//import javax.mail.search.IntegerComparisonTerm;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dto.Cluster;
import com.infosys.aiauto.operationsportal.dto.ClusterChild;
import com.infosys.aiauto.operationsportal.dto.ClusterDetails;
import com.infosys.aiauto.operationsportal.dto.ClusterPortfolio;
import com.infosys.aiauto.operationsportal.dto.Portfolio;

@Component
public class ClusterDetailsDAO {

	@Autowired
	private DataSource datasource;
	
	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	@Value("${clusterDetailsQuery}")
	private String clusterDetailsQuery;
	
	@Value("${portfolioDetailsQuery}")
	private String portfolioDetailsQuery;
	
	@Value("${critical}")
	private String critical;

	@Value("${warn}")
	private String warn;

	@Value("${healthy}")
	private String healthy;
	
	@Value("${clusterChildQuery}")
	private String clusterChildQuery;
	
	@Value("${serverStateNew}")
	private String serverStateNew;
	
	@Value("${resourceNameQuery}")
	private String resourceNameQuery;
	
	private String state = "healthy";
	
	public List<ClusterDetails> getClustersDetails() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<ClusterDetails> list = template.query(clusterDetailsQuery, new Object[] {  }, new RowMapper<ClusterDetails>() {
			public ClusterDetails mapRow(ResultSet rs, int rownumber) throws SQLException {
				ClusterDetails resource = new ClusterDetails();
				resource.setClusterId(rs.getString("resourceid"));
				resource.setClusterName(rs.getString("resourcename"));
				resource.setResourceType(Integer.toString(rs.getInt("resourcetypeid")));
				resource.setDateCreated(rs.getString("createdate"));
				resource.setClusterChild(getPortfolioDetails(rs.getString("resourceid")));
				return resource;
			}
		});
		return list;
	}
	
	public List<Cluster> getClusters() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Cluster> list = template.query(clusterDetailsQuery, new Object[] {  }, new RowMapper<Cluster>() {
			public Cluster mapRow(ResultSet rs, int rownumber) throws SQLException {
				Cluster resource = new Cluster();
				resource.setClusterId(rs.getString("resourceid"));
				resource.setClusterName(rs.getString("resourcename"));
				resource.setPortfolios(getPortfolios(rs.getString("resourceid")));
				return resource;
			}
		});
		return list;
	}
	
	public List<ClusterPortfolio> getPortfolios(String clusterId) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<ClusterPortfolio> list = template.query(portfolioDetailsQuery, new Object[] { clusterId }, new RowMapper<ClusterPortfolio>() {
			public ClusterPortfolio mapRow(ResultSet rs, int rownumber) throws SQLException {
				ClusterPortfolio resource = new ClusterPortfolio();
				resource.setPortfolioId(rs.getString("resourceid"));
				resource.setPortfolioName(rs.getString("resourcename"));
				return resource;
			}
		});
		return list;
	}
	
	public List<Portfolio> getPortfolioDetails(String clusterId) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Portfolio> list = template.query(portfolioDetailsQuery, new Object[] { clusterId }, new RowMapper<Portfolio>() {
			public Portfolio mapRow(ResultSet rs, int rownumber) throws SQLException {
				Portfolio resource = new Portfolio();
				resource.setPortfolioId(rs.getString("resourceid"));
				resource.setPortfolioName(rs.getString("resourcename"));
				resource.setResourceType(Integer.toString(rs.getInt("resourcetypeid")));
				resource.setDateCreated(rs.getString("createdate"));
				resource.setState(state);
				return resource;
			}
		});
		return list;
	}
	
	public Map<String, Integer> getServerState(String portfolioId) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		Map<String, Integer> stateCount = new HashMap<String, Integer>();
		List<Map<String, Integer>> stateCountmap = template.query(serverStateNew, new Object[] { portfolioId },
				new RowMapper<Map<String, Integer>>() {
					public Map<String, Integer> mapRow(ResultSet rs, int rownumber) throws SQLException {
						stateCount.put(critical, rs.getInt("critical"));
						stateCount.put(warn, rs.getInt("warning"));
						stateCount.put(healthy, rs.getInt("healthy"));
						return stateCount;
					}
				});
		if(stateCountmap != null && !stateCountmap.isEmpty()){
			return stateCountmap.get(0);
		}else{
			return stateCount;
		}
		
	}
	
	public String getResourceName(String resourceId) {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(resourceNameQuery, new Object[] { resourceId }, new RowMapper<String>() {
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
	
	public List<ClusterChild> getChildDetails(String clusterid) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<ClusterChild> list = template.query(clusterChildQuery, new Object[] { clusterid }, new RowMapper<ClusterChild>() {
			public ClusterChild mapRow(ResultSet rs, int rownumber) throws SQLException {
				ClusterChild resource = new ClusterChild();
				resource.setPortfolioId(getResourceName(rs.getString("portfolioid")));
				resource.setComponentCount(rs.getInt("componentcount"));
				return resource;
			}
		});
		return list;
	}
}
