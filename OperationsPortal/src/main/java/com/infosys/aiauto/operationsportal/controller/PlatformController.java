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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.aiauto.operationsportal.dto.NiaPlatform;
import com.infosys.aiauto.operationsportal.facade.PlatformFacade;

/**
 * @author Devesh_Mankar
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value="/platform")
public class PlatformController {
	
	private static Logger LOG = LoggerFactory.getLogger(PlatformController.class);
	
	@Autowired
	private PlatformFacade platformFacade;
	
	@RequestMapping(method=RequestMethod.GET, value= "/all")
	public ResponseEntity<List<NiaPlatform>> getAllPlatforms() {
		// Return the list of all platforms
		List<NiaPlatform> platforms = new ArrayList<NiaPlatform> ();
		try {
			platforms = platformFacade.getAllPlatforms();
			return new ResponseEntity<List<NiaPlatform>>(platforms, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<List<NiaPlatform>>(platforms, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
