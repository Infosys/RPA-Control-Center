/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class Cluster {
	private String clusterId;
	private String clusterName;
	private List<ClusterPortfolio> portfolios;
		
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
	public List<ClusterPortfolio> getPortfolios() {
		return portfolios;
	}
	public void setPortfolios(List<ClusterPortfolio> portfolios) {
		this.portfolios = portfolios;
	}
}
