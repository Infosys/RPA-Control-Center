/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

/**
 * @author Infosys
 *
 */
public class NiaPlatform {
	private String platformId;
	private String platformName;
	private int executionMode;
	/**
	 * @return the platformId
	 */
	public String getPlatformId() {
		return platformId;
	}
	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	/**
	 * @return the platformName
	 */
	public String getPlatformName() {
		return platformName;
	}
	/**
	 * @param platformName the platformName to set
	 */
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	
	public int getExecutionMode() {
		return executionMode;
	}
	
	public void setExecutionMode(int executionMode) {
		this.executionMode = executionMode;
	}
	
}
