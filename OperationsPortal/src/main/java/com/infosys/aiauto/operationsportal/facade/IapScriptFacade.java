/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

/*package com.infosys.aiauto.operationsportal.facade;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dto.NodesContent;
import com.infosys.iapsln.adapter.impl.IapAdapter;
import com.infosys.niasln.adapter.exception.AdapterException;

@Component
public class IapScriptFacade {
	
	
	public JSONArray getScriptsByCategoryId(String categoryId) throws AdapterException, JSONException {
		
		JSONArray res = new JSONArray();
		IapAdapter iapAdapter = new IapAdapter();
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("mapperSetting", "iap,IAP");
		fieldMap.put("operationEnvirnoment", "IAP");
		iapAdapter.init(fieldMap);
		res = iapAdapter.getAllBusinessProcessByCategoryId(categoryId);
		return res;
	}
	
	public JSONArray getCategories() throws AdapterException {
		// Fetch categories list 
		IapAdapter iapAdapter = new IapAdapter();
		JSONArray categories = new JSONArray();
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("mapperSetting", "iap,IAP");
		fieldMap.put("operationEnvirnoment", "IAP");
		iapAdapter.init(fieldMap);
		categories = iapAdapter.getAllCategories(new JSONArray());
		return categories;
	}
	
	public List<NodesContent> getAllNodes(String components) throws AdapterException, JSONException {
		// Fetch categories list 
		List<NodesContent> nodesList = new ArrayList<NodesContent>();
		IapAdapter iapAdapter = new IapAdapter();
		JSONArray nodes = new JSONArray();
		JSONObject componentJson = new JSONObject(components);
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("mapperSetting", "nia,NIA");
		fieldMap.put("operationEnvirnoment", "NIA");
		iapAdapter.init(fieldMap);
		nodes = iapAdapter.findAllNodes();		
		
		
		for (int i = 0; i < nodes.length(); i++) {
			JSONObject nodeJson = nodes.getJSONObject(i);
			NodesContent node = new NodesContent();
			
			node.setId(nodeJson.getInt("id"));
			node.setName(nodeJson.getString("name"));
			node.setOs(nodeJson.getString("os"));
			//node.setServiceAreas(Arrays.asList(serviceAreaList));
			node.setStagePath(nodeJson.getString("stagePath"));
			node.setUserId(nodeJson.getString("userId"));
			node.setWorkingDir(nodeJson.getString("workingDir"));
			
			if(nodeJson.getString("name").equalsIgnoreCase(componentJson.getString("component"))) {
				node.setDefaultNode(true);
			}else{
				node.setDefaultNode(false);
			}
			
			nodesList.add(node);
		}
		return nodesList;
	}

}
*/