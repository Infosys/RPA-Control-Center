/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

public class ClusterComponent {

	private int totalCount;
	private int criticalCount;
	private int warningCount;
	private int healthyCount;
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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
}
