/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.dto;

import java.util.List;

public class ActionPlan {
	
	private boolean isdeleted;
	private String remediationplanactionid;
	private int actionid;
	private String actionstageid;
	private String actionname;
	private int categoryid;
	private String actionstagename;
	private int actionsequence;
	private List<ActionParameter> parameters;
	
	public boolean isIsdeleted() {
		return isdeleted;
	}
	public void setIsdeleted(boolean isdeleted) {
		this.isdeleted = isdeleted;
	}
	public String getRemediationplanactionid() {
		return remediationplanactionid;
	}
	public void setRemediationplanactionid(String remediationplanactionid) {
		this.remediationplanactionid = remediationplanactionid;
	}
	public int getActionid() {
		return actionid;
	}
	public void setActionid(int actionid) {
		this.actionid = actionid;
	}
	public String getActionstageid() {
		return actionstageid;
	}
	public void setActionstageid(String actionstageid) {
		this.actionstageid = actionstageid;
	}
	public String getActionstagename() {
		return actionstagename;
	}
	public void setActionstagename(String actionstagename) {
		this.actionstagename = actionstagename;
	}
	public int getActionsequence() {
		return actionsequence;
	}
	public void setActionsequence(int actionsequence) {
		this.actionsequence = actionsequence;
	}
	public List<ActionParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<ActionParameter> parameters) {
		this.parameters = parameters;
	}
	public String getActionname() {
		return actionname;
	}
	public void setActionname(String actionname) {
		this.actionname = actionname;
	}
	public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
}
