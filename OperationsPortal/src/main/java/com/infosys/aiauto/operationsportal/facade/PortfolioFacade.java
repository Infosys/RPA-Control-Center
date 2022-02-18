/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.facade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dao.PortfolioDAO;
import com.infosys.aiauto.operationsportal.dto.Observations;
import com.infosys.aiauto.operationsportal.dto.PortfolioHistory;
import com.infosys.aiauto.operationsportal.dto.RecentComponents;
import com.infosys.aiauto.operationsportal.dto.ReportComponents;
import com.infosys.aiauto.operationsportal.dto.ReportDetails;
import com.infosys.aiauto.operationsportal.dto.ResourceType;
import com.infosys.aiauto.operationsportal.dto.ThresholdSummary;
import com.infosys.aiauto.operationsportal.exception.ValidationException;

/**
 * @author Infosys Class returns portfolio details,resource details and
 *         healthcheck execution details
 *
 */
@Component
public class PortfolioFacade {

	@Autowired
	private PortfolioDAO portfolioDAO;
	
	private List<ResourceType> resourceTypeList = new ArrayList<ResourceType>();
	
	public List<ResourceType> getResourceTypeList() {
		return resourceTypeList;
	}

	public void setResourceTypeList(List<ResourceType> resourceTypeList) {
		this.resourceTypeList = resourceTypeList;
	}
	

	/**
	 * retrieves details of last five healthcheck executed for portfolio
	 * 
	 * @param portfolioId
	 * @return portHistory
	 * @throws ValidationException
	 * @throws SQLException
	 */
	public PortfolioHistory getHealthCheckExecHistory(String portfolioName, int count) throws ValidationException, SQLException {
		System.out.println("getHealthCheckExecHistory:started");
		PortfolioHistory data = new PortfolioHistory();
		List<String> innerPortfolio = new ArrayList<String>();
		List<ThresholdSummary> incidentList = new ArrayList<ThresholdSummary>();
		String portfolioId = portfolioName;
		portfolioId = portfolioDAO.getPortfolioId(portfolioName);

		innerPortfolio = portfolioDAO.getPortfolioChilds(portfolioId);
		if (innerPortfolio.size() != 0) {
			// do nothing with list
		}

		else {
			innerPortfolio.add(portfolioId);
		}
		
		/*
		 * Control Center Dash board not using the configid, so this call is not required
		 */
		//incidentList = portfolioDAO.getIncidentDetails(portfolioId, count);
		
		ThresholdSummary thresholdSummary = new ThresholdSummary();
		thresholdSummary.setIncidentId(" ");
		thresholdSummary.setPortfolioId(portfolioId);
		thresholdSummary.setConfigID(" ");
		incidentList.add(thresholdSummary);

		for (ThresholdSummary ts : incidentList) {

			ts.setComponents(portfolioDAO.getComponentsCount(innerPortfolio));
			ts.setCountOfChecks(portfolioDAO.getCountOfChecks(ts.getPortfolioId()));
			ts.setStateCount(portfolioDAO.getServerState(ts.getPortfolioId()));
			/*System.out.println("IncidentId: " + ts.getIncidentId() + " portfolioId: " + ts.getPortfolioId()
					+ " configId: " + ts.getConfigID() + " starttime: " + ts.getStartTime() + " endtime: "
					+ ts.getEndTime() + " Components: " + ts.getComponents() + " CountOfCheks: " + ts.getCountOfChecks()
					+ " serverState: " + ts.getStateCount());*/
		}

		data.setStatusBydate(incidentList);
		data.setResourceId(portfolioId);
		System.out.println("getHealthCheckExecHistory:ended");
		return getComponentStateCount(data);
	}
	
	public PortfolioHistory getComponentStateCount(PortfolioHistory data) {
		// TODO Auto-generated method stub
		Map<String, String> stateCount = new HashMap<String, String>();
		stateCount = portfolioDAO.getComponentState(data.getResourceId());
		
		int healthyCount = 0;
		int warnCount = 0;
		int criticalCount = 0;
		for( String value : stateCount.values()) {
			if(value.equalsIgnoreCase("critical")) {
				criticalCount++;
			}else if (value.equalsIgnoreCase("warning")) {
				warnCount++;
			}else {
				healthyCount++;
			}
		}
		data.setCriticalCount(criticalCount);
		data.setHealthyCount(healthyCount);
		data.setWarningCount(warnCount);
		return data;
	}

