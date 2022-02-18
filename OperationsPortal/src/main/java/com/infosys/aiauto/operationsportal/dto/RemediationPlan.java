/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class RemediationPlan {

	private String remediationplanid;
	private String sourcetype;
	
	public String getRemediationplanid() {
		return remediationplanid;
	}
	public void setRemediationplanid(String remediationplanid) {
		this.remediationplanid = remediationplanid;
	}
	public String getSourcetype() {
		return sourcetype;
	}
	public void setSourcetype(String sourcetype) {
		this.sourcetype = sourcetype;
	}
}
