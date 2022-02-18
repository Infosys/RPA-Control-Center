/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

public class Remediation {

	private String remediationPlanid;
	private String remediationPlanName;
	private String status;
	private String dateCreated;
	private String lastRunDateTime;
	private String lastRunBy;
	private String runDuration;
	private boolean isUserDefined;
	
	public String getRemediationPlanid() {
		return remediationPlanid;
	}
	public void setRemediationPlanid(String remediationPlanid) {
		this.remediationPlanid = remediationPlanid;
	}
	public String getRemediationPlanName() {
		return remediationPlanName;
	}
	public void setRemediationPlanName(String remediationPlanName) {
		this.remediationPlanName = remediationPlanName;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getLastRunDateTime() {
		return lastRunDateTime;
	}
	public void setLastRunDateTime(String lastRunDateTime) {
		this.lastRunDateTime = lastRunDateTime;
	}
	public String getLastRunBy() {
		return lastRunBy;
	}
	public void setLastRunBy(String lastRunBy) {
		this.lastRunBy = lastRunBy;
	}
	public String getRunDuration() {
		return runDuration;
	}
	public void setRunDuration(String runDuration) {
		this.runDuration = runDuration;
	}
	public boolean isUserDefined() {
		return isUserDefined;
	}
	public void setUserDefined(boolean isUserDefined) {
		this.isUserDefined = isUserDefined;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