	public List<ReportDetails> getPortfolioReportDetails(String portfolioId) throws ValidationException, SQLException {
		System.out.println("getPortfolioReport:started");
		List<ReportComponents> reportCompList = new ArrayList<ReportComponents>();
		List<String> innerPortfolio = new ArrayList<String>();
		List<ReportDetails> reportDetails = new ArrayList<ReportDetails>();

				
		innerPortfolio = portfolioDAO.getPortfolioChilds(portfolioId);
		
		if (innerPortfolio.size() != 0) {
			innerPortfolio.add(portfolioId);
		}

		else {
			innerPortfolio.add(portfolioId);
		}
		
		
				
		reportCompList = portfolioDAO.getPortReportDetails(innerPortfolio);
		
		
		reportCompList = getServerState(reportCompList);
		
		 
		
		System.out.println("list empty:? "+reportCompList.isEmpty());

		System.out.println("getPortfolioReport:ended");
		
		reportDetails = getTicketComponentDetails(reportCompList);
		
		 
		
		return reportDetails;
	}
	
	/**
	 * method will iterate over serverStateCount map and find max key having
	 * highest RGB value and will set it as a serverState for ReportComponents
	 * DTO
	 * 
	 * @param reportCompList
	 *            - List of ReportComponents DTO
	 * @return reportCompList - List of ReportComponents DTO after calculating
	 *         serverState by iterating over serverStateCount map
	 * @throws ValidationException
	 */
	public List<ReportComponents> getServerState(List<ReportComponents> reportCompList) throws ValidationException {

		Map<String, Integer> stateCount = new HashMap<String, Integer>();

		/*
		 * iterate over reportCompList to access serverStateCount() map
		 */
		for (ReportComponents reportComp : reportCompList) {
			
				stateCount = (reportComp.getServerStateCount());
				// getting key of max RGB value and setting as a state of
				// portfolio
				String state = stateCount.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
				reportComp.setServerState((state));

			} 
		
		return reportCompList;
	}
	
