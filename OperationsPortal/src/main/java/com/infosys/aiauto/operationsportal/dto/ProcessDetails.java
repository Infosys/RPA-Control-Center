/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class ProcessDetails {
	
	private String id;
	private String categoryId;
	private String name;
	private String version;
	private String description;
	private List<InputVariable> inputVariables;
	private List<OutputVariable> outputVariables;
	private List<RuntimeResourceDefinition> runtimeResourceDefinition;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<InputVariable> getInputVariables() {
		return inputVariables;
	}
	public void setInputVariables(List<InputVariable> inputVariables) {
		this.inputVariables = inputVariables;
	}
	public List<OutputVariable> getOutputVariables() {
		return outputVariables;
	}
	public void setOutputVariables(List<OutputVariable> outputVariables) {
		this.outputVariables = outputVariables;
	}
	public List<RuntimeResourceDefinition> getRuntimeResourceDefinition() {
		return runtimeResourceDefinition;
	}
	public void setRuntimeResourceDefinition(List<RuntimeResourceDefinition> runtimeResourceDefinition) {
		this.runtimeResourceDefinition = runtimeResourceDefinition;
	}
}
