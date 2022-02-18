/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class ScriptParams {
	
	private String ParameterName;
	private String ParameterValue;
	
	public String getParameterName() {
		return ParameterName;
	}
	public void setParameterName(String parameterName) {
		ParameterName = parameterName;
	}
	public String getParameterValue() {
		return ParameterValue;
	}
	public void setParameterValue(String parameterValue) {
		ParameterValue = parameterValue;
	}
}
