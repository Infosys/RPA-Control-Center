/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

public class ParameterDTO {

	private String parametername;
	private String parametervalue;

	public String getParametername() {
		return parametername;
	}
	public void setParametername(String parametername) {
		this.parametername = parametername;
	}
	public String getParametervalue() {
		return parametervalue;
	}
	public void setParametervalue(String parametervalue) {
		this.parametervalue = parametervalue;
	}
}
