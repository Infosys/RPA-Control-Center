/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class ScriptDetails {
	
	private String environmentName;
	private String environmentVersion;
	private List<ProcessDetails> businessProcesses;
	
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	public String getEnvironmentVersion() {
		return environmentVersion;
	}
	public void setEnvironmentVersion(String environmentVersion) {
		this.environmentVersion = environmentVersion;
	}
	public List<ProcessDetails> getBusinessProcesses() {
		return businessProcesses;
	}
	public void setBusinessProcesses(List<ProcessDetails> businessProcesses) {
		this.businessProcesses = businessProcesses;
	}	
}
