/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;


public class Observations {
	
	private String observableName;
	private String value;
	private String serverState;
	private String threshold;
	private String thresholdType;
	private String observationTime;
	private String observableId;
	
	public String getObservableName() {
		return observableName;
	}
	public void setObservableName(String observableName) {
		this.observableName = observableName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getServerState() {
		return serverState;
	}
	public void setServerState(String serverState) {
		this.serverState = serverState;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getThresholdType() {
		return thresholdType;
	}
	public void setThresholdType(String thresholdType) {
		this.thresholdType = thresholdType;
	}
	public String getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}
	public String getObservableId() {
		return observableId;
	}
	public void setObservableId(String observableId) {
		this.observableId = observableId;
	}

}
