/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportDetails {
	
	private String resourcetypeId;
	private String resourceId;
	private String resourceTypeID;
	private String resourceName;
	private String componentHealth;
	private int componentParam;
	private int criticalCount;
	private int warningCount;
	private int healthyCount;
	private List<Observations> observations;
	
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
	public List<Observations> getObservations() {
		return observations;
	}
	public void setObservations(List<Observations> observations) {
		this.observations = new ArrayList<Observations>();
		this.observations.addAll(observations);
	}
	public String getComponentHealth() {
		return componentHealth;
	}
	public void setComponentHealth(String componentHealth) {
		this.componentHealth = componentHealth;
	}
	public int getComponentParam() {
		return componentParam;
	}
	public void setComponentParam(int componentParam) {
		this.componentParam = componentParam;
	}
	public int getCriticalCount() {
		return criticalCount;
	}
	public void setCriticalCount(int criticalCount) {
		this.criticalCount = criticalCount;
	}
	public int getWarningCount() {
		return warningCount;
	}
	public void setWarningCount(int warningCount) {
		this.warningCount = warningCount;
	}
	public int getHealthyCount() {
		return healthyCount;
	}
	public void setHealthyCount(int healthyCount) {
		this.healthyCount = healthyCount;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceTypeID() {
		return resourceTypeID;
	}
	public void setResourceTypeID(String resourceTypeID) {
		this.resourceTypeID = resourceTypeID;
	}
}
