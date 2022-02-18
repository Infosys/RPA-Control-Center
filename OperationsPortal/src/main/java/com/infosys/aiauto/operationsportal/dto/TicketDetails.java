/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.dto;

public class TicketDetails {
	
	private int totalTickets;
	private int automationTickets;
	private int manualTickets;
	
	public int getTotalTickets() {
		return totalTickets;
	}
	public void setTotalTickets(int totalTickets) {
		this.totalTickets = totalTickets;
	}
	public int getAutomationTickets() {
		return automationTickets;
	}
	public void setAutomationTickets(int automationTickets) {
		this.automationTickets = automationTickets;
	}
	public int getManualTickets() {
		return manualTickets;
	}
	public void setManualTickets(int manualTickets) {
		this.manualTickets = manualTickets;
	}
}
