/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class RemediationDetails {
	
	private String remediationplanid;
	private String remediationplanname;
	private boolean isuserdefined;
	private String servertype;
	private String observabletype;
	private String platformname;
	private List<ActionPlan> actions;
	
	public String getRemediationplanid() {
		return remediationplanid;
	}
	public void setRemediationplanid(String remediationplanid) {
		this.remediationplanid = remediationplanid;
	}
	public String getRemediationplanname() {
		return remediationplanname;
	}
	public void setRemediationplanname(String remediationplanname) {
		this.remediationplanname = remediationplanname;
	}
	public boolean isIsuserdefined() {
		return isuserdefined;
	}
	public void setIsuserdefined(boolean isuserdefined) {
		this.isuserdefined = isuserdefined;
	}
	public List<ActionPlan> getActions() {
		return actions;
	}
	public void setActions(List<ActionPlan> actions) {
		this.actions = actions;
	}
	public String getServertype() {
		return servertype;
	}
	public void setServertype(String servertype) {
		this.servertype = servertype;
	}
	public String getObservabletype() {
		return observabletype;
	}
	public void setObservabletype(String observabletype) {
		this.observabletype = observabletype;
	}
	public String getPlatformname() {
		return platformname;
	}
	public void setPlatformname(String platformname) {
		this.platformname = platformname;
	}
}
