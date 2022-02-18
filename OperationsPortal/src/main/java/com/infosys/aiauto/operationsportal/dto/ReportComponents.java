/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

import java.util.HashMap;
import java.util.Map;

public class ReportComponents {
	
	
	private String resourcetypeId;
	private String resourceTypeID;
	private String resourceId;
	private String resourceName;
	private String observableName;
	private String value;
	private String serverState;
	private String threshold;
	private String thresholdType;
	private String observationTime;
	private String observableId;
	
	private Map<String, Integer> serverStateCount = new HashMap<String, Integer>();
	
	
	public String getResourcetypeId() {
		return resourcetypeId;
	}
	public void setResourcetypeId(String resourcetypeId) {
		this.resourcetypeId = resourcetypeId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getObservableName() {
		return observableName;
	}
	public void setObservableName(String observableName) {
		this.observableName = observableName;
	}
	
	
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getServerState() {
		return serverState;
	}
	public void setServerState(String serverState) {
		this.serverState = serverState;
	}
	public String getThresholdType() {
		return thresholdType;
	}
	public void setThresholdType(String thresholdType) {
		this.thresholdType = thresholdType;
	}
	public Map<String, Integer> getServerStateCount() {
		return serverStateCount;
	}
	public void setServerStateCount(Map<String, Integer> serverStateCount) {
		this.serverStateCount = serverStateCount;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}
	public String getObservableId() {
		return observableId;
	}
	public void setObservableId(String observableId) {
		this.observableId = observableId;
	}
	public String getResourceTypeID() {
		return resourceTypeID;
	}
	public void setResourceTypeID(String resourceTypeID) {
		this.resourceTypeID = resourceTypeID;
	}
}
