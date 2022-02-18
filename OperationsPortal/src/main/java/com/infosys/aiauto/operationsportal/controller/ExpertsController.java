/* 
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
  
package com.infosys.aiauto.operationsportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.aiauto.operationsportal.dto.Expert;
import com.infosys.aiauto.operationsportal.dto.RemediationPlan;
import com.infosys.aiauto.operationsportal.facade.ExpertsFacade;


@RestController
@CrossOrigin
@RequestMapping(value="/experts")
public class ExpertsController {
	
	private static Logger LOG = LoggerFactory.getLogger(ExpertsController.class);
	
	@Autowired
	private ExpertsFacade expertsFacade;

	@RequestMapping(method=RequestMethod.POST, value= "/all")
	public ResponseEntity<List<Expert>> getAllExpertsByComponents(@RequestBody String obj) {
		// Return the list of all platforms
		List<Expert> experts = new ArrayList<Expert> ();
		try {
			experts = expertsFacade.getAllExperts(obj);
			return new ResponseEntity <List<Expert>> (experts, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<List<Expert>> (experts, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
*/