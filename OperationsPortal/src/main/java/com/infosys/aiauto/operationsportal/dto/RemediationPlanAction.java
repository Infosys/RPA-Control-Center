/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

public class RemediationPlanAction {

	private String remediationplanid;
	private String remediationplanactionid;
	private String status;
	private String logdata;
	private String output;
	private String scriptname;
	
	public String getRemediationplanid() {
		return remediationplanid;
	}
	public void setRemediationplanid(String remediationplanid) {
		this.remediationplanid = remediationplanid;
	}
	public String getRemediationplanactionid() {
		return remediationplanactionid;
	}
	public void setRemediationplanactionid(String remediationplanactionid) {
		this.remediationplanactionid = remediationplanactionid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLogdata() {
		return logdata;
	}
	public void setLogdata(String logdata) {
		this.logdata = logdata;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getScriptname() {
		return scriptname;
	}
	public void setScriptname(String scriptname) {
		this.scriptname = scriptname;
	}
}
