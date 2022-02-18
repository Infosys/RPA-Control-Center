/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ThresholdSummary {

	private String incidentId;
	private Date startTime;
	private Date endTime;
	private int components;
	private int countOfChecks;
	private String portfolioId;
	private String configID;

	private Map<String, Integer> stateCount = new HashMap<String, Integer>();

	public String getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}

	public int getComponents() {
		return components;
	}

	public void setComponents(int components) {
		this.components = components;
	}

	public int getCountOfChecks() {
		return countOfChecks;
	}

	public void setCountOfChecks(int countOfChecks) {
		this.countOfChecks = countOfChecks;
	}

	public Map<String, Integer> getStateCount() {
		return stateCount;
	}

	public void setStateCount(Map<String, Integer> stateCount) {
		this.stateCount = stateCount;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}

	public String getConfigID() {
		return configID;
	}

	public void setConfigID(String configID) {
		this.configID = configID;
	}

	

}
