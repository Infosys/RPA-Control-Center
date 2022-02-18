/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class NodesContent {
	
	private int id;
	private String ipAddress;
	private String name;
	private String os;
	private String password;
	private List<String> serviceAreas;
	private String stagePath;
	private String userId;
	private String workingDir;
	private boolean defaultNode;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getServiceAreas() {
		return serviceAreas;
	}
	public void setServiceAreas(List<String> serviceAreas) {
		this.serviceAreas = serviceAreas;
	}
	public String getStagePath() {
		return stagePath;
	}
	public void setStagePath(String stagePath) {
		this.stagePath = stagePath;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWorkingDir() {
		return workingDir;
	}
	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}
	public boolean isDefaultNode() {
		return defaultNode;
	}
	public void setDefaultNode(boolean defaultNode) {
		this.defaultNode = defaultNode;
	}
}