	public List<ReportDetails> getTicketComponentDetails(List<ReportComponents> reportCompList) {
		List<ReportDetails> reportDetailsList = new ArrayList<ReportDetails>();
		String resourceId = "";
		int criticalCount,warnCount,healthyCount;
		boolean flag = false;
		List<Observations> observationList =  new ArrayList<Observations>();
		
		for (int i = 0; i < reportCompList.size(); i++) {
			Observations observationObj = new Observations();
			ReportDetails reportDetailsObj = new ReportDetails();
			
			observationObj.setObservableName(reportCompList.get(i).getObservableName());
			observationObj.setValue(reportCompList.get(i).getValue());
			observationObj.setServerState(reportCompList.get(i).getServerState());
			observationObj.setThreshold(reportCompList.get(i).getThreshold());
			observationObj.setThresholdType(reportCompList.get(i).getThresholdType());
			observationObj.setObservationTime(reportCompList.get(i).getObservationTime());
			observationObj.setObservableId(reportCompList.get(i).getObservableId());
			
			if(i == 0) {
				resourceId = reportCompList.get(i).getResourceId();
				reportDetailsObj.setResourceId(reportCompList.get(i).getResourceId());
				reportDetailsObj.setResourcetypeId(reportCompList.get(i).getResourcetypeId());//Resource Type Name
				reportDetailsObj.setResourceTypeID(reportCompList.get(i).getResourceTypeID());
				reportDetailsObj.setResourceName(reportCompList.get(i).getResourceName());
				//reportDetailsObj.setResourceTypeID(portfolioDAO.getResourceTypeId(reportCompList.get(i).getResourcetypeId()));//Resourcetypeid
				//reportDetailsObj.setResourceName(portfolioDAO.getResourceName(reportDetailsObj.getResourceId()));//Resourcename				
				observationList.add(observationObj);
				flag = false;
				//If the list contains only one record, need add that record into reportDetailsList
				if (i == (reportCompList.size()-1)){
					healthyCount = 0;
					criticalCount = 0;
					warnCount = 0;
					reportDetailsObj.setComponentParam(observationList.size());
					for(int j=0 ; j<observationList.size() ; j++) {
						if(observationList.get(j).getServerState().equalsIgnoreCase("healthy")) {
							healthyCount++;
						}else if (observationList.get(j).getServerState().equalsIgnoreCase("critical")) {
							criticalCount++;
						}else {
							warnCount++;
						}
					}
					
					if(criticalCount != 0) {
						reportDetailsObj.setComponentHealth("critical");
					}else if(warnCount != 0) {
						reportDetailsObj.setComponentHealth("warning");
					}else {
						reportDetailsObj.setComponentHealth("healthy");
					}
					
					reportDetailsObj.setCriticalCount(criticalCount);
					reportDetailsObj.setWarningCount(warnCount);
					reportDetailsObj.setHealthyCount(healthyCount);
					reportDetailsObj.setObservations(observationList);
					reportDetailsList.add(reportDetailsObj);
				}
			}else if(i == (reportCompList.size()-1)) {
				if(reportCompList.get(i).getResourceId().equalsIgnoreCase(resourceId)) {
					healthyCount = 0;
					criticalCount = 0;
					warnCount = 0;
					resourceId = reportCompList.get(i).getResourceId();
					reportDetailsObj.setResourceId(reportCompList.get(i).getResourceId());
					reportDetailsObj.setResourcetypeId(reportCompList.get(i).getResourcetypeId());
					reportDetailsObj.setResourceTypeID(reportCompList.get(i).getResourceTypeID());
					reportDetailsObj.setResourceName(reportCompList.get(i).getResourceName());
//					reportDetailsObj.setResourceTypeID(portfolioDAO.getResourceTypeId(reportCompList.get(i).getResourcetypeId()));
//					reportDetailsObj.setResourceName(portfolioDAO.getResourceName(reportDetailsObj.getResourceId()));
					observationList.add(observationObj);
					reportDetailsObj.setComponentParam(observationList.size());
					
					for(int j=0 ; j<observationList.size() ; j++) {
						if(observationList.get(j).getServerState().equalsIgnoreCase("healthy")) {
							healthyCount++;
						}else if (observationList.get(j).getServerState().equalsIgnoreCase("critical")) {
							criticalCount++;
						}else {
							warnCount++;
						}
					}
					
					if(criticalCount != 0) {
						reportDetailsObj.setComponentHealth("critical");
					}else if(warnCount != 0) {
						reportDetailsObj.setComponentHealth("warning");
					}else {
						reportDetailsObj.setComponentHealth("healthy");
					}
					
					reportDetailsObj.setCriticalCount(criticalCount);
					reportDetailsObj.setWarningCount(warnCount);
					reportDetailsObj.setHealthyCount(healthyCount);
					reportDetailsObj.setObservations(observationList);
					reportDetailsList.add(reportDetailsObj);
				}else {
					healthyCount = 0;
					criticalCount = 0;
					warnCount = 0;
					reportDetailsObj.setResourceId(reportCompList.get(i-1).getResourceId());
					reportDetailsObj.setResourcetypeId(reportCompList.get(i-1).getResourcetypeId());
					reportDetailsObj.setResourceTypeID(reportCompList.get(i-1).getResourceTypeID());
					reportDetailsObj.setResourceName(reportCompList.get(i-1).getResourceName());
//					reportDetailsObj.setResourceTypeID(portfolioDAO.getResourceTypeId(reportCompList.get(i-1).getResourcetypeId()));
//					reportDetailsObj.setResourceName(portfolioDAO.getResourceName(reportDetailsObj.getResourceId()));
					reportDetailsObj.setComponentParam(observationList.size());
					
					for(int j=0 ; j<observationList.size() ; j++) {
						if(observationList.get(j).getServerState().equalsIgnoreCase("healthy")) {
							healthyCount++;
						}else if (observationList.get(j).getServerState().equalsIgnoreCase("critical")) {
							criticalCount++;
						}else {
							warnCount++;
						}
					}
					
					if(criticalCount != 0) {
						reportDetailsObj.setComponentHealth("critical");
					}else if(warnCount != 0) {
						reportDetailsObj.setComponentHealth("warning");
					}else {
						reportDetailsObj.setComponentHealth("healthy");
					}
					
					reportDetailsObj.setCriticalCount(criticalCount);
					reportDetailsObj.setWarningCount(warnCount);
					reportDetailsObj.setHealthyCount(healthyCount);
					reportDetailsObj.setObservations(observationList);
					reportDetailsList.add(reportDetailsObj);
					
					//add the last element also
					ReportDetails reportDetailsObjLast = new ReportDetails();
					healthyCount = 0;
					criticalCount = 0;
					warnCount = 0;
					observationList.clear();
					resourceId = reportCompList.get(i).getResourceId();
					reportDetailsObjLast.setResourceId(reportCompList.get(i).getResourceId());
					reportDetailsObjLast.setResourcetypeId(reportCompList.get(i).getResourcetypeId());
					reportDetailsObjLast.setResourceTypeID(reportCompList.get(i).getResourceTypeID());
					reportDetailsObjLast.setResourceName(reportCompList.get(i).getResourceName());
//					reportDetailsObjLast.setResourceTypeID(portfolioDAO.getResourceTypeId(reportCompList.get(i).getResourcetypeId()));
//					reportDetailsObjLast.setResourceName(portfolioDAO.getResourceName(reportDetailsObjLast.getResourceId()));
					observationList.add(observationObj);
					reportDetailsObjLast.setComponentParam(observationList.size());
					
					for(int j=0 ; j<observationList.size() ; j++) {
						if(observationList.get(j).getServerState().equalsIgnoreCase("healthy")) {
							healthyCount++;
						}else if (observationList.get(j).getServerState().equalsIgnoreCase("critical")) {
							criticalCount++;
						}else {
							warnCount++;
						}
					}
					
					if(criticalCount != 0) {
						reportDetailsObjLast.setComponentHealth("critical");
					}else if(warnCount != 0) {
						reportDetailsObjLast.setComponentHealth("warning");
					}else {
						reportDetailsObjLast.setComponentHealth("healthy");
					}
					
					reportDetailsObjLast.setCriticalCount(criticalCount);
					reportDetailsObjLast.setWarningCount(warnCount);
					reportDetailsObjLast.setHealthyCount(healthyCount);
					reportDetailsObjLast.setObservations(observationList);
					reportDetailsList.add(reportDetailsObjLast);
				}
			}else {
				if(reportCompList.get(i).getResourceId().equalsIgnoreCase(resourceId)) {
					observationList.add(observationObj);				
					flag = false;
				}else {
					flag = true;
				}
				
				if(flag) {
					healthyCount = 0;
					criticalCount = 0;
					warnCount = 0;
					resourceId = reportCompList.get(i).getResourceId();
					reportDetailsObj.setResourceId(reportCompList.get(i-1).getResourceId());
					reportDetailsObj.setResourcetypeId(reportCompList.get(i-1).getResourcetypeId());
					reportDetailsObj.setResourceTypeID(reportCompList.get(i-1).getResourceTypeID());
					reportDetailsObj.setResourceName(reportCompList.get(i-1).getResourceName());
//					reportDetailsObj.setResourceTypeID(portfolioDAO.getResourceTypeId(reportCompList.get(i-1).getResourcetypeId()));
//					reportDetailsObj.setResourceName(portfolioDAO.getResourceName(reportDetailsObj.getResourceId()));
					reportDetailsObj.setComponentParam(observationList.size());
					
					for(int j=0 ; j<observationList.size() ; j++) {
						if(observationList.get(j).getServerState().equalsIgnoreCase("healthy")) {
							healthyCount++;
						}else if (observationList.get(j).getServerState().equalsIgnoreCase("critical")) {
							criticalCount++;
						}else {
							warnCount++;
						}
					}
					
					if(criticalCount != 0) {
						reportDetailsObj.setComponentHealth("critical");
					}else if(warnCount != 0) {
						reportDetailsObj.setComponentHealth("warning");
					}else {
						reportDetailsObj.setComponentHealth("healthy");
					}
					
					reportDetailsObj.setCriticalCount(criticalCount);
					reportDetailsObj.setWarningCount(warnCount);
					reportDetailsObj.setHealthyCount(healthyCount);
					reportDetailsObj.setObservations(observationList);
					observationList =  new ArrayList<Observations>();
					reportDetailsList.add(reportDetailsObj);
					observationList.add(observationObj);
				}
			}
		}
		return reportDetailsList;
	}

	public List<RecentComponents> getRecentComponents(String portfolioId) {
		// TODO Auto-generated method stub
		List<RecentComponents> recentComponentList = new ArrayList<RecentComponents>();
		
		recentComponentList = portfolioDAO.getRecentComponentList(portfolioId);
		return recentComponentList;
	}
	
	public void getResourceType() throws SQLException {
		resourceTypeList = portfolioDAO.getResourceType();
	}

}
