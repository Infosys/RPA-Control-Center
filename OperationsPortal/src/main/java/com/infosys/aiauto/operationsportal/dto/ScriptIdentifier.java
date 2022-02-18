/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class ScriptIdentifier {
	
	private String remediationplanexecid;
	private String remediationplanactionid;
	private ScriptsDetails plandetails;

	public String getRemediationplanexecid() {
		return remediationplanexecid;
	}

	public void setRemediationplanexecid(String remediationplanexecid) {
		this.remediationplanexecid = remediationplanexecid;
	}

	public ScriptsDetails getPlandetails() {
		return plandetails;
	}

	public void setPlandetails(ScriptsDetails plandetails) {
		this.plandetails = plandetails;
	}

	public String getRemediationplanactionid() {
		return remediationplanactionid;
	}

	public void setRemediationplanactionid(String remediationplanactionid) {
		this.remediationplanactionid = remediationplanactionid;
	}
}
