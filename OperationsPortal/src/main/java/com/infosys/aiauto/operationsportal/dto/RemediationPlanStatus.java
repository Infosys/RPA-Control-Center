/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class RemediationPlanStatus {
	
	private List<RemediationExecStatus> status;

	public List<RemediationExecStatus> getStatus() {
		return status;
	}

	public void setStatus(List<RemediationExecStatus> status) {
		this.status = status;
	}
}
