/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class ScriptIdentifierDTO {
	
	private int categoryid;
	private int companyid;
	private int executionmode;
	private List<ParameterDTO> parameters;
	private String password;
	private String referencekey;
	private String remoteservernames;
	private int scriptid;
	private String username;
	private String responsenotificationcallbackurl;
	
	public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
	public int getCompanyid() {
		return companyid;
	}
	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}
	public int getExecutionmode() {
		return executionmode;
	}
	public void setExecutionmode(int executionmode) {
		this.executionmode = executionmode;
	}
	public List<ParameterDTO> getParameters() {
		return parameters;
	}
	public void setParameters(List<ParameterDTO> parameters) {
		this.parameters = parameters;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getReferencekey() {
		return referencekey;
	}
	public void setReferencekey(String referencekey) {
		this.referencekey = referencekey;
	}
	public String getRemoteservernames() {
		return remoteservernames;
	}
	public void setRemoteservernames(String remoteservernames) {
		this.remoteservernames = remoteservernames;
	}
	public int getScriptid() {
		return scriptid;
	}
	public void setScriptid(int scriptid) {
		this.scriptid = scriptid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getResponsenotificationcallbackurl() {
		return responsenotificationcallbackurl;
	}
	public void setResponsenotificationcallbackurl(String responsenotificationcallbackurl) {
		this.responsenotificationcallbackurl = responsenotificationcallbackurl;
	}
}
