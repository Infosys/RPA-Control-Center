/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class ScriptContent {

	private int CategoryId;
	private List<NodesContent> Nodes;
	private String Operation;
	private List<ScriptParams>Parameters;
	private String Password;
	private String ReferenceKey;
	private int ScriptId;
	private String UserName;
	private String WEMScriptServiceUrl;
	
	public int getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(int categoryId) {
		CategoryId = categoryId;
	}
	public List<NodesContent> getNodes() {
		return Nodes;
	}
	public void setNodes(List<NodesContent> nodes) {
		Nodes = nodes;
	}
	public String getOperation() {
		return Operation;
	}
	public void setOperation(String operation) {
		Operation = operation;
	}
	public List<ScriptParams> getParameters() {
		return Parameters;
	}
	public void setParameters(List<ScriptParams> parameters) {
		Parameters = parameters;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getReferenceKey() {
		return ReferenceKey;
	}
	public void setReferenceKey(String referenceKey) {
		ReferenceKey = referenceKey;
	}
	public int getScriptId() {
		return ScriptId;
	}
	public void setScriptId(int scriptId) {
		ScriptId = scriptId;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getWEMScriptServiceUrl() {
		return WEMScriptServiceUrl;
	}
	public void setWEMScriptServiceUrl(String wEMScriptServiceUrl) {
		WEMScriptServiceUrl = wEMScriptServiceUrl;
	}
}
