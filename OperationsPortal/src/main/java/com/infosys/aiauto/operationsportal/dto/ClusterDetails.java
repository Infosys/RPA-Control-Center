/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class ClusterDetails {
	
	private String clusterId;
	private String clusterName;
	private String dateCreated;
	private String resourceType;
	private List<Portfolio> clusterChild;
	private String clusterState;
	private int portfolioCount;
	private int criticalCount;
	private int warningCount;
	private int healthyCount;
	
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public List<Portfolio> getClusterChild() {
		return clusterChild;
	}
	public void setClusterChild(List<Portfolio> clusterChild) {
		this.clusterChild = clusterChild;
	}
	public String getClusterState() {
		return clusterState;
	}
	public void setClusterState(String clusterState) {
		this.clusterState = clusterState;
	}
	public int getPortfolioCount() {
		return portfolioCount;
	}
	public void setPortfolioCount(int portfolioCount) {
		this.portfolioCount = portfolioCount;
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
