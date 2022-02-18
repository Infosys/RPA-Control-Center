 /*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
package com.infosys.aiauto.operationsportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.aiauto.operationsportal.dto.ResourceHistory;
import com.infosys.aiauto.operationsportal.dto.ResourceObservationComponent;
import com.infosys.aiauto.operationsportal.facade.ResourceFacade;

@RestController
@CrossOrigin
@RequestMapping(value = "/resource", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResourceDetailsController {

	private static Logger LOG = LoggerFactory.getLogger(ResourceDetailsController.class);

	@Autowired
	private ResourceFacade resourceFacade;

	@RequestMapping(method = RequestMethod.POST, value = "/history", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ResourceHistory>> getPortfolioHistory(
			@RequestBody ResourceObservationComponent[] resObsComponent) {
		LOG.info("getPortfolioHistory : started *************");
		List<ResourceHistory> resourceHistory = new ArrayList<ResourceHistory>();

		ArrayList<String> resList = new ArrayList<String>();
		ArrayList<String> obsList = new ArrayList<String>();
		try {
			for (int i = 0; i < resObsComponent.length; i++) {
				resList.add(resObsComponent[i].getResourceid());
				obsList.add(resObsComponent[i].getObservableid());
			}

			String[] resArr = new String[resList.size()];
			String[] obsArr = new String[obsList.size()];

			if (resObsComponent.length == 0) {
				LOG.error("ResourceId's not provided");
			} else {
				resArr = resList.toArray(resArr);
				obsArr = obsList.toArray(obsArr);
				resourceHistory = resourceFacade.getResourceHistory(resArr, obsArr);
			}

			LOG.info("getPortfolioHistory : ended *************");
			return new ResponseEntity<List<ResourceHistory>>(resourceHistory, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new ResponseEntity<List<ResourceHistory>>(resourceHistory, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/filtereddata", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ResourceHistory>> getFilteredPortfolioHistory(
			@RequestBody ResourceHistory[] resHistory) {
		LOG.info("getPortfolioHistory : started *************");
		List<ResourceHistory> resourceHistory = new ArrayList<ResourceHistory>();

		try {
			for(int i=0 ; i<resHistory.length ; i++) {
				resourceHistory.add(resourceFacade.getFilteredResourceHistory(resHistory[i].getResourceId(), resHistory[i].getObservableId(), resHistory[i].getStartDate(), resHistory[i].getEndDate()));
			}
			LOG.info("getPortfolioHistory : ended *************");
			return new ResponseEntity<List<ResourceHistory>>(resourceHistory, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new ResponseEntity<List<ResourceHistory>>(resourceHistory, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
