/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

package com.infosys.aiauto.operationsportal.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dao.PlatformDao;
import com.infosys.aiauto.operationsportal.dto.NiaPlatform;

/**
 * @author Infosys
 *
 */
@Component
public class PlatformFacade {

	@Autowired
	private PlatformDao platformDao;
	
	public List<NiaPlatform> getAllPlatforms() {
		// fetch list of all platforms
		List<NiaPlatform> allPlatforms = new ArrayList<NiaPlatform>();
		allPlatforms = platformDao.getAllPlatforms();
		return allPlatforms;
	}

}
