/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class ScriptActivity {
	
	private String remediationplanid;
	private List<ScriptsContentDetails> scriptdetails;
	
	public String getRemediationplanid() {
		return remediationplanid;
	}
	public void setRemediationplanid(String remediationplanid) {
		this.remediationplanid = remediationplanid;
	}
	public List<ScriptsContentDetails> getScriptdetails() {
		return scriptdetails;
	}
	public void setScriptdetails(List<ScriptsContentDetails> scriptdetails) {
		this.scriptdetails = scriptdetails;
	}
}
