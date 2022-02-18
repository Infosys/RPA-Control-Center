/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class RemediationPlanExecution {

	private String remediationplanid;
	private String resourceid;
	private String observableid;
	private String domain;
	private int iapnodetransport;
	private String username;
	private String password;
	private String referencekey;
	private String remoteservernames;
	
	public String getRemediationplanid() {
		return remediationplanid;
	}
	public void setRemediationplanid(String remediationplanid) {
		this.remediationplanid = remediationplanid;
	}
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getObservableid() {
		return observableid;
	}
	public void setObservableid(String observableid) {
		this.observableid = observableid;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getIapnodetransport() {
		return iapnodetransport;
	}
	public void setIapnodetransport(int iapnodetransport) {
		this.iapnodetransport = iapnodetransport;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getReferencekey() {
		return referencekey;
	}
	public void setReferencekey(String referencekey) {
		this.referencekey = referencekey;
	}
	public String getRemoteservernames() {
		return remoteservernames;
	}
	public void setRemoteservernames(String remoteservernames) {
		this.remoteservernames = remoteservernames;
	}
}
