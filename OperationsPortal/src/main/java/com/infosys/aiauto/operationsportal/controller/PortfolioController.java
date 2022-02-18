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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.aiauto.operationsportal.dto.PortfolioHistory;
import com.infosys.aiauto.operationsportal.dto.RecentComponents;
import com.infosys.aiauto.operationsportal.dto.ReportDetails;
import com.infosys.aiauto.operationsportal.dto.ResourceType;
import com.infosys.aiauto.operationsportal.facade.PortfolioFacade;



/**
 * @author Infosys
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value="/portfolios",produces = MediaType.APPLICATION_JSON_VALUE)
public class PortfolioController {

	private static Logger LOG = LoggerFactory.getLogger(PortfolioController.class);

	@Autowired
	PortfolioFacade portfolioFacade;

	@RequestMapping(method = RequestMethod.GET, value = "/{portfolioId}/component-health-counts")
	public ResponseEntity<PortfolioHistory> getPortfolioHistory(@PathVariable("portfolioId") String portfolioId) {
		LOG.info("getPortfolioHistory : started *************");
		PortfolioHistory portHistory = new PortfolioHistory();

		try {
			if (!portfolioId.isEmpty()) {
				portHistory = portfolioFacade.getHealthCheckExecHistory(portfolioId,1);
			} else {
				LOG.error("PortfolioId not provided");
			}
			LOG.info("getPortfolioHistory : ended *************");
			return new ResponseEntity<PortfolioHistory>(portHistory, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new ResponseEntity<PortfolioHistory>(portHistory, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{portfolioId}/component-health-details")
	public ResponseEntity<List<ReportDetails>> getPortfolioDetailsById(@PathVariable("portfolioId") String portfolioId) {
		LOG.info("getPortfolioReport : started *************");

		List<ReportDetails> portfolioReport = new ArrayList<ReportDetails>();
		try {
			if (portfolioId != "") {
				portfolioReport = portfolioFacade.getPortfolioReportDetails(portfolioId);
			} else {
				LOG.error("portfolioId/incidentId not provided");
			}
			LOG.info("getPortfolioReport : ended *************");
			return new ResponseEntity<List<ReportDetails>>(portfolioReport, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new ResponseEntity<List<ReportDetails>>(portfolioReport, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{portfolioId}/recent")
	public ResponseEntity<List<RecentComponents>> getComponentDetailsById(@PathVariable("portfolioId") String portfolioId) {
		LOG.info("getComponentDetailsById : started *************");

		List<RecentComponents> portfolioReport = new ArrayList<RecentComponents>();
		try {
			if (portfolioId != "") {
				portfolioReport = portfolioFacade.getRecentComponents(portfolioId);
			} else {
				LOG.error("portfolioId/incidentId not provided");
			}
			LOG.info("getPortfolioReport : ended *************");
			return new ResponseEntity<List<RecentComponents>>(portfolioReport, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new ResponseEntity<List<RecentComponents>>(portfolioReport, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/resourcetypes")
	public ResponseEntity<List<ResourceType>> getType() {
		
		List<ResourceType> resourceType = new ArrayList<ResourceType>();
		
		try {
			portfolioFacade.getResourceType();
			resourceType = portfolioFacade.getResourceTypeList();
			return new ResponseEntity<List<ResourceType>>(resourceType, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new ResponseEntity<List<ResourceType>>(resourceType, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

}
