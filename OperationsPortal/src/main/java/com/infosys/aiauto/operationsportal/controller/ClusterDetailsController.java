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

import com.infosys.aiauto.operationsportal.dto.Cluster;
import com.infosys.aiauto.operationsportal.dto.ClusterChild;
import com.infosys.aiauto.operationsportal.dto.ClusterComponent;
import com.infosys.aiauto.operationsportal.dto.ClusterDetails;
import com.infosys.aiauto.operationsportal.facade.ClusterDetailsFacade;

@RestController
@CrossOrigin
@RequestMapping(value = "/clusters")
public class ClusterDetailsController {

	private static Logger LOG = LoggerFactory.getLogger(ClusterDetailsController.class);
	
	@Autowired
	ClusterDetailsFacade clusterDetailsFacade;

	@RequestMapping(method = RequestMethod.GET, value = "/health-details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClusterDetails>> getAllClusters() {
		List<ClusterDetails> allClusterList = new ArrayList<ClusterDetails>();

		try {
			allClusterList = clusterDetailsFacade.getAllClusters();
			return new ResponseEntity<List<ClusterDetails>>(allClusterList, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<ClusterDetails>>(allClusterList, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cluster>> getClusters() {
		List<Cluster> allClusterList = new ArrayList<Cluster>();

		try {
			allClusterList = clusterDetailsFacade.getClustersDetails();
			return new ResponseEntity<List<Cluster>>(allClusterList, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<Cluster>>(allClusterList, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/health-counts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClusterComponent> getClustersDetails() {
		List<ClusterDetails> allClusterList = new ArrayList<ClusterDetails>();
		ClusterComponent allClusterComponent = new ClusterComponent();

		try {
			allClusterList = clusterDetailsFacade.getAllClusters();
			allClusterComponent = clusterDetailsFacade.getClusterDetails(allClusterList);
			return new ResponseEntity<ClusterComponent>(allClusterComponent, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<ClusterComponent>(allClusterComponent, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{clusterid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClusterChild>> getClustersChild(@PathVariable("clusterid") String clusterid) {
		List<ClusterChild> allClusterChild = new ArrayList<ClusterChild>();

		try {
			allClusterChild = clusterDetailsFacade.getClusterChilds(clusterid);
			return new ResponseEntity<List<ClusterChild>>(allClusterChild, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<ClusterChild>>(allClusterChild, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
