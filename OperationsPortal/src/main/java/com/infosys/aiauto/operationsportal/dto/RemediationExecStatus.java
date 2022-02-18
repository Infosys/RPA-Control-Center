/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class RemediationExecStatus {

	private String remediationplanexecid;
	private String remediationplanid;
	private String status;
	
	public String getRemediationplanexecid() {
		return remediationplanexecid;
	}
	public void setRemediationplanexecid(String remediationplanexecid) {
		this.remediationplanexecid = remediationplanexecid;
	}
	public String getRemediationplanid() {
		return remediationplanid;
	}
	public void setRemediationplanid(String remediationplanid) {
		this.remediationplanid = remediationplanid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
