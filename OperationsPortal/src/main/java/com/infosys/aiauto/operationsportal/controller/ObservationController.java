 /*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
package com.infosys.aiauto.operationsportal.controller;

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

import com.infosys.aiauto.operationsportal.dto.Observation;
import com.infosys.aiauto.operationsportal.dto.RemediationPlan;
import com.infosys.aiauto.operationsportal.facade.ObservationFacade;

@RestController
@CrossOrigin
@RequestMapping(value = "/observations")
public class ObservationController {

	private static Logger LOG = LoggerFactory.getLogger(ObservationController.class);
	
	@Autowired
	private ObservationFacade observationFacade;
	
	@RequestMapping(method = RequestMethod.POST, value = "/observation_details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveObservationDetails(@RequestBody Observation observation){
		String observationDetailsJson = "";
		
		try{
			observationDetailsJson = observationFacade.saveObservationDetail(observation);
			LOG.info("Successfully completed.");
			return new ResponseEntity<String>(observationDetailsJson, HttpStatus.OK);
			
		} catch (Exception e) {
			observationDetailsJson = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(observationDetailsJson, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
