/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
  
 
package com.infosys.aiauto.operationsportal.facade;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dao.ExpertsDao;
import com.infosys.aiauto.operationsportal.dto.Expert;

*//**
 * @author Infosys
 *
 *//*
@Component
public class ExpertsFacade {

	@Autowired
	private ExpertsDao expertsDao;
	
	public List<Expert> getAllExperts(String inputJSON) throws JSONException {
		// fetch list of all platforms
		String obsId = "", restypeId = "";
		JSONObject statusDetailsJSON = new JSONObject(inputJSON);
		obsId = statusDetailsJSON.getString("observableid");
		restypeId = statusDetailsJSON.getString("resourcetypeid");
		List<Expert> allExperts = new ArrayList<Expert>();
		allExperts = expertsDao.getAllExperts(obsId, restypeId);
		return allExperts;
	}


}
*/