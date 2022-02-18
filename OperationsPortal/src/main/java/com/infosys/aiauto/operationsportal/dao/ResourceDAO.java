 /*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dto.ResourceHistory;

@Component
public class ResourceDAO {

	@Autowired
	private DataSource datasource;
	
	@Value("${resourceNameQuery}")
	private String resourceNameQuery;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void setNamedParameterJdbcTemplate(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	@Value("${resourceHistoryDetailsQuery}")
	private String resourceHistoryDetailsQuery;

	@Value("${resourceActualValuesQuery}")
	private String resourceActualValuesQuery;
	
	@Value("${resourceFilterValuesQuery}")
	private String resourceFilterValuesQuery;
	
	@Value("${observableNameQuery}")
	private String observableNameQuery;

	public List<ResourceHistory> getResourceHistoryDetails(String[] resourceIdstr, String[] obsIdstr)
			throws SQLException {
		List<ResourceHistory> list = new ArrayList<ResourceHistory>();
		for (int i = 0; i < resourceIdstr.length; i++) {
			List<ResourceHistory> resHistList = getResourceHistDetails(resourceIdstr[i], obsIdstr[i]);
			for (int j = 0; j < resHistList.size(); j++) {
				list.add(resHistList.get(j));
			}
		}

		return list;
	}

	public ResourceHistory getFilteredResourceHistoryDetails(String resourceIdstr, String obsIdstr, Date startDate,
			Date endDate) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		JdbcTemplate template = new JdbcTemplate(datasource);
		
		List<ResourceHistory> list = template.query(observableNameQuery, new Object[] { obsIdstr },
			new RowMapper<ResourceHistory>() {
				public ResourceHistory mapRow(ResultSet rs, int rownumber) throws SQLException {
					ResourceHistory resource = new ResourceHistory();
					resource.setObservable(rs.getString("observablename"));
					resource.setUnitOfMeasure(rs.getString("unitofmeasure").trim());
					return resource;
				}
			});
		
		ResourceHistory resource = new ResourceHistory();
		resource.setEndDate(sdf.format(endDate));
		resource.setStartDate(sdf.format(startDate));
		resource.setObservableId(obsIdstr);
		resource.setResourceId(resourceIdstr);
		resource.setResourceName(getResourceName(resourceIdstr));
		List<Object[]> resourceData = getFilteredValues(resourceIdstr, obsIdstr, resourceFilterValuesQuery, startDate, endDate);
		resource.setObservableValue((String[]) resourceData.get(0));
		resource.setThresholdValue((String[]) resourceData.get(1));
		resource.setCurrentDate((String[]) resourceData.get(2));
		resource.setUpperthresholdValue((String[]) resourceData.get(3));
		resource.setObservable(list.get(0).getObservable());
		resource.setUnitOfMeasure(list.get(0).getUnitOfMeasure());
		
		return resource;
	}

	public List<ResourceHistory> getResourceHistDetails(String resourceIdstr, String obsIdstr) throws SQLException {
		setNamedParameterJdbcTemplate(datasource);
		Map<String, Object> reportInput = new HashMap<String, Object>();
		reportInput.put("resId", resourceIdstr);
		reportInput.put("obsId", obsIdstr);
		List<ResourceHistory> listData = namedParameterJdbcTemplate.query(resourceHistoryDetailsQuery, reportInput,
				new ResultSetExtractor<List<ResourceHistory>>() {

					public List<ResourceHistory> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<ResourceHistory> list = new ArrayList<ResourceHistory>();
						while (rs.next()) {
							ResourceHistory resource = new ResourceHistory();
							resource.setEndDate(rs.getDate("endtime").toString());
							resource.setStartDate(rs.getDate("starttime").toString());
							resource.setObservable(rs.getString("observablename"));
							resource.setResourceId(rs.getString("resourceid"));
							resource.setResourceName(getResourceName(rs.getString("resourceid")));
							resource.setObservableId(rs.getString("observableid"));
							resource.setUnitOfMeasure(rs.getString("unitofmeasure").trim());
							resource.setObservableValue(
									(String[]) getActualValues(rs.getString("resourceid"), obsIdstr, resourceActualValuesQuery).get(0));
							resource.setThresholdValue(
									(String[]) getActualValues(rs.getString("resourceid"), obsIdstr, resourceActualValuesQuery).get(1));
							resource.setCurrentDate(
									(String[]) getActualValues(rs.getString("resourceid"), obsIdstr, resourceActualValuesQuery).get(2));
							list.add(resource);
							resource.setUpperthresholdValue(
									(String[]) getActualValues(rs.getString("resourceid"), obsIdstr, resourceActualValuesQuery).get(3));

						}
						System.out.println("Is List Empty:? " + list.isEmpty());
						return list;
					}

				});
		return listData;
	}

	public List<Object[]> getActualValues(String resourceId, String observableId, String query) {
		// calling function to get JDBC connection
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Object[]> dataList = new ArrayList<Object[]>();
		List<String> list = template.query(query, new Object[] { resourceId, observableId },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource;
						resource = rs.getString("value");
						return resource;
					}
				});
		String[] values = new String[list.size()];
		values = list.toArray(values);
		dataList.add(values);

		List<String> list1 = template.query(query, new Object[] { resourceId, observableId },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource;
						resource = rs.getString("lowerthreshold");
						return resource;
					}
				});
		String[] thresholdValues = new String[list1.size()];
		thresholdValues = list1.toArray(thresholdValues);
		dataList.add(thresholdValues);

		List<String> list2 = template.query(query, new Object[] { resourceId, observableId },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource;
						resource = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(rs.getTimestamp("observationtime"));
						return resource;
					}
				});
		String[] observationTimeValues = new String[list2.size()];
		observationTimeValues = list2.toArray(observationTimeValues);
		dataList.add(observationTimeValues);
		
		List<String> list3 = template.query(query, new Object[] { resourceId, observableId },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource;
						resource = rs.getString("upperthreshold");
						return resource;
					}
				});
		String[] upperthresholdValues = new String[list3.size()];
		upperthresholdValues = list3.toArray(upperthresholdValues);
		dataList.add(upperthresholdValues);

		return dataList;
	}
	
	public List<Object[]> getFilteredValues(String resourceId, String observableId, String query, Date startDate, Date endDate) {
		// calling function to get JDBC connection
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Object[]> dataList = new ArrayList<Object[]>();
		List<String> list = template.query(query, new Object[] { resourceId, observableId, startDate, endDate },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource;
						resource = rs.getString("value");
						return resource;
					}
				});
		String[] values = new String[list.size()];
		values = list.toArray(values);
		dataList.add(values);

		List<String> list1 = template.query(query, new Object[] { resourceId, observableId, startDate, endDate },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource;
						resource = rs.getString("lowerthreshold");
						return resource;
					}
				});
		String[] thresholdValues = new String[list1.size()];
		thresholdValues = list1.toArray(thresholdValues);
		dataList.add(thresholdValues);

		List<String> list2 = template.query(query, new Object[] { resourceId, observableId, startDate, endDate },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource;
						resource = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(rs.getTimestamp("observationtime"));
						return resource;
					}
				});
		String[] observationTimeValues = new String[list2.size()];
		observationTimeValues = list2.toArray(observationTimeValues);
		dataList.add(observationTimeValues);
		
		List<String> list3 = template.query(query, new Object[] { resourceId, observableId, startDate, endDate  },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource;
						resource = rs.getString("upperthreshold");
						return resource;
					}
				});
		String[] upperthresholdValues = new String[list3.size()];
		upperthresholdValues = list3.toArray(upperthresholdValues);
		dataList.add(upperthresholdValues);
		
		return dataList;
	}
	
	public String getResourceName(String observableId) {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(datasource);
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

}
