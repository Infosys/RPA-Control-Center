/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class ClusterChild {

	private String portfolioId;
	private int componentCount;
	private String status;
	
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public int getComponentCount() {
		return componentCount;
	}
	public void setComponentCount(int componentCount) {
		this.componentCount = componentCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
