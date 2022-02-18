/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.facade;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dao.ResourceDAO;
import com.infosys.aiauto.operationsportal.dto.ResourceHistory;

@Component
public class ResourceFacade {

	@Autowired
	ResourceDAO resourceDAO;

	public List<ResourceHistory> getResourceHistory(String[] resourceId, String[] obsList) throws SQLException {
		List<ResourceHistory> data = new ArrayList<ResourceHistory>();

		data = resourceDAO.getResourceHistoryDetails(resourceId, obsList);
		return data;
	}

	public ResourceHistory getFilteredResourceHistory(String resourceId, String obsId, String startDate, String endDate)
			throws SQLException, ParseException {
		ResourceHistory data = new ResourceHistory();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		Date stDate = format.parse(startDate);
		Date edDate = format.parse(endDate);
		data = resourceDAO.getFilteredResourceHistoryDetails(resourceId, obsId, stDate, edDate);
		return data;
	}

}
