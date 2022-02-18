/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

public class ResourceObservationComponent {

	private String resourceid;
	private String observableid;
	
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
}
