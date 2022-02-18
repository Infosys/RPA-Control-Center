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

import com.infosys.aiauto.operationsportal.dao.ClusterDetailsDAO;
import com.infosys.aiauto.operationsportal.dto.Cluster;
import com.infosys.aiauto.operationsportal.dto.ClusterChild;
import com.infosys.aiauto.operationsportal.dto.ClusterComponent;
import com.infosys.aiauto.operationsportal.dto.ClusterDetails;
import com.infosys.aiauto.operationsportal.dto.Portfolio;
import com.infosys.aiauto.operationsportal.dto.PortfolioHistory;
import com.infosys.aiauto.operationsportal.exception.ValidationException;

@Component
public class ClusterDetailsFacade {
	
	@Autowired
	private ClusterDetailsDAO clusterDetailsDAO;
	
	@Autowired
	private PortfolioFacade portfolioFacade;
	
	public List<ClusterDetails> getAllClusters() throws SQLException {
		List<ClusterDetails> allClusterList = new ArrayList<ClusterDetails> ();
		
		allClusterList = clusterDetailsDAO.getClustersDetails();
		return getClusterState(allClusterList);
	}
	
	public List<ClusterDetails> getClusterState(List<ClusterDetails> allClusterList) {
		String state="healthy";
		int critical,warning,healthy;
		 
		List<ClusterDetails> allPortfolioStateList = new ArrayList<ClusterDetails> ();
		allPortfolioStateList = getPortfolioState(allClusterList);
		for (ClusterDetails clusterDetails : allPortfolioStateList) {
			clusterDetails.setPortfolioCount(clusterDetails.getClusterChild().size());
			for (Portfolio portfolio : clusterDetails.getClusterChild()) {
				if(portfolio.getState().equalsIgnoreCase("critical")) {
					state = "critical";
					break;
				}else if (portfolio.getState().equalsIgnoreCase("warning")) {
					state = "warning";
					break;
				}else {
					state = "healthy";
				}
			}
			clusterDetails.setClusterState(state);
		}
		
		for (ClusterDetails clusterDetails : allPortfolioStateList) {
			critical = warning = healthy = 0;
			for (Portfolio portfolio : clusterDetails.getClusterChild()) {
				if(portfolio.getState().equalsIgnoreCase("critical")) {
					critical++;
				}else if (portfolio.getState().equalsIgnoreCase("warning")) {
					warning++;
				}else {
					healthy++;
				}
			}
			clusterDetails.setCriticalCount(critical);
			clusterDetails.setWarningCount(warning);
			clusterDetails.setHealthyCount(healthy);
		}
		
		return allPortfolioStateList;
	}
	
	public List<Cluster> getClustersDetails() throws SQLException {
		List<Cluster> allClusterList = new ArrayList<Cluster> ();
		
		allClusterList = clusterDetailsDAO.getClusters();
		return allClusterList;
	}
	
	

	
	public List<ClusterDetails> getPortfolioState(List<ClusterDetails> allClusterList) {
		String state="healthy";
		Map<String, Integer> stateCount = new HashMap<String, Integer>();
		for (ClusterDetails clusterDetails : allClusterList) {
			for (Portfolio portfolio : clusterDetails.getClusterChild()) {
				stateCount = clusterDetailsDAO.getServerState(portfolio.getPortfolioId());
				if(stateCount != null && !stateCount.isEmpty()){
					if(stateCount.get("critical") > 0) {
						state = "critical";
					}else if(stateCount.get("warning") > 0) {
						state = "warning";
					}else {
						state = "healthy";
					}
				}
				portfolio.setState(state);
			}
		}
		return allClusterList;
	}
	
	public ClusterComponent getClusterDetails(List<ClusterDetails> allClusterList) {
		ClusterComponent clusterComponent = new ClusterComponent();
		int totalCount,criticalCount,warningCount,healthyCount;
		
		totalCount = criticalCount = warningCount = healthyCount = 0;
		
		for (ClusterDetails clusterDetails : allClusterList) {
			if(clusterDetails.getClusterState().equalsIgnoreCase("critical")) {
				criticalCount++;
			}else if (clusterDetails.getClusterState().equalsIgnoreCase("warning")) {
				warningCount++;
			}else {
				healthyCount++;
			}
		}
		
		totalCount = criticalCount + warningCount + healthyCount;
		
		clusterComponent.setTotalCount(totalCount);
		clusterComponent.setCriticalCount(criticalCount);
		clusterComponent.setWarningCount(warningCount);
		clusterComponent.setHealthyCount(healthyCount);
		
		return clusterComponent;
	}

	public List<ClusterChild> getClusterChilds(String clusterid) throws ValidationException, SQLException {
		List<ClusterChild> clusterChildList = new ArrayList<ClusterChild>();
		clusterChildList = clusterDetailsDAO.getChildDetails(clusterid);
		String status = "";
		
		for (ClusterChild clusterChild : clusterChildList) {
			PortfolioHistory portfolioHistory = portfolioFacade.getHealthCheckExecHistory(clusterChild.getPortfolioId(), 1);
			clusterChild.setComponentCount(portfolioHistory.getStatusByDate().get(0).getComponents());
			if(portfolioHistory.getCriticalCount() > 0) {
				status = "critical";
			}else if(portfolioHistory.getWarningCount() > 0) {
				status = "warning";
			}else {
				status = "healthy";
			}
			clusterChild.setStatus(status);
		}
		return clusterChildList;
	}
}
