/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.facade;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dao.LogErrorDAO;
import com.infosys.aiauto.operationsportal.dto.LogError;

@Component
public class LogErrorFacade {
	
	@Autowired
	LogErrorDAO logErrorDAO;
	
	public LogError getErrorDetails() throws SQLException {
		LogError logError = new LogError();
		logError = logErrorDAO.getErrorDetails();
		return logError;
	}
	
}
