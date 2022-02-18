/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.facade;

import java.sql.SQLException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dao.ObservationDAO;
import com.infosys.aiauto.operationsportal.dto.Observation;

@Component
public class ObservationFacade {

	@Autowired
	private ObservationDAO observationDAO;

	public String saveObservationDetail(Observation observation) throws SQLException {
		String result = "{\"res\":\"", updateRes="", configid = "", portfolioid = "", resid = "", obsid = "", observationName = "",
				observationTime = "", source = "", resTypeId = "", aliasId = "";
		float metricValue = 0.0f;
		int updated_rows = 0, sequence= 0;

		Random rand = new Random();
		sequence = rand.nextInt(8) + 1;
		observationName = observation.getObservationname();
		observationTime = observation.getMetrictime();
		metricValue = observation.getMetricvalue();
		resid = observationDAO.getResourceId(observation.getResourcename());
		source = observationDAO.getSource(observation.getResourcename());
		resTypeId = observationDAO.getResourceTypeId(observation.getResourcename());
		obsid = observationDAO.getObservationId(observationName);
		aliasId = observationDAO.getAliasId(resTypeId, obsid);
		portfolioid = observationDAO.getPortfolioId(resid);
		configid = observationDAO.getConfigId(portfolioid, aliasId);

		updated_rows = observationDAO.saveObservations(configid, portfolioid, resid, obsid, observationName,
				metricValue, sequence, observationTime, source);
		
		if (updated_rows > 0) {
			updateRes = "Updated " + updated_rows + " rows";
		}else {
			updateRes = "No rows updated.";
		}

		result += updateRes + "\"}";
		return result;
	}
}
