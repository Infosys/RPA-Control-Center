/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 

/*package com.infosys.aiauto.operationsportal.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.infosys.aiauto.operationsportal.dto.InputVariable;
import com.infosys.aiauto.operationsportal.dto.NodesContent;
import com.infosys.aiauto.operationsportal.dto.ProcessDetails;
import com.infosys.aiauto.operationsportal.dto.ScriptDetails;
import com.infosys.mana.auth.exception.ManaAuthException;
import com.infosys.niasln.adapter.exception.AdapterException;
import com.infosys.niasln.adapter.impl.NiaAdapter;


@Component
public class NiaScriptFacade {
	

	@Value("${serviceArea}")
	private String serviceArea;
	
	public List<ScriptDetails> getScriptsByCategoryId(String categoryId) throws AdapterException, JSONException {
		List<ScriptDetails> scriptDetailsList = new ArrayList<ScriptDetails>();
		ScriptDetails scriptDetails = new ScriptDetails();
		JSONArray res = new JSONArray();
		NiaAdapter niaAdapter = new NiaAdapter();
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("mapperSetting", "nia,NIA");
		fieldMap.put("operationEnvirnoment", "NIA");
		niaAdapter.init(fieldMap);
		res = niaAdapter.getAllBusinessProcessByCategoryId(categoryId);
		
		List<ProcessDetails> processDetailsList = new ArrayList<ProcessDetails>();
		
		for(int i=0 ; i<res.length() ; i++) {
			JSONObject niaScriptJson = res.getJSONObject(i);
			ProcessDetails processDetails = new ProcessDetails();
			
			processDetails.setName(niaScriptJson.getString("name").concat(".").concat(niaScriptJson.getString("scriptType")));
			processDetails.setDescription(niaScriptJson.getString("description"));
			processDetails.setCategoryId(categoryId);
			processDetails.setId(String.valueOf(niaScriptJson.getInt("id")));
			
			JSONArray scriptParamListJSON = new JSONArray(niaScriptJson.get("scriptParamVOList").toString());
			List<InputVariable> inputVariableList = new ArrayList<InputVariable>();
			
			for (int j = 0; j < scriptParamListJSON.length(); j++) {
				JSONObject scriptParamsJson = scriptParamListJSON.getJSONObject(j);
				InputVariable inputVariable = new InputVariable();
				
				inputVariable.setName(scriptParamsJson.getString("paramName"));
				inputVariable.setDefaultValue(scriptParamsJson.getString("defParamValue"));
				inputVariable.setMandatory(scriptParamsJson.getBoolean("isMandatory"));
				
				inputVariableList.add(inputVariable);
	        }
			processDetails.setInputVariables(inputVariableList);
			
			processDetailsList.add(processDetails);
		}
		
		scriptDetails.setEnvironmentName("NIA");
		scriptDetails.setEnvironmentVersion("2.3.0.4");
		scriptDetails.setBusinessProcesses(processDetailsList);
		
		scriptDetailsList.add(scriptDetails);
		
		return scriptDetailsList;
	}
	
	public List<ScriptDetails> getScripts() throws AdapterException, JSONException, ManaAuthException {
		List<ScriptDetails> scriptDetailsList = new ArrayList<ScriptDetails>();
		ScriptDetails scriptDetails = new ScriptDetails();
		List<ProcessDetails> businessProcess = new ArrayList<ProcessDetails>();
		
		String[] categoryId = new String[1];
		for(int i=0 ; i<categoryId.length ; i++) {
			List<ScriptDetails> scriptList = getScriptsByCategoryId(categoryId[i]);
			if(!scriptList.isEmpty()) {
				ScriptDetails scriptJson = new ScriptDetails();
				for(int j=0 ; j<scriptList.size() ; j++)
					scriptJson = scriptList.get(j);
					List<ProcessDetails> scriptsDetails = scriptJson.getBusinessProcesses();
					for(int k=0 ; k<scriptsDetails.size() ; k++) {
						businessProcess.add(scriptsDetails.get(k));
					}
			}
		}
		
		scriptDetails.setEnvironmentName("NIA");
		scriptDetails.setEnvironmentVersion("2.3.0.4");
		scriptDetails.setBusinessProcesses(businessProcess);
		scriptDetailsList.add(scriptDetails);
		
		return scriptDetailsList;
	}

	public JSONArray getCategories() throws AdapterException {
		// Fetch categories list 
		NiaAdapter niaAdapter = new NiaAdapter();
		JSONArray categories = new JSONArray();
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("mapperSetting", "nia,NIA");
		fieldMap.put("operationEnvirnoment", "NIA");
		niaAdapter.init(fieldMap);
		categories = niaAdapter.getAllCategories(new JSONArray());
		return categories;
	}
	
	public List<NodesContent> getAllNodes(String components) throws AdapterException, JSONException {
		// Fetch categories list 
		List<NodesContent> nodesList = new ArrayList<NodesContent>();
		NiaAdapter niaAdapter = new NiaAdapter();
		JSONArray categories = new JSONArray();
		JSONObject componentJson = new JSONObject(components);
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("mapperSetting", "nia,NIA");
		fieldMap.put("operationEnvirnoment", "NIA");
		niaAdapter.init(fieldMap);
		categories = niaAdapter.findAllNodes();
		
		String[] serviceAreaList = serviceArea.split(",");
		
		for (int i = 0; i < categories.length(); i++) {
			JSONObject nodeJson = categories.getJSONObject(i);
			NodesContent node = new NodesContent();
			
			node.setId(nodeJson.getInt("id"));
			node.setName(nodeJson.getString("name"));
			node.setOs(nodeJson.getString("os"));
			node.setServiceAreas(Arrays.asList(serviceAreaList));
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