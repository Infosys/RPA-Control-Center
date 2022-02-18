/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

public class ResourceHistory {

	private String resourceId;
	private String resourceName;
	private String[] observableValue;
	private String[] thresholdValue;
	private String[] upperthresholdValue;
	private String[] currentDate;
	private String observable;
	private String startDate;
	private String endDate;
	private String observableId;
	private String unitOfMeasure;
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String[] getObservableValue() {
		return observableValue;
	}
	public void setObservableValue(String[] observableValue) {
		this.observableValue = observableValue;
	}
	public String[] getThresholdValue() {
		return thresholdValue;
	}
	public void setThresholdValue(String[] thresholdValue) {
		this.thresholdValue = thresholdValue;
	}
	public String getObservable() {
		return observable;
	}
	public void setObservable(String observable) {
		this.observable = observable;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String[] getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(String[] currentDate) {
		this.currentDate = currentDate;
	}
	public String getObservableId() {
		return observableId;
	}
	public void setObservableId(String observableId) {
		this.observableId = observableId;
	}
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	public String[] getUpperthresholdValue() {
		return upperthresholdValue;
	}
	public void setUpperthresholdValue(String[] upperthresholdValue) {
		this.upperthresholdValue = upperthresholdValue;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
}
