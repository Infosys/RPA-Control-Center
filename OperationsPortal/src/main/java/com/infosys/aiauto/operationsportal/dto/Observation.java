/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class Observation {

	private String resourcename;
	private String observationname;
	private float metricvalue;
	private String metrictime;
	
	public String getResourcename() {
		return resourcename;
	}
	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	public String getObservationname() {
		return observationname;
	}
	public void setObservationname(String observationname) {
		this.observationname = observationname;
	}
	public float getMetricvalue() {
		return metricvalue;
	}
	public void setMetricvalue(float metricvalue) {
		this.metricvalue = metricvalue;
	}
	public String getMetrictime() {
		return metrictime;
	}
	public void setMetrictime(String metrictime) {
		this.metrictime = metrictime;
	}
}
