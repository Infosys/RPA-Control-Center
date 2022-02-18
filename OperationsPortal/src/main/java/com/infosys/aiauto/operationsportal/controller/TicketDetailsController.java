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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.aiauto.operationsportal.dto.TicketDetails;
import com.infosys.aiauto.operationsportal.facade.TicketDetailsFacade;


@RestController
@CrossOrigin
@RequestMapping(value="/tickets")
public class TicketDetailsController {
	
	private static Logger LOG = LoggerFactory.getLogger(TicketDetailsController.class);
	
	@Autowired
	TicketDetailsFacade ticketDetailsFacade;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicketDetails> getAllTickets() {
		TicketDetails allTicketDetails = new TicketDetails();
		
		try {
			allTicketDetails = ticketDetailsFacade.getAllTicketDetails();
			return new ResponseEntity<TicketDetails>(allTicketDetails, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<TicketDetails>(allTicketDetails, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
