/*
  *Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class TestRemediation {
	
	private String remediationplanid;
	private String remediationplanname;
	private String remediationplan;
	
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
	public String getRemediationplan() {
		return remediationplan;
	}
	public void setRemediationplan(String remediationplan) {
		this.remediationplan = remediationplan;
	}
}
