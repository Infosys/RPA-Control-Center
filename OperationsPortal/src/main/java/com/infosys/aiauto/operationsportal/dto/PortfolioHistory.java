/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class PortfolioHistory {

	private String resourceId;
	private String resourceName;
	private List<ThresholdSummary> statusByDate;
	private int healthyCount;
	private int criticalCount;
	private int warningCount;

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

	public List<ThresholdSummary> getStatusByDate() {
		return statusByDate;
	}

	public void setStatusBydate(List<ThresholdSummary> statusByDate) {
		this.statusByDate = statusByDate;
	}

	public int getHealthyCount() {
		return healthyCount;
	}

	public void setHealthyCount(int healthyCount) {
		this.healthyCount = healthyCount;
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

}
