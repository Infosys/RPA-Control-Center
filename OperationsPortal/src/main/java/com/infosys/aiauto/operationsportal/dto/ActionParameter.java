/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

public class ActionParameter {

	private String remediationplanactionid;
	private int paramid;
	private String name;
	private boolean ismandatory;
	private String defaultvalue;
	private String providedvalue;
	private boolean isfield;
	
	public String getRemediationplanactionid() {
		return remediationplanactionid;
	}
	public void setRemediationplanactionid(String remediationplanactionid) {
		this.remediationplanactionid = remediationplanactionid;
	}
	public int getParamid() {
		return paramid;
	}
	public void setParamid(int paramid) {
		this.paramid = paramid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIsmandatory() {
		return ismandatory;
	}
	public void setIsmandatory(boolean ismandatory) {
		this.ismandatory = ismandatory;
	}
	public String getDefaultvalue() {
		return defaultvalue;
	}
	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}
	public String getProvidedvalue() {
		return providedvalue;
	}
	public void setProvidedvalue(String providedvalue) {
		this.providedvalue = providedvalue;
	}
	public boolean isIsfield() {
		return isfield;
	}
	public void setIsfield(boolean isfield) {
		this.isfield = isfield;
	}
}
