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
public class Expert {
	private String expertId;
	private String expertName;
	private String expertChatId;
	private String expertise;
	/**
	 * @return the platformId
	 */
	public String getExpertId() {
		return expertId;
	}
	/**
	 * @param platformId the platformId to set
	 */
	public void setExpertId(String platformId) {
		this.expertId = platformId;
	}
	/**
	 * @return the platformName
	 */
	public String getExpertName() {
		return expertName;
	}
	/**
	 * @param platformName the platformName to set
	 */
	public void setExpertName(String platformName) {
		this.expertName = platformName;
	}
	/**
	 * @return the expertChatId
	 */
	public String getExpertChatId() {
		return expertChatId;
	}
	/**
	 * @param expertChatId the expertChatId to set
	 */
	public void setExpertChatId(String expertChatId) {
		this.expertChatId = expertChatId;
	}
	/**
	 * @return the expertise
	 */
	public String getExpertise() {
		return expertise;
	}
	/**
	 * @param expertise the expertise to set
	 */
	public void setExpertise(String expertise) {
		this.expertise = expertise;
	} 
}
