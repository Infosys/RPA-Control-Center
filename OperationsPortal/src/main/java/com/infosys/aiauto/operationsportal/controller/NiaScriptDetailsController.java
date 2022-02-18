/* 
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
   
package com.infosys.aiauto.operationsportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.aiauto.operationsportal.dto.NodesContent;
import com.infosys.aiauto.operationsportal.dto.ScriptDetails;
import com.infosys.aiauto.operationsportal.facade.NiaScriptFacade;
import com.infosys.niasln.adapter.exception.AdapterException;

@RestController
@CrossOrigin
@RequestMapping(value = "/nia-api")
public class NiaScriptDetailsController {
	
	 * path - /nia
	 * getScriptsByCategory endpoint /category/<categoryid>/scripts
	 * getAllScripts endpoint /scripts/all 
	 * getAllCategories enpoint - /categories/all
	 
	private static Logger LOG = LoggerFactory.getLogger(NiaScriptDetailsController.class);
	
	@Autowired
	NiaScriptFacade niaScriptFacade;
	
	// TODO - Add request param as platformname 
	@RequestMapping(method = RequestMethod.GET, value = "/category/{categoryid}/scripts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ScriptDetails>> getAllScriptsByCategory(@PathVariable("categoryid") String categoryid) {
		List<ScriptDetails> allScripts = new ArrayList<ScriptDetails>();
		try {
			allScripts = niaScriptFacade.getScriptsByCategoryId(categoryid);
			return new ResponseEntity<List<ScriptDetails>>(allScripts,HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<ScriptDetails>>(allScripts, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	// TODO - Add platformname as request type
	@RequestMapping(method = RequestMethod.GET, value = "/scripts/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ScriptDetails>> getAllScripts() {
		List<ScriptDetails> allScripts = new ArrayList<ScriptDetails>();
		try {
			allScripts = niaScriptFacade.getScripts();
			return new ResponseEntity<List<ScriptDetails>>(allScripts,HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<ScriptDetails>>(allScripts, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	// TODO -- getAllCategories  /{platformname}/categories
	@RequestMapping(method = RequestMethod.GET, value ="/categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getAllCategories() {
		JSONArray categories = new JSONArray();
		try {
			categories = niaScriptFacade.getCategories();
			System.out.println("***printcats*** "+ categories);
			return new ResponseEntity<String>(categories.toString(), HttpStatus.OK);
		} catch (AdapterException e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<String>(categories.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/nodes/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<NodesContent>> getAllNodes(@RequestBody String components) {
		List<NodesContent> allNodes = new ArrayList<NodesContent>();
		try {
			allNodes = niaScriptFacade.getAllNodes(components);
			return new ResponseEntity<List<NodesContent>>(allNodes,HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<NodesContent>>(allNodes, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
*/